/**************************************************************************
 * The <tt>RandomizedQueue</tt> class represents a data structure that is 
 * similar to a stack or queue, except that the item removed is chosen 
 * uniformly at random from items in the data structure.
 * <p>
 * This class satisfies the constraints defined 
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/queues.html>
 * here</a>.
 **************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] a;      // an array of items
    private int N = 0;     // number of items on this queue
    
    /**
     * Initializes an empty queue.
     */
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
    }
    
    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }
    
    /**
     * Returns the number of items on this queue.
     * @return the number of items on this queue
     */
    public int size() {
        return N;
    }
    
    // resize the underlying array holding the items
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            temp[i] = a[i];
        a = temp;
    }
    
    /**
     * Adds the item to this queue.
     * @param item the item to add to this queue
     * @throw java.util.NullPointerException if item is null
     */
    public void enqueue(Item item) {
        if (item == null)
           throw new NullPointerException("Cannot add null item"); 
        if (N == a.length) resize(2*a.length);
        a[N++] = item;
    }
    
    /**
     * Deletes and returns a random item on this queue.
     * @return a random item on this queue
     * @throw java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) 
            throw new NoSuchElementException("Queue underflow"); 
        int rand = (int) (StdRandom.uniform()*N);
        Item item = a[rand];
        a[rand] = a[N-1];
        a[N-1] = null;
        N--;
        if (N > 0 && N == a.length/4) 
            resize(a.length/2);
        return item;
    }
    
    /**
     * Returns (but does not delete) a random item on this queue.
     * @return a random item on this queue
     * @throw java.util.NoSuchElementException if this queue is empty
     */
    public Item sample() {
        if (isEmpty()) 
            throw new NoSuchElementException("Queue underflow"); 
        int rand = (int) (StdRandom.uniform()*N);
        return a[rand];
    }
    
    /**
     * Returns an iterator to this queue that iterates through the items in random order.
     * @return an iterator to this queue that iterates through the items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class RandomArrayIterator implements Iterator<Item> {
        private int[] rand = new int[N];
        private int i = 0;
        
        public RandomArrayIterator() {
            for (int j = 0; j < rand.length; j++)
                rand[j] = j;
            StdRandom.shuffle(rand);
        }
        
        public boolean hasNext() {
            return i < N;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return a[rand[i++]];
        }
    }      
}