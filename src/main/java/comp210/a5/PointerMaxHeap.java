package comp210.a5;

import bridges.base.BinTreeElement;
import bridges.connect.Bridges;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * EXTRA CREDIT. The same max-heap as MaxHeap, stored as linked tree nodes
 * instead of an array. This is the version the original BRIDGES assignment
 * uses.
 *
 * An array heap knows where the next entry goes: heap[size]. A linked heap
 * has no index to compute, so each node keeps the sizes of its two subheaps
 * and every insert walks toward the smaller side. That keeps the tree
 * balanced while you only insert.
 *
 * The invariant is the same heap order: every node's key is greater than or
 * equal to the keys of its children.
 */
public class PointerMaxHeap<K extends Comparable<K>, V> {

    /** One node: a BRIDGES tree node plus a key and the two subheap sizes. */
    static class HeapNode<K, V> extends BinTreeElement<V> {
        K key;
        int sizeLeft;
        int sizeRight;

        HeapNode(K key, V value) {
            super(value);
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        HeapNode<K, V> left() {
            return (HeapNode<K, V>) getLeft();
        }

        @SuppressWarnings("unchecked")
        HeapNode<K, V> right() {
            return (HeapNode<K, V>) getRight();
        }
    }

    private HeapNode<K, V> root;

    // ------------------------------------------------------------------
    // Your part: insert and pop. Keep the root field; the provided methods
    // and the autograder read it.
    // ------------------------------------------------------------------

    /**
     * Adds a key/value pair. Recursive, starting at the root:
     *
     *   insert(h, k, v):
     *     if h is null, return a new node holding k, v
     *     if k > h.key, swap (k, v) with h's key and value
     *     if h.sizeLeft <= h.sizeRight:
     *       h.sizeLeft += 1,  h.left  = insert(h.left, k, v)
     *     else:
     *       h.sizeRight += 1, h.right = insert(h.right, k, v)
     *     return h
     *
     * @throws IllegalArgumentException if key is null
     */
    public void insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("PointerMaxHeap keys cannot be null");
        }
        // TODO
    }

    /**
     * Removes and returns the entry with the largest key. Recursive:
     *
     *   removeTop(h):   (h's own entry is being thrown away)
     *     if h has no children, return null
     *     pick the child with the larger key (or the only child)
     *     copy that child's key and value into h
     *       (h.key = child.key; h.setValue(child.getValue()))
     *     subtract 1 from h's size on that side
     *     set that child to removeTop(child)
     *     return h
     *
     * @throws NoSuchElementException if the heap is empty
     */
    public Map.Entry<K, V> pop() {
        if (root == null) {
            throw new NoSuchElementException("pop on an empty heap");
        }
        // TODO
        return null;
    }

    // ------------------------------------------------------------------
    // Provided.
    // ------------------------------------------------------------------

    public Map.Entry<K, V> peek() {
        if (root == null) {
            throw new NoSuchElementException("peek on an empty heap");
        }
        return new AbstractMap.SimpleEntry<>(root.key, root.getValue());
    }

    public int size() {
        return root == null ? 0 : 1 + root.sizeLeft + root.sizeRight;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /** The root node, for drawing and for the autograder. */
    HeapNode<K, V> root() {
        return root;
    }

    /** Labels every node "value: key" and sends the tree to BRIDGES. */
    public void visualize(Bridges bridges) {
        if (root == null) {
            System.out.println("The heap is empty, so there is nothing to draw.");
            return;
        }
        label(root);
        bridges.setDataStructure(root);
        try {
            bridges.visualize();
        } catch (Exception e) {
            System.err.println("BRIDGES could not draw the heap: " + e.getMessage());
        }
    }

    private void label(HeapNode<K, V> n) {
        if (n != null) {
            n.setLabel(n.getValue() + ": " + n.key);
            label(n.left());
            label(n.right());
        }
    }
}
