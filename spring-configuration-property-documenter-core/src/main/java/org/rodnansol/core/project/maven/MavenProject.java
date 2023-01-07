package org.rodnansol.core.project.maven;

import org.rodnansol.core.generator.template.TemplateType;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.project.ProjectType;

import java.io.File;
import java.util.List;

/**
 * Class representing a Maven project.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class MavenProject extends Project {

    /**
     * Default folder name where the generated files will be stored.
     */
    private static final String DEFAULT_TARGET_FOLDER = "property-docs";

    /**
     * Default output directory for the documents.
     */
    private static final String DEFAULT_OUTPUT_DIRECTORY = "%s/target/" + DEFAULT_TARGET_FOLDER;

    /**
     * Default target file path, must be formatted.
     */
    private static final String DEFAULT_TARGET_FILE_PATH = "%s/%s-property-docs%s";

    /**
     * Default aggregated target file path, must be formatted.
     */
    private static final String DEFAULT_AGGREGATED_FILE_PATH = "%s/%s-property-docs-aggregated.%s";

    public MavenProject(File basedir, String name) {
        super(basedir, name, ProjectType.MAVEN);
    }

    public MavenProject(File basedir, String name, List<String> modules) {
        super(basedir, name, ProjectType.MAVEN);
        this.modules = modules;
    }

    @Override
    public String getDefaultOutputFolder(String moduleName) {
        return this.getBasedir() + "/" + String.format(DEFAULT_OUTPUT_DIRECTORY, moduleName);
    }

    @Override
    public String getDefaultOutputFolder() {
        return String.format(DEFAULT_OUTPUT_DIRECTORY, this.getBasedir());
    }

    @Override
    public String getDefaultTargetFilePath(TemplateType templateType) {
        return String.format(DEFAULT_TARGET_FILE_PATH, getDefaultOutputFolder(), this.getName(), templateType.getExtension());
    }

    @Override
    public String getDefaultAggregatedTargetFilePath(String extension) {
        return String.format(DEFAULT_AGGREGATED_FILE_PATH, getDefaultOutputFolder(), this.getName(), extension);
    }

    @Override
    public List<String> getPossibleMetadataFilePaths() {
        return List.of(
            "/target/classes/META-INF/spring-configuration-metadata.json",
            "/classes/META-INF/spring-configuration-metadata.json",
            "/META-INF/spring-configuration-metadata.json",
            "/spring-configuration-metadata.json"
        );
    }
}
