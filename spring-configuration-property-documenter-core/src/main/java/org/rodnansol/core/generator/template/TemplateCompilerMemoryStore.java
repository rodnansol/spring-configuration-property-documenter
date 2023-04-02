package org.rodnansol.core.generator.template;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Interface representing methods for an in-memory store.
 * <p>
 * Template compiles can have their worn stores to pass objects between the different calls.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public interface TemplateCompilerMemoryStore {

    /**
     * Adds item to the memory store.
     *
     * @param key    key of the value.
     * @param value object to be stored.
     */
    void addItemToMemory(String key, Object value);

    /**
     * Returns an item from the store.
     *
     * @param key key value.
     * @param <T> type of the object to be returned.
     * @return stored object, or null if it does not exist in the store.
     */
    @Nullable
    <T> T getItem(String key);

    /**
     * Resets the memory.
     */
    void resetMemory();

}
