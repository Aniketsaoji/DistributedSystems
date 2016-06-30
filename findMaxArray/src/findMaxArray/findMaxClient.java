package findMaxArray;

import java.net.*;
import java.io.*;
import findMaxArray.SentArray;


public class findMaxClient {
	public static void main(String[] args) throws IOException {
		DatagramPacket sPacket, rPacket;
		InetAddress ia = InetAddress.getByName("localhost");
		DatagramSocket datasocket = new DatagramSocket();
		BufferedReader stdinp = new BufferedReader(new InputStreamReader(System.in));
		String echoline = "hello, I'm here!";
		byte[] buffer = new byte[echoline.length()];
		buffer = echoline.getBytes();
		sPacket = new DatagramPacket(buffer, buffer.length, ia, 2018);
		datasocket.send(sPacket);
		byte[] rbuffer = new byte[1024];
		rPacket = new DatagramPacket(rbuffer, rbuffer.length);
		datasocket.receive(rPacket);
		byte[] data = rPacket.getData();
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		SentArray arr = null;
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			arr = (SentArray) is.readObject();
			is.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		arr.print2DArray();
		Integer greatest = -1;
		for (int i = 0; i <arr.getSize(); i++) {
			if (arr.getValue(arr.getRow(), i) > greatest)
				greatest = arr.getValue(arr.getRow(), i);
		}
		String biggest = greatest.toString();
		byte [] data2 = biggest.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(data2, data2.length, rPacket.getAddress(), rPacket.getPort());
		try {
			datasocket.send(sendPacket);
			} 
		catch (IOException e1) {
			}
		}
	}
