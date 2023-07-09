package org.rodnansol.gradle.tasks;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.options.Option;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Class representing an input in the gradle.build's task configuration.
 *
 * @author nandorholozsnyak
 * @author tkhadiradeo
 * @since 0.5.0
 */
public class AggregationInput implements Serializable {

    /**
     * List of excluded properties.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "excludedProperties", description = "List of excluded properties")
    private List<String> excludedProperties = new ArrayList<>();
    /**
     * List of included properties.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "includedProperties", description = "List of included properties")
    private List<String> includedProperties = new ArrayList<>();
    /**
     * List of excluded groups.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "excludedGroups", description = "List of excluded groups")
    private List<String> excludedGroups = new ArrayList<>();
    /**
     * List of included groups.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "includedGroups", description = "List of included groups")
    private List<String> includedGroups = new ArrayList<>();
    /**
     * Name of the section.
     *
     * @since 0.5.0
     */
    @Input
    @Option(option = "name", description = "Name/header of the generated document")
    private String name;
    /**
     * Description of the section.
     *
     * @since 0.5.0
     */
    @Input
    @Optional
    @Option(option = "description", description = "Description")
    private String description;
    /**
     * Metadata input that can be:
     * <ul>
     *     <li>A path to JSON file for example: <b>target/classes/META-INF/spring-configuration-metadata.json</b></li>
     *     <li>A directory that contains the file</li>
     *     <li>A jar/zip file that contains the file within the following entry <b>META-INF/spring-configuration-metadata.json</b></li>
     * </ul>
     *
     * @since 0.5.0
     */
    @InputFile
    @Option(option = "metadataInput", description = "Description")
    private File input;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", AggregationInput.class.getSimpleName() + "[", "]")
            .add("excludedProperties=" + excludedProperties)
            .add("includedProperties=" + includedProperties)
            .add("excludedGroups=" + excludedGroups)
            .add("includedGroups=" + includedGroups)
            .add("name='" + name + "'")
            .add("description='" + description + "'")
            .add("input=" + input)
            .toString();
    }
}
