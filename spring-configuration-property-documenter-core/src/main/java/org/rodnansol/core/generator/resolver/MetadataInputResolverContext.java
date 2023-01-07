package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.generator.DocumentGenerationException;
import org.rodnansol.core.project.Project;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Class that uses different {@link MetadataInputResolver} instances to resolve the <code>spring-configuration-metadata.json</code> file from different inputs.
 *
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
     * Returns the requested file from the given input.
     * <p>
     * The input can be a file, a directory or a ZIP/JAR file, these are the supported inputs now and this method will use different {@link MetadataInputResolver} instances to resolve the metadata file.
     * <p>
     * It will run the resolvers in the following order:
     * <ol>
     *     <li>JarFileMetadataInputResolver - First it will check if it is a ZIP/JAR file or not</li>
     *     <li>FileMetadataInputResolver - Second it will check if it is file or not</li>
     *     <li>DirectoryMetadataInputResolver - Third it will check if it is directory, if yes it will search the {@link Project#getPossibleMetadataFilePaths()} for the <code>spring-configuration-metadata.json</code> file</li>
     * </ol>
     *
     * @param project project instance.
     * @param input   input.
     * @return the file's content in an {@link InputStream} instance.
     * @throws DocumentGenerationException if none of the resolvers are able to find the file.
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
