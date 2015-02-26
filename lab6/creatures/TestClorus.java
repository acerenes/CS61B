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

	@Test
	public void testChoose() {
		Clorus c= new Clorus(2); 
		HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>(); 
		surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        Clorus c2 = new Clorus(2); 
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>(); 
        surrounded2.put(Direction.TOP, new Impassible()); 
        surrounded2.put(Direction.BOTTOM, new Impassible());
        surrounded2.put(Direction.LEFT, new Empty()); 
        surrounded2.put(Direction.RIGHT, new Plip()); 

        Action actual2= c2.chooseAction(surrounded2);
        Action expected2= new Action(Action.ActionType.ATTACK, Direction.RIGHT);
        assertEquals(expected2, actual2);


        Clorus c3 = new Clorus(2); 
        HashMap<Direction, Occupant> surrounded3 = new HashMap<Direction, Occupant>(); 
        surrounded3.put(Direction.TOP, new Impassible()); 
        surrounded3.put(Direction.BOTTOM, new Impassible());
        surrounded3.put(Direction.LEFT, new Impassible()); 
        surrounded3.put(Direction.RIGHT, new Empty()); 

        Action actual3= c3.chooseAction(surrounded3);
        Action expected3= new Action(Action.ActionType.REPLICATE, Direction.RIGHT);
        assertEquals(expected3, actual3);


		Clorus c4 = new Clorus(0.5); 
        HashMap<Direction, Occupant> surrounded4 = new HashMap<Direction, Occupant>(); 
        surrounded4.put(Direction.TOP, new Impassible()); 
        surrounded4.put(Direction.BOTTOM, new Impassible());
        surrounded4.put(Direction.LEFT, new Impassible()); 
        surrounded4.put(Direction.RIGHT, new Empty()); 

        Action actual4= c4.chooseAction(surrounded4);
        Action expected4= new Action(Action.ActionType.MOVE, Direction.RIGHT);
        assertEquals(expected4, actual4);        
	}


	public static void main(String[] args) {
		System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
	}
}