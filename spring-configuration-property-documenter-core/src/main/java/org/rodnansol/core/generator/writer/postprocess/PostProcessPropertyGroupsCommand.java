package org.rodnansol.core.generator.writer.postprocess;

import org.rodnansol.core.generator.template.data.PropertyGroup;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.util.List;
import java.util.Objects;

/**
 * Class grouping attributes together that helps to post process the property groups and properties.
 *
 * @since 0.4.0
 */
public class PostProcessPropertyGroupsCommand {

    private final TemplateCustomization templateCustomization;
    private final List<PropertyGroup> propertyGroups;
    private final List<String> excludedGroups;
    private final List<String> includedGroups;
    private final List<String> excludedProperties;
    private final List<String> includedProperties;

    public PostProcessPropertyGroupsCommand(TemplateCustomization templateCustomization, List<PropertyGroup> propertyGroups, List<String> excludedGroups, List<String> includedGroups, List<String> excludedProperties, List<String> includedProperties) {
        this.templateCustomization = templateCustomization;
        this.propertyGroups = propertyGroups;
        this.excludedGroups = excludedGroups;
        this.includedGroups = includedGroups;
        this.excludedProperties = excludedProperties;
        this.includedProperties = includedProperties;
    }

    public static PostProcessPropertyGroupsCommand ofTemplate(TemplateCustomization templateCustomization, List<PropertyGroup> propertyGroups) {
        return new PostProcessPropertyGroupsCommand(templateCustomization, propertyGroups, null, null, null, null);
    }

    public static PostProcessPropertyGroupsCommand ofPropertyFilter(List<PropertyGroup> propertyGroups, List<String> excludedProperties, List<String> includedProperties) {
        return new PostProcessPropertyGroupsCommand(null, propertyGroups, null, null, excludedProperties, includedProperties);
    }

    public static PostProcessPropertyGroupsCommand ofGroupFilter(List<PropertyGroup> propertyGroups, List<String> excludedGroups, List<String> includedGroups) {
        return new PostProcessPropertyGroupsCommand(null, propertyGroups, excludedGroups, includedGroups, null, null);
    }

    public TemplateCustomization getTemplateCustomization() {
        return templateCustomization;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    public List<String> getExcludedGroups() {
        return excludedGroups;
    }

    public List<String> getIncludedGroups() {
        return includedGroups;
    }

    public List<String> getExcludedProperties() {
        return excludedProperties;
    }

    public List<String> getIncludedProperties() {
        return includedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostProcessPropertyGroupsCommand that = (PostProcessPropertyGroupsCommand) o;
        return Objects.equals(templateCustomization, that.templateCustomization) && Objects.equals(excludedGroups, that.excludedGroups) && Objects.equals(includedGroups, that.includedGroups) && Objects.equals(excludedProperties, that.excludedProperties) && Objects.equals(includedProperties, that.includedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateCustomization, excludedGroups, includedGroups, excludedProperties, includedProperties);
    }

    @Override
    public String toString() {
        return "PostProcessPropertyGroupsCommand{" +
            "templateCustomization=" + templateCustomization +
            ", propertyGroups=" + propertyGroups +
            ", excludedGroups=" + excludedGroups +
            ", includedGroups=" + includedGroups +
            ", excludedProperties=" + excludedProperties +
            ", includedProperties=" + includedProperties +
            '}';
    }
}
