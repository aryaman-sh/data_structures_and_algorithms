public class QuaternaryHeapsort {

    /**
     * Sorts the input array, in-place, using a quaternary heap sort.
     *
     * Runtime complexity: O(n^3)
     * Memory complexity: O(1)
     *
     * @param input to be sorted (modified in place)
     */
    public static <T extends Comparable<T>> void quaternaryHeapsort(T[] input) {
        mainSortHelper(input,0, input.length);
    }

    /**
     * Sorts the input array, in place by using array to represent a quaternary heap
     *
     * Runtime complexity: O(n^3)
     * Memory complexity: O(1)
     *
     * @param input array to be sorted
     * @param start index in array where sorting needs to start
     * @param size size of array representation of heap to sort
     */
    public static <T extends Comparable<T>> void mainSortHelper(T[] input, int start, int size) {
        if (size > 0) {
            int heapPointer = size - 1;
            //downheap on last subtree
            quaternaryDownheap(input, parent(heapPointer), size);
            //number of leaves of last subtree
            int childrenLastSubTree = 0;
            for (int child = 1; child <= 4; child++) {
                if (childExistence(child,parent(heapPointer), size)) {
                    childrenLastSubTree += 1;
                }
            }
            heapPointer -= childrenLastSubTree;
            while (heapPointer >= 0) {
                quaternaryDownheap(input, parent(heapPointer), size);
                heapPointer -= 4;
            }
            //swap root (largest of heap) to end of array
            swap(input, start,size - 1);
            //recursive step
            mainSortHelper(input, start, size - 1);
        }
    }

    /**
     * Performs a downheap from the element in the given position on the given max heap array.
     *
     * A downheap should restore the heap order by swapping downwards as necessary.
     * The array should be modified in place.
     *
     * You should only consider elements in the input array from index 0 to index (size - 1)
     * as part of the heap (i.e. pretend the input array stops after the inputted size).
     *
     * Runtime Complexity: O(n)
     * Memory Complexity: O(1)
     *
     * @param input array representing a quaternary max heap.
     * @param start position in the array to start the downheap from.
     * @param size the size of the heap in the input array, starting from index 0
     */
    public static <T extends Comparable<T>> void quaternaryDownheap(T[] input, int start, int size) {
        int firstChildIndex;
        int bigChildIndex;
        //while at least one child (leftmost) exists
        while (childExistence(1, start, size)) {
            firstChildIndex = childIndex(1, start);
            bigChildIndex = firstChildIndex;
            //Check existence of other children and identify biggest value child
            for (int childNumber = 2; childNumber <= 4; childNumber++) {
                if (childExistence(childNumber, start, size)) {
                    if (input[bigChildIndex].compareTo(input[childIndex(childNumber, start)]) < 0) {
                        bigChildIndex = childIndex(childNumber, start);
                    }
                }
            }
            //check heap property satisfied
            if (input[start].compareTo(input[bigChildIndex]) > 0) {
                break;
            }
            //swap
            swap(input, start, bigChildIndex);
            start = bigChildIndex;
        }
    }

    /**
     * Swaps the elements at given index in the given array
     *
     * Runtime complexity: O(1)
     * memory complexity: O(1)
     *
     * @param inputArr Array elements of
     * @param index1 index of first element
     * @param index2 index of second element
     * @param <T>
     */
    private static <T extends Comparable<T>> void swap(T[] inputArr, int index1, int index2) {
        T temp = inputArr[index1];
        inputArr[index1] = inputArr[index2];
        inputArr[index2] = temp;
    }

    /**
     * Returns index of parent node
     *
     * runtime complexity: O(1)
     * memory complexity: O(1)
     *
     * @param index return parent of child at index
     * @return index of parent in array
     */
    private static int parent(int index) {
        return (index - 1) / 4;
    }

    /**
     * Returns index of child.
     * child number 1: first child, left child
     * child number 2: second child, middle left
     * child number 3: third child, middle right
     * child number 4: fourth child, right child
     *
     * runtime complexity: O(1)
     * memory complexity: O(1)
     *
     * @param childNumber Child whose index is required
     * @param index index of parent
     * @return int , index of child
     */
    private static int childIndex(int childNumber, int index) {
        return 4 * index + childNumber;
    }

    /**
     * Return true if the child (child number) exists
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @param childNumber child whose existence needs to be checked
     * @param index index of parent
     * @param size size of heap
     * @return Boolean, true if child exists false otherwise
     */
    private static Boolean childExistence(int childNumber, int index, int size) {
        return childIndex(childNumber, index) < size;
    }
}