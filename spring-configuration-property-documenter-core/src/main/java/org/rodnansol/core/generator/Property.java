package org.rodnansol.core.generator;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Property {

    private final String fqName;
    private String key;
    private final String type;
    private String description;
    private String defaultValue;
    private PropertyDeprecation propertyDeprecation;


    public Property(String fqName, String type) {
        this.fqName = fqName;
        this.type = type;
    }

    public String getKey() {
        return key;
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

    @Override
    public String toString() {
        return "Property{" +
            "key='" + key + '\'' +
            ", type='" + type + '\'' +
            ", description='" + description + '\'' +
            ", defaultValue='" + defaultValue + '\'' +
            '}';
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

    public void setKey(String key) {
        this.key = key;
    }
}
