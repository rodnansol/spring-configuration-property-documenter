package org.rodnansol.core.generator.template.data;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

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
    @Nullable
    private String key;
    /**
     * Property's description if specified.
     */
    @Nullable
    private String description;
    /**
     * Property's default value if specified.
     */
    @Nullable
    private String defaultValue;
    /**
     * Property's deprecation.
     */
    @Nullable
    private PropertyDeprecation propertyDeprecation;

    public Property(String fqName, String type) {
        this.fqName = Objects.requireNonNull(fqName, "fqName is NULL");
        this.type = Objects.requireNonNull(type, "type is NULL");
    }

    public Property(String fqName, String type, String key, String description, String defaultValue, PropertyDeprecation propertyDeprecation) {
        this(fqName, type);
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
        this.propertyDeprecation = propertyDeprecation;
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

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(@Nullable String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @Nullable
    public PropertyDeprecation getPropertyDeprecation() {
        return propertyDeprecation;
    }

    public void setPropertyDeprecation(@Nullable PropertyDeprecation propertyDeprecation) {
        this.propertyDeprecation = propertyDeprecation;
    }
    @NonNull
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
        return new StringJoiner(",\n\t", Property.class.getSimpleName() + "[", "]")
            .add("fqName='" + fqName + "'")
            .add("type='" + type + "'")
            .add("key='" + key + "'")
            .add("description='" + description + "'")
            .add("defaultValue='" + defaultValue + "'")
            .add("propertyDeprecation=" + propertyDeprecation)
            .toString();
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
