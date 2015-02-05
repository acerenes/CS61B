
public class DoubleChain {
	
	private DNode head;
	
	public DoubleChain(double val) {
		head = new DNode(val);
	}

	public DNode getFront() {
		return head; // WHY DOES THIS WORK B/C WON'T IT RETURN LIKE ALL OF THE THINGS hsrliughlirduhgliuhliuhdarlgihulr@@@@@@@@@@HELEEPSOIEHFOSIHFOSHOIHSOGIHOSIHGOSHGODSHOSIHGOSIDHGOISHDGOISHOISHGOISHGOISHDGOISHDGOIHSDOGIHSOGIHOISHDOIGHSODIGHO
	}

	/** Returns the last item in the DoubleChain. */		
	public DNode getBack() {
		DNode pointer= head; 
		while (pointer.next!=null) {
			pointer= pointer.next;
		}
		return pointer;
	}
	
	/** Adds D to the front of the DoubleChain. */	
	public void insertFront(double d) {
		DNode newbie= new DNode(d); 
		newbie.next= head; 
		head= newbie;
	}
	
	/** Adds D to the back of the DoubleChain. */	
	public void insertBack(double d) {
		DNode pointer= head;
		DNode newbie= new DNode(d);
		while (pointer.next!=null) {
			pointer= pointer.next;
		}
		pointer.next= newbie; 
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