import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of simple deque using linkedList
 *
 * Memory complexity: O(n)
 *  for a deque with number of elements n there are n+2 nodes (including leftHead and rightTail nodes) and each node
 *  stores previous, next, element hence memory complexity is O(n). Variables size, capacity take constant memory.
 */
public class SimpleLinkedDeque<T> implements SimpleDeque<T> {
    private int size;
    private Node<T> leftHead;
    private Node<T> rightTail;
    private int capacity;

    /**
     * Node class represents a node of a linked list
     *
     * Memory Complexity: O(1)
     */
    private static class Node<T> {
        //Element stored in the node
        private T element;
        //Previous node linked to this node
        private Node<T> previous;
        //next node linked to this node
        private Node<T> next;

        /**
         * Creates a new node
         *
         * Runtime Complexity: O(1)
         *  Assignment takes constant time
         *
         * Memory Complexity: O(1)
         *  Constant memory used
         *
         * @param element element to be stored in this node
         * @param previous previous node
         * @param next next node
         */
        public Node(T element, Node<T> previous, Node<T> next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }

        /**
         * Returns element in this node
         *
         * Runtime complexity: O(1)
         *  Return operation in constant time
         *
         * Memory Complexity: O(1)
         *  Constant memory use
         *
         * @return element in this node
         */
        public T getElement() {
            return this.element;
        }

        /**
         * Returns the linked node
         *
         * Runtime complexity: O(1)
         *  Return operation in constant time
         *
         * Memory complexity: O(1)
         *  constant memory use
         *
         * @return returns previous linked node
         */
        public Node<T> getPrevious() {
            return this.previous;
        }

        /**
         * Returns the next linked node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @return returns the next node
         */
        public Node<T> getNext() {
            return this.next;
        }

        /**
         * Links a node as previous of this node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *  Memory use remains constant
         *
         * @param newPrevious new previous node
         */
        public void setPrevious(Node<T> newPrevious) {
            this.previous = newPrevious;
        }

        /**
         * Links a node as next of this node
         *
         * Runtime complexity: O(1)
         * Memory complexity: O(1)
         *
         * @param newNext links next node in the linked list
         */
        public void setNext(Node<T> newNext) {
            this.next = newNext;
        }
    }

    /**
     * Constructs a new linked list based deque with unlimited capacity.
     *
     * Runtime complexity: O(1)
     *  Creates a head, tail node in O(1) and links head to tail in O(1) time. Other assignment and arithmetic
     *  operations happen in constant time complexity.
     *
     * Memory complexity: O(1)
     *  Creates two new nodes and assigns value to leftHead, capacity and size which has fixed memory i.e. has O(1)
     *  memory complexity
     */
    public SimpleLinkedDeque() {
        this.leftHead = new Node<>(null, null, null);
        this.rightTail = new Node<>(null, leftHead, null);
        this.leftHead.setNext(rightTail);
        //this.capacity set to -1, deque has no capacity
        this.capacity = -1;
        this.size = 0;
    }

    /**
     * Constructs a new linked list based deque with limited capacity.
     *
     * Runtime complexity: O(1)
     *  Creates a head, tail node in O(1) and links head to tail in O(1) time. Other assignment and arithmetic
     *  operations happen in constant time complexity.
     *
     * Memory complexity: O(1)
     *  Creates two new nodes and assigns value to leftHead, capacity and size which has fixed memory i.e. has O(1)
     *  memory complexity
     *
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0
     */
    public SimpleLinkedDeque(int capacity) throws IllegalArgumentException {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.size = 0;
        //creating head and tail nodes for linkedList
        this.leftHead = new Node<>(null, null, null);
        this.rightTail = new Node<>(null, leftHead, null);
        this.leftHead.setNext(rightTail);
    }

