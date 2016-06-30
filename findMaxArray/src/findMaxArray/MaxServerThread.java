package findMaxArray;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MaxServerThread implements Runnable{

	public DatagramSocket socket = null;
	public BufferedReader in = null;
	public DatagramPacket packet;
	public Integer row;
	public SentArray arr;

	public MaxServerThread(DatagramPacket packet, SentArray arr) throws IOException {
		this.arr = arr;
		this.packet = packet;
		socket = new DatagramSocket();
	}

	public void run(){
		System.out.println("Thread ID# " + Thread.currentThread().getId() + " started for thread IP " + packet.getAddress() + " port " + packet.getPort() );
		byte[] data = packet.getData();
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		byte[] buf = new byte[1024];
		String msg = new String(data,0,data.length);
		System.out.println(msg);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(arr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte [] data2 = outputStream.toByteArray();
		DatagramPacket sendPacket = new DatagramPacket(data2, data2.length, packet.getAddress(), packet.getPort());
		try {
			socket.send(sendPacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte [] data3 = new byte[1024];
		DatagramPacket rPacket = new DatagramPacket(data3, data3.length);
		try {
			socket.receive(rPacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = new String(rPacket.getData(),0,rPacket.getLength());
		System.out.println(result);
		Integer greatest = Integer.parseInt(result);
		arr.setGreatest(greatest);
	}

}
