package org.rodnansol.core.generator;

import java.util.ArrayList;
import java.util.List;

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

    public void addChildGroup(PropertyGroup parentGroup) {
        if (childrenGroups == null) {
            childrenGroups = new ArrayList<>();
        }
        childrenGroups.add(parentGroup);
    }

}
