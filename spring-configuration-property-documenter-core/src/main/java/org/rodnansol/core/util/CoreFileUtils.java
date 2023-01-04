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
public final class CoreFileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreFileUtils.class);

    private CoreFileUtils() {
    }

    /**
     * Initialize a file and its parent directories and returns the file.
     *
     * @param targetFilePath target file path.
     * @return the created file instance.
     * @throws DocumentGenerationException if the target file or its directories can not be created.
     */
    public static File initializeFileWithPath(String targetFilePath) throws DocumentGenerationException {
        File outputFile = new File(targetFilePath);
        if (!outputFile.exists()) {
            try {
                createParentDirectoriesIfNeeded(targetFilePath, outputFile);
                createTheTargetFile(targetFilePath, outputFile);
            } catch (IOException e) {
                throw new DocumentGenerationException("Unable to create output file:[" + outputFile + "]", e);
            }
        }
        return outputFile;
    }

    /**
     * Initialize a file and its parent directories and returns the file.
     *
     * @param targetFile target file.
     * @return the created file instance.
     * @throws DocumentGenerationException if the target file or its directories can not be created.
     */
    public static File initializeFileWithPath(File targetFile) throws DocumentGenerationException {
        return initializeFileWithPath(targetFile.getPath());
    }

    private static void createTheTargetFile(String targetFilePath, File outputFile) throws IOException {
        boolean newFile = outputFile.createNewFile();
        if (newFile) {
            LOGGER.debug("File at path:[{}] were created", targetFilePath);
        } else {
            LOGGER.debug("File at path:[{}] already exists, new file were not created...", targetFilePath);
        }
    }

    private static void createParentDirectoriesIfNeeded(String targetFilePath, File outputFile) {
        File parentFile = outputFile.getParentFile();
        if (parentFile != null) {
            boolean created = parentFile.mkdirs();
            if (created) {
                LOGGER.debug("Parent folders for path:[{}] were created", targetFilePath);
            } else {
                LOGGER.debug("Parent folders for path:[{}] were NOT created", targetFilePath);
            }
        }
    }

}
