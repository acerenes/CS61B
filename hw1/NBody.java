public class NBody {
	public static void main(String[] args) {
		double T = Double.valueOf(args[0]);
		double dt = Double.valueOf(args[1]);
		String filename= args[2];
		In universe= new In(filename); //In is a class created by Josh Hug
		int planetnum= universe.readInt();
		double radius= universe.readDouble();
		}

	public static Planet getPlanet(In universe) {
			double xpos= universe.readDouble(); 
			/* readDouble doesn't take in arguments, so hopefully it'll just automatically read the next double column-wise */
			double ypos= universe.readDouble();
			double xVel= universe.readDouble();
			double yVel= universe.readDouble();
			double m= universe.readDouble();
			String image= universe.readString();
			return new Planet(xpos, ypos, xVel, yVel, m, image);
	}
}