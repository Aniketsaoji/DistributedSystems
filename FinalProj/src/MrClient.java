import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.nio.*;

public class MrClient {
	final static String theMiddlewareIP = "136.167.116.196";
	final static String myIP = "136.167.117.191";
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream os = null;
	static Socket sock = null;
	static int bytesRead;
	static int current = 0;
	static ServerSocket servsock = null;



	//5154 = Client
	//5155 = Middleware Server
	//5158 = RM1
	//2150 = RM1 TCPIP
	//5159 = FileServer1
	//5160 = FileServer2
	//5161 = FileServer3
	//5258 = RM2
	//2151 = RM2 TCPIP
	//5162 = FileServer4
	//5163 = FileServer5
	//5164 = FileServer6


	public static void sendFile(String filename) throws FileNotFoundException, IOException {
		File myFile = new File (filename);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os = sock.getOutputStream();
		System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
		os.write(mybytearray,0,mybytearray.length);
		System.out.println("life");
		os.flush();
		if (bis != null) bis.close();
		if (os != null) os.close();
	}

	public static void getFile(String filename) throws IOException {
		byte [] mybytearray  = new byte [100];
		InputStream is = sock.getInputStream();
		fos = new FileOutputStream(filename);
		bos = new BufferedOutputStream(fos);
		bytesRead = is.read(mybytearray,0,mybytearray.length);
		current = bytesRead;
		do {
			bytesRead =
					is.read(mybytearray, current, (mybytearray.length-current));
			if(bytesRead >= 0) current += bytesRead;
		} while(bytesRead > -1);

		bos.write(mybytearray, 0 , current);
		bos.flush();
		System.out.println("File " + filename + " downloaded (" + current + " bytes read)");
		if (fos != null) fos.close();
		if (bos != null) bos.close();
	}

	public static void main(String [] args) throws IOException {		
		DatagramSocket socket = new DatagramSocket(5154);

		System.out.println("Sup fam. How you doin'");
		String uOrD = null;
		Scanner scan = new Scanner(System.in);

		while (true) {
			System.out.println("Are you tryna upload or download (u/d)");
			uOrD = scan.nextLine();
			System.out.println(uOrD);
			if (uOrD.equals("u")|| uOrD .equals("d")) {
				break;
			}
			else {
				System.out.println("Bro, I can't understand that");
			}
		}	

		if (uOrD.equals("u")) {
			uOrD = "up";
		}
		System.out.println("What is the name of the file?");
		String nameOfFile = scan.nextLine();
		String request = uOrD + "," + nameOfFile;
		System.out.println(request);
		//Request should look like u,fred.txt

		//Send yourIP to the Middleware Server
		InetAddress MiddlewareIP = InetAddress.getByName(theMiddlewareIP);
		byte[] myIPBytes = myIP.getBytes();
		DatagramPacket sPacket = new DatagramPacket(myIPBytes, myIPBytes.length, MiddlewareIP, 5155);
		socket.send(sPacket);


		//Receive a ping from the appropriate RM
		byte[] rbuf = new byte[1024];
		DatagramPacket rPacket = new DatagramPacket(rbuf, rbuf.length);

		socket.receive(rPacket);
		byte [] RMTCPPBytes = rPacket.getData();
		String RMTCPp = new String(RMTCPPBytes, 0, RMTCPPBytes.length);
		RMTCPp = RMTCPp.trim();
		System.out.println(RMTCPp);
		int RMtcpPort = Integer.parseInt(RMTCPp);

		//Let's hit him up with our dope request
		sock = new Socket(rPacket.getAddress(),RMtcpPort);
		PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
		pout.println(request);
		System.out.println(request);

		if (uOrD.equals("up")) {
			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			String whatDoYouMean = bin.readLine();
			System.out.println(whatDoYouMean);
			sendFile(nameOfFile);
		}
		else {
			getFile(nameOfFile);
		}
	}
}
