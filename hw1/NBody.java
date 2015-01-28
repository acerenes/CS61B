public class NBody {
	public static void main(String[] args) {
		double T = Double.valueOf(args[0]); //0th command line argument
		double dt = Double.valueOf(args[1]); //1st command line argument
		String filename= args[2]; //2nd command line argument
		In universe= new In(filename); //In is a class created by Josh Hug
		int planetnum= universe.readInt();
		double radius= universe.readDouble();

		/* Drawing the Initial Universe State */
		Planet[] allplanets= new Planet[planetnum]; //read all planets into array
		// dividing by 6 because of allplanets[i] thing
		for (int i=0; i<=planetnum-1; i= i+1) {
			allplanets[i]= getPlanet(universe);
			/*allplanets= (allplanets + getPlanet(universe));*/
		}
		StdDraw.setScale(-radius, radius); 
		String starfield= "images/starfield.jpg"; //it's not in the hw1 folder; it's in the images folder IN hw1
		StdDraw.picture(0, 0, starfield); // like normal coordinate system
		StdDraw.show(); //need this to make it show up
		int length= allplanets.length-1;
		int i=0;
		while (i<=length) {
			allplanets[i].draw();
			i= i+1;
			}

		/* Creating an Animation */
		double time= 0;
		while (time<T) {
			for (int j=0; j<=length; j= j+1) {
				allplanets[j].setNetForce(allplanets);
				//setNetForce needs all the planets info in, but it only sets net force for one object at a time, so that's why it's in the loop
			}
			for (int q=0; q<=length; q= q+1) {
				allplanets[q].update(dt);
			}
			StdDraw.setScale(-radius, radius);
			StdDraw.picture(0,0, starfield);
			int k=0;
			while (k<=length) {
				allplanets[k].draw();
				k= k+1;
			}
			StdDraw.show(10); //do show for like EVERYTHING that's been drawn so far
			time= time+dt;
		}

		/* Printing the Universe */
		System.out.println(planetnum);
		System.out.println(radius);
		//StdOut.out.println(getPlanet(allplanets));
		for (int z=0; z<length+1; z= z+1) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", allplanets[z].x, allplanets[z].y, allplanets[z].xVelocity, allplanets[z].yVelocity, allplanets[z].mass, allplanets[z].img); 
			// was in the hw hints I have no idea wat
			// if declare public variables at top, then can .them and .methods
		}
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