import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>Deque</tt> class represents a double-ended queue, or deque ("deck"), 
 * that supports inserting and removing items at either end of the data 
 * structure. This class is implemented using a linked list.
 * <p>
 * This class satisfies the constraints defined 
 * <a href="http://coursera.cs.princeton.edu/algs4/assignments/queues.html>
 * here</a>.
 */
public class Deque<Item> implements Iterable<Item> {

   private int N;            // number of items in the deque
   private Node first, last; // front and end of the deque
   
   // helper linked list class
   private class Node {
       private Item item;
       private Node prior;
       private Node next;
   }
  
   /**
    * Initializes an empty deque.
    */
   public Deque() {
       N = 0;
       first = null;
       last = null;
   }
   
   /**
    * Is this deque empty?
    * @return true if this stack is empty; false otherwise
    */
   public boolean isEmpty() {
       return (first == null || last == null);
   }
   
   /**
    * Returns the number of items in the deque.
    * @return the number of items in the deque
    */
   public int size() {
       return N;
   }
   
   /**
    * Adds the item to the front of the deque.
    * @param item the item to add
    * @throw java.util.NullPointerException if item is null
    */
   public void addFirst(Item item) {
       if (item == null)
           throw new NullPointerException("Cannot add null item"); 
       Node oldfirst = first;
       first = new Node();
       first.item = item;
    if (isEmpty()) {
           last = first;
       } else {
           first.next = oldfirst;
           oldfirst.prior = first;
       }
       N++;
   }
   
   /**
    * Adds the item to the end of the deque.
    * @param item the item to add
    * @throw java.util.NullPointerException if item is null
    */
   public void addLast(Item item) {
       if (item == null)
           throw new NullPointerException("Cannot add null item"); 
       Node oldlast = last;
       last = new Node();
       last.item = item;
       if (isEmpty()) {
           first = last;
       } else {
           oldlast.next = last;
           last.prior = oldlast;
       }
       N++;
   }
   
   /**
    * Removes and returns the item at the front of the deque.
    * @return the item at the front of the deque
    * @throw java.util.NoSuchElementException if this stack is empty
    */
   public Item removeFirst() {
       if (isEmpty()) 
           throw new NoSuchElementException("Queue underflow"); 
       Item item = first.item;
       first = first.next;
       if (isEmpty()) {
           last = null;
       } else {
           first.prior = null;
       }
       N--;
       return item;
   }
   
   /**
    * Removes and returns the item at the end of the deque.
    * @return the item at the end of the deque
    * @throw java.util.NoSuchElementException if this stack is empty
    */
   public Item removeLast() {
       if (isEmpty()) 
           throw new NoSuchElementException("Queue underflow");
       Item item = last.item;
       last = last.prior;
       if (isEmpty()) {
           first = null;
       } else {
           last.next = null;
       }
       N--;
       return item;
   }
   
   /**
    * Returns an iterator to this stack that iterates through the items in LIFO order.
    * @return an iterator to this stack that iterates through the items in LIFO order
    */
   public Iterator<Item> iterator() {
       return new ListIterator();
   }
   
    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
    
        private Node current = first;
        
        public boolean hasNext() { 
            return current != null; 
        }
        
        public void remove() { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException("Queue underflow");
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
}
