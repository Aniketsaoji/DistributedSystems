import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer2 {

	static int bytesRead;
	static int current = 0;
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static Socket sock = null;
	static ServerSocket servsock = null;
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream os = null;
	static int PORT;

	public static void uploadFile(String filename) throws IOException {

		byte [] mybytearray  = new byte [100];
		InputStream is = sock.getInputStream();
		fos = new FileOutputStream("/Users/aniketsaoji/Desktop/DistributedSystems/SimpleLoadBalancing/FileServer2/" + filename);
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
	}

	public static void sendFile(String filename) throws FileNotFoundException, IOException {
		File myFile = new File ("/Users/aniketsaoji/Desktop/DistributedSystems/SimpleLoadBalancing/FileServer2/" + filename);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os = sock.getOutputStream();
		System.out.println("Sending " + "fred.txt" + "(" + mybytearray.length + " bytes)");
		os.write(mybytearray,0,mybytearray.length);
		os.flush();
	}


	public static void main(String [] args) throws IOException {	
		servsock = new ServerSocket(5157);
		while (true) {

			sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);

			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			String whatDoYouMean = bin.readLine();
			System.out.println(whatDoYouMean);

			String [] tokens = whatDoYouMean.split(",");
			String uOrD = tokens[0];
			String filename = tokens[1];
			System.out.println(uOrD);
			System.out.println(filename);

			if (uOrD.equals("u")) {
				PrintWriter pout = new PrintWriter(sock.getOutputStream(),true);
				pout.println("Uploading " + filename + " for you.");
				uploadFile(filename);
			}
			else {
				sendFile(filename);
			}
			if (bis != null) bis.close();
			if (os != null) os.close();
		}


	}

}
