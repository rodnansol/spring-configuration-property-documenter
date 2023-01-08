package org.rodnansol.core.generator.writer;

import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;
import org.rodnansol.core.project.Project;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class CreateAggregationCommand {

    private final Project project;
    private final String aggregatedDocumentHeader;
    private final List<CombinedInput> combinedInputs;
    private final TemplateType templateType;
    private final TemplateCustomization templateCustomization;
    private final File output;
    private String description;

    public CreateAggregationCommand(Project project, String aggregatedDocumentHeader, List<CombinedInput> inputStreams, TemplateType templateType, TemplateCustomization templateCustomization, File output) {
        this.project = project;
        this.aggregatedDocumentHeader = Objects.requireNonNull(aggregatedDocumentHeader, "name is NULL");
        this.combinedInputs = Objects.requireNonNull(inputStreams, "inputStreams is NULL");
        this.templateType = Objects.requireNonNull(templateType, "templateType is NULL");
        this.templateCustomization = Objects.requireNonNull(templateCustomization, "templateCustomization is NULL");;
        this.output = Objects.requireNonNull(output, "output is NULL");
    }

    public String getAggregatedDocumentHeader() {
        return aggregatedDocumentHeader;
    }

    public List<CombinedInput> getCombinedInputs() {
        return combinedInputs;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public File getOutput() {
        return output;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public TemplateCustomization getTemplateCustomization() {
        return templateCustomization;
    }

    @Override
    public String toString() {
        return "CreateAggregationCommand{" +
            "aggregatedDocumentHeader='" + aggregatedDocumentHeader + '\'' +
            ", combinedInputs=" + combinedInputs +
            ", templateType=" + templateType +
            ", output=" + output +
            ", description='" + description + '\'' +
            '}';
    }

}
