package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.Project;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class MetadataInputResolverContext {

    public static final MetadataInputResolverContext INSTANCE = new MetadataInputResolverContext();

    private static final List<MetadataInputResolver> METADATA_INPUT_RESOLVERS = List.of(
        new JarFileMetadataInputResolver(),
        new FileMetadataInputResolver(),
        new DirectoryMetadataInputResolver()
    );

    /**
     * @param project
     * @param input
     * @return
     */
    public InputStream getInputStreamFromFile(Project project, File input) {
        for (MetadataInputResolver metadataInputResolver : METADATA_INPUT_RESOLVERS) {
            if (metadataInputResolver.supports(input)) {
                InputStream inputStream = metadataInputResolver.resolveInputStream(project, input);
                if (inputStream != null) {
                    return inputStream;
                }
            }
        }
        throw new DocumentGenerationException("Unable to locate the spring-configuration-metadata.json file from the given input:[" + input + "]");
    }

}
