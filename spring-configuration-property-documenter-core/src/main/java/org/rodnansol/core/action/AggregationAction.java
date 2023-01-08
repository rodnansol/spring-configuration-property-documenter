package org.rodnansol.core.action;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.util.CoreFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing aggregation action.
 * <p>
 * This aggregation action can do a lot of things, if it is put into a parent module (like a parent Maven module) it can read all the child module generated files, but the only problem is the lifecycle.
 * <p>
 * Without defining the inputs the action will behave differently between builds because the parent modules are running first and this action can be invoked before the documents are ready to be aggregated.
 * <p>
 * This can be useful to use in project where the developers are explicitly defining the inputs that must be aggregated into a single output file.
 * <p>
 * <b>THIS IS A VERY EXPERIMENTAL CODE HERE.</b>
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class AggregationAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregationAction.class);

    private final Project project;

    private final List<File> inputFiles;

    private final File outputFile;

    public AggregationAction(Project project, List<File> inputFiles, File outputFile) {
        this.project = project;
        this.inputFiles = inputFiles;
        this.outputFile = outputFile;
    }

    public void executeAggregation() {
        if (emptyInputFiles()) {
            aggregateAlreadyExistingSources();
        } else {
            aggregateSpecifiedInputFiles();
        }
    }

    private boolean emptyInputFiles() {
        return inputFiles == null || inputFiles.isEmpty();
    }

    private void aggregateAlreadyExistingSources() throws DocumentGenerationException {
        if (outputFile == null) {
            LOGGER.debug("Input files are empty and output file is not specified, creating aggregations from all existing sources...");
            createAggregatedFilesFromExistingSources();
        } else {
            String requestedExtension = getFileExtension(outputFile);
            LOGGER.debug("Input files are empty but output file is specified with the following extension:[{}], files with that extension will be aggregated", requestedExtension);
            createAggregatedFilesFromExistingSourcesWithGivenExtension(outputFile.getAbsolutePath(), requestedExtension);
        }
    }

    private void aggregateSpecifiedInputFiles() throws DocumentGenerationException {
        String requiredExtension = getFileExtension(inputFiles.get(0));
        boolean allMatch = inputFiles.stream()
            .allMatch(file -> getFileExtension(file).equals(requiredExtension));
        if (!allMatch) {
            throw new DocumentGenerationException("Unable to aggregate files with different extensions");
        }
        if (outputFile == null) {
            String aggregatedTargetFileName = project.getDefaultAggregatedTargetFilePath(requiredExtension);
            LOGGER.debug("Input files are specified, but the output file is missing, aggregating files with the following extension:[{}] into [{}]", requiredExtension, aggregatedTargetFileName);
            aggregateFiles(aggregatedTargetFileName, inputFiles);
        } else {
            LOGGER.debug("Input files are specified and the output file is also specified, creating aggregation to the following file:[{}]", outputFile);
            aggregateFiles(outputFile.getAbsolutePath(), inputFiles);
        }
    }


    private void createAggregatedFilesFromExistingSources() throws DocumentGenerationException {
        Map<String, List<File>> fileExtensionMap = getChildModuleBasedDocumentationsGroupedByFileExtensions();
        if (fileExtensionMap.isEmpty()) {
            LOGGER.info("No files to be aggregated...");
            return;
        }
        for (Map.Entry<String, List<File>> entry : fileExtensionMap.entrySet()) {
            String aggregatedTargetFileName = project.getDefaultAggregatedTargetFilePath(entry.getKey());
            List<File> files = entry.getValue();
            aggregateFiles(aggregatedTargetFileName, files);
        }
    }

    private void createAggregatedFilesFromExistingSourcesWithGivenExtension(String outputFileName, String extension) throws DocumentGenerationException {
        Map<String, List<File>> fileExtensionMap = getChildModuleBasedDocumentationsGroupedByFileExtensions();
        if (fileExtensionMap.isEmpty()) {
            LOGGER.info("No files to be aggregated...");
            return;
        }
        List<File> requestedFiles = fileExtensionMap.get(extension);
        if (requestedFiles != null) {
            aggregateFiles(outputFileName, requestedFiles);
        }
    }

    private void aggregateFiles(String aggregatedTargetFileName, List<File> files) throws DocumentGenerationException {
        try (FileWriter fileWriter = new FileWriter(CoreFileUtils.initializeFileWithPath(aggregatedTargetFileName))) {
            LOGGER.info("Aggregating files:[{}] into: [{}]", files, aggregatedTargetFileName);
            for (File file : files) {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                fileWriter.append(content);
            }
        } catch (IOException e) {
            LOGGER.warn("Unable to generate aggregated file at path:[" + aggregatedTargetFileName + "], please check the logs for more information", e);
        }
    }

    private Map<String, List<File>> getChildModuleBasedDocumentationsGroupedByFileExtensions() {
        return project.getModules()
            .stream()
            .map(project::getDefaultOutputFolder)
            .map(this::listFilesOfGivenFolder)
            .filter(Objects::nonNull)
            .flatMap(Arrays::stream)
            .collect(Collectors.groupingBy(this::getFileExtension));
    }

    private String getFileExtension(File item) {
        return FilenameUtils.getExtension(item.getName());
    }

    private File[] listFilesOfGivenFolder(String outputFolder) {
        return Path.of(outputFolder).toFile().listFiles();
    }

}
