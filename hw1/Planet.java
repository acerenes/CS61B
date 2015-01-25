public class Planet {
	// create Planet class and instance variables
	public double x;
	public double y;
	public double xVelocity;
	public double yVelocity;
	public double mass;
	public String img;

	// constructor inializes an instance of the Planet class
	public Planet(double xpos, double ypos, double xVel, double yVel, double m, String image) {
		x= xpos;
		y= ypos;
		xVelocity= xVel;
		yVelocity= yVel;
		mass= m;
		img= image;
	}

	public double calcDistance(Planet otherplanet) {
		double xdist= Math.abs(otherplanet.x - x);
		double ydist= Math.abs(otherplanet.y -y);
		return Math.sqrt(xdist*xdist + ydist*ydist);
	}

	public double calcPairwiseForce(Planet otherplanet) {
		double G= 6.67 * Math.pow(10, -11);
		return G*mass*otherplanet.mass / (calcDistance(otherplanet)*calcDistance(otherplanet));
	}

}