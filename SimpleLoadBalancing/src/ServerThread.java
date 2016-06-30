import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerThread  implements Runnable {
	
	public static DatagramSocket socket = null;
	public DatagramPacket rPacket;
	
	public ServerThread(DatagramPacket packet) throws SocketException {
		this.rPacket = packet;
		socket = new DatagramSocket();
	}
	
	public static int myHash(String filename) {
		int sum = 0;
		for (int i = 0; i<filename.length(); i++) {
			int num = filename.charAt(i) - 'a' + 1;
			sum = sum + num;
		}
		return sum%3;
	}
	
	public void run() {
		System.out.println("Thread start");
		System.out.println(socket.getPort());
		byte[] data = rPacket.getData();
		String errything = new String(data, 0, data.length);
		errything = errything.trim();
		System.out.println(errything);
		String [] tokens = errything.split(",");
		int hash = myHash(tokens[1]);
		String port2Send = null;
		if (hash == 0) {
			port2Send = "5156";
		}
		else if (hash == 1) {
			port2Send = "5157";
		}
		else if (hash == 2) {
			port2Send = "5158";
		}
		System.out.println(port2Send);
		System.out.println(rPacket.getPort());
		System.out.println(rPacket.getAddress());
		byte [] dataSend = port2Send.getBytes();
		DatagramPacket sPacket = new DatagramPacket(dataSend, dataSend.length, rPacket.getAddress(), rPacket.getPort());
		try {
			socket.send(sPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Thread end");
		
	}

}
