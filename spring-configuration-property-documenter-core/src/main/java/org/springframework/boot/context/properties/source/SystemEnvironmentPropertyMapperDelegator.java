package org.springframework.boot.context.properties.source;

/**
 * Delegate the transformation of a property to the Spring package private {@link SystemEnvironmentPropertyMapper} class.
 *
 * @author allsimon
 * @since 0.3.1
 */
public class SystemEnvironmentPropertyMapperDelegator {
    private SystemEnvironmentPropertyMapperDelegator() {
    }

    public static String convertToEnvVariable(String property) {
        var configurationPropertyName = ConfigurationPropertyName.of(property);

        return SystemEnvironmentPropertyMapper.INSTANCE.map(configurationPropertyName)
            .get(0); //It returns a list and the first element will be the value we are looking forward
    }
}
