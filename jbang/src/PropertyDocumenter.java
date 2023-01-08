///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.rodnansol:spring-configuration-property-documenter-core:999-SNAPSHOT
//DEPS info.picocli:picocli:4.7.0
//JAVA 17

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.reader.MetadataReader;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.generator.template.TemplateCompilerFactory;
import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.generator.template.customization.TemplateCustomizationFactory;
import org.rodnansol.core.generator.writer.AggregationDocumenter;
import org.rodnansol.core.generator.writer.CombinedInput;
import org.rodnansol.core.generator.writer.CreateAggregationCommand;
import org.rodnansol.core.generator.writer.CreateDocumentCommand;
import org.rodnansol.core.generator.writer.Documenter;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.ProjectType;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(
    mixinStandardHelpOptions = true,
    version = PropertyDocumenter.VERSION,
    description = "CLI tool that reads Jar or raw files to generate documentation about the Spring properties",
    subcommands = {
        PropertyDocumenter.GenerateCommand.class,
        PropertyDocumenter.AggregatorCommand.class
    })
public class PropertyDocumenter {

    protected static final String VERSION = "0.1.0";

    public static void main(String... args) {
        int exitCode = new CommandLine(new PropertyDocumenter()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "generate",
        mixinStandardHelpOptions = true,
        version = "PropertyDocumenter 0.1",
        description =
            """
                Documents the incoming input into a file.
                                
                Example: jbang PropertyDocumenter.java document \\
                            -n "Header title"  -i ~/module-a \\
                            -tt HTML \\
                            -o property-docs.html
                """
    )
    static class GenerateCommand implements Runnable {

        private static final Documenter DOCUMENTER = new Documenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultProvidedInstance(), MetadataInputResolverContext.INSTANCE);

        /**
         * Main header name in the aggregated output file.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-n", "--name"}, description = "Main header name", arity = "1", defaultValue = "Spring Properties")
        String documentName;

        /**
         * List of the inputs, they can be proper JSON files, JAR/Zip files or only directories. The script will determine what to consume.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-i", "--input"}, description = "Input file, directory or JAR file", arity = "1", required = true)
        File input;

        /**
         * Name of the output file.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-o", "--output"}, description = "Output file name", arity = "1", required = true)
        File outputFile;

        /**
         * Desired template type, it can be MARKDOWN, ADOC or HTML
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-tt", "--template-type"},
            description = """
                Desired template type, supported:
                ${COMPLETION-CANDIDATES}
                """,
            arity = "1", defaultValue = "MARKDOWN")
        TemplateType templateType;

        /**
         * Type of the project. Currently supported:
         *
         * <ul>
         *     <li>MAVEN</li>
         *     <li>GRADLE</li>
         * </ul>
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-pt", "--project-type"},
            description = """
                Type of the project,
                currently supported: MAVEN, GRADLE
                """, arity = "1", defaultValue = "MAVEN")
        ProjectType projectType;

        @Override
        public void run() {
            Project project = ProjectFactory.ofType(new File("."), documentName, projectType);
            CreateDocumentCommand createDocumentCommand = new CreateDocumentCommand(project, documentName, input, templateType.getSingleTemplate(), outputFile, TemplateCustomizationFactory.getDefaultTemplateCustomizationByType(templateType));
            try {
                DOCUMENTER.readMetadataAndGenerateRenderedFile(createDocumentCommand);
            } catch (IOException e) {
                throw new DocumentGenerationException("Error during generating the documentation, please check the logs...", e);
            }
        }
    }

    @CommandLine.Command(name = "aggregate",
        mixinStandardHelpOptions = true,
        version = "PropertyDocumenter 0.1",
        description =
            """
                Aggregates the incoming inputs into one big file while.
                Please specify the module names for each input with a sequence.
                                
                Example: jbang PropertyDocumenter.java aggregate \\
                            -n "Multi module project" \\
                            -mn "Module A" -i ~/module-a \\
                            -mn "Module B" -i ~/module-b \\
                            -mn "Module C" -i ~/module-c \\
                            -tt HTML \\
                            -o aggregated-property-docs.html
                """
    )
    static class AggregatorCommand implements Runnable {

        private static final AggregationDocumenter AGGREGATION_DOCUMENTER = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompilerFactory.getDefaultProvidedInstance(), MetadataInputResolverContext.INSTANCE);

        /**
         * Main header name in the aggregated output file.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-n", "--name"}, description = "Main header name", arity = "1", defaultValue = "Spring Properties")
        String mainName;

        /**
         * List of the module names.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-mn", "--module-names"}, description = "List of the module names", arity = "1..*", defaultValue = "Spring Properties")
        List<String> moduleNames;

        /**
         * List of the inputs, they can be proper JSON files, JAR/Zip files or only directories. The script will determine what to consume.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-i", "--inputs"}, description = "List of input files, directories or JAR files", arity = "1..*", required = true)
        List<String> inputs;

        /**
         * Name of the output file.
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-o", "--output"}, description = "Output file name", arity = "1", required = true)
        String outputFile;

        /**
         * Desired template type, it can be MARKDOWN, ADOC or HTML
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-tt", "--template-type"},
            description = """
                Desired template type, supported:
                ${COMPLETION-CANDIDATES}
                """,
            arity = "1", defaultValue = "MARKDOWN")
        TemplateType templateType;

        /**
         * Type of the project. Currently supported:
         *
         * <ul>
         *     <li>MAVEN</li>
         *     <li>GRADLE</li>
         * </ul>
         *
         * @since 0.1.0
         */
        @CommandLine.Option(names = {"-pt", "--project-type"},
            description = """
                Type of the project,
                currently supported: MAVEN, GRADLE
                """, arity = "1", defaultValue = "MAVEN")
        ProjectType projectType;

        @Override
        public void run() {
            List<CombinedInput> combinedInputs = new ArrayList<>(inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                String input = inputs.get(i);
                combinedInputs.add(new CombinedInput(new File(input), getModuleName(i)));
            }
            Project project = ProjectFactory.ofType(new File("."), mainName, projectType);
            CreateAggregationCommand createAggregationCommand = new CreateAggregationCommand(project,
                mainName,
                combinedInputs,
                templateType,
                TemplateCustomizationFactory.getDefaultTemplateCustomizationByType(templateType),
                new File(outputFile));
            AGGREGATION_DOCUMENTER.createDocumentsAndAggregate(createAggregationCommand);
        }

        private String getModuleName(int i) {
            try {
                return moduleNames.get(i);
            } catch (IndexOutOfBoundsException e) {
                return "Module " + i;
            }
        }
    }
}
