package org.rodnansol.core.generator.template;

import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representing a sub template data.
 */
public class SubTemplateData implements TemplateData {

    private final String moduleName;

    private final List<PropertyGroup> propertyGroups;

    private String moduleDescription;

    private LocalDateTime generationDate;

    private TemplateCustomization templateCustomization;

    public SubTemplateData(String moduleName, List<PropertyGroup> propertyGroups) {
        this.moduleName = moduleName;
        this.propertyGroups = propertyGroups;
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
    public String toString() {
        return "SubTemplateData{" +
            "moduleName='" + moduleName + '\'' +
            ", propertyGroups=" + propertyGroups +
            ", moduleDescription='" + moduleDescription + '\'' +
            ", generationDate=" + generationDate +
            '}';
    }
}