    /**
     * Constructs a new linked list based deque with unlimited capacity, and initially
     * populates the deque with the elements of another SimpleDeque.
     *
     * Runtime complexity: O(n)
     *  Creates a new node to link to this linkedList for each element in otherDeque. For n elements in otherDeque
     *  runs in linear time complexity. Other actions like assignment + creation of head and tail nodes happens in
     *  constant O(1) time. Hence time complexity of this method is O(n)
     *
     * Memory complexity: O(n)
     *  Where n is the number of elements in otherDeque. Since all elements from other deque are copied ot this
     *  linked list a node is created in for each element. Other operations (creation of head, tailNode, assignment to
     *  class variables capacity,size take constant memory and are O(1) hence memory complexity of this method is O(n).
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @requires otherDeque != null
     */
    public SimpleLinkedDeque(SimpleDeque<? extends T> otherDeque) {
        //capacity set to -1, deque does not have a capacity
        this.capacity = -1;
        this.size = otherDeque.size();
        //Creating head and tail nodes for linkedList
        this.leftHead = new Node<>(null, null, null);
        this.rightTail = new Node<>(null, leftHead, null);
        this.leftHead.setNext(rightTail);
        //Iterator for other deque
        Iterator<? extends T> otherDeq = otherDeque.iterator();
        //create a node for each element in otherDeque and link to this list
        while (otherDeq.hasNext()) {
            Node<T> newTempNode = new Node<>(otherDeq.next(), leftHead, leftHead.getNext());
            leftHead.getNext().setPrevious(newTempNode);
            leftHead.setNext(newTempNode);
        }
    }

