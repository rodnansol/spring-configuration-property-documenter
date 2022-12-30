package org.rodnansol.core.util;


import org.rodnansol.core.generator.DocumentGenerationException;
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
                    LOGGER.debug("File at path:[{}] were created", targetFilePath);
                } else {
                    LOGGER.debug("File at path:[{}] already exists, new file were not created...", targetFilePath);
                }
            } catch (IOException e) {
                throw new DocumentGenerationException("Unable to create output file:[" + outputFile + "]", e);
            }
        }
        return outputFile;
    }

}
