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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class FS5 {
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
	static ArrayList<Integer> myServerPorts = new ArrayList<Integer>();
	static String filesIHaz = "";
	static ArrayList<String> theFilesIHaz = new ArrayList<String>();


	public static synchronized void uploadFile(String filename) throws IOException {
		byte [] mybytearray  = new byte [100];
		InputStream is = sock.getInputStream();
		fos = new FileOutputStream("../FinalProj/FileServer5/" + filename);
		bos = new BufferedOutputStream(fos);
		bytesRead = is.read(mybytearray,0,mybytearray.length);
		current = bytesRead;
		do {
			bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
			if(bytesRead >= 0) 
				current += bytesRead;
		} while(bytesRead > -1);

		bos.write(mybytearray, 0 , current);
		bos.flush();
		System.out.println("File " + filename + " downloaded (" + current + " bytes read)");
		if (fos != null) fos.close();
		if (bos != null) bos.close();
	}

	public static synchronized void sendFile(Socket disSock, String filename) throws FileNotFoundException, IOException {
		File myFile = new File ("../FinalProj/FileServer5/" + filename);
		byte [] mybytearray  = new byte [(int)myFile.length()];
		fis = new FileInputStream(myFile);
		bis = new BufferedInputStream(fis);
		bis.read(mybytearray,0,mybytearray.length);
		os = disSock.getOutputStream();
		System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
		os.write(mybytearray,0,mybytearray.length);
		os.flush();
		if (bis != null) bis.close();
		if (os != null) os.close();
	}


	public static void main(String [] args) throws IOException {
		servsock = new ServerSocket(5163);
		while (true) {
			sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);

			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			String request = bin.readLine();
			System.out.println(request);
			//request: d,fred.txt
			String [] tokens = request.split(",");
			String uOrD = tokens[0];
			String filename = tokens[1];
			System.out.println(uOrD);
			System.out.println(filename);

			if (uOrD.equals("u")) {

				//Upload to local file Server
				PrintWriter pout = new PrintWriter(sock.getOutputStream(),true);
				pout.println("Uploading " + filename + " for you.");
				uploadFile(filename);
			}
		}
	}
}	
