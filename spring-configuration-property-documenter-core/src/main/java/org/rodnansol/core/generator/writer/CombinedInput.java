package org.rodnansol.core.generator.writer;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class CombinedInput {

    private final File input;
    private final String sectionName;
    private final String description;
    private List<String> excludedGroups;
    private List<String> includedGroups;
    private List<String> excludedProperties;
    private List<String> includedProperties;

    public CombinedInput(File input, String sectionName, String description) {
        this.input = Objects.requireNonNull(input, "stream is NULL");
        this.sectionName = Objects.requireNonNull(sectionName, "sectionName is NULL");
        this.description = description;
    }

    public File getInput() {
        return input;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDescription() {
        return description;
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
        return new StringJoiner(", ", CombinedInput.class.getSimpleName() + "[", "]")
            .add("input=" + input)
            .add("sectionName='" + sectionName + "'")
            .add("description='" + description + "'")
            .add("excludedGroups=" + excludedGroups)
            .add("includedGroups=" + includedGroups)
            .add("excludedProperties=" + excludedProperties)
            .add("includedProperties=" + includedProperties)
            .toString();
    }
}
