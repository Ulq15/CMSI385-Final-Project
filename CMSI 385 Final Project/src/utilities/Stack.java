package utilities;

import java.util.ArrayList;

public class Stack<T> {
	int currentSize;
	ArrayList<T> data;
	
	public Stack(){
		empty();
	}
	
	public void empty() {
		this.currentSize = 0;
		this.data = new ArrayList<T>();
	}
	
	public boolean isEmpty() {
		return this.currentSize == 0;
	}
	
	public void push(T item) {
		this.data.add(item);
		this.currentSize++;
	}
	
	public T pop() {
		T top = this.data.get(--this.currentSize);
		this.data.remove(this.currentSize);
		return top;
	}
	
	public T peek() {
		return this.data.get(this.currentSize-1);
	}
	
	public String toString() {
		String sentence = "";
		for( int i = this.currentSize-1; i>=0; i--) {
			sentence+=this.data.get(i).toString();
		}
		return "["+sentence+"] "+this.currentSize;
	}
}
