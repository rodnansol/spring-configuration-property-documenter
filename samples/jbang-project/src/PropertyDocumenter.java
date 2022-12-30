///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.rodnansol:spring-configuration-property-documenter-core:999-SNAPSHOT
//DEPS info.picocli:picocli:4.7.0
//JAVA 17

import org.apache.commons.io.FilenameUtils;
import org.rodnansol.core.generator.AggregationDocumenter;
import org.rodnansol.core.generator.CombinedInput;
import org.rodnansol.core.generator.CreateAggregationCommand;
import org.rodnansol.core.generator.Documenter;
import org.rodnansol.core.generator.MetadataReader;
import org.rodnansol.core.generator.TemplateCompiler;
import org.rodnansol.core.generator.TemplateType;
import org.rodnansol.core.generator.resolver.MetadataInputResolverContext;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.project.ProjectFactory;
import org.rodnansol.core.project.ProjectType;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(
    mixinStandardHelpOptions = true,
    version = "PropertyDocumenter 0.1",
    description = "CLI tool that reads Jar or raw files to generate documentation about the Spring properties",
    subcommands = {
        PropertyDocumenter.OldCommand.class,
        PropertyDocumenter.AggegatorCommand.class
    })
public class PropertyDocumenter {

    public static void main(String... args) {
        int exitCode = new CommandLine(new PropertyDocumenter()).execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Command(name = "old",
        description = "CLI tool that reads Jar or raw files to generate documentation about the Spring properties")
    static class OldCommand implements Runnable {

        private static final Documenter DOCUMENTER = new Documenter(MetadataReader.INSTANCE, TemplateCompiler.INSTANCE, MetadataInputResolverContext.INSTANCE);

        @CommandLine.Option(names = {"-n", "--name"}, description = "List of the generated document header name", arity = "0..*", defaultValue = "Spring Properties")
        private List<String> documentNames;

        @CommandLine.Option(names = {"-j", "--jars"}, description = "List of the JAR files", arity = "0..*")
        private List<String> jarFiles;

        @CommandLine.Option(names = {"-f", "--files"}, description = "List of the raw metadata files", arity = "0..*")
        private List<String> metadataFiles;

        @CommandLine.Option(names = {"-o", "--output"}, description = "List of the output file names", arity = "0..*", required = true)
        private List<String> outputFileNames;

        @CommandLine.Option(names = {"-tt", "--template-type"}, description = "List of the template types", arity = "0..*", defaultValue = "MARKDOWN")
        private TemplateType templateType;

        @CommandLine.Option(names = {"-t", "--template"}, description = "List of the template files", arity = "0..*")
        private String template;

        @Override
        public void run() {
//            if (jarFiles != null && !jarFiles.isEmpty()) {
//                for (int i = 0; i < jarFiles.size(); i++) {
//                    String jarFileName = jarFiles.get(i);
//                    try (JarFile jarFile = new JarFile(jarFileName)) {
//                        ZipEntry entry = jarFile.getEntry("META-INF/spring-configuration-metadata.json");
//                        InputStream metadataStream = jarFile.getInputStream(entry);
//                        DOCUMENTER.readMetadataAndGenerateRenderedFile(new CreateDocumentCommand(getDocumentName(i), metadataStream, getFinalTemplate(), new File(getOutputFileName(i))));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                return;
//            }
//            if (metadataFiles != null && !metadataFiles.isEmpty()) {
//                for (int i = 0; i < metadataFiles.size(); i++) {
//                    String fileName = metadataFiles.get(i);
//                    try {
//                        DOCUMENTER.readMetadataAndGenerateRenderedFile(new CreateDocumentCommand(getDocumentName(i), new FileInputStream(fileName), getFinalTemplate(), new File(getOutputFileName(i))));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
        }

        private String getDocumentName(int index) {
            try {
                return documentNames.get(index);
            } catch (IndexOutOfBoundsException e) {
                return "Spring Properties";
            }
        }

        private String getOutputFileName(int index) {
            try {
                return outputFileNames.get(index);
            } catch (IndexOutOfBoundsException e) {
                return outputFileNames.isEmpty()
                    ? "property-docs" + FilenameUtils.getExtension(getFinalTemplate())
                    : FilenameUtils.getBaseName(outputFileNames.get(0)) + "-" + index + "." + FilenameUtils.getExtension(getFinalTemplate());
            }
        }

        private String getFinalTemplate() {
            return template != null ? template : templateType.findSingleTemplate();
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
    static class AggegatorCommand implements Runnable {

        private static final AggregationDocumenter AGGREGATION_DOCUMENTER = new AggregationDocumenter(MetadataReader.INSTANCE, TemplateCompiler.INSTANCE, MetadataInputResolverContext.INSTANCE);

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
        @CommandLine.Option(names = {"-i", "--inputs"}, description = "List of the inputs", arity = "1..*", required = true)
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
