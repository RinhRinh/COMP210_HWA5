package comp210.a5;

import bridges.base.BinTreeElement;
import bridges.connect.Bridges;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A max-heap of key/value pairs stored in an array, the layout from L18.
 *
 * The tree lives in heap[0] through heap[size - 1]. For the entry at index i:
 *
 *     left child   2i + 1
 *     right child  2i + 2
 *     parent       (i - 1) / 2
 *
 * The invariant: every entry's key is greater than or equal to the keys of
 * its children, so the largest key is always at heap[0].
 *
 * Do not use java.util.PriorityQueue, Arrays.sort, or any other collection in
 * here. The array and the two percolates are yours.
 */
public class MaxHeap<K extends Comparable<K>, V> {

    private Map.Entry<K, V>[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MaxHeap() {
        heap = (Map.Entry<K, V>[]) new Map.Entry[8];
        size = 0;
    }

    // ------------------------------------------------------------------
    // Your part: insert, pop, percolateUp, percolateDown, and buildTree.
    // Keep the two fields above; the provided methods and the autograder
    // read them.
    // ------------------------------------------------------------------

    /**
     * Adds a key/value pair. Put it in the first open slot, heap[size], then
     * percolate it up. Call ensureRoom() first so the array has space.
     *
     * @throws IllegalArgumentException if key is null
     */
    public void insert(K key, V value) {
        // TODO: make the entry with new AbstractMap.SimpleEntry<>(key, value)
    }

    /**
     * Removes and returns the entry with the largest key. Move the last
     * entry into heap[0], shrink size, then percolate it down.
     *
     * @throws NoSuchElementException if the heap is empty
     */
    public Map.Entry<K, V> pop() {
        // TODO
        return null;
    }

    /**
     * Swaps the entry at index i with its parent while it is larger than the
     * parent. Loop invariant: heap order holds everywhere except possibly
     * between i and its parent.
     */
    private void percolateUp(int i) {
        // TODO
    }

    /**
     * Swaps the entry at index i with its larger child while that child is
     * larger. Loop invariant: heap order holds everywhere except possibly
     * between i and its children.
     */
    private void percolateDown(int i) {
        // TODO
    }

    /**
     * Builds the BRIDGES tree for the subheap rooted at index i, or returns
     * null when i is past the last entry. Make a BinTreeElement holding the
     * entry's value, label it with label(i), and attach the trees built from
     * the two child indexes.
     */
    private BinTreeElement<V> buildTree(int i) {
        // TODO
        return null;
    }

    // ------------------------------------------------------------------
    // Provided.
    // ------------------------------------------------------------------

    /** Returns the entry with the largest key without removing it. */
    public Map.Entry<K, V> peek() {
        if (size == 0) {
            throw new NoSuchElementException("peek on an empty heap");
        }
        return heap[0];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Doubles the array when it is full. Call it before adding at heap[size]. */
    private void ensureRoom() {
        if (size == heap.length) {
            @SuppressWarnings("unchecked")
            Map.Entry<K, V>[] bigger = (Map.Entry<K, V>[]) new Map.Entry[2 * heap.length];
            System.arraycopy(heap, 0, bigger, 0, size);
            heap = bigger;
        }
    }

    private void swap(int i, int j) {
        Map.Entry<K, V> tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /** The text BRIDGES shows on the node for heap[i], like "the: 412". */
    String label(int i) {
        return heap[i].getValue() + ": " + heap[i].getKey();
    }

    /** The whole heap as a BRIDGES tree, or null when the heap is empty. */
    public BinTreeElement<V> toTree() {
        return buildTree(0);
    }

    /** Sends the heap to BRIDGES as a binary tree. */
    public void visualize(Bridges bridges) {
        BinTreeElement<V> root = toTree();
        if (root == null) {
            System.out.println("The heap is empty, so there is nothing to draw.");
            return;
        }
        bridges.setDataStructure(root);
        try {
            bridges.visualize();
        } catch (Exception e) {
            System.err.println("BRIDGES could not draw the heap: " + e.getMessage());
        }
    }
}
