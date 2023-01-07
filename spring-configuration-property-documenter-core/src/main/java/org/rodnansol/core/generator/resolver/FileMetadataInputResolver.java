package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.project.Project;
import org.rodnansol.core.util.CoreFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class that is resolving an input to a file stream.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
class FileMetadataInputResolver implements MetadataInputResolver {

    @Override
    public InputStream resolveInputStream(Project project, File input) {
        if (input.isDirectory()) {
            return null;
        }
        try {
            return new FileInputStream(input);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean supports(File input) {
        return !CoreFileUtils.isJarOrZipFile(input) && input.isFile();
    }

}
