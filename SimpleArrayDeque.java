import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class implements simpleDeque with a circular array
 * Memory complexity: O(n)
 *  Array of n elements (capacity) of the deque is create which happens in O(n) memory complexity.
 *  Other variables like capacity, size, leftHeadIndex require constant memory hence are O(1) memory complexity.
 *  Hence upper bound for total memory complexity is O(n)
 */
public class SimpleArrayDeque<T> implements SimpleDeque<T> {
    /* Array for storing elements of this deque */
    private T[] dequeArray;
    /* Capacity of this deque */
    private int capacity;
    /* size (number of elements) in this deque */
    private int size;
    /* Index of first element at the left of deque in the list of elements of this deque */
    private int leftHeadIndex;

    /**
     * Constructs a new array based deque with limited capacity.
     *
     * Runtime Complexity: O(n) where n = size of array generated
     *  Generation of a new array of size n takes O(n) time. Array is generated with size = capacity.
     *  Assignment to private variables is O(1)
     *
     * Memory Complexity: O(n) where n = size of array generated
     *  Array of size n has O(n) memory complexity. Assignment to private class variable is O(1) memory complexity
     *  as there are only three private variables.
     *
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0
     */
    public SimpleArrayDeque(int capacity) throws IllegalArgumentException {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        //Initialise dequeArray with the given capacity
        dequeArray = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.size = 0;
        this.leftHeadIndex = 0;
    }

    /**
     * Constructs a new array based deque with limited capacity, and initially populates the deque
     * with the elements of another SimpleDeque.
     *
     * Runtime Complexity: O(n) where n = size of array generated.
     *  Generating a new array with size n takes O(n) time here n = capacity.
     *  Assignment to private variables is O(1) time
     *  Copying elements from otherDeque take O(n1) time where n1 = no of elements in otherDeque (i.e. the number of
     *  iterations of the for loop.
     *
     * Memory Complexity: O(n) where n = size of array.
     *  Generation of a new array of size n has a memory complexity of O(n)
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0 or size of otherDeque is > capacity
     */
    public SimpleArrayDeque(int capacity, SimpleDeque<? extends T> otherDeque)
            throws IllegalArgumentException {
        if (capacity <= 0 || otherDeque.size() > capacity) {
            throw new IllegalArgumentException();
        }
        //Initialise dequeArray with the given capacity
        this.dequeArray = (T[]) new Object[capacity];
        this.size = otherDeque.size();
        this.capacity = capacity;
        //Copy elements of otherDeque
        for (int i = 0; i < otherDeque.size(); i++) {
            this.dequeArray[i] = otherDeque.popLeft();
        }
    }

    /**
     * Returns whether the deque is empty.
     *
     * Runtime Complexity: O(1)
     *  Method only has a return statement
     *
     * Memory Complexity: O(1)
     *  constant memory use
     *
     * @return true if the deque is empty, otherwise false.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns whether the deque is full, i.e. it has a capacity and its size == capacity.
     *
     * Runtime Complexity: O(1)
     *  Method only has one return statement hence linear time complexity = O(1)
     *
     * Memory Complexity: O(1)
     *
     * @return true if the deque has reached capacity (if it has one), otherwise false.
     */
    @Override
    public boolean isFull() {
        return this.size == this.capacity;
    }

