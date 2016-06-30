package findMaxArray;

import java.net.*;
import java.io.*;
import findMaxArray.SentArray;

public class findMaxServer {

	public static DatagramSocket socket = null;

	public static void main(String[] args) throws IOException, InterruptedException {
		socket = new DatagramSocket(2018);
		int count = 0;
		SentArray twodarr = new SentArray();
		twodarr.initialize();
		while (count != twodarr.getSize()) {
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			MaxServerThread maxT = new MaxServerThread(packet, twodarr);
			Thread findMT = new Thread(maxT);
			findMT.start();
			findMT.join();
			count++;
			System.out.println(count);
			twodarr.setRow(count);
			twodarr.printMaxArray();
		}
		System.out.println("The greatest value is: " + twodarr.findGreatest());
		
	}
}
