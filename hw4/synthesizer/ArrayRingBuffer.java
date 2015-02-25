package synthesizer;

public class ArrayRingBuffer extends AbstractBoundedQueue {
  /* Index for the next dequeue or peek. */
  private int first;           
  /* Index for the next enqueue. */
  private int last;             
  /* Array for storing the buffer data. */
  private double[] rb;

  /** Create a new ArrayRingBuffer with the given capacity. */
  public ArrayRingBuffer(int capacity) {
    // TODO: Create new array with capacity elements.
    rb= new double[capacity]; 
    //       first, last, and fillCount should all be set to 0. 
    first= 0; 
    last= 0;
    fillCount= 0; 
    //       this.capacity should be set appropriately. Note that the local variable here shadows the field we inherit from AbstractBoundedQueue.
    this.capacity= capacity; 
  }

  /** Adds x to the end of the ring buffer. If there is no room, then
    * throw new RuntimeException("Ring buffer overflow") 
    */
  public void enqueue(double x) {
    // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
    if (this.isFull()) {
      throw new RuntimeException("Ring buffer overflow");
    }
    rb[last]= x; 
    fillCount++;
    if (last==capacity-1) {
      last = 0; // Wrap around 
    }
    else {
      last++; 
    }
  }


  /** Dequeue oldest item in the ring buffer. If the buffer is empty, then
    * throw new RuntimeException("Ring buffer underflow");
    */
  public double dequeue() {
    // TODO: Dequeue the first item. Don't forget to decrease fillCount and update first.
    if (this.isEmpty()) {
      throw new RuntimeException("Cannot dequeue an empty buffer.");
    }
    double first_item= rb[first]; 
    rb[first]= 0; // Default value of int arrays is 0; for string array= null b/c strings are objects
    // rb[first] won't let you set it to null 
    fillCount--;
    if (first== capacity-1) {
      // You're at the end- don't want to do first++
      first= 0; 
    }
    else {
      first++; 
    }
    return first_item; 
  }

  /** Return oldest item, but don't remove it. */
  public double peek() {
    // TODO: Return the first item. None of your instance variables should change.
    if (this.isEmpty()) {
      throw new RuntimeException("Cannot peek at an empty buffer.");
    }
    return rb[first]; 
  }

}
