package ru.vood.freemarker.ext.sql;

import java.util.HashMap;
import java.util.Map;

public class SharedHash {

    private final Map storage = new HashMap();


    /**
     * Saves the specified key value into the storage.
     *
     * @param key   the key name
     * @param value the key value
     */
    public void put(String key, Object value) {
        storage.put(key, value);
    }


    /**
     * Returns the previously set key from the storage. If the key is not set, returns null.
     *
     * @param key the key name
     * @return stored key value
     */
    public Object get(String key) {
        return storage.get(key);
    }


    /**
     * Returns the whole storage as a {@link Map}.
     *
     * @return the inner storage
     */
    public Map get() {
        return storage;
    }


    /**
     * Drops the specified key from the storage, if presented.
     *
     * @param key the key name
     */
    public void remove(String key) {
        storage.remove(key);
    }


    /**
     * Cleans up the storage.
     */
    public void clear() {
        storage.clear();
    }


}
