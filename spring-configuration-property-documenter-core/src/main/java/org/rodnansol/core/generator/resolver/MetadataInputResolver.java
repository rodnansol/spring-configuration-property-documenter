package org.rodnansol.core.generator.resolver;

import org.rodnansol.core.project.Project;

import java.io.File;
import java.io.InputStream;

/**
 * Interface representing methods that a new instance must implement if it wants to resolve <code>spring-configuration-metadata.json</code> files from different sources.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public interface MetadataInputResolver {

    /**
     * Resolves the metadata file from the given parameters.
     *
     * @param project project instance.
     * @param input   input that could be a file, directory or a jar/zip file.
     * @return resolved input stream if the input contains the desired element, otherwise it returns null.
     */
    InputStream resolveInputStream(Project project, File input);

    /**
     * Checks if the input resolver supports this input or not.
     *
     * @param input input object.
     * @return <b>true</b> if the resolver supports this input, otherwise <b>false</b>.
     */
    boolean supports(File input);

}
