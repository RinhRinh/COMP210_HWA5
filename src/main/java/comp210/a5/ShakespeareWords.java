package comp210.a5;

import bridges.connect.Bridges;
import bridges.data_src_dependent.Shakespeare;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Counts the words in one Shakespeare play, prints the most frequent ones,
 * and draws your hash table and your heap in BRIDGES.
 *
 * The only line to change is WORK, below. Everything else calls the
 * methods you write in HashTable, MaxHeap, and WordStats.
 */
public class ShakespeareWords {

    // ------------------------------------------------------------------
    // YOUR CHOICE: which play to count. Any part of the title works, and
    // case does not matter: "hamlet", "Macbeth", "midsummer", "tempest".
    // Run once with a title that matches nothing to see the full list.
    // ------------------------------------------------------------------
    static final String WORK = "hamlet";

    /** How many top words to print. */
    static final int TOP = 20;

    /** How many top words go into the heap picture. 15 fills four levels. */
    static final int HEAP_PICTURE = 15;

    public static void main(String[] args) throws Exception {
        BridgesConfig config = BridgesConfig.load();
        System.out.println("Loaded BRIDGES credentials: " + config.describe());

        Bridges bridges = new Bridges(config.getAssignmentNumber(), config.getUsername(), config.getApiKey());
        bridges.setTitle("A5: Shakespeare's Words");

        Shakespeare play = findPlay(bridges, WORK);
        if (play == null) {
            return;
        }
        String[] words = WordStats.tokenize(play.getText());

        // Count with your HashTable, then again with java.util.HashMap to check it.
        HashTable counts = new HashTable();
        long start = System.nanoTime();
        WordStats.countWords(words, counts);
        long hashTableMs = (System.nanoTime() - start) / 1_000_000;

        StandardDictionary<String, Integer> check = new StandardDictionary<>();
        WordStats.countWords(words, check);

        System.out.println();
        System.out.println(play.getTitle());
        System.out.println("  words in the text:      " + words.length);
        System.out.println("  distinct words:         " + counts.size());
        System.out.println("  buckets after counting: " + counts.capacity());
        System.out.println("  longest chain:          " + counts.longestChain());
        System.out.println("  counting took:          " + hashTableMs + " ms");
        System.out.println("  matches java.util.HashMap: " + (sameCounts(counts, check) ? "yes" : "NO, see below"));

        List<Map.Entry<String, Integer>> top = WordStats.topWords(counts, TOP);
        System.out.println();
        System.out.println("Top " + TOP + " words:");
        for (int i = 0; i < top.size(); i++) {
            System.out.printf("  %2d. %-12s %d%n", i + 1, top.get(i).getKey(), top.get(i).getValue());
        }

        // Picture 1: a small table built from the first few distinct words.
        // The high load factor keeps it from resizing, so you can see chains.
        HashTable small = new HashTable(8, 3.0);
        for (String w : words) {
            if (small.size() == 20) {
                break;
            }
            if (small.get(w) == null) {
                small.set(w, check.get(w));
            }
        }
        bridges.setDescription("Picture 1 of 2: a HashTable with 8 buckets holding the first 20 distinct "
                + "words of " + play.getTitle() + ", with their counts. Each row is one bucket's chain.");
        small.visualize(bridges);

        // Picture 2: the most frequent words, as a heap.
        MaxHeap<Integer, String> heap = new MaxHeap<>();
        for (Map.Entry<String, Integer> e : WordStats.topWords(counts, HEAP_PICTURE)) {
            heap.insert(e.getValue(), e.getKey());
        }
        bridges.setDescription("Picture 2 of 2: the " + heap.size() + " most frequent words of "
                + play.getTitle() + " in a max-heap, drawn from the array by toTree().");
        heap.visualize(bridges);
    }

    /** Finds the first play whose title contains name, or lists them all. */
    static Shakespeare findPlay(Bridges bridges, String name) throws Exception {
        List<Shakespeare> plays = bridges.getDataSource().getShakespeareData("plays", true);
        String want = name.toLowerCase(Locale.ROOT);
        for (Shakespeare s : plays) {
            if (s.getTitle().toLowerCase(Locale.ROOT).contains(want)) {
                return s;
            }
        }
        System.out.println("No play title contains \"" + name + "\". Set WORK to part of one of these:");
        for (Shakespeare s : plays) {
            System.out.println("  " + s.getTitle());
        }
        return null;
    }

    /** True when both dictionaries hold the same words with the same counts. */
    static boolean sameCounts(Dictionary<String, Integer> mine, Dictionary<String, Integer> reference) {
        boolean same = mine.size() == reference.size();
        if (!same) {
            System.out.println("  Your table has " + mine.size() + " words; HashMap has " + reference.size() + ".");
        }
        for (Map.Entry<String, Integer> e : reference) {
            Integer got = mine.get(e.getKey());
            if (!e.getValue().equals(got)) {
                System.out.println("  \"" + e.getKey() + "\": yours says " + got + ", HashMap says " + e.getValue());
                return false;
            }
        }
        return same;
    }
}
