
public class DoubleChain {
	
	private DNode head;
	
	public DoubleChain(double val) {
		head = new DNode(val);
	}

	public DNode getFront() {
		return head; // Okay so it only returns the node and not the whole list of things because head refers to the DNode, which contains val and 2 pointers. But the pointers aren't like actual things (they're not stored next to each other), they're just addresses of the next DNodes. 
	}

	/** Returns the last item in the DoubleChain. */		
	public DNode getBack() {
		if (head== null) {
			return null;
		}
		DNode pointer= head; 
		while (pointer.next!=null) {
			pointer= pointer.next;
		}
		return pointer;
	}
	
	/** Adds D to the front of the DoubleChain. */	
	public void insertFront(double d) {
		if (head==null) { 
			head= new DNode(d);
		} else { 
			DNode D= new DNode(d);
			D.next= head; 
			head= D;
		}
	}
	
	/** Adds D to the back of the DoubleChain. */	
	public void insertBack(double d) {
		if (head==null) {
			head= new DNode(d);
		}
		DNode pointer= head;
		DNode D= new DNode(d);
		while (pointer.next!=null) {
			pointer= pointer.next;
		}
		pointer.next= D; 
		D.prev= pointer; 
	}
	
	/** Removes the last item in the DoubleChain and returns it. 
	  * This is an extra challenge problem. */
	public DNode deleteBack() {
		/* your code here */
		return null;
	}
	
	/** Returns a string representation of the DoubleChain. 
	  * This is an extra challenge problem. */
	public String toString() {
		/* your code here */		
		return null;
	}

	public static class DNode {
		public DNode prev;
		public DNode next;
		public double val;
		
		private DNode(double val) {
			this(null, val, null);
		}
		
		private DNode(DNode prev, double val, DNode next) {
			this.prev = prev;
			this.val = val;
			this.next =next;
		}
	}
	
}
