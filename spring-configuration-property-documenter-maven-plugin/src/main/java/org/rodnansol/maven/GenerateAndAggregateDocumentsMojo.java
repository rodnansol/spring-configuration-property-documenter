package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.generator.AggregationDocumenter;
import org.rodnansol.core.generator.CombinedInput;
import org.rodnansol.core.generator.CreateAggregationCommand;
import org.rodnansol.core.generator.MetadataReader;
import org.rodnansol.core.generator.TemplateCompiler;
import org.rodnansol.core.generator.TemplateType;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates the documents and aggregates them into a single output file.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Mojo(name = GenerateAndAggregateDocumentsMojo.GOAL_NAME)
public class GenerateAndAggregateDocumentsMojo extends AbstractMojo {

    protected static final String GOAL_NAME = "generate-and-aggregate-documents";

    /**
     * Maven project instance.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    /**
     * Main header section name.
     */
    @Parameter(property = "name", required = true, defaultValue = "${project.name}")
    String name;

    /**
     * Main module description.
     */
    @Parameter(property = "description", defaultValue = "${project.description}")
    String description;

    /**
     * Type of the document.
     * <p>
     * The following template types are supported:
     * <ul>
     *     <li>MARKDOWN</li>
     *     <li>ADOC</li>
     *     <li>HTML</li>
     * </ul>
     */
    @Parameter(property = "type", defaultValue = "MARKDOWN")
    String type;

    /**
     * Input files.
     */
    @Parameter(property = "inputs", required = true)
    List<AggregationMojoInput> inputs;

    /**
     * Output file.
     */
    @Parameter(property = "outputFile", required = true)
    File outputFile;

    @Override
    public void execute() {
        AggregationDocumenter aggregationDocumenter = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompiler.INSTANCE, MetadataInputResolverContext.INSTANCE);
        CreateAggregationCommand createAggregationCommand = createAggregationCommand();
        aggregationDocumenter.createDocumentsAndAggregate(createAggregationCommand);
    }

    private CreateAggregationCommand createAggregationCommand() {
        List<CombinedInput> combinedInputs = inputs.stream().map(this::mapToCombinedInput).collect(Collectors.toList());
        CreateAggregationCommand createAggregationCommand = new CreateAggregationCommand(ProjectFactory.ofMavenProject(project.getBasedir(), name, project.getModules()), name, combinedInputs, TemplateType.valueOf(type), outputFile);
        createAggregationCommand.setDescription(description);
        return createAggregationCommand;
    }

    private CombinedInput mapToCombinedInput(AggregationMojoInput aggregationMojoInput) {
        CombinedInput combinedInput = new CombinedInput(aggregationMojoInput.getInput(), aggregationMojoInput.getName());
        combinedInput.setDescription(aggregationMojoInput.getDescription());
        return combinedInput;
    }
}
