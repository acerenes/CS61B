public class TestPlanet {
	// create two planets and print the pairwise force between them
	// TRY WRITING MY OWN TEST FOR THE PLANET CLASS???!!!!

	public static void main(String[] args) {
		Planet planet1 = new Planet(15, 20, 100, 5, 1000, "starfield.jpg");
		Planet planet2 = new Planet(72, 10, 2, 500, 1000, "star.jpg"); 
		System.out.println(planet1.calcPairwiseForce(planet2));
	}
}

// YES I THINK IT WORKS YESSSSSSSSS 