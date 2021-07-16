import javax.swing.plaf.ComponentInputMapUIResource;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class SortingAlgorithms {
    /**
     * Sorts the given array using the selection sort algorithm.
     * This should modify the array in-place.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void selectionSort(T[] input, boolean reversed) {
        selectionSortHelper(input, input.length, reversed);
    }

    /**
     * Selection sort recursive helper method
     * @param array the array to be sorted
     * @param sortingLength length of array to sort
     * @param flag If false, array sorted in ascending order.
     *             Otherwise, sorted in descending order.
     */
    private static <T extends Comparable> void selectionSortHelper(T[] array, int sortingLength, boolean flag) {
        if (sortingLength > 1) {
            //operative index = maxIndex when !flag , minIndex when flag
            int operativeIndex = 0;
            for (int i = 0; i < sortingLength; i++) {
                if (!flag) {
                    if (array[i].compareTo(array[operativeIndex]) > 0) {
                        operativeIndex = i;
                    }
                } else {
                    if (array[i].compareTo(array[operativeIndex]) < 0) {
                        operativeIndex = i;
                    }
                }
            }
            //swap elements
            T tempHolder = array[operativeIndex];
            array[operativeIndex] = array[sortingLength - 1];
            array[sortingLength - 1] = tempHolder;
            //recursive call
            selectionSortHelper(array, sortingLength - 1, flag);
        }
    }

    /**
     * Sorts the given array using the insertion sort algorithm.
     * This should modify the array in-place.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void insertionSort(T[] input, boolean reversed) {
        for (int keyIndex = 0; keyIndex < input.length; keyIndex++) {
            T key = input[keyIndex];
            int iterationIndex = keyIndex - 1;
            if (!reversed) {
                while (iterationIndex >= 0 && input[iterationIndex].compareTo(key) > 0) {
                    input[iterationIndex + 1] = input[iterationIndex];
                    iterationIndex -= 1;
                }
            } else {
                while (iterationIndex >= 0 && input[iterationIndex].compareTo(key) < 0) {
                    input[iterationIndex + 1] = input[iterationIndex];
                    iterationIndex -= 1;
                }
            }
            input[iterationIndex + 1] = key;
        }
    }

    /**
     * Sorts the given array using the merge sort algorithm.
     * This should modify the array in-place.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void mergeSort(T[] input, boolean reversed) {
        int left = 0;
        int right = input.length ;
        if (right >= 2) {
            int middle = (right) / 2;
            //Create first half subarray
            int firstHalfLength = input.length / 2;
            T[] firstHalfSubArr = (T[]) new Comparable[firstHalfLength];
            for (int i = 0; i < middle; i++) {
                firstHalfSubArr[i] = input[i];
            }
            //Create second half subarray
            int secondHalfLength = input.length - firstHalfLength;
            T[] secondHalfSubArr = (T[]) new Comparable[secondHalfLength];
            for (int j = middle; j < right; j++) {
                secondHalfSubArr[j - middle] = input[j];
            }
            //recursive step
            mergeSort(firstHalfSubArr, reversed);
            mergeSort(secondHalfSubArr, reversed);
            merge(firstHalfSubArr, secondHalfSubArr, input, reversed);
        }
    }

    /**
     * Combines arrays of two sorted halves to origionalArr
     * @param sortedHalf1 sorted array
     * @param sortedHalf2 sorted array
     * @param inputArr OrigionalArr
     * @param reversed reversed flag
     */
    private static <T extends Comparable> void merge(T[] sortedHalf1, T[] sortedHalf2, T[] inputArr, boolean reversed) {
        int leftHalfIndex = 0;
        int rightHalfIndex = 0;
        if (!reversed) {
            while (leftHalfIndex + rightHalfIndex < inputArr.length) {
                if (rightHalfIndex == sortedHalf2.length || (leftHalfIndex < sortedHalf1.length
                        && sortedHalf1[leftHalfIndex].compareTo(sortedHalf2[rightHalfIndex]) < 0)) {
                    inputArr[leftHalfIndex+rightHalfIndex] = sortedHalf1[leftHalfIndex++];
                } else {
                    inputArr[leftHalfIndex+rightHalfIndex] = sortedHalf2[rightHalfIndex++];
                }
            }
        } else {
            while (leftHalfIndex + rightHalfIndex < inputArr.length) {
                if (rightHalfIndex == sortedHalf2.length || (leftHalfIndex < sortedHalf1.length
                        && sortedHalf1[leftHalfIndex].compareTo(sortedHalf2[rightHalfIndex]) > 0)) {
                    inputArr[leftHalfIndex+rightHalfIndex] = sortedHalf1[leftHalfIndex++];
                } else {
                    inputArr[leftHalfIndex+rightHalfIndex] = sortedHalf2[rightHalfIndex++];
                }
            }
        }
    }

    /**
     * Sorts the given array using the quick sort algorithm.
     * This should modify the array in-place.
     *
     * You should use the value at the middle of the input  array(i.e. floor(n/2))
     * as the pivot at each step.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void quickSort(T[] input, boolean reversed) {
        quickSortHelper(input, 0, input.length-1, reversed);
    }

    /**
     * Sorts the given array between index a and b inclusive
     * @param input array to be sorted
     * @param first first index to sort inclusive
     * @param last last index to sort inclusive
     * @param reversed If false sort ascending, otherwise sort descending
     */
    private static <T extends Comparable> void quickSortHelper(T[] input, int first, int last, boolean reversed) {
        if(! (first >= last)) {
            int length = first + last + 1;
            int positionToChange = length / 2;
            //switch pivot position
            T temp2 = input[last];
            int pivotIndex = length / 2;
            input[last] = input[pivotIndex];
            input[length / 2] = temp2;
            int left = first;
            int right = last - 1;
            T pivot = input[last];
            //temp object used for swapping
            T temp;
            while (left <= right) {
                if (!reversed) {
                    while (left <= right && input[left].compareTo(pivot) < 0) {
                        left++;
                    }
                    while (left <= right && input[right].compareTo(pivot) > 0) {
                        right--;
                    }
                } else {
                    while (left <= right && input[left].compareTo(pivot) > 0) {
                        left++;
                    }
                    while (left <= right && input[right].compareTo(pivot) < 0) {
                        right--;
                    }
                }
                if (left <= right) {
                    temp = input[left];
                    input[left] = input[right];
                    input[right] = temp;
                    left++;
                    right--;
                }
            }
            //swap
            temp = input[left];
            input[left] = input[last];
            input[last] = temp;
            //recursive step
            quickSortHelper(input,first,left-1,reversed);
            quickSortHelper(input,left +1, last,reversed);
        }
    }
}