package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
	private int r;
	private int g;
	private int b;

	/* create Clorus with energy= e */
	public Clorus(double e) {
		super("clorus"); 
		r = 34;
		g = 0;
		b = 231; 
		energy = e; 
	}

	/** creates a Clorus with energy = 1 **/
	public Clorus() {
		this(1); 
	}

	/** Color of Clorus is always R = 34, G = 0, B = 231 **/
	public Color color() {
		r = 34;
		g = 0;
		b = 231; 
		return color(r, g, b); 
	}

	/** Cloruses lose 0.03 units of energy on move **/ 
	public void move() {
		this.energy= this.energy() - 0.03; 
	}

	/** Cloruses lose 0.01 units of energy on stay **/
	public void stay() {
		this.energy= this.energy() - 0.01; 
	}

	public void attack(Creature c) {
		this.energy= this.energy() + c.energy(); 
	}

	public Clorus replicate() {
		return new Clorus(); // JUST HERE AS A PLACEHOLDER
	}
	

	public Action chooseAction(Map<Direction, Occupant> neighbors) {
		return new Action(Action.ActionType.STAY); // JUST HERE AS A PLACEHOLDER
	}
}