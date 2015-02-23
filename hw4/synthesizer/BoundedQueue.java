package synthesizer; 

public interface BoundedQueue {
	
	public int capacity();
	public int fillCount();
	public boolean isEmpty();
	public boolean isFull();
	public void enqueue(double x);
	public double dequeue();
	public double peek();

}