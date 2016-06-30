package findMaxArray;

import java.io.Serializable;
import java.util.ArrayList;

public class SentArray implements Serializable {
	private int size = 5;
	private int[][] arr = new int[size][size];
	private ArrayList<Integer> greatestarr = new ArrayList<Integer>();
	private int row = 0;
	
	public void initialize() {
		int count = 0;
		for (int i = 0; i <size; i++) {
			for (int j = 0; j<size; j++) {
				int randomNum = (int)(Math.random()*100);
				arr[i][j] = randomNum; 
			}
		}
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
		System.out.println(greatestarr);
	}
	public void setValue(int row, int column, int value) {
		arr[row][column] = value;
	}
	public synchronized void setGreatest(int value) {
		greatestarr.add(value);
	}
	public int getSize() {
		return size;
	}
	public int getValue(int row, int col) {
		return arr[row][col];
	}
	public void setRow(int rownum) {
		row = rownum;
	}
	public int getRow() {
		return row;
	}
	public int findGreatest() {
		int greatest = -1;
		for (int i=0; i<size; i++) {
			if (greatestarr.get(i)>greatest)
				greatest = greatestarr.get(i);
		}
		return greatest;
	}
}