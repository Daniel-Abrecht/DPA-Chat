package connectionManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ArrayBlockingQueue;

public class Client extends Thread {
	private MulticastSocket socket;
	private int port;
	private InetAddress group;
	private ArrayBlockingQueue<DataPacket> queue;
	private boolean running = true;

	public Client(InetAddress group, int port) {
		this.port = port;
		this.group = group;
		this.queue = new ArrayBlockingQueue<DataPacket>(256);
	}

	public void end() {
		running = false;
	}

	public void send(DataPacket datas) {
		while (true) {
			try {
				queue.put(datas);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	private int getFragmentCount(int size,int bufferSize){
		return (bufferSize+size)/(bufferSize-5);
	}
	
	// 1 bit first packet flag=1, 7 bit packet id, 4 byte size, 1 byte
	// type, total: 6 byte
	// 1 bit first packet flag=0, 7 bit packet id, 7bit reserved, 25bit
	// packetNr, total 5 bit
	// log2(((1<<(8*4)) -256-6)/(256-5)+1)+1 = 25
	private int createFragment(byte buffer[],byte datas[],int i,byte type,int id){
		System.out.println("createFragment: "+buffer.length+" | "+datas.length+" | "+i+" | "+type+" | "+id);
		int offset;
		int seek = 0;
		int size = datas.length;
		buffer[seek++] = (byte) (id | (i==0 ? 0x80 : 0x00));
		if (i == 0) {
			offset = 0;
			buffer[seek++] = (byte) ((size >> 24) & 0xff);
			buffer[seek++] = (byte) ((size >> 16) & 0xff);
			buffer[seek++] = (byte) ((size >> 8) & 0xff);
			buffer[seek++] = (byte) ((size >> 0) & 0xff);
			buffer[seek++] = type;
		} else {
			int packetNr = i-1;
			offset = (buffer.length - 6) + packetNr * (buffer.length - 5);
			buffer[seek++] = (byte) ((packetNr >> 24) & 0x01);
			buffer[seek++] = (byte) ((packetNr >> 16) & 0xff);
			buffer[seek++] = (byte) ((packetNr >> 8) & 0xff);
			buffer[seek++] = (byte) ((packetNr >> 0) & 0xff);
		}
		int space = buffer.length - seek;
		if (offset+space > size)
			space = size-offset;
		System.out.println("arraycopy: "+datas.length+" | "+offset+" | "+buffer.length+" | "+seek+" | "+space);
		if(space<0||offset>size){
			System.err.println("Invalid packet segment number");
			Thread.dumpStack();
			return 0;
		}
		if(offset<0){
			System.err.println("Integer overflow!");
			Thread.dumpStack();
			return 0;
		}
		System.arraycopy(datas, offset, buffer, seek, space);
		seek += space;
		return seek;
	}
	
	private int idCounter = 0;
	DataPacket retransmissionCache[]= new DataPacket[1<<3]; // 1<<3=8; must be a power of two, must be smaller than 128=(1<<7)
	
	public void retransmit(DataPacket dataPacket,int packetNumber,byte id,InetAddress destination){
		byte[] buffer = new byte[DataPacket.segmentSize];
		if (destination == null)
			destination = group;
		byte[] datas = dataPacket.getBuffer();
		byte type = dataPacket.getType();
		int size = createFragment(buffer, datas, packetNumber, type, id);
		if(size==0)
			return;
		DatagramPacket packet = new DatagramPacket(buffer, size, destination, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DataPacket getCachedPacket(int id){
		int diff = (idCounter - id) & 0xFF;
		if(diff>=retransmissionCache.length)
			return null; // not in cache
		return retransmissionCache[id % retransmissionCache.length];
	}
	
	public void run() {
		byte[] buffer = new byte[DataPacket.segmentSize];
		try {
			socket = new MulticastSocket();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (running) {
			DataPacket dataPacket;
			try {
				dataPacket = queue.take();
			} catch (InterruptedException e) {
				continue;
			}
			InetAddress destination = dataPacket.getDestination();
			if (destination == null)
				destination = group;
			byte[] datas = dataPacket.getBuffer();
			byte type = dataPacket.getType();
			int n = getFragmentCount(datas.length,buffer.length);
			retransmissionCache[idCounter%retransmissionCache.length] = dataPacket;
			for(int i=0;i<n;i++){
				int size = createFragment(buffer, datas, i, type, idCounter);
				DatagramPacket packet = new DatagramPacket(buffer, size, destination, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			idCounter = (idCounter + 1) & 0x7f;
		}
	}
}
