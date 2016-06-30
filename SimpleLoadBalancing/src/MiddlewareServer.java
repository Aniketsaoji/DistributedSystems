import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MiddlewareServer {
	static int bytesRead;
	static int current = 0;
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static Socket sock = null;
	static ServerSocket servsock = null;

	public static int myHash(String filename) {
		int sum = 0;
		for (int i = 0; i<filename.length(); i++) {
			int num = filename.charAt(i) - 'a' + 1;
			sum = sum + num;
		}
		return sum%3;
	}

	public static void main(String [] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(5155);
		while (true) {
			byte[] rbuf = new byte[1024];
			DatagramPacket rPacket = new DatagramPacket(rbuf, rbuf.length);
			socket.receive(rPacket);
			ServerThread newST = new ServerThread(rPacket);
			Thread newT = new Thread(newST);
			newT.start();
			//			byte[] data = rPacket.getData();
			//			String errything = new String(data, 0, data.length);
			//			errything = errything.trim();
			//			System.out.println(errything);
			//			String [] tokens = errything.split(",");
			//			int hash = myHash(tokens[1]);
			//			String port2Send = null;
			//			if (hash == 0) {
			//				port2Send = "5156";
			//			}
			//			else if (hash == 1) {
			//				port2Send = "5157";
			//			}
			//			else if (hash == 2) {
			//				port2Send = "5158";
			//			}
			//			byte [] dataSend = port2Send.getBytes();
			//			DatagramPacket sPacket = new DatagramPacket(dataSend, dataSend.length, rPacket.getAddress(), rPacket.getPort());
			//			try {
			//				socket.send(sPacket);
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}

		}






		//		servsock = new ServerSocket(5155);
		//		sock = servsock.accept();
		//		System.out.println("Accepted connection : " + sock);
		//		InputStream in = sock.getInputStream();
		//		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		//		String palindrome = bin.readLine();
		//		System.out.print(palindrome);
	}
	//		socket = new DatagramSocket(2018);
	//		while (true) {
	//			//Setting up the socket to get players
	//			String fileDetails = (getPacket(socket));
	//			System.out.println(fileDetails);
	//			String [] tokens = fileDetails.split(",");
	//			System.out.println(tokens[0]);
	//			System.out.println(tokens[1]);
	//			System.out.println(myHash(tokens[1]));
	//		}
}
