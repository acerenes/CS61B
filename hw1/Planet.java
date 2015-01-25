public class Planet {
	public double x;
	public double y;
	public double xVelocity;
	public double yVelocity;
	public double mass;
	public String img;

	public Planet(double xpos, double ypos, double xVel, double yVel, double m, String image) {
		x= xpos;
		y= ypos;
		xVelocity= xVel;
		yVelocity= yVel;
		mass= m;
		img= image;
	}

}