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

/** Tests the plip class   
 *  @authr FIXME
 */

public class TestPlip {

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.10, p.energy(), 0.01); // Originally this test said it should return 2.00, but I'm pretty sure 1.90 + 0.2= 2.10. 
    }

    @Test
    public void testReplicate() {
        Plip p2 = new Plip(2); 
        Plip baby_p2= p2.replicate(); 
        assertEquals(1, p2.energy(), 0); 
        assertEquals(1, baby_p2.energy(), 0); 
        assertNotSame(p2, baby_p2); 

    }

    @Test
    public void testChoose() {
        Plip p = new Plip(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry! 

        Plip p2= new Plip(1.2); 
        HashMap<Direction, Occupant> surrounded2= new HashMap<Direction, Occupant>(); 
        surrounded2.put(Direction.TOP, new Impassible()); 
        surrounded2.put(Direction.BOTTOM, new Impassible());
        surrounded2.put(Direction.LEFT, new Impassible()); 
        surrounded2.put(Direction.RIGHT, new Empty()); 

        Action actual2= p2.chooseAction(surrounded2);
        Action expected2= new Action(Action.ActionType.REPLICATE, Direction.RIGHT);
        assertEquals(expected2, actual2); 




        Plip p4= new Plip(0.3); 
        HashMap<Direction, Occupant> surrounded4= new HashMap<Direction, Occupant>(); 
        surrounded4.put(Direction.TOP, new Impassible()); 
        surrounded4.put(Direction.BOTTOM, new Empty());
        surrounded4.put(Direction.LEFT, new Empty()); 
        surrounded4.put(Direction.RIGHT, new Empty()); 

        Action actual4= p4.chooseAction(surrounded2);
        Action expected4= new Action(Action.ActionType.STAY);
        assertEquals(expected4, actual4); 


    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
} 
