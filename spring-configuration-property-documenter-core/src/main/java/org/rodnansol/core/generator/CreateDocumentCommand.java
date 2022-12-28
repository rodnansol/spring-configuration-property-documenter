package org.rodnansol.core.generator;

import java.io.File;
import java.util.Objects;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class CreateDocumentCommand {

    private final String name;
    private final File metadataFile;
    private final String template;
    private final File output;
    private String description;

    public CreateDocumentCommand(String name, File metadataFile, String template, File output) {
        this.name = Objects.requireNonNull(name, "name is NULL");
        this.metadataFile = Objects.requireNonNull(metadataFile, "name is NULL");
        this.template = Objects.requireNonNull(template, "template is NULL");
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getMetadataFile() {
        return metadataFile;
    }

    public File getOutput() {
        return output;
    }
}
