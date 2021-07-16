import java.util.Comparator;

/**
 * A comparator for Binary Trees.
 */
public class BinaryTreeComparator<E extends Comparable<E>> implements Comparator<BinaryTree<E>> {

    /**
     * Compares two binary trees with the given root nodes.
     *
     * Two nodes are compared by their left childs, their values, then their right childs,
     * in that order. A null is less than a non-null, and equal to another null.
     *
     * Runtime complexity: O(n)
     * Memory complexity: O(1)
     *
     * @param tree1 root of the first binary tree, may be null.
     * @param tree2 root of the second binary tree, may be null.
     * @return -1, 0, +1 if tree1 is less than, equal to, or greater than tree2, respectively.
     */
    @Override
    public int compare(BinaryTree<E> tree1, BinaryTree<E> tree2) {
        int compareResult = 0;
        //In order traversal
        if (tree1.getLeft() != null && tree2.getLeft() != null) {
            //both tress have valid node. recursive step
            compareResult = compare(tree1.getLeft(), tree2.getLeft());
            if (compareResult == 1 || compareResult == -1) {
                return compareResult;
            }
        } else if (tree1.getLeft() == null && tree2.getLeft() != null) {
            return -1;
        } else if (tree2.getLeft() == null && tree1.getLeft() != null) {
            return 1;
        }
        //node value comparison
        if (tree1.getValue().compareTo(tree2.getValue()) > 0) {
            return 1;
        } else if (tree1.getValue().compareTo(tree2.getValue()) < 0) {
            return -1;
        }
        //traversal
        if (tree1.getRight() != null && tree2.getRight() != null) {
            //valid nodes for recursion
            compareResult = compare(tree1.getRight(), tree2.getRight());
            if (compareResult == 1 || compareResult == -1) {
                return compareResult;
            }
        } else if (tree1.getRight() == null && tree2.getRight() != null) {
            return -1;
        } else if (tree2.getRight() == null && tree1.getRight() != null) {
            return 1;
        }
        return compareResult;
    }
}