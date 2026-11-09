package comp210.a5;

import bridges.base.Color;
import bridges.base.Polyline;
import bridges.base.Rectangle;
import bridges.base.SymbolCollection;
import bridges.base.Text;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A Dictionary from words to counts, built as a hash table with separate
 * chaining (L14/15). Each slot of table holds a linked chain of Nodes, and a
 * key lives in the chain at index indexFor(key, table.length).
 *
 * The invariant: every key sits in the bucket its hashCode selects, and
 * count / table.length stays at or under maxLoadFactor.
 *
 * Do not use any java.util collection in here (HashMap, ArrayList, LinkedList,
 * and so on). The chains are your own Nodes.
 */
public class HashTable implements Dictionary<String, Integer> {

    /** One link in a chain. */
    static class Node {
        String key;
        int value;
        Node next;

        Node(String key, int value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node[] table;
    private final double maxLoadFactor;
    private int count;

    /** 16 buckets, resizing once the load factor would pass 0.75. */
    public HashTable() {
        this(16, 0.75);
    }

    public HashTable(int capacity, double maxLoadFactor) {
        if (capacity < 1 || maxLoadFactor <= 0) {
            throw new IllegalArgumentException("capacity must be at least 1 and maxLoadFactor above 0");
        }
        this.table = new Node[capacity];
        this.maxLoadFactor = maxLoadFactor;
        this.count = 0;
    }

    // ------------------------------------------------------------------
    // Your part: indexFor, get, set, resize, and the iterator below. Keep
    // the three fields above; the provided methods and the autograder read
    // them.
    // ------------------------------------------------------------------

    /**
     * Returns the bucket index for key in a table of the given capacity: a
     * number from 0 to capacity - 1, picked from key.hashCode().
     *
     * hashCode() can be negative, and Java's % keeps the sign, so -7 % 4 is
     * -3. Math.floorMod(a, b) always lands in 0 to b - 1.
     */
    int indexFor(String key, int capacity) {
        // TODO
        return 0;
    }

    /** Returns the count stored under key, or null if key is absent. */
    @Override
    public Integer get(String key) {
        // TODO
        return null;
    }

    /**
     * Stores value under key, replacing the old value if key is already
     * there. Adding a new key that pushes count above
     * maxLoadFactor * table.length doubles the table with resize.
     *
     * @throws IllegalArgumentException if key or value is null
     */
    @Override
    public void set(String key, Integer value) {
        // TODO
    }

    /** Returns how many distinct keys are stored. */
    @Override
    public int size() {
        return count;
    }

    /**
     * Moves every entry into a new table of newCapacity buckets. Each key
     * goes to the bucket indexFor picks for the NEW capacity, which is
     * often a different bucket than before.
     */
    private void resize(int newCapacity) {
        // TODO
    }

    /**
     * Visits every entry once: bucket 0's chain, then bucket 1's, and so on.
     * Skip empty buckets. next() throws NoSuchElementException when there is
     * nothing left.
     */
    class HashTableIterator implements Iterator<Map.Entry<String, Integer>> {
        private int bucket;     // the bucket that current belongs to
        private Node current;   // the next node to hand out, or null when done

        HashTableIterator() {
            // TODO: point current at the first node of the first non-empty bucket
        }

        @Override
        public boolean hasNext() {
            // TODO
            return false;
        }

        /** Hand out each entry as new AbstractMap.SimpleEntry<>(key, value). */
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
        return new HashTableIterator();
    }

    /** How many buckets the table has right now. */
    public int capacity() {
        return table.length;
    }

    /** Length of the longest chain, a quick check on how even the spread is. */
    public int longestChain() {
        int longest = 0;
        for (Node chain : table) {
            int len = 0;
            for (Node n = chain; n != null; n = n.next) {
                len++;
            }
            longest = Math.max(longest, len);
        }
        return longest;
    }

    /**
     * Draws the table: one row per bucket, with its chain running to the
     * right. Best on a small table; a few thousand rows is hard to read.
     */
    public void visualize(Bridges bridges) throws IOException, RateLimitException {
        SymbolCollection vis = new SymbolCollection();
        float labelWidth = 100;
        float labelHeight = 25;
        float spacing = 50;
        float maxX = 0;

        for (int i = 0; i < table.length; i++) {
            float x = 0;
            float y = (table.length - i) * 30;

            Rectangle slot = new Rectangle(x, y, 25, 25);
            slot.setFillColor(new Color(255, 255, 255, 0));
            slot.setStrokeColor("black");
            vis.addSymbol(slot);

            Text index = new Text(String.valueOf(i));
            index.setAnchorLocation(x + 12.5f, y + 12.5f);
            index.setAnchorAlignment("middle", "middle");
            index.setFontSize(12.0f);
            vis.addSymbol(index);

            x += 62.5f;
            for (Node n = table[i]; n != null; n = n.next) {
                Rectangle box = new Rectangle(x, y, labelWidth, labelHeight);
                box.setFillColor(new Color(255, 255, 255, 0));
                box.setStrokeColor("black");
                vis.addSymbol(box);

                Text label = new Text(n.key + ": " + n.value);
                label.setAnchorLocation(x + labelWidth / 2, y + labelHeight / 2);
                label.setAnchorAlignment("middle", "middle");
                label.setFontSize(12.0f);
                vis.addSymbol(label);

                Polyline arrow = new Polyline();
                arrow.addPoint(x + labelWidth, y + labelHeight / 2);
                arrow.addPoint(x + labelWidth + spacing, y + labelHeight / 2);
                arrow.addPoint(x + labelWidth + 0.75f * spacing, y);
                arrow.addPoint(x + labelWidth + spacing, y + labelHeight / 2);
                arrow.addPoint(x + labelWidth + 0.75f * spacing, y + labelHeight);
                arrow.setStrokeWidth(1);
                arrow.setStrokeColor("red");
                arrow.setFillColor(new Color(255, 255, 255, 0));
                vis.addSymbol(arrow);

                x += labelWidth + spacing;
            }
            maxX = Math.max(maxX, x);

            Rectangle end = new Rectangle(x, y, labelWidth, labelHeight);
            end.setFillColor(new Color(255, 255, 255, 0));
            end.setStrokeColor("black");
            vis.addSymbol(end);

            Text nul = new Text("null");
            nul.setAnchorLocation(x + labelWidth / 2, y + labelHeight / 2);
            nul.setAnchorAlignment("middle", "middle");
            nul.setFontSize(12.0f);
            vis.addSymbol(nul);
        }

        vis.setViewport(-30f, maxX + 200, -30f, (table.length + 1) * 30f);
        bridges.setDataStructure(vis);
        bridges.visualize();
    }
}
