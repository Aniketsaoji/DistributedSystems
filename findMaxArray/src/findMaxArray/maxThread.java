package findMaxArray;

public class maxThread implements Runnable {
	twoDArray arr;
	int row;
	
	public maxThread(twoDArray arr, int i) {
		this.arr = arr;
		this.row = i;
	}
	
	public void run() {
		int greatest = -5;
		for (int i = 0; i < arr.size; i++) {
			if (arr.arr[row][i] > greatest) {
				greatest = arr.arr[row][i];
			}
		}
		arr.greatestarr[row] = greatest;
		
	  }

}
