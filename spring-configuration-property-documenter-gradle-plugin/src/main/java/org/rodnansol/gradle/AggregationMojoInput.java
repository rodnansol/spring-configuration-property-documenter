package org.rodnansol.gradle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an input in the pom.xml.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class AggregationMojoInput {

    /**
     * Name of the section.
     *
     * @since 0.1.0
     */
    private String name;

    /**
     * Description of the section.
     *
     * @since 0.1.0
     */
    private String description;

    /**
     * Input.
     *
     * @since 0.1.0
     */
    private File input;

    /**
     * List of excluded properties.
     *
     * @since 0.4.0
     */
    private List<String> excludedProperties;


    /**
     * List of included properties.
     *
     * @since 0.4.0
     */
    private List<String> includedProperties;

    /**
     * List of excluded groups.
     *
     * @since 0.4.0
     */
    List<String> excludedGroups = new ArrayList<>();

    /**
     * List of included groups.
     *
     * @since 0.4.0
     */
    List<String> includedGroups = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public List<String> getExcludedProperties() {
        return excludedProperties;
    }

    public void setExcludedProperties(List<String> excludedProperties) {
        this.excludedProperties = excludedProperties;
    }

    public List<String> getIncludedProperties() {
        return includedProperties;
    }

    public void setIncludedProperties(List<String> includedProperties) {
        this.includedProperties = includedProperties;
    }

    public List<String> getExcludedGroups() {
        return excludedGroups;
    }

    public void setExcludedGroups(List<String> excludedGroups) {
        this.excludedGroups = excludedGroups;
    }

    public List<String> getIncludedGroups() {
        return includedGroups;
    }

    public void setIncludedGroups(List<String> includedGroups) {
        this.includedGroups = includedGroups;
    }
}
