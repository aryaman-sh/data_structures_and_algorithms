import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Memory complexity: O(n)
 *  Memory complexity depends on the implementation method of deque. In addition we use this.deque which is a deque of
 *   the same implementation type and and reverse() also has a memory complexity of O(n).
 *   Hence memory complexity is O(n) as it is O(n) for both implementations.
 */
public class ReversibleDeque<T> implements SimpleDeque<T> {

    /* This.deque */
    private SimpleDeque<T> deque;

    /**
     * Constructs a new reversible deque, using the given data deque to store
     * elements.
     * The data deque must not be used externally once this ReversibleDeque
     * is created.
     *
     * Runtime complexity: O(1)
     *  assignment takes place in constant time
     *
     * Memory Complexity: O(1)
     *  constant memory use
     *
     * @param data a deque to store elements in.
     */
    public ReversibleDeque(SimpleDeque<T> data) {
        this.deque = data;
    }

    /**
     * Reverses this deque
     *
     * Runtime complexity: O(n)
     *  Where n is the number of elements in the deque. We iterate over all n when copying objects to tempArr. Iterate
     *  over tempArr n times and push to deque.
     *
     * Memory complexity: O(n)
     *  Created a new temp array of length n where n is the number of elements in the deque.
     */
    public void reverse() {
        int size = this.deque.size();
        //temporary array to store elements
        T[] tempArr = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            tempArr[i] = this.deque.popLeft();
        }
        for (int i = 0; i < size; i++) {
            this.deque.pushLeft(tempArr[i]);
        }
    }

    /**
     * Returns whether the deque is empty.
     *
     * Runtime Complexity: O(1)
     *  worst case runtime complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case memory complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * @return true if the deque is empty, otherwise false.
     */
    @Override
    public int size() {
        return this.deque.size();
    }

    /**
     * Returns whether the deque is empty.
     *
     * Runtime Complexity: O(1)
     *  worst case runtime complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case memory complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * @return true if the deque is empty, otherwise false.
     */
    @Override
    public boolean isEmpty() {
        return this.deque.isEmpty();
    }

    /**
     * Returns whether the deque is full, i.e. it has a capacity and its size == capacity.
     *
     * Runtime Complexity: O(1)
     *  worst case runtime complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case memory complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * @return true if the deque has reached capacity (if it has one), otherwise false.
     */
    @Override
    public boolean isFull() {
        return this.deque.isFull();
    }

    /**
     * Pushes an element to the left of the deque.
     *
     * Runtime Complexity: O(1)
     *  worst case runtime complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case memory complexity of the two implementation. Complexity is same when using array or linkedList
     *
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        this.deque.pushLeft(e);
    }

    /**
     * Pushes an element to the right of the deque.
     *
     * Runtime Complexity: O(1)
     *  worst case runtime complexity of the two implementation
     *
     * Memory Complexity: O(1)
     *  Worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        this.deque.pushRight(e);
    }

    /**
     * Returns the element at the left of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T peekLeft() throws NoSuchElementException {
        return this.deque.peekLeft();
    }

    /**
     * Returns the element at the left of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * Memory Complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        return this.deque.peekRight();
    }

    /**
     * Removes and returns the element at the left of the deque.
     *
     * Runtime complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * Memory complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T popLeft() throws NoSuchElementException {
        return this.deque.popLeft();
    }

    /**
     * Removes and returns the element at the right of the deque.
     *
     * Runtime complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * Memory complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @returns the rightmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T popRight() throws NoSuchElementException {
        return this.deque.popRight();
    }

    /**
     * Returns an iterator for the deque in left to right sequence.
     *
     * The methods hasNext() and next() in the Iterator should run in O(1) time.
     * The remove() method in the iterator should not be implemented.
     *
     * You can assume that the elements in the deque will never change while the iterator is being used.
     *
     * Runtime Complexity: O(n)
     *  complexity of ArrayDeque implementation is O(1) and of implementation using linkedlist is O(n) hence
     *  complexity in worst case is O(n)
     *
     * Memory Complexity: O(1)
     *  worst case complexity of the two implementations. Complexity is same when using array or linkedList
     *
     * @returns an iterator over the elements in in order from leftmost to rightmost.
     */
    @Override
    public Iterator<T> iterator() {
        return this.deque.iterator();
    }

    /**
     * Returns an iterator for the deque in right to left sequence.
     *
     * The methods hasNext() and next() in the Iterator should run in O(1) time.
     * The remove() method in the iterator should not be implemented.
     *
     * You can assume that the elements in the deque will never change while the iterator is being used.
     *
     * Runtime Complexity: O(n)
     *  complexity of ArrayDeque implementation is O(1) and of implementation using linkedlist is O(n) hence
     *   complexity in worst case is O(n)
     *
     * Memory Complexity: O(1)
     *  worst case complexity of either implementation. Complexity is same when using array or linkedList
     *
     * @returns an iterator over the elements in in order from leftmost to rightmost.
     */
    @Override
    public Iterator<T> reverseIterator() {
        return this.deque.reverseIterator();
    }
}