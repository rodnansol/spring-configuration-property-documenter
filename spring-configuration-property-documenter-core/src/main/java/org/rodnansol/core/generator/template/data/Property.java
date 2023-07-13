package org.rodnansol.core.generator.template.data;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Objects;

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
    @NonNull
    private final String fqName;
    /**
     * Type of the property, most likely to be a Java class.
     */
    @NonNull
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
        this.fqName = Objects.requireNonNull(fqName, "fqName is NULL");
        this.type = Objects.requireNonNull(type, "type is NULL");
    }

    public Property(String fqName, String type, String key, String description, String defaultValue, PropertyDeprecation propertyDeprecation) {
        this(fqName, type);
        this.key = StringUtils.defaultIfBlank(key, "");
        this.description = StringUtils.defaultIfBlank(description, "");
        this.defaultValue = StringUtils.defaultIfBlank(defaultValue, "");
        this.propertyDeprecation = Objects.requireNonNullElse(propertyDeprecation, new PropertyDeprecation());
    }

    /**
     * Returns a new instance of Builder class with the given fully qualified name and type.
     *
     * @param fqName the fully qualified name of the object
     * @param type   the type of the object
     * @return a new instance of Builder class
     */
    public static Builder builder(String fqName, String type) {
        return new Builder(fqName, type);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(fqName, property.fqName) && Objects.equals(type, property.type) && Objects.equals(key, property.key) && Objects.equals(description, property.description) && Objects.equals(defaultValue, property.defaultValue) && Objects.equals(propertyDeprecation, property.propertyDeprecation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fqName, type, key, description, defaultValue, propertyDeprecation);
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

    public static final class Builder {

        private final String fqName;
        private final String type;
        private String key;
        private String description;
        private String defaultValue;
        private PropertyDeprecation propertyDeprecation;

        public Builder(String fqName, String type) {
            this.fqName = fqName;
            this.type = type;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder withPropertyDeprecation(PropertyDeprecation propertyDeprecation) {
            this.propertyDeprecation = propertyDeprecation;
            return this;
        }

        public Property build() {
            Property property = new Property(fqName, type);
            property.key = this.key;
            property.description = this.description;
            property.defaultValue = this.defaultValue;
            property.propertyDeprecation = this.propertyDeprecation;
            return property;
        }
    }
}
