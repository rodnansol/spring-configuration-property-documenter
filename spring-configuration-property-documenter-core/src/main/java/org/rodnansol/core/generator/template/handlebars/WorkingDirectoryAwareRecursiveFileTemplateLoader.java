package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.io.FileTemplateLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;

/**
 * This {@link FileTemplateLoader} implementation is able to find recursively a template file by it's location.
 *
 * @author nandorholozsnyak
 * @since 0.2.1
 */
public class WorkingDirectoryAwareRecursiveFileTemplateLoader extends FileTemplateLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingDirectoryAwareRecursiveFileTemplateLoader.class);

    private final WorkingDirectoryProvider workingDirectoryProvider;

    public WorkingDirectoryAwareRecursiveFileTemplateLoader(String basedir, String suffix, WorkingDirectoryProvider workingDirectoryProvider) {
        super(basedir, suffix);
        this.workingDirectoryProvider = workingDirectoryProvider;
    }

    public WorkingDirectoryAwareRecursiveFileTemplateLoader(String basedir, WorkingDirectoryProvider workingDirectoryProvider) {
        super(basedir);
        this.workingDirectoryProvider = workingDirectoryProvider;
    }

    @Override
    protected URL getResource(String location) throws IOException {
        LOGGER.debug("Resolving template with the following location:[{}]", location);
        URL resource = super.getResource(location);
        if (resource != null) {
            return resource;
        }
        String normalizedInput = Path.of(location).normalize().toString();
        String extension = FilenameUtils.getExtension(location);
        Collection<File> childFiles = FileUtils.listFiles(workingDirectoryProvider.getCurrentWorkingDirectoryPath().toFile(), new String[]{extension}, true);
        File optionalFile = childFiles.stream()
            .filter(file -> file.getPath().contains(normalizedInput))
            .findFirst()
            .orElse(null);
        if (optionalFile == null) {
            return null;
        }
        LOGGER.debug("Final location:[{}] for input:[{}]", optionalFile.getPath(), location);
        return optionalFile.exists() ? optionalFile.toURI().toURL() : null;
    }

}
