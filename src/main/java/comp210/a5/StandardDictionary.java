package comp210.a5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A Dictionary wrapped around java.util.HashMap. Provided, no edits needed.
 *
 * Use it to check your HashTable: count the same words with both and compare.
 */
public class StandardDictionary<K, V> implements Dictionary<K, V> {

    private final HashMap<K, V> map = new HashMap<>();

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void set(K key, V value) {
        map.put(key, value);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }
}
