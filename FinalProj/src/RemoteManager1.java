import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RemoteManager1 {

	static ServerSocket servsock = null;
	static Socket sock = null;
	static Socket sock3 = null;
	static Socket sock2 = null;
	static int bytesRead;
	static int current = 0;
	static FileOutputStream fos = null;
	static BufferedOutputStream bos = null;
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream os = null;
	static ArrayList<Integer> myServerPorts = new ArrayList<Integer>();
	static ArrayList<String> RMList = new ArrayList<String>();
	static HashMap<String, Integer> myFiles = new HashMap<String, Integer>();
	static String whereBStuff = "../FinalProj/src/rm1/";

	public static void initialize() {
		RMList.add("localhost,2150");
		RMList.add("localhost,2151");
		RMList.add("localhost,2152");
	}
	public static synchronized void uploadFile(String filename) throws IOException {
		byte [] mybytearray  = new byte [100];
		InputStream is = sock.getInputStream();
		fos = new FileOutputStream(whereBStuff + filename);
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

	public synchronized static void sendFile(Socket disSock, String filename) throws FileNotFoundException, IOException {
		File myFile = new File (whereBStuff + filename);
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

	public static synchronized void send2Erryone(String data, String filename) throws FileNotFoundException {
		for (int portNum: myServerPorts) {
			try {
				Socket newSock = new Socket("localhost", portNum);
				PrintWriter pout = new PrintWriter(newSock.getOutputStream(),true);
				pout.println(data);	
				InputStream in = newSock.getInputStream();
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));
				String whatDoYouMean = bin.readLine();
				System.out.println(whatDoYouMean);
				sendFile(newSock, filename);
			}
			catch (IOException e){
				continue;
			}
		}
	}

	public static void send2RMHomies(String data, String filename) throws UnknownHostException {
		for (String RMinfo: RMList) {
			String [] RMinfoArray = RMinfo.split(",");
			int portNum = Integer.parseInt(RMinfoArray[1]);
			InetAddress RMia = InetAddress.getByName(RMinfoArray[0]);
			if (portNum == 2150)
				continue;
			try {
				Socket newSock = new Socket(RMia, portNum);
				PrintWriter pout = new PrintWriter(newSock.getOutputStream(),true);
				pout.println(data);	
				InputStream in = newSock.getInputStream();
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));
				String whatDoYouMean = bin.readLine();
				System.out.println(whatDoYouMean);
				sendFile(newSock, filename);
			}
			catch (IOException e){
				continue;
			}
		}
	}

	public static synchronized void send2Someone(String data, String filename, String info) throws IOException {
		System.out.println(info);
		String []infoArray = info.split(",");
		InetAddress ia = InetAddress.getByName(infoArray[0]);
		int port = Integer.parseInt(infoArray[1]);

		Socket newSock = new Socket(ia, port);
		PrintWriter pout = new PrintWriter(newSock.getOutputStream(),true);
		pout.println(data);

		InputStream in = newSock.getInputStream();
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		String whatDoYouMean = bin.readLine();
		System.out.println(whatDoYouMean);

		System.out.println(filename);
		sendFile(newSock, filename);
	}

	public static void getUp2Date() throws UnknownHostException {
		for (String RMinfo : RMList ) {
			String [] RMinfoArray = RMinfo.split(",");
			int portNum = Integer.parseInt(RMinfoArray[1]);
			InetAddress RMia = InetAddress.getByName(RMinfoArray[0]);
			if (portNum == 2150) {
				continue;
			}
			else {
				try {
					Socket newSock = new Socket(RMia, portNum);
					PrintWriter pout = new PrintWriter(newSock.getOutputStream(),true);
					pout.println("c," + myFiles + "/"+ RMList.get(0));
				}
				catch (IOException e){
					continue;
				}
			}
		}
	}

	public static void myCurrentFiles() {
		File[] files = new File(whereBStuff).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		for (File file : files) {
			myFiles.put(file.getName(), 1);
		}
	}

	public static void main(String [] args) throws IOException, ClassNotFoundException {
		myServerPorts.add(5159);
		myServerPorts.add(5160);
		myServerPorts.add(5161);
		initialize();


		DatagramSocket socket = new DatagramSocket(5158);
		servsock = new ServerSocket(2150);
		myCurrentFiles();
		getUp2Date();

		while (true) {
			//Handle ping from ServerThread
			sock = servsock.accept();
			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));
			String allRequests = bin.readLine();
			System.out.println(allRequests);
			String [] jobArray = allRequests.split(",");
			String job = jobArray[0];
			if (job.equals("ping")) {
				continue;

			}
			else if (job.equals("IP")) {
				byte [] clientPing = "2150".getBytes();
				InetAddress ia = InetAddress.getByName(jobArray[1]);
				DatagramPacket sPacket = new DatagramPacket(clientPing, clientPing.length, ia, 5154);
				socket.send(sPacket);
			}
			else if (job.equals("u")) {
				PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
				pout.println("Uploading for you");


				uploadFile(jobArray[1]);
				//Send to FileServers
				send2Erryone(allRequests,jobArray[1]);
				if (myFiles.containsKey(jobArray[1])) {
					myFiles.put(jobArray[1], myFiles.get(jobArray[1])+1);
				}
				else {
					myFiles.put(jobArray[1], 1);
				}
			}
			else if (job.equals("d")){
				sendFile(sock, jobArray[1]);
			}
			else if (job.equals("up")) {
				PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
				pout.println("Uploading for you");

				uploadFile(jobArray[1]);
				//Send to FileServers
				allRequests = "u,"+jobArray[1];
				send2Erryone(allRequests,jobArray[1]);
				send2RMHomies(allRequests, jobArray[1]);
				if (myFiles.containsKey(jobArray[1])) {
					myFiles.put(jobArray[1], myFiles.get(jobArray[1])+1);
				}
				else {
					myFiles.put(jobArray[1], 1);
				}
			}
			else if (job.equals("c")) {
				HashMap<String, Integer> theirFileList = new HashMap<String, Integer>();
				String requestReformat = allRequests.replace("c,", "");
				String [] requestReformat2 = requestReformat.split("/");
				String theirInfo = requestReformat2[1];
				requestReformat = requestReformat2[0];			
				requestReformat = requestReformat.substring(1, requestReformat.length()-1);          
				String[] keyValuePairs = requestReformat.split(",");          
				for(String pair : keyValuePairs)                      
				{
					String[] entry = pair.split("=");                   
					theirFileList.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));
				}
				//Now we have their files in a hashmap

				for (String key : myFiles.keySet()) {
					if (!theirFileList.containsKey(key)) {
						String command = "u," + key;
						System.out.println(command);
						//command is u,drugs.txt
						send2Someone(command, key, theirInfo);

					}
					else if (theirFileList.get(key) < myFiles.get(key)) {
						String command = "u," + key;
						System.out.println(command);
						send2Someone(command, key, theirInfo);
					}
				}
			}
			System.out.println(myFiles);

		}
	}
}


