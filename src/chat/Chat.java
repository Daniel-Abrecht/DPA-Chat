package chat;

import static connectionManager.EventHandler.createEventHandler;

import java.util.Timer;

import ui.ProfilManager;
import chat.checksum.ChecksumDistributor;
import chat.event.ChatEventHandler;
import chat.event.EndpointEventHandler;
import chat.eventListenerImpl.EndpointListenerImpl;
import chat.receiveHandler.EndpointControlInfoReciveHandler;
import chat.receiveHandler.ResourceChecksumContainerReciveHandler;
import chat.receiveHandler.ResourceRequestHandler;
import chat.receiveHandler.RespoolChecksumReciveHandler;
import chat.receiveHandler.ResourceReciveHandler;
import chat.resources.Profil;
import static chat.utils.Data.loadProfiles;
import static chat.utils.Data.saveProfiles;
import connectionManager.ConnectionManager;

public class Chat {

	private final static String multicastAddr = "231.255.255.10";
	private final static int port = 3311;
	
	public static final int dataIntegrityCheckInterval = 1000 * 10; // 10 every seconds

	public static final ConnectionManager connectionManager = new ConnectionManager(
			multicastAddr, port);
	public static final EndpointEventHandler endpointEventHandler = createEventHandler(EndpointEventHandler.class);
	private static Profil currentProfil = null;
	public static final ChatEventHandler events = createEventHandler(ChatEventHandler.class);;

	public final static String appName = "DPA-Chat";
	public final static String version = "1.0.0";
	public final static String developer = "Daniel Patrik Abrecht";
	public final static String licence =
			"The MIT License (MIT)\n"+
			"\n"+
			"Copyright (c) 2015 Daniel Patrick Abrecht\n"+
			"\n"+
			"Permission is hereby granted, free of charge, to any person obtaining a copy\n"+
			"of this software and associated documentation files (the \"Software\"), to deal\n"+
			"in the Software without restriction, including without limitation the rights\n"+
			"to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"+
			"copies of the Software, and to permit persons to whom the Software is\n"+
			"furnished to do so, subject to the following conditions:\n"+
			"\n"+
			"The above copyright notice and this permission notice shall be included in all\n"+
			"copies or substantial portions of the Software.\n"+
			"\n"+
			"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"+
			"IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"+
			"FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"+
			"AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"+
			"LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"+
			"OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n"+
			"SOFTWARE.";
	
	public static void main(String[] args) {
		
		connectionManager.addHandler(new EndpointControlInfoReciveHandler());
		connectionManager.addHandler(new ResourceReciveHandler());
		connectionManager.addHandler(new RespoolChecksumReciveHandler());
		connectionManager.addHandler(new ResourceChecksumContainerReciveHandler());
		connectionManager.addHandler(new ResourceRequestHandler());
		endpointEventHandler.addEventListener(new EndpointListenerImpl());
		connectionManager.start();

		loadProfiles();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new ChecksumDistributor(connectionManager.getLocalEndpointManager()), 0, dataIntegrityCheckInterval);

		ProfilManager.getInstance().setVisible(true);
	}

	public static Profil getCurrentProfil() {
		return currentProfil;
	}

	public static void setCurrentProfil(Profil currentProfil) {
		Profil oldProfil = Chat.currentProfil;
		Chat.currentProfil = currentProfil;
		events.currentProfilChanged(oldProfil, currentProfil);
	}

	public static void closeChatApp() {
		saveProfiles();
		System.exit(0);
	}
}
