import java.util.AbstractList;

public class ArrayList61B<E> extends AbstractList {

    E[] array;
    int size;

    public ArrayList61B(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        array = (E[]) new Object[initialCapacity]; // Weird thing because Java doesn't like mixing generics and arrays;
        size = 0; 
    }



}