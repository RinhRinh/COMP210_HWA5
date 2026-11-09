package comp210.a5;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Counting words and picking out the most frequent ones. ShakespeareWords
 * calls these on real text; the autograder calls them on small word lists.
 */
public final class WordStats {

    private WordStats() {
    }

    /**
     * Splits raw text into lowercase words with the punctuation trimmed off
     * each end. Provided.
     *
     * "To be, or not to be: that is the question." becomes
     * [to, be, or, not, to, be, that, is, the, question].
     */
    public static String[] tokenize(String text) {
        List<String> words = new ArrayList<>();
        for (String raw : text.split("\\s+")) {
            String w = raw.replaceAll("^\\W+", "").replaceAll("\\W+$", "").toLowerCase(Locale.ROOT);
            if (!w.isEmpty()) {
                words.add(w);
            }
        }
        return words.toArray(new String[0]);
    }

    // ------------------------------------------------------------------
    // Your part: countWords and topWords.
    // ------------------------------------------------------------------

    /**
     * Adds one to counts for every word in words. A word seen for the first
     * time gets a count of 1. Use only get and set, so this works on any
     * Dictionary.
     */
    public static void countWords(String[] words, Dictionary<String, Integer> counts) {
        // TODO
    }

    /**
     * Returns the k most frequent words as (word, count) entries, largest
     * count first. When counts holds fewer than k words, returns all of them.
     *
     * Use a MaxHeap keyed on the count: insert every entry of counts, then
     * pop k times. Words that tie on count can come out in either order.
     */
    public static List<Map.Entry<String, Integer>> topWords(Dictionary<String, Integer> counts, int k) {
        List<Map.Entry<String, Integer>> top = new ArrayList<>();
        // TODO: fill the heap, then pop into top. Add each entry as
        // new AbstractMap.SimpleEntry<>(word, count).
        return top;
    }
}
