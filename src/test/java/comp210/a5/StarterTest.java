package comp210.a5;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A few starter tests. Right-click this file in IntelliJ and choose Run.
 *
 * These are a sample, not the whole autograder. Gradescope runs many more,
 * including resizing, collisions, big heaps, and timing. Passing everything
 * here is a good sign, not a guarantee. Add your own tests below.
 */
class StarterTest {

    @Test
    void hashTableStoresAndFindsCounts() {
        HashTable t = new HashTable();
        t.set("thou", 3);
        t.set("art", 1);
        assertEquals(3, t.get("thou"));
        assertEquals(1, t.get("art"));
        assertNull(t.get("doth"), "a word that was never added comes back null");
        assertEquals(2, t.size());
    }

    @Test
    void hashTableSetReplacesTheOldValue() {
        HashTable t = new HashTable();
        t.set("wherefore", 1);
        t.set("wherefore", 2);
        assertEquals(2, t.get("wherefore"));
        assertEquals(1, t.size(), "setting the same word twice is still one word");
    }

    @Test
    void heapPopsLargestFirst() {
        MaxHeap<Integer, String> h = new MaxHeap<>();
        h.insert(5, "five");
        h.insert(12, "twelve");
        h.insert(1, "one");
        h.insert(8, "eight");
        assertEquals(12, h.pop().getKey());
        assertEquals(8, h.pop().getKey());
        assertEquals(5, h.pop().getKey());
        assertEquals(1, h.pop().getKey());
        assertTrue(h.isEmpty());
    }

    @Test
    void emptyHeapRefusesToPop() {
        MaxHeap<Integer, String> h = new MaxHeap<>();
        assertThrows(NoSuchElementException.class, h::pop);
    }

    @Test
    void countWordsCountsEveryWord() {
        HashTable t = new HashTable();
        WordStats.countWords(WordStats.tokenize("To be, or not to be"), t);
        assertEquals(2, t.get("to"));
        assertEquals(2, t.get("be"));
        assertEquals(1, t.get("not"));
        assertEquals(4, t.size());
    }

    @Test
    void topWordsComeOutMostFrequentFirst() {
        StandardDictionary<String, Integer> d = new StandardDictionary<>();
        d.set("the", 50);
        d.set("ham", 30);
        d.set("yorick", 2);
        List<Map.Entry<String, Integer>> top = WordStats.topWords(d, 2);
        assertEquals(2, top.size());
        assertEquals("the", top.get(0).getKey());
        assertEquals("ham", top.get(1).getKey());
    }

    @Test
    void iteratorVisitsEveryWordOnce() {
        // 4 buckets that never resize: several words share a chain, and some
        // buckets may be empty, so the iterator has to walk chains and skip.
        HashTable t = new HashTable(4, 1000.0);
        String[] words = {"to", "be", "or", "not", "that", "is", "the", "question"};
        for (int i = 0; i < words.length; i++) {
            t.set(words[i], i + 1);
        }
        Map<String, Integer> seen = new java.util.HashMap<>();
        for (Map.Entry<String, Integer> e : t) {
            assertNull(seen.put(e.getKey(), e.getValue()), "the iterator handed out \"" + e.getKey() + "\" twice");
        }
        assertEquals(words.length, seen.size(), "the iterator should hand out every word");
        for (int i = 0; i < words.length; i++) {
            assertEquals(i + 1, seen.get(words[i]), "wrong count for \"" + words[i] + "\"");
        }
    }

    @Test
    void resizeKeepsEveryWordFindable() {
        HashTable t = new HashTable();
        for (int i = 0; i < 100; i++) {
            t.set("word" + i, i);
        }
        assertTrue(t.capacity() > 16, "100 words in 16 buckets is past 0.75, so resize should have run");
        assertTrue((double) t.size() / t.capacity() <= 0.75, "the load factor should stay at or under 0.75");
        for (int i = 0; i < 100; i++) {
            assertEquals(i, t.get("word" + i), "\"word" + i + "\" got lost in a resize");
        }
    }

    @Test
    void toTreeMatchesTheArray() {
        MaxHeap<Integer, String> h = new MaxHeap<>();
        h.insert(5, "five");
        h.insert(12, "twelve");
        h.insert(1, "one");
        // The array is now [12, 5, 1]: index 0 is the root, 1 and 2 its children.
        bridges.base.BinTreeElement<String> root = h.toTree();
        assertNotNull(root);
        assertEquals("twelve", root.getValue());
        assertNotNull(root.getLeft(), "index 1 should be the root's left child");
        assertNotNull(root.getRight(), "index 2 should be the root's right child");
        assertEquals("five", root.getLeft().getValue());
        assertEquals("one", root.getRight().getValue());
        assertNull(root.getLeft().getLeft(), "past the last entry, buildTree returns null");
    }
}
