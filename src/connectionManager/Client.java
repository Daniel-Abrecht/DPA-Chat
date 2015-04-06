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

	public void run() {
		int idCounter = 0;
		byte[] buffer = new byte[256];
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
			int size = dataPacket.getSize();
			byte type = dataPacket.getType();
			int offset = 0;
			int packetNr = 0;
			// 1 bit first packet flag=1, 7 bit packet id, 4 byte size, 1 byte
			// type, total: 6 byte
			// 1 bit first packet flag=0, 7 bit packet id, 7bit reserved, 25bit
			// packetNr, total 5 bit
			// log2(((1<<(8*4)) -256-6)/(256-5)+1)+1 = 25
			while (offset < size) {
				boolean firstPacket = (offset == 0);
				int seek = 0;
				buffer[seek++] = (byte) (idCounter | (firstPacket ? 0x80 : 0x00));
				if (firstPacket) {
					buffer[seek++] = (byte) ((size >> 24) & 0xff);
					buffer[seek++] = (byte) ((size >> 16) & 0xff);
					buffer[seek++] = (byte) ((size >> 8) & 0xff);
					buffer[seek++] = (byte) ((size >> 0) & 0xff);
					buffer[seek++] = type;
				} else {
					buffer[seek++] = (byte) ((packetNr >> 24) & 0x01);
					buffer[seek++] = (byte) ((packetNr >> 16) & 0xff);
					buffer[seek++] = (byte) ((packetNr >> 8) & 0xff);
					buffer[seek++] = (byte) ((packetNr >> 0) & 0xff);
					packetNr++;
				}
				int space = buffer.length - seek;
				if (offset+space > size)
					space = size-offset;
//				System.out.println("arraycopy: "+datas.length+" | "+offset+" | "+buffer.length+" | "+seek+" | "+space);
				System.arraycopy(datas, offset, buffer, seek, space);
				offset += space;
				seek += space;
				DatagramPacket packet = new DatagramPacket(buffer, seek, destination,
						port);
				int maxAttemps;
				for (maxAttemps = 3; maxAttemps > 0; maxAttemps--) {
					try {
						try {
							// The network will discard
							// udp packets it can't
							// handle in time
/*							if ((packetNr + 1) % 15 == 0)
								Thread.sleep(100); 
							if ((packetNr + 1) % 60 == 0)
								Thread.sleep(200); */
							socket.send(packet);
							Thread.sleep(3);
						} catch (InterruptedException e) {	}
						break;
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
				if (maxAttemps == 0)
					break; // Failed to deliver packet
			}
			idCounter = (idCounter + 1) & 0x7f;
		}
	}
}