    /**
     * Returns the number of elements currently stored in the deque.
     *
     * Runtime Complexity: O(1)
     *
     * Memory Complexity: O(1)
     *
     * @return Number of elements.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Pushes an element to the left of the deque.
     *
     * Runtime Complexity: O(1)
     *  Since it takes constant time to insert element in a array at a given index. Other operations in the method
     *  like assignment, arithmetic, incrementation run with constant complexity.
     *
     * Memory Complexity: O(1)
     *  Data values for the variables leftHeadIndex, size can change however no additional memory space is required
     *  as old values are replaced.
     *
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }
        this.leftHeadIndex = (this.leftHeadIndex - 1 + this.capacity) % this.capacity;
        this.dequeArray[this.leftHeadIndex] = e;
        this.size++;
    }

    /**
     * Pushes an element to the right of the deque.
     *
     * Runtime Complexity: O(1)
     *  Since it takes constant time to insert element in a array at a given index. Other operations in the method
     *  like assignment, arithmetic, incrementation run with constant complexity.
     *
     * Memory Complexity: O(1)
     *  Constant memory used, no new datq
     *
     *  Data values for the variables size changes however no additional memory space is required
     *  as old values are replaced.
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }
        this.dequeArray[(this.leftHeadIndex + this.size) % this.capacity] = e;
        this.size++;
    }

    /**
     * Returns the element at the left of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  Accessing a value from an array at a given index takes constant time.
     *
     * Memory Complexity: O(1)
     *  Constant memory used, no new memory used
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T peekLeft() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.dequeArray[this.leftHeadIndex];
    }

    /**
     * Returns the element at the right of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  Accessing a value from an array at a given index takes constant time.
     *
     * Memory Complexity: O(1)
     *  Constant memory occupied.
     *
     * @returns the rightmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.dequeArray[this.leftHeadIndex + this.size - 1];
    }

    /**
     * Removes and returns the element at the left of the deque.
     *
     * Runtime Complexity: O(1)
     *  Accessing element from array takes constant time, other assignment and arithmetic operations happen in linear
     *  time.
     *
     * Memory Complexity: O(1)
     *  New temporary variable created which stores element removed from array hence constant memory complexity.
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T popLeft() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        T returnElement = this.peekLeft();
        this.dequeArray[this.leftHeadIndex] = null;
        this.leftHeadIndex = (this.leftHeadIndex + 1) % this.capacity;
        this.size--;
        return returnElement;
    }

    /**
     * Removes and returns the element at the right of the deque.
     *
     * Runtime Complexity: O(1)
     *  Accessing element from array takes constant time, other assignment and arithmetic operations happen in linear
     *  time.
     *
     * Memory Complexity: O(1)
     *  New temporary variable created which stores element removed from array hence constant memory complexity.
     *
     * @returns the rightmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T popRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        T returnElement = this.peekRight();
        this.dequeArray[this.leftHeadIndex + size - 1] = null;
        this.size--;
        return returnElement;
    }

    /**
     * Returns an iterator for the deque in left to right sequence.
     *
     * The methods hasNext() and next() in the Iterator should run in O(1) time.
     * The remove() method in the iterator should not be implemented.
     *
     * You can assume that the elements in the deque will never change while the iterator is being used.
     *
     * Runtime Complexity: O(1)
     *  The methods hasNext, next() have a runtime complexity of O(1)
     *
     * Memory Complexity: O(1)
     *  Just iterates over dequeArray
     *
     * @returns an iterator over the elements in in order from leftmost to rightmost.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            /**
             * Return true if iterator has a next element, false if it does not have next element
             *
             * Runtime complexity: O(1)
             * Memory Complexity: O(1)
             *
             * @return True if another element exists, false if it does not
             */
            @Override
            public boolean hasNext() {
                return index < size;
            }

            /**
             * Returns the next element in the deque iterator
             *
             * Runtime complexity: O(1) : returning element from array at a given index
             * Memory complexity: O(1)
             *
             * @return next element in deque iterator
             */
            @Override
            public T next() {
                return dequeArray[index++];
            }
        };
    }

    /**
     * Returns an iterator for the deque in right to left sequence.
     *
     * The methods hasNext() and next() in the Iterator should run in O(1) time.
     * The remove() method in the iterator should not be implemented.
     *
     * You can assume that the elements in the deque will never change while the iterator is being used.
     *
     * Runtime Complexity: O(1)
     *  The methods hasNext, next() have a runtime complexity of O(1)
     *
     * Memory Complexity: O(1)
     *  Just iterates over dequeArray list
     *
     * @returns an iterator over the elements in in order from rightmost to leftmost.
     */
    @Override
    public Iterator<T> reverseIterator() {
        return new Iterator<T>() {
            private int index = size - 1 + leftHeadIndex;

            /**
             * Return true if iterator has a next element, false if it does not have next element
             *
             * Runtime complexity: O(1)
             *  Comparison and return statement
             *
             * Memory Complexity: O(1)
             *  Constant memory use
             *
             * @return True if another element exists, false if it does not
             */
            @Override
            public boolean hasNext() {
                return index >= leftHeadIndex;
            }

            /**
             * Returns the next element in the deque iterator
             *
             * Runtime complexity: O(1) : returning element from array at a given index and decrement value are O(1)
             * Memory complexity: O(1)
             *  constant memory use
             *
             * @return next element in deque iterator
             */
            @Override
            public T next() {
                return dequeArray[index--];
            }
        };
    }
}