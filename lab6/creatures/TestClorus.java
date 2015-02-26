package creatures; 
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant; 
import huglife.Impassible;
import huglife.Empty; 

public class TestClorus {

	@Test
	public void testBasics() {
		Clorus c= new Clorus(2); 
		assertEquals(2, c.energy(), 0);
		assertEquals(new Color(34, 0, 231), c.color());
		c.move();
		assertEquals(1.97, c.energy(), 0);
		c.move();
		assertEquals(1.94, c.energy(), 0);
		c.stay();
		assertEquals(1.93, c.energy(), 0);
		c.stay();
		assertEquals(1.92, c.energy(), 0); 
	}

	@Test
	public void testAttack() {
		Clorus c= new Clorus(); 
		assertEquals(1, c.energy(), 0); 
		Plip p= new Plip(2); 
		c.attack(p); 
		assertEquals(3, c.energy(), 0); 
	}

	@Test
	public void testReplicate() {
		Clorus c = new Clorus(4);
		Clorus baby_c = c.replicate();
		assertEquals(2, c.energy(), 0);
		assertEquals(2, baby_c.energy(), 0);
		assertNotSame(c, baby_c); 
	}


	public static void main(String[] args) {
		System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
	}
}