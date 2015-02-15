import java.util.Comparator;
/* Import anything else you need here. */
import java.util.Arrays; 

/**
 * MaxPlanet.java
 */

public class MaxPlanet {

    /** Returns the Planet with the maximum value according to Comparator c. */
    public static Planet maxPlanet(Planet[] planets, Comparator<Planet> c) {
    	// According to the Java documentation, can put in a comparator after the array to do like a ~special sort~
    	Arrays.sort(planets, c); 
    	return planets[planets.length -1]; 
    }
}