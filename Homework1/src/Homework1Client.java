import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Homework1Client {
	
	public static void main(String argv[]) throws IOException {
		String palindrome;
		String reversedString;
		// DatagramSocket clientSocket = new DatagramSocket();
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));

		//InetAddress address = InetAddress.getByName(argv[0]);
		Socket clientSocket = new Socket("127.0.0.1", 5155);

		while (true) {
			System.out.print("Enter some text:   ");
			palindrome = inFromUser.readLine();
			
		    // create a new PrintWriter with automatic flushing
			PrintWriter pout = new PrintWriter(clientSocket.getOutputStream(),
					true);
			pout.println(palindrome);
			if (palindrome.equals("done"))
				break;
			InputStream in = clientSocket.getInputStream();
			// Connect a reader for easier access
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			System.out.println("And the server says......");
			reversedString = reader.readLine();
			System.out.println("FROM SERVER after some unwraping: "
					+ reversedString);

		}
		clientSocket.close();
		System.out.println("Goodbye");
	}

}
