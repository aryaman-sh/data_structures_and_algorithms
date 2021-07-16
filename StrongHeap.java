import java.util.LinkedList;
import java.util.Queue;

public class StrongHeap {
    /**
     * Determines whether the binary tree with the given root node is
     * a "strong binary heap", as described in the assignment task sheet.
     *
     * A strong binary heap is a binary tree which is:
     *  - a complete binary tree, AND
     *  - its values satisfy the strong heap property.
     *
     * Runtime complexity: O(n)
     * Memory complexity: O(n)
     *
     * @param root root of a binary tree, cannot be null.
     * @return true if the tree is a strong heap, otherwise false.
     */
    public static boolean isStrongHeap(BinaryTree<Integer> root) {
        Queue<BinaryTree<Integer>> treeNodesQueue = new LinkedList<>();
        //flag becomes true on reaching node at h-1 with one or both leaves as null
        boolean flag = false;
        treeNodesQueue.add(root);
        while (treeNodesQueue.size() != 0) {
            BinaryTree<Integer> focusTree = treeNodesQueue.remove();
            if (!flag) {
                //check left leaf
                if (focusTree.getLeft() == null) {
                    if (focusTree.getRight() != null) {
                        return false;
                    }
                    flag = true;
                } else {
                    //add left leave to queue
                    treeNodesQueue.add(focusTree.getLeft());
                    //check strong heap property: parent(x) > x
                    if (focusTree.getValue() <= focusTree.getLeft().getValue()) {
                        return false;
                    }
                }
                //check right leaf
                if (focusTree.getRight() == null) {
                    flag = true;
                } else {
                    treeNodesQueue.add(focusTree.getRight());
                    //check strong heap property: parent(x) > x
                    if (focusTree.getValue() <= focusTree.getRight().getValue()) {
                        return false;
                    }
                    if (!checkSum(focusTree)) {
                        return false;
                    }
                }
                //heap sum property 2
                if (!flag) {
                    if (!checkSum(focusTree)) {
                        return false;
                    }
                }
            } else {
                //flag is true, remaining nodes must not have any children
                if (focusTree.getLeft() != null || focusTree.getRight() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * A helper to check strong heap property parent(parent x) > parent(x) + x
     *
     * Runtime complexity: O(1)
     * Memory complexity: O(1)
     *
     * @param root the parent(parent(x)) node
     * @return boolean: true if strong heap property is satisfied, false otherwise
     */
    private static boolean checkSum (BinaryTree<Integer> root) {
        if (root.getLeft().getLeft() != null) {
            return root.getValue() > root.getLeft().getValue() + root.getLeft().getLeft().getValue();
        }
        if (root.getLeft().getRight() != null) {
            return root.getValue() > root.getLeft().getValue() + root.getLeft().getRight().getValue();
        }
        if (root.getRight().getLeft() != null) {
            return root.getValue() > root.getRight().getValue() + root.getRight().getLeft().getValue();
        }
        if (root.getRight().getRight() != null) {
            return root.getValue() > root.getRight().getValue() + root.getRight().getRight().getValue();
        }
        return true;
    }
}