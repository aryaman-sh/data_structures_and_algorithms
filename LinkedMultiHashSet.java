import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * LinkedMultiHashSet is an implementation of a (@see MultiSet), using a hashtable as the internal
 * data structure, and with predictable iteration order based on the insertion order
 * of elements.
 *
 * Its iterator orders elements according to when the first occurrence of the element
 * was added. When the multiset contains multiple instances of an element, those instances
 * are consecutive in the iteration order. If all occurrences of an element are removed,
 * after which that element is added to the multiset, the element will appear at the end of the
 * iteration.
 *
 * The internal hashtable array should be doubled in size after an add that would cause it to be
 * at full capacity. The internal capacity should never decrease.
 *
 * Collision handling for elements with the same hashcode (i.e. with hashCode()) should be done
 * using linear probing, as described in lectures.
 *
 * @param <T> type of elements in the set
 */
public class LinkedMultiHashSet<T> implements MultiSet<T>, Iterable<T> {

    /**
     * Represents a node.
     *
     * Next contains the index of element in array where element which collided is stored
     * Previous contains the index of element as given by hashing function
     */
    private static class Node<T> {
        //Node element
        private T element;
        //quantity of elements
        private int count;
        //index of previous node in array
        private int previous;
        //index of next node stored in array
        private int next;
        //indicates if node is active or not
        private boolean flagged;

        /**
         * Creates a new node
         *
         * Runtime Complexity: O(1)
         * Memory complexity: O(1)
         *
         * @param element element to be stored in this node
         * @param previous previous node
         * @param next next node
         */
        private Node(T element, int quantity, int previous, int next) {
            this.element = element;
            this.count = quantity;
            this.previous = previous;
            this.next = next;
            this.flagged = false;
        }

        /**
         * Returns if node is flagged or not
         *
         * runtime complexity: O(1)
         * memory complexity: O(1)
         *
         * @return true if node is flagged, false otherwise
         */
        private boolean isFlagged() {
            return this.flagged;
        }

        /**
         * this node is flagged true
         *
         * runtime complexity: O(1)
         * memory complexity: O(1)
         */
        private void flag() {
            this.flagged = true;
        }

        /**
         * Returns element in this node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @return element in this node
         */
        private T getElement() {
            return this.element;
        }

        /**
         * Returns the quantity of the element in this node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @return quantity of element
         */
        private int getCount() {
            return this.count;
        }

        /**
         * Changes count by the given quantity
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @param changeAmount the amount to change count by
         */
        private void changeCount(int changeAmount) {
            this.count += changeAmount;
        }

        /**
         * Increments element count by 1
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         */
        private void incrementCount() {
            this.count += 1;
        }

        /**
         * Decrements element count by 1
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         */
        private void decrementCount() {
            this.count -= 1;
        }

        /**
         * Returns index of previous node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @return returns previous node
         */
        private int getPrevious() {
            return this.previous;
        }

        /**
         * Returns index of next node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @return returns the next node
         */
        private int getNext() {
            return this.next;
        }

        /**
         * Sets index of previous
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @param newPrevious new previous node
         */
        private void setPrevious(int newPrevious) {
            this.previous = newPrevious;
        }

        /**
         * Sets index of nexy
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @param newNext links next node in the linked list
         */
        private void setNext(int newNext) {
            this.next = newNext;
        }
    }

    //Internal array
    private Node<T>[] array;
    //Current capacity of internal array
    private int capacity;
    //No of distinct elements in the array
    private int distinctElementCount;
    //Total number of elements (includes repetitions)
    private int totalElementCount;
    //Element to start iterator from
    private int iteratorOrder;

    /**
     * Creates a new LinkedMultiHashSet with initial capacity of internal array as initial capacity
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(n)
     *
     * @param initialCapacity initial capacity of this internal array
     */
    public LinkedMultiHashSet(int initialCapacity) {
        this.array = (Node<T>[]) new Node<?>[initialCapacity];
        this.capacity = initialCapacity;
        this.distinctElementCount = 0;
        this.totalElementCount = 0;
    }

    /**
     * Add an element to this set. If an equal element already exists increase count by on
     *
     * Runtime complexity: O(n^2)
     * Memory complexity: O(n)
     *
     * @param element to add
     */
    @Override
    public void add(T element) {
        //single count added
        add(element,1);
    }

    /**
     * Uses hashcode and division to given to place object in internal array
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @param element element to be inserted
     * @param capacity max capacity of current array
     * @return int: index corresponding to element
     */
    private int hashFunction(T element, int capacity) {
        return element.hashCode() % capacity;
    }

    /**
     * Returns the next index in a circular array of given capacity.
     *
     * Runtime Complexity: O(1)
     * Memory Complexity: O(1)
     *
     * @param index current index, index next of this index
     * @param capacity capacity of circular array
     * @return index of next array in this circular array
     */
    private int circularArrayNextIndex(int index, int capacity) {
        return (index + 1) % capacity;
    }

    /**
     * Returns the previous index in circular array with the given capacity
     *
     * runtime complexity: O(1)
     * memory complexity: O(1)
     *
     * @param index index whose previous index is returned
     * @param capacity capcity of circular array
     * @return previous index
     */
    private int circularArrayPrevious(int index, int capacity) {
        return (index - 1 + capacity) % capacity;
    }

    /**
     * Adds count to the number of occurrences of the element in set.
     *
     * Runtime complexity: O(n^2)
     * memory complexity: O(n)
     *
     * @param element to add
     * @param count number of occurrences
     */
    @Override
    public void add(T element, int count) {
        addHelper(element, count, this.array, this.capacity);
        // case: internal array full
        if (this.capacity == this.distinctElementCount) {
            @SuppressWarnings("unchecked")
            Node<T>[] newArray = (Node<T>[]) new Node<?>[this.capacity * 2];
            this.totalElementCount = 0;
            this.distinctElementCount = 0;
            for (int counter = 0; counter < this.capacity; counter++) {
                addHelper(this.array[counter].getElement(), this.array[counter].getCount(), newArray,
                        capacity * 2);
            }
            this.array = newArray;
            this.capacity = this.capacity * 2;
        }
    }

    /**
     * Adds given element with given number of occurrence to the given array with given capacity.
     *
     * Runtime complexity (worst case probing): O(n)
     * Memory complexity: O(1)
     *
     * @param element element to be added
     * @param count number of occurrences of element
     * @param arrayToAdd array to add to
     * @param maxCapacity capacity of arrray
     */
    private void addHelper(T element, int count, Node<T>[] arrayToAdd, int maxCapacity) {
        boolean stepflag = false;
        //index as given by hash function
        int addIndex = hashFunction(element, maxCapacity);
        //Case1: null present at position
        if (arrayToAdd[addIndex] == null) {
            // -1 are sentinal values TODO: change sentinals for iterator implementation
            Node<T> addNode = new Node<>(element, count, -1, -1);
            arrayToAdd[addIndex] = addNode;
            this.distinctElementCount += 1;
            this.totalElementCount += count;
            stepflag = true;
        }
        //if first position was not null then check if duplicate exists (because of linear probing)
        if (!stepflag) {
            int tempPointer = addIndex;
            while (tempPointer != circularArrayPrevious(addIndex, maxCapacity)) {
                //if not flagged compare elements
                if (!arrayToAdd[tempPointer].flagged) {
                    //if duplicate change count
                    if (arrayToAdd[tempPointer].getElement().equals(element)) {
                        arrayToAdd[tempPointer].changeCount(count);
                        this.totalElementCount += count;
                        stepflag = true;
                        break;
                    }
                }
                tempPointer = circularArrayNextIndex(tempPointer, maxCapacity);
                //null reached, not probed yet
                if (arrayToAdd[tempPointer] == null) {
                    break;
                }
            }
            // if no duplicate then probing to find appropriate position
            if (!stepflag) {
                tempPointer = addIndex;
                if (arrayToAdd[tempPointer].isFlagged()) {
                    //if first is flagged
                    Node<T> addNode = new Node<>(element, count, -1, -1);
                    arrayToAdd[tempPointer] = addNode;
                    this.totalElementCount += count;
                    this.distinctElementCount += 1;
                } else {
                    tempPointer += 1;
                    while (tempPointer != circularArrayPrevious(addIndex, maxCapacity)) {
                        if (arrayToAdd[tempPointer] == null) {
                            //-1,1 sentinals //TODO: change for iterator implementation
                            Node<T> addNode = new Node<>(element, count, -1, -1);
                            arrayToAdd[tempPointer] = addNode;
                            this.distinctElementCount += 1;
                            this.totalElementCount += count;
                            stepflag = true;
                            break;
                        } else if (arrayToAdd[tempPointer].isFlagged()) {
                            Node<T> addNode = new Node<>(element, count, -1, -1);
                            arrayToAdd[addIndex] = addNode;
                            this.totalElementCount += count;
                            this.distinctElementCount += 1;
                            break;
                        }
                        tempPointer = circularArrayNextIndex(tempPointer, maxCapacity);
                    }
                }
            }
        }
    }

