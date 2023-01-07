package org.rodnansol.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.HandlebarsTemplateCompiler;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.writer.AggregationDocumenter;
import org.rodnansol.core.generator.writer.CombinedInput;
import org.rodnansol.core.generator.writer.CreateAggregationCommand;
import org.rodnansol.core.project.ProjectFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This goal reads all the given `spring-configuration-metadata.json` files from the given/specified sources and it will be generating one single document that will contain all documentation created by the given sources. This goal is good for a multi module setup, it is able to read multiple files and aggregate them.
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

    /**
     * Template compiler class's fully qualified name .
     * <p>
     * With this option you can use your own template compiler implementation if the default {@link HandlebarsTemplateCompiler}. based one is not enough.
     */
    @Parameter(property = "templateCompilerName")
    String templateCompilerName = HandlebarsTemplateCompiler.class.getName();

    @Override
    public void execute() {
        AggregationDocumenter aggregationDocumenter = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getInstance(templateCompilerName), MetadataInputResolverContext.INSTANCE);
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
