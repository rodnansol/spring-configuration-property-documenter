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

    private final String groupName;
    private final String type;
    private final String sourceType;
    private List<Property> properties;
    private PropertyGroup parentGroup;
    private List<PropertyGroup> childrenGroups;
    private boolean nested;

    public PropertyGroup(String groupName, String type, String sourceType) {
        this.groupName = groupName;
        this.type = type;
        this.sourceType = sourceType;
        this.nested = !type.equals(sourceType);
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

    /**
     * Add a new child group.
     *
     * @param childGroup group to be added as a new child.
     */
    public void addChildGroup(PropertyGroup childGroup) {
        if (childrenGroups == null) {
            childrenGroups = new ArrayList<>();
        }
        childrenGroups.add(childGroup);
    }

    /**
     * Add a new property to the property group.
     *
     * @param property new property.
     */
    public void addProperty(Property property) {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        properties.add(property);
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
