package org.rodnansol.core.generator.template;

/**
 * Class representing a property.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Property {

    /**
     * Property's fully qualified name.
     */
    private final String fqName;
    /**
     * Type of the property, most likely to be a Java class.
     */
    private final String type;
    /**
     * Property's key.
     */
    private String key;
    /**
     * Property's description if specified.
     */
    private String description;
    /**
     * Property's default value if specified.
     */
    private String defaultValue;
    /**
     * Property's deprecation.
     */
    private PropertyDeprecation propertyDeprecation;

    public Property(String fqName, String type) {
        this.fqName = fqName;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public PropertyDeprecation getPropertyDeprecation() {
        return propertyDeprecation;
    }

    public void setPropertyDeprecation(PropertyDeprecation propertyDeprecation) {
        this.propertyDeprecation = propertyDeprecation;
    }

    public String getFqName() {
        return fqName;
    }

    @Override
    public String toString() {
        return "Property{" +
            "fqName='" + fqName + '\'' +
            ", type='" + type + '\'' +
            ", key='" + key + '\'' +
            ", description='" + description + '\'' +
            ", defaultValue='" + defaultValue + '\'' +
            ", propertyDeprecation=" + propertyDeprecation +
            '}';
    }
}
