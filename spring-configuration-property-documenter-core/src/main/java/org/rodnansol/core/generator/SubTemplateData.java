package org.rodnansol.core.generator;

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
    public String toString() {
        return "SubTemplateData{" +
            "moduleName='" + moduleName + '\'' +
            ", propertyGroups=" + propertyGroups +
            ", moduleDescription='" + moduleDescription + '\'' +
            ", generationDate=" + generationDate +
            '}';
    }
}
