package comp210.a5;

import bridges.base.BSTElement;
import bridges.connect.Bridges;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * EXTRA CREDIT. A Dictionary from words to counts, built as a binary search
 * tree (L17) out of BRIDGES BSTElement nodes, so BRIDGES can draw it.
 *
 * The invariant: every key in a node's left subtree is less than the node's
 * key, and every key in its right subtree is greater. Compare Strings with
 * compareTo.
 *
 * To try it, swap "new HashTable()" for "new BST()" in ShakespeareWords and
 * check that the top words come out the same.
 */
public class BST implements Dictionary<String, Integer> {

    private BSTElement<String, Integer> root;
    private int size;

    public BST() {
        root = null;
        size = 0;
    }

    // ------------------------------------------------------------------
    // Your part: get, set, and the iterator below. Keep the two fields
    // above. A node's key is getKey(), its count is getValue(), and its
    // children are getLeft() and getRight(). Make a leaf with
    // new BSTElement<>(key, value, null, null). The two-argument
    // constructor looks right but treats key as a display label, so
    // getKey() comes back null.
    // ------------------------------------------------------------------

    /** Returns the count stored under key, or null if key is absent. */
    @Override
    public Integer get(String key) {
        // TODO
        return null;
    }

    /**
     * Stores value under key, replacing the old value if key is already in
     * the tree, otherwise adding a new leaf where the search fell off.
     *
     * @throws IllegalArgumentException if key or value is null
     */
    @Override
    public void set(String key, Integer value) {
        // TODO (a recursive helper that returns the subtree's new root works well)
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Visits every entry in alphabetical order (an in-order walk). Use a
     * stack of nodes instead of recursion: push the path down the left side,
     * and after handing out a node, push the left side of its right subtree.
     */
    class BSTIterator implements Iterator<Map.Entry<String, Integer>> {
        private final Deque<BSTElement<String, Integer>> stack = new ArrayDeque<>();

        BSTIterator() {
            // TODO
        }

        @Override
        public boolean hasNext() {
            // TODO
            return false;
        }

        @Override
        public Map.Entry<String, Integer> next() {
            // TODO
            throw new NoSuchElementException();
        }
    }

    // ------------------------------------------------------------------
    // Provided.
    // ------------------------------------------------------------------

    @Override
    public Iterator<Map.Entry<String, Integer>> iterator() {
        return new BSTIterator();
    }

    /** The root node, for drawing and for the autograder. */
    BSTElement<String, Integer> root() {
        return root;
    }

    /** Labels every node "word: count" and sends the tree to BRIDGES. */
    public void visualize(Bridges bridges) {
        if (root == null) {
            System.out.println("The tree is empty, so there is nothing to draw.");
            return;
        }
        label(root);
        bridges.setDataStructure(root);
        try {
            bridges.visualize();
        } catch (Exception e) {
            System.err.println("BRIDGES could not draw the tree: " + e.getMessage());
        }
    }

    private void label(BSTElement<String, Integer> n) {
        if (n != null) {
            n.setLabel(n.getKey() + ": " + n.getValue());
            label(n.getLeft());
            label(n.getRight());
        }
    }
}
