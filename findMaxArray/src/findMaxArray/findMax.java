package findMaxArray;

public class findMax {
	
	public static void main (String [] args) throws InterruptedException {
		
		twoDArray u = new twoDArray();
		
		u.print2DArray();
		System.out.println();
		Thread[] myThreads;
		myThreads = new Thread[u.size];
		
		for (int i=0; i <u.size; i++) {
			maxThread k = new maxThread(u,i);
			myThreads[i] = new Thread(k);
			myThreads[i].start();
		}
		Thread current;
		for (int i = 0; i < u.size; i++){
		    current = myThreads[i];
		    current.join();
		}
		int greatest = -5;
		for (int i = 0; i<u.size;i++) {
			if (u.greatestarr[i] > greatest)
				greatest = u.greatestarr[i];
		}
		System.out.println("The greatest value is: " + greatest);
	}

}
