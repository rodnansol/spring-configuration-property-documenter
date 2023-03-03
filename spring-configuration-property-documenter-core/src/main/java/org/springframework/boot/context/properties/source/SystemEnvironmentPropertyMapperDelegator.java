package org.springframework.boot.context.properties.source;

import java.util.List;

/**
 * Delegate the transformation of a property to the Spring package private {@link SystemEnvironmentPropertyMapper} class.
 *
 * @author allsimon
 * @since 0.4.0
 */
public class SystemEnvironmentPropertyMapperDelegator {

    private SystemEnvironmentPropertyMapperDelegator() {
    }

    /**
     * Converts the incoming property to its environment variable form.
     *
     * @param property to be converted.
     * @return property in environment variable form.
     */
    public static String convertToEnvVariable(String property) {
        var configurationPropertyName = ConfigurationPropertyName.of(property);
        List<String> map = SystemEnvironmentPropertyMapper.INSTANCE.map(configurationPropertyName);
        return !map.isEmpty() ? map.get(0) : "";
    }
}
