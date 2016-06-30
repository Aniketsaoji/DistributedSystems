import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UnderwearServer {
	
	public static void main(String [] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(5155);
		while (true) {
			byte[] rbuf = new byte[1024];
			DatagramPacket rPacket = new DatagramPacket(rbuf, rbuf.length);
			socket.receive(rPacket);
			ServerThread newST = new ServerThread(rPacket);
			Thread newT = new Thread(newST);
			newT.start();
			System.out.println("We done");
		}
	}
}
