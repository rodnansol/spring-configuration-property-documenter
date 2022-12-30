package org.rodnansol.core.generator.resolver;

import org.apache.commons.io.FilenameUtils;
import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.Project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

/**
 *
 */
class JarFileMetadataInputResolver implements MetadataInputResolver {

    protected static final String PATH_IN_JAR_FILE = "META-INF/spring-configuration-metadata.json";

    @Override
    public InputStream resolveInputStream(Project project, File input) {
        try (JarFile jarFile = new JarFile(input)) {
            InputStream inputStream = jarFile.getInputStream(jarFile.getEntry(PATH_IN_JAR_FILE));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            inputStream.transferTo(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new DocumentGenerationException("Unable to open the jar/zip file:[" + input + "]", e);
        }
    }

    @Override
    public boolean supports(File input) {
        String extension = FilenameUtils.getExtension(input.getName());
        return isJarOrZip(extension);
    }

    private boolean isJarOrZip(String extension) {
        return extension.equals("jar") || extension.equals("zip");
    }
}
