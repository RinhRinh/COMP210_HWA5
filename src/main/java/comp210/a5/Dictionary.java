package comp210.a5;

import java.util.Map;

/**
 * A map from keys to values, the BRIDGES name for the Map ADT from L14.
 *
 * Looping over a Dictionary with for-each hands you each key/value pair as a
 * Map.Entry: e.getKey() and e.getValue().
 */
public interface Dictionary<K, V> extends Iterable<Map.Entry<K, V>> {

    /**
     * Returns the value stored under key, or null if key is not in the
     * dictionary.
     */
    V get(K key);

    /**
     * Stores value under key. If key is already there, its old value is
     * replaced.
     */
    void set(K key, V value);

    /** Returns how many distinct keys the dictionary holds. */
    int size();
}
