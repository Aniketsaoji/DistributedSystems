import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable {

	public static DatagramSocket socket = null;
	public DatagramPacket rPacket;
	public static ArrayList<String>RMList = new ArrayList<String>();
	public static ArrayList<String>aliveRMList = new ArrayList<String>();
	public static ArrayList<String>realPortList = new ArrayList<String>();

	public ServerThread(DatagramPacket packet) throws SocketException {
		this.rPacket = packet;
		socket = new DatagramSocket();
	}

	public void run() {
		Socket newsock = null;
		RMList.add("localhost,2150");
		RMList.add("localhost,2151");
		RMList.add("localhost,2152");

		System.out.println("Thread finna start");
		byte[] clientIPBytes = rPacket.getData();
		String clientIP = new String(clientIPBytes, 0, clientIPBytes.length);
		clientIP = clientIP.trim();
		System.out.println(clientIP);
		//Should print out the client's IP

		//Now, we need to see, which RM are up and trillin'
		for (String RMinfo : RMList) {
			try {
				String [] RMtokens = RMinfo.split(",");
				InetAddress RMia = InetAddress.getByName(RMtokens[0]);
				newsock = new Socket(RMia, Integer.parseInt(RMtokens[1]));
				PrintWriter pout = new PrintWriter(newsock.getOutputStream(), true);
				pout.println("ping");
				aliveRMList.add(RMinfo);
			} catch (Exception e) {
				continue;
			}
		}
		System.out.println(aliveRMList);
		String [] RMinfo = aliveRMList.get(0).split(",");

		int RMport = Integer.parseInt(RMinfo[1]); 
		InetAddress RMia;

		try {
			RMia = InetAddress.getByName(RMinfo[0]);
			newsock = new Socket(RMia, RMport);
			PrintWriter pout = new PrintWriter(newsock.getOutputStream(), true);
			pout.println("IP"+","+clientIP);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Thread peacin' out");
		RMList.clear();
		aliveRMList.clear();
	}
}
