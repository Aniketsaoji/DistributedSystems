package findMaxArray;

public class twoDArray {
	static int size = 5;
	
	static int[][] arr = new int[size][size];
	static int[] greatestarr = new int[size];
	
	public twoDArray() {
		
		for (int i = 0; i <size; i++) {
			for (int j = 0; j<size; j++) {
				int randomNum = (int)(Math.random()*100);
				arr[i][j] = randomNum; 
			}
		}
	}
	
	public int[][] return2DArray() {
		return arr;
	}
	
	public int[] returnGreatestArray() {
		return greatestarr;
	}
	
	public int returnSize() {
		return size;
	}
	 
	public void print2DArray() {
		for (int i = 0; i<size; i++) {
			for (int j = 0; j<size; j++) {
				System.out.print(arr[i][j] + "   ");
			}
			System.out.println();
		}
	}
	
	public void printMaxArray() {
		for (int i = 0; i < size; i++) {
			System.out.print(greatestarr[i] + "   ");
		}
		System.out.println();
	}

}
