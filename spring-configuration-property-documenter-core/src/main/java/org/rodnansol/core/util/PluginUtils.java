package org.rodnansol.core.util;


import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.generator.TemplateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Class containing utility methods.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public final class PluginUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginUtils.class);

    private PluginUtils() {
    }


    /**
     * Returns the default output folder based on the {@link Project} instance and module name (in multi-module setup).
     * <p>
     * For example: <b>multi-module-parent/multi-module-a/target/property-docs</b>
     *
     * @param project    maven project instance.
     * @param moduleName module's name.
     * @return output folder based on the incoming parameters.
     */
    public static String getDefaultOutputFolder(Project project, String moduleName) {
        return project.getBasedir() + "/" + String.format(PluginConstants.DEFAULT_OUTPUT_DIRECTORY, moduleName);
    }

    /**
     * Returns the default output folder based on the {@link Project} instance.
     * <p>
     * For example: <b>module-a/target/property-docs</b>
     *
     * @param project maven project instance.
     * @return output folder based on the incoming parameter.
     */
    public static String getDefaultOutputFolder(Project project) {
        return String.format(PluginConstants.DEFAULT_OUTPUT_DIRECTORY, project.getBasedir());
    }

    /**
     * Returns the output file path based on  the {@link Project} instance and template type value.
     *
     * <p>
     * For example: <b>multi-module-parent/target/property-docs/multi-module-parent-property-docs-aggregated.md</b>
     *
     * @param project      maven project instance.
     * @param templateType type of the template, the extension will be determined based on this.
     * @return target file path based on the project instance and template type.
     */
    public static String getDefaultTargetFilePath(Project project, TemplateType templateType) {
        return String.format(PluginConstants.DEFAULT_TARGET_FILE_PATH, PluginUtils.getDefaultOutputFolder(project), project.getName(), templateType.getExtension());
    }

    /**
     * Returns the output file path based on  the {@link Project} instance and template type value.
     *
     * <p>
     * For example: <b>module-a/target/property-docs/module-a-property-docs.md</b>
     *
     * @param project   maven project instance.
     * @param extension extension (like .md, .adoc etc)
     * @return target file path based on the incoming parameters.
     */
    public static String getDefaultAggregatedTargetFilePath(Project project, String extension) {
        return String.format(PluginConstants.DEFAULT_AGGREGATED_FILE_PATH, PluginUtils.getDefaultOutputFolder(project), project.getName(), extension);
    }

    /**
     * Initialize a file and its parent folders and returns the file.
     *
     * @param targetFilePath target file path.
     * @return the created file instance.
     */
    public static File initializeFile(String targetFilePath) throws DocumentGenerationException {
        File outputFile = new File(targetFilePath);
        if (!outputFile.exists()) {
            try {
                File parentFile = outputFile.getParentFile();
                if (parentFile != null) {
                    boolean created = parentFile.mkdirs();
                    if (created) {
                        LOGGER.debug("Parent folders for path:[{}] were created", targetFilePath);
                    } else {
                        LOGGER.debug("Parent folders for path:[{}] were NOT created", targetFilePath);
                    }
                }
                boolean newFile = outputFile.createNewFile();
                if (newFile) {
                    LOGGER.debug("File at path:[{}] were created",targetFilePath);
                } else {
                    LOGGER.debug("File at path:[{}] already exists, new file were not created...",targetFilePath);
                }
            } catch (IOException e) {
                throw new DocumentGenerationException("Unable to create output file:[" + outputFile + "]", e);
            }
        }
        return outputFile;
    }

}
