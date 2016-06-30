import java.lang.*;
import java.io.*;
import java.net.*;

public class Homework1Server {
	public static void main(String argv[]) throws IOException {
		String palindrome;
		String reversedString;
		ServerSocket s;
		Socket client;
		try {
			s = new ServerSocket(5155);
			// OK, now listen for connections
			while (true) {  //keep looping accepting new incoming calls
				System.out.println("Server is listening for new connection ....");
				client = s.accept();  //create a new socket to communicate with a client
				while (true) {  //keep the conversation with a client until client is done
					try {
						InputStream in = client.getInputStream();
						BufferedReader bin = new BufferedReader(
								new InputStreamReader(in));
						System.out.print("And the client says......  ");
						palindrome = bin.readLine();
						System.out.print(palindrome);
						if (palindrome.equals("done")) { //client is done, so close connection to this client
							System.out.println(" ,so close socket to client and wait for next client");
							break;
						}
						// create a new PrintWriter with automatic flushing
						PrintWriter pout = new PrintWriter(
								client.getOutputStream(), true);
						reversedString = ReverseString.reverseIt(palindrome);
						System.out.println();
						// now send a message to the client
						pout.println(reversedString);
					} catch (java.io.IOException e) {
						System.out.println(e);
					}
				}
				client.close();
			}
		} catch (java.io.IOException e) {
			System.out.println(e);
			System.exit(1);
		}

	}

}
