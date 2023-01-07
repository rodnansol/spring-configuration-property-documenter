package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.Project;
import org.rodnansol.core.util.CoreFileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class that is able to read a jar/zip file and get a specific entry from that.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
class JarFileMetadataInputResolver implements MetadataInputResolver {

    protected static final String PATH_IN_JAR_FILE = "META-INF/spring-configuration-metadata.json";

    @Override
    public InputStream resolveInputStream(Project project, File input) {
        try (ZipFile jarFile = new ZipFile(input)) {
            return getEntry(jarFile);
        } catch (IOException e) {
            throw new DocumentGenerationException("Unable to open the jar/zip file:[" + input + "]", e);
        }
    }

    @Override
    public boolean supports(File input) {
        return CoreFileUtils.isJarOrZipFile(input);
    }

    private ByteArrayInputStream getEntry(ZipFile zipFile) throws IOException {
        ZipEntry entry = zipFile.getEntry(PATH_IN_JAR_FILE);
        if (entry == null) {
            return null;
        }
        InputStream inputStream = zipFile.getInputStream(entry);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        inputStream.transferTo(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
