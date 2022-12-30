package org.rodnansol.maven;

import java.io.File;

/**
 * Class representing an input in the pom.xml.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class AggregationMojoInput {

    /**
     * Name of the section.
     */
    private String name;

    /**
     * Description of the section.
     */
    private String description;

    /**
     * Input file
     */
    private File inputFile;

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

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }
}
