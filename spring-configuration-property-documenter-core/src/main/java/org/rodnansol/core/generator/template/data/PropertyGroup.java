package org.rodnansol.core.generator.template.data;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static org.rodnansol.core.generator.template.data.PropertyGroupConstants.UNKNOWN;
import static org.rodnansol.core.generator.template.data.PropertyGroupConstants.UNKNOWN_GROUP;

/**
 * Class representing a property group.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class PropertyGroup {

    @Nullable
    private final String type;
    @Nullable
    private final String sourceType;
    @Nullable
    private String groupName;
    @NonNull
    private List<Property> properties;
    @Nullable
    private PropertyGroup parentGroup;
    private List<PropertyGroup> childrenGroups;
    private boolean nested;
    private boolean unknownGroup = false;

    public PropertyGroup(String groupName, String type, String sourceType) {
        this(groupName, type, sourceType, new ArrayList<>());
    }

    public PropertyGroup(String groupName, String type, String sourceType, List<Property> properties) {
        this.groupName = groupName;
        this.type = type;
        this.sourceType = sourceType;
        this.nested = !type.equals(sourceType);
        this.properties = Objects.requireNonNull(properties, "properties is NULL");
    }

    /**
     * Creates a group that has 'Unknown' type and source type.
     *
     * @return unknown group.
     */
    public static PropertyGroup createUnknownGroup() {
        PropertyGroup propertyGroup = new PropertyGroup(UNKNOWN_GROUP, UNKNOWN, UNKNOWN);
        propertyGroup.setUnknownGroup(true);
        return propertyGroup;
    }

    /**
     * Creates a new instance of the Builder class.
     *
     * @return a new instance of the Builder class.
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getType() {
        return type;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public PropertyGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(PropertyGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public boolean isNested() {
        return nested;
    }

    public void setNested(boolean nested) {
        this.nested = nested;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public List<PropertyGroup> getChildrenGroups() {
        return childrenGroups;
    }

    public boolean isUnknownGroup() {
        return unknownGroup;
    }

    public void setUnknownGroup(boolean unknownGroup) {
        this.unknownGroup = unknownGroup;
    }

    /**
     * Add a new child group.
     *
     * @param childGroup group to be added as a new child.
     */
    public PropertyGroup addChildGroup(PropertyGroup childGroup) {
        if (childrenGroups == null) {
            childrenGroups = new ArrayList<>();
        }
        childrenGroups.add(childGroup);
        return this;
    }

    /**
     * Add a new property to the property group.
     *
     * @param property new property.
     */
    public PropertyGroup addProperty(Property property) {
        properties.add(property);
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(",\n\t", PropertyGroup.class.getSimpleName() + "[", "]")
            .add("type='" + type + "'")
            .add("sourceType='" + sourceType + "'")
            .add("groupName='" + groupName + "'")
            .add("properties=" + properties)
            .add("parentGroup=" + parentGroup)
            .add("nested=" + nested)
            .add("unknownGroup=" + unknownGroup)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyGroup that = (PropertyGroup) o;
        return nested == that.nested
            && unknownGroup == that.unknownGroup
            && Objects.equals(groupName, that.groupName)
            && Objects.equals(type, that.type)
            && Objects.equals(sourceType, that.sourceType)
            && Objects.equals(properties, that.properties)
            && Objects.equals(parentGroup, that.parentGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, type, sourceType, properties, parentGroup, nested, unknownGroup);
    }

    public static final class Builder {

        private String groupName;
        private String type;
        private String sourceType;
        private List<Property> properties = new ArrayList<>();
        private PropertyGroup parentGroup;
        private boolean nested;
        private boolean unknownGroup = false;

        public Builder withGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withSourceType(String sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder withProperties(List<Property> properties) {
            this.properties = properties;
            return this;
        }

        public Builder withParentGroup(PropertyGroup parentGroup) {
            this.parentGroup = parentGroup;
            return this;
        }

        public Builder withNested(boolean nested) {
            this.nested = nested;
            return this;
        }

        public Builder withUnknownGroup(boolean unknownGroup) {
            this.unknownGroup = unknownGroup;
            return this;
        }

        public PropertyGroup build() {
            PropertyGroup propertyGroup = new PropertyGroup(groupName, type, sourceType, properties);
            propertyGroup.setNested(nested);
            propertyGroup.setParentGroup(parentGroup);
            propertyGroup.setUnknownGroup(unknownGroup);
            return propertyGroup;
        }
    }
}
