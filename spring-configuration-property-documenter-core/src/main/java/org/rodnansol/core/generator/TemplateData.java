package org.rodnansol.core.generator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class TemplateData {

    private final String name;
    private final List<PropertyGroup> propertyGroups;
    private String description;
    private LocalDateTime generationDate;

    public TemplateData(String name, List<PropertyGroup> propertyGroups) {
        this.name = name;
        this.propertyGroups = propertyGroups;
    }

    public String getName() {
        return name;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    @Override
    public String toString() {
        return "TemplateData{" +
            "projectName='" + name + '\'' +
            ", properties=" + propertyGroups +
            '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }
}