    /**
     * Constructs a new linked list based deque with limited capacity, and initially
     * populates the deque with the elements of another SimpleDeque.
     *
     * Runtime complexity: O(n)
     *  Creates a new node to link to this linkedList for each element in otherDeque. For n elements in otherDeque
     *  runs in linear time complexity. Other actions like assignment + creation of head and tail nodes happens in
     *  constant O(1) time. Hence time complexity of this method is O(n)
     *
     * Memory complexity: O(n)
     *  Where n is the number of elements in otherDeque. Since all elements from other deque are copied ot this
     *  linked list a node is created in for each element. Other operations (creation of head, tailNode, assignment to
     *  class variables capacity,size take constant memory and are O(1) hence memory complexity of this method is O(n).
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0 or size of otherDeque is > capacity
     */
    public SimpleLinkedDeque(int capacity, SimpleDeque<? extends T> otherDeque)
            throws IllegalArgumentException {
        if (capacity <= 0 || otherDeque.size() > capacity) {
            throw new IllegalArgumentException();
        }
        this.size = otherDeque.size();
        this.capacity = capacity;
        //create head and tail nodes for linkedList
        this.leftHead = new Node<>(null, null, null);
        this.rightTail = new Node<>(null, leftHead, null);
        this.leftHead.setNext(rightTail);
        //Iterator for otherDeque
        Iterator<? extends T> otherDeq = otherDeque.iterator();
        //create a node for each element in otherDeque and link to this list
        while (otherDeq.hasNext()) {
            Node<T> newTempNode = new Node<>(otherDeq.next(), leftHead, leftHead.getNext());
            leftHead.getNext().setPrevious(newTempNode);
            leftHead.setNext(newTempNode);
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
     *  If statement check and return statement run in constant time.
     *
     * Memory Complexity: O(1)
     *  constant memory use
     *
     * @return true if the deque has reached capacity (if it has one), otherwise false.
     */
    @Override
    public boolean isFull() {
        if (this.capacity == -1) {
            //deque created without capacity
            return false;
        } else {
            return this.size == this.capacity;
        }
    }

    /**
     * Returns the number of elements currently stored in the deque.
     *
     * Runtime Complexity: O(1)
     *  Method only has a return statement
     *
     * Memory Complexity: O(1)
     *  constant memory use
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
     *  Creation of a node and linking it to other node happens in constant time. Calling node.getNext() also run in
     *  constant time. size incrementation happens in constant time.
     *
     * Memory Complexity: O(1)
     *  Only one new node is created and size is overridden hence has constant memory complexity.
     *
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }
        Node<T> newNode = new Node<>(e, this.leftHead, this.leftHead.getNext());
        this.leftHead.getNext().setPrevious(newNode);
        this.leftHead.setNext(newNode);
        this.size++;
    }

    /**
     * Pushes an element to the right of the deque.
     *
     * Runtime Complexity: O(1)
     *  Creation of a node and linking it to other node happens in constant time. Calling node.getNext() also run in
     *  constant time. size incrementation happens in constant time.
     *
     * Memory Complexity: O(1)
     *  Only one new node is created and size is overridden hence has constant memory complexity.
     *
     * @param e Element to push
     * @throws RuntimeException if the deque is already full
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }
        Node<T> newNode = new Node<>(e, this.rightTail.getPrevious(), this.rightTail);
        this.rightTail.getPrevious().setNext(newNode);
        this.rightTail.setPrevious(newNode);
        this.size++;
    }

    /**
     * Returns the element at the left of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  Getting node.getNext() and node.getElement() happen in constant time.
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
        return this.leftHead.getNext().getElement();
    }

    /**
     * Returns the element at the left of the deque, but does not remove it.
     *
     * Runtime Complexity: O(1)
     *  Getting node.getPrevious() and node.getElement() happen in constant time.
     *
     * Memory Complexity: O(1)
     *  Constant memory used, no new memory used
     *
     * @returns the leftmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.rightTail.getPrevious().getElement();
    }

    /**
     * Removes and returns the element at the left of the deque.
     *
     * Runtime complexity: O(1)
     *  node.getNext , node.setPrevious, node.setNext, this.peekLeft(), size decrement take constant time.
     *
     * Memory complexity: O(1)
     *  variable returnElement is created to temporarily store return value takes constant memory.
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
        //set node after left
        this.leftHead.getNext().getNext().setPrevious(this.leftHead);
        //set head to above node
        this.leftHead.setNext(this.leftHead.getNext().getNext());
        this.size--;
        return returnElement;
    }

    /**
     * Removes and returns the element at the right of the deque.
     *
     * Runtime complexity: O(1)
     *  node.getNext , node.setPrevious, node.setNext, this.peekLeft(), size decrement take constant time.
     *
     * Memory complexity: O(1)
     *  variable returnElement is created to temporarily store return value takes constant memory.
     *
     * @returns the rightmost element
     * @throws NoSuchElementException if the deque is empty
     */
    @Override
    public T popRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        T returnElement = this.rightTail.getPrevious().getElement();
        //set third last node to tail
        this.rightTail.getPrevious().getPrevious().setNext(this.rightTail);
        //set prev for tail
        this.rightTail.setPrevious(this.rightTail.getPrevious().getPrevious());
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
     * Runtime Complexity: O(n)
     *  The methods hasNext has O(1) and next() has O(n) runtime, for a iterator call next in the worst case time
     *  complexity will be O(n).
     *
     * Memory Complexity: O(1)
     *  Just iterates on the linkedList, nodes already exist hence constant memory complexity.
     *
     * @returns an iterator over the elements in in order from leftmost to rightmost.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            //current node number (order)
            private int counter = 0;

            /**
             * Return true if iterator has a next element, false if it does not have next element
             *
             * Runtime complexity: O(1)
             *  Comparison and return statement
             *
             * Memory Complexity: O(1)
             *  constant memory use
             *
             * @return True if another element exists, false if it does not
             */
            @Override
            public boolean hasNext() {
                return counter < size;
            }

            /**
             * Returns the next element in the deque iterator
             *
             * Runtime complexity: O(n)
             *  For a linked list of length n, needs to iterate over all nodes from leftHead to rightTail node
             *
             * Memory complexity: O(1)
             *  tempNode is overridden hence take up constant memory. All other operations like counter++ take constant
             *  memory.
             *
             * @return next element in deque iterator
             */
            @Override
            public T next() {
                Node<T> tempNode = leftHead.getNext();
                for (int i = 1; i <= this.counter; i++) {
                    tempNode = tempNode.getNext();
                }
                this.counter++;
                return tempNode.getElement();
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
     * Runtime Complexity: O(n)
     *  The methods hasNext has O(1) and next() has O(n) runtime, for a iterator call next in the worst case time
     *  complexity will be O(n).
     *
     * Memory Complexity: O(1)
     *  Just iterates on the linkedList, nodes already exist hence constant memory complexity.
     *
     * @returns an iterator over the elements in in order from leftmost to rightmost.
     */
    @Override
    public Iterator<T> reverseIterator() {
        return new Iterator<T>() {
            private int counter = 0;

            /**
             * Return true if iterator has a next element, false if it does not have next element
             *
             * Runtime complexity: O(1)
             *  Comparison and return statement
             *
             * Memory Complexity: O(1)
             *  constant memory use
             *
             * @return True if another element exists, false if it does not
             */
            @Override
            public boolean hasNext() {
                return counter < size;
            }

            /**
             * Returns the next element in the deque iterator
             *
             * Runtime complexity: O(n)
             *  For a linked list of length n, needs to iterate over all nodes from rightTail to leftHead node.
             *
             * Memory complexity: O(1)
             *  tempNode is overridden hence take up constant memory. All other operations like counter++ take constant
             *  memory.
             *
             * @return next element in deque iterator
             */
            @Override
            public T next() {
                Node<T> tempNode = rightTail.getPrevious();
                for (int i = 1; i <= this.counter; i++) {
                    tempNode = tempNode.getPrevious();
                }
                this.counter++;
                return tempNode.getElement();
            }
        };
    }
}