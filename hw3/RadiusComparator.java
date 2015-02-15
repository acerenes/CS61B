import java.util.Comparator;

/**
 * MassComparator.java
 */

public class RadiusComparator implements Comparator<Planet> {

    public RadiusComparator() {
    }

    /** Returns the difference in radius as an int.
     *  Round after calculating the difference. */
    public int compare(Planet planet1, Planet planet2) {
        return (int) (planet1.radius_value() - planet2.radius_value()); 
    }
}