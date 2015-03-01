import java.util.AbstractList;

public class ArrayList61B<E> extends AbstractList<E> {

    private E[] array;
    private int size;
    private int total_capacity;

    public ArrayList61B(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException();
        }
        total_capacity = initialCapacity;
        array = (E[]) new Object[initialCapacity]; // Weird thing because Java doesn't like mixing generics and arrays;
        size = 0; 
    }


    public ArrayList61B() {
        this(1); 
    }


    @Override
    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IllegalArgumentException();
        }
        return array[i];
    }


    @Override
    public boolean add(E item) {
        // Take care of resizing if necessary
        if (size == total_capacity) {
            // First create larger array
            total_capacity = total_capacity * 2;
            E[] new_array = (E[]) new Object[total_capacity];
            // Now copy all elements into new array
            for (int i = 0; i < array.length; i = i + 1) {
                new_array[i] = array[i];
            }
            array = new_array;
        }
        array[size] = item;
        size = size + 1;
        return true; 
    }


    @Override
    public int size() {
        return size;
    }


}