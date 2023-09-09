package org.rodnansol.core.generator.template.data;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing a sub template data.
 */
public class SubTemplateData implements TemplateData {

    @Nullable
    private final String moduleName;
    @NonNull

    private final List<PropertyGroup> propertyGroups;

    @Nullable
    private String moduleDescription;

    @Nullable
    private LocalDateTime generationDate;

    @Nullable
    private TemplateCustomization templateCustomization;

    public SubTemplateData(String moduleName, List<PropertyGroup> propertyGroups) {
        this.moduleName = moduleName;
        this.propertyGroups = Objects.requireNonNull(propertyGroups,"propertyGroups is NULL");
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    @Override
    public TemplateCustomization getTemplateCustomization() {
        return templateCustomization;
    }

    @Override
    public void setTemplateCustomization(TemplateCustomization templateCustomization) {
        this.templateCustomization = templateCustomization;
    }

    @Override
    public List<Property> getAggregatedProperties() {
        return propertyGroups.stream()
            .flatMap(groups -> groups.getProperties().stream())
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "SubTemplateData{" +
            "moduleName='" + moduleName + '\'' +
            ", propertyGroups=" + propertyGroups +
            ", moduleDescription='" + moduleDescription + '\'' +
            ", generationDate=" + generationDate +
            '}';
    }
}
