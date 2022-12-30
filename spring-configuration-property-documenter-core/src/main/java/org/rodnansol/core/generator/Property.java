package org.rodnansol.core.generator;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class Property {

    private final String fqName;
    private final String type;
    private String key;
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