    /**
     * Checks if element is in the set at least once
     *
     * Runtime complexity (worst case): O(n)
     * memory complexity: O(1)
     *
     * @param element to check
     * @return true if element exists false otherwise
     */
    @Override
    public boolean contains(T element) {
        int indexToCheck = hashFunction(element, this.capacity);
        if (this.array[indexToCheck].getElement().equals(element) && !this.array[indexToCheck].flagged) {
            return true;
        }
        int tempPointer = indexToCheck + 1;
        while (this.array[tempPointer] != null) {
            if (tempPointer == indexToCheck) {
                break;
            }
            if (this.array[tempPointer].getElement().equals(element) && !this.array[tempPointer].flagged) {
                return true;
            }
            tempPointer = circularArrayNextIndex(tempPointer, this.capacity);
        }
        return false;
    }

    /**
     * Returns the number of occurrences of given element in the set.
     *
     * Runtime complexity: O(n)
     * memory complexity : O(1)
     *
     * @param element to check
     * @return int: number of occurrences
     */
    @Override
    public int count(T element) {
        if (this.contains(element)) {
            int returnVal = 0;
            int indexToCheck = hashFunction(element, this.capacity);
            if (this.array[indexToCheck].getElement().equals(element)) {
                returnVal = this.array[indexToCheck].getCount();
            }
            int tempPointer = indexToCheck + 1;
            if (returnVal != 0) {
                while (this.array[tempPointer] != null) {
                    if (tempPointer == indexToCheck) {
                        break;
                    }
                    if (this.array[tempPointer].getElement().equals(element)) {
                        returnVal = this.array[tempPointer].getCount();
                    }
                    tempPointer = circularArrayNextIndex(tempPointer, this.capacity);
                }
            }
            return returnVal;
        } else {
            return 0;
        }
    }

    /**
     * Removes a single occurrence of element from the set.
     *
     * Runtime complexity: O(n)
     * Memory complexity: O(1)
     * @param element to remove
     * @throws NoSuchElementException
     */
    @Override
    public void remove(T element) throws NoSuchElementException {
        //remove single count
        remove(element, 1);
    }

    /**
     * Removes several occurrences of the element from the set.
     *
     * Runtime complexity: O(n) worst case
     * Memory complexity: O(1)
     *
     * @param element to remove
     * @param count the number of occurrences of element to remove
     * @throws NoSuchElementException
     */
    @Override
    public void remove(T element, int count) throws NoSuchElementException {
        int removeIndex = hashFunction(element, this.capacity);
        //If position is null no element was added
        if (this.array[removeIndex] == null) {
            throw new NoSuchElementException();
        }
        int pointer = removeIndex;
        while (pointer != circularArrayPrevious(removeIndex, this.capacity)) {
            if (this.array[pointer] == null) {
                throw new NoSuchElementException();
            }
            if (this.array[pointer].getElement().equals(element)) {
                if (this.array[pointer].getCount() < count) {
                    throw new NoSuchElementException();
                }
                if (this.array[pointer].getCount() == count) {
                    this.array[pointer].flag();
                    this.array[pointer].changeCount(( -1 * count ));
                    this.distinctElementCount -= 1;
                    this.totalElementCount -= count;
                    break;
                }
                if (this.array[pointer].getCount() > count) {
                    this.array[pointer].changeCount(( -1 * count ));
                    this.totalElementCount -= count;
                    break;
                }
            }
        }
    }

    /**
     * Returns total count of all elements
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @return total count of elements in this collection
     */
    @Override
    public int size() {
        return this.totalElementCount;
    }

    /**
     * Returns the maximum number of distinct elements in the internal data
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @return Capacity of internal array
     */
    @Override
    public int internalCapacity() {
        return this.capacity;
    }

    /**
     * Returns number of distinct elements in array
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @return Number of distinct elements multiset
     */
    @Override
    public int distinctCount() {
        return this.distinctElementCount;
    }

    /**
     * Implementation logic:
     * Each node stores the index of next linked element in array (in next)(last linked has a sentinal to indicate),
     *  original hash index (previous), value and flag.
     *  This.iterator order keeps track of node to start iteration from.
     */
    @Override
    public Iterator<T> iterator() {
        // TODO: if time
        return null;
    }
}