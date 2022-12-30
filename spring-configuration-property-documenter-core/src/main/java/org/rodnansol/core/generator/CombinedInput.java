package org.rodnansol.core.generator;

import java.io.File;
import java.util.Objects;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class CombinedInput {

    private final File input;
    private final String sectionName;
    private String description;

    public CombinedInput(File input, String sectionName) {
        this.input = Objects.requireNonNull(input, "stream is NULL");
        this.sectionName = Objects.requireNonNull(sectionName, "sectionName is NULL");
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

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CombinedInput{" +
            " input=" + input +
            ", sectionName='" + sectionName + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
