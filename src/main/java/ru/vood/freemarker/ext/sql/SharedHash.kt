package ru.vood.freemarker.ext.sql

import java.util.*

/**
 * A helper class for storing FTL variables and passing them between different templates. Can be accessed in FTL as
 * a shared variable named `shared_hash`.
 *
 *
 * Usage examples in FTL:
 * <pre>
 * `<#assign void = shared_hash.put("a", 1)/>
 * <#assign void = shared_hash.put("b", "text")/>
 * a = ${shared_hash.get("a")?c}
 * <#assign map = shared_hash.get()/>
 * b = ${map["b"]}
 * <#assign void = shared_hash.remove("b")/>
 * <#assign void = shared_hash.clear()/>
` *
</pre> *
 */
class SharedHash {
    private val storage: MutableMap<String, Any> = HashMap()
    /**
     * Saves the specified key value into the storage.
     *
     * @param key the key name
     * @param value the key value
     */
    fun put(key: String, value: Any) {
        storage[key] = value
    }

    /**
     * Returns the previously set key from the storage. If the key is not set, returns null.
     *
     * @param key the key name
     * @return stored key value
     */
    operator fun get(key: String?): Any? {
        return storage[key]
    }

    /**
     * Returns the whole storage as a [Map].
     *
     * @return the inner storage
     */
    fun get(): Map<String, Any> {
        return storage
    }

    /**
     * Drops the specified key from the storage, if presented.
     *
     * @param key the key name
     */
    fun remove(key: String) {
        storage.remove(key)
    }

    /**
     * Cleans up the storage.
     */
    fun clear() {
        storage.clear()
    }
}