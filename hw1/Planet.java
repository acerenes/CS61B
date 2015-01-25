public class Planet {
	// create Planet class and instance variables
	public double x;
	public double y;
	public double xVelocity;
	public double yVelocity;
	public double mass;
	public String img;

	public double xNetForce; 
	public double yNetForce; 

	public double xAccel;
	public double yAccel;

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
		double xdist= otherplanet.x - x;
		double ydist= otherplanet.y -y;
		return Math.sqrt(xdist*xdist + ydist*ydist);
	}

	public double calcPairwiseForce(Planet otherplanet) {
		double G= 6.67 * Math.pow(10, -11);
		return G*mass*otherplanet.mass / (calcDistance(otherplanet)*calcDistance(otherplanet));
	}

	public double calcPairwiseForceX(Planet otherplanet) {
		double xdist= otherplanet.x - x;
		return calcPairwiseForce(otherplanet)*xdist / calcDistance(otherplanet);
	}

	public double calcPairwiseForceY(Planet otherplanet) {
		double ydist= otherplanet.y -y;
		return calcPairwiseForce(otherplanet)*ydist / calcDistance(otherplanet);
	}

	public void setNetForce(Planet [] planets) { 
		this.xNetForce=0; // initializing as 0
		this.yNetForce=0; // initializing as 0
		for (int i=0; i<=planets.length-1; i= i+1) {
			if (planets[i]==this) {
				continue; 
			} else {
				this.xNetForce= this.xNetForce + calcPairwiseForceX(planets[i]);
				this.yNetForce= this.yNetForce + calcPairwiseForceY(planets[i]);
			}

		}
	}

	public void draw() {
		StdDraw.setCanvasSize(); // set window size to default 512x512 pixels
		StdDraw.setPenRadius(); // set pen size to default .002
		StdDraw.setPenColor(); // set pen color to default- black
		StdDraw.point(x, y); // draw a point at (x,y)
	}

	public void update(double dt) {
		xAccel= xNetForce/mass;
		yAccel= yNetForce/mass;
		xVelocity= xVelocity + dt*xAccel;
		yVelocity= yVelocity + dt*yAccel;
		x= x + dt*xVelocity;
		y= y + dt*yVelocity;
	}

}