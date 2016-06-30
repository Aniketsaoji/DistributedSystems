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
import java.util.Scanner;

public class SimpleClient {
	static int bytesRead;
	static int current = 0;
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream os = null;
	static Socket sock = null;
	static int PORT;


	public static void sendFile(String filename) throws FileNotFoundException, IOException {
		File myFile = new File (filename);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os = sock.getOutputStream();
		System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
		os.write(mybytearray,0,mybytearray.length);
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

	public static void main(String [] args) throws Exception {
		DatagramSocket socket = new DatagramSocket();
		System.out.println("Connecting...");
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
		System.out.println("What is the name of the file?");
		String nameOfFile = scan.nextLine();
		String data2send = uOrD + "," + nameOfFile;

		InetAddress ia = InetAddress.getByName("localhost");
		byte[] data = data2send.getBytes();
		DatagramPacket sPacket = new DatagramPacket(data, data.length, ia, 5155);
		socket.send(sPacket);

		byte[] rbuf = new byte[1024];
		DatagramPacket rPacket = new DatagramPacket(rbuf, rbuf.length);
		socket.receive(rPacket);
		byte[] data2 = rPacket.getData();
		String errything = new String(data2, 0, data2.length);
		errything = errything.trim();
		System.out.println(errything);

		int datPort = Integer.parseInt(errything);		
		sock = new Socket("127.0.0.1", datPort);
		PrintWriter pout = new PrintWriter(sock.getOutputStream(),true);
		pout.println(data2send);		
		if (uOrD.equals("u")) {
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
