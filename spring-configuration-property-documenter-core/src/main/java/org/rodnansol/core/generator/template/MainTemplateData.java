package org.rodnansol.core.generator.template;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public String toString() {
        return "MainTemplateData{" +
            "mainName='" + mainName + '\'' +
            ", propertyGroups=" + propertyGroups +
            '}';
    }

    public List<SubTemplateData> getSubTemplateDataList() {
        return subTemplateDataList;
    }

    public void setSubTemplateDataList(List<SubTemplateData> subTemplateDataList) {
        this.subTemplateDataList = subTemplateDataList;
    }
}
