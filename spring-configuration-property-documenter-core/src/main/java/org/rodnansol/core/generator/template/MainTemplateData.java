package org.rodnansol.core.generator.template;

import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Class representing the main template data.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class MainTemplateData implements TemplateData {

    /**
     * Header section.
     */
    private final String mainName;

    /**
     * List of the property groups.
     */
    private final List<PropertyGroup> propertyGroups;

    /**
     * List of the sub template data.
     */
    private List<SubTemplateData> subTemplateDataList;

    /**
     * Main description.
     */
    private String mainDescription;

    /**
     * Date of the generation.
     */
    private LocalDateTime generationDate;

    /**
     * Template customization.
     */
    private TemplateCustomization templateCustomization;

    public MainTemplateData(String mainName, List<PropertyGroup> propertyGroups) {
        this.mainName = mainName;
        this.propertyGroups = propertyGroups;
    }

    public static MainTemplateData ofMainSection(String header, List<PropertyGroup> propertyGroups) {
        return new MainTemplateData(header, propertyGroups);
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    public String getMainName() {
        return mainName;
    }


    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public List<SubTemplateData> getSubTemplateDataList() {
        return subTemplateDataList;
    }

    public void setSubTemplateDataList(List<SubTemplateData> subTemplateDataList) {
        this.subTemplateDataList = subTemplateDataList;
    }

    public TemplateCustomization getTemplateCustomization() {
        return templateCustomization;
    }

    public void setTemplateCustomization(TemplateCustomization templateCustomization) {
        this.templateCustomization = templateCustomization;
    }

    @Override
    public String toString() {
        return "MainTemplateData{" +
            "mainName='" + mainName + '\'' +
            ", propertyGroups=" + propertyGroups +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainTemplateData that = (MainTemplateData) o;
        return Objects.equals(mainName, that.mainName) && Objects.equals(propertyGroups, that.propertyGroups) && Objects.equals(subTemplateDataList, that.subTemplateDataList) && Objects.equals(mainDescription, that.mainDescription) && Objects.equals(templateCustomization, that.templateCustomization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainName, propertyGroups, subTemplateDataList, mainDescription, templateCustomization);
    }
}
