package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.project.Project;

import java.io.File;
import java.io.InputStream;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public interface MetadataInputResolver {

    /**
     * Resolves the metadata file from the given parameters.
     *
     * @param project project instance.
     * @param input   input that could be a file, directory or a jar/zip file.
     * @return resolved input stream.
     */
    InputStream resolveInputStream(Project project, File input);

    /**
     * Checks if the input resolver supports this input or not.
     *
     * @param input
     * @return
     */
    boolean supports(File input);

}
