package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

class DirectoryMetadataInputResolver implements MetadataInputResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryMetadataInputResolver.class);

    @Override
    public InputStream resolveInputStream(Project project, File input) {
        List<String> possibleMetadataFilePaths = project.getPossibleMetadataFilePaths();
        for (String possibleMetadataFilePath : possibleMetadataFilePaths) {
            File possiblePath = new File(input + possibleMetadataFilePath);
            try {
                return new FileInputStream(possiblePath);
            } catch (FileNotFoundException e) {
                LOGGER.debug("Unable to locate spring-configuration-metadata.json in the following path:[{}]", possiblePath);
            }
        }
        return null;
    }

    @Override
    public boolean supports(File input) {
        return input.isDirectory();
    }

}
