package org.rodnansol.core.generator.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal based {@link TemplateCompilerMemoryStore}.
 * <p>
 * Good candidate to store values between the different template compiling.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class ThreadLocalTemplateCompilerStore implements TemplateCompilerMemoryStore {

    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalTemplateCompilerStore.class);

    public static final ThreadLocalTemplateCompilerStore INSTANCE = new ThreadLocalTemplateCompilerStore();
    private static final ThreadLocal<Map<String, Object>> STORE = ThreadLocal.withInitial(HashMap::new);

    private ThreadLocalTemplateCompilerStore() {
    }

    @Override
    public void addItemToMemory(String key, Object value) {
        LOGGER.debug("Adding item to memory store with key:[{}] and value:[{}]", key, value);
        STORE.get().put(key, value);
    }

    @Override
    public <T> T getItem(String key) {
        LOGGER.debug("Accessing value with key:[{}]", key);
        return (T) STORE.get().get(key);
    }

    @Override
    public void resetMemory() {
        LOGGER.debug("Resetting memory store.");
        STORE.remove();
    }


}
