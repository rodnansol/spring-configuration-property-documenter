package org.rodnansol.core.generator.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a property group.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class PropertyGroup {

    public static final String WITHOUT_TYPE = "Without type";
    public static final String UNKNOWN = "Unknown";

    private final String groupName;
    private final String type;
    private final String sourceType;
    private List<Property> properties;
    private PropertyGroup parentGroup;
    private List<PropertyGroup> childrenGroups;
    private boolean nested;
    private boolean unknownGroup;

    public PropertyGroup(String groupName, String type, String sourceType) {
        this.groupName = groupName;
        this.type = type;
        this.sourceType = sourceType;
        this.nested = !type.equals(sourceType);
    }

    public PropertyGroup(String groupName, String type, String sourceType, List<Property> properties) {
        this.groupName = groupName;
        this.type = type;
        this.sourceType = sourceType;
        this.nested = !type.equals(sourceType);
        this.properties = properties;
    }

    /**
     * Creates a group that has 'Unknown' type and source type.
     *
     * @return unknown group.
     */
    public static PropertyGroup createUnknownGroup() {
        PropertyGroup propertyGroup = new PropertyGroup(WITHOUT_TYPE, UNKNOWN, UNKNOWN);
        propertyGroup.setUnknownGroup(true);
        return propertyGroup;
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
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(property);
        return this;
    }

    @Override
    public String toString() {
        return "PropertyGroup{" +
            "groupName='" + groupName + '\'' +
            ", type='" + type + '\'' +
            ", sourceType='" + sourceType + '\'' +
            ", properties=" + properties +
            ", parentGroup=" + parentGroup +
            ", nested=" + nested +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyGroup that = (PropertyGroup) o;
        return nested == that.nested
            && Objects.equals(groupName, that.groupName)
            && Objects.equals(type, that.type)
            && Objects.equals(sourceType, that.sourceType)
            && Objects.equals(properties, that.properties)
            && Objects.equals(parentGroup, that.parentGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, type, sourceType, properties, parentGroup, nested);
    }

}
