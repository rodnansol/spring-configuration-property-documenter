package org.rodnansol.core.generator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class holding a singleton {@link com.fasterxml.jackson.databind.ObjectMapper} instance with custom bindings.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public final class ObjectMapperContext {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        customizeObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    private static void customizeObjectMapper() {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //Empty intentionally yet
    }

}
