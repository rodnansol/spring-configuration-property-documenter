package org.rodnansol.core.generator.template.handlebars;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that is able to return the current working directory.
 *
 * @author nandorholozsnyak
 * @since 0.2.1
 */
public class WorkingDirectoryProvider {

    public static final WorkingDirectoryProvider INSTANCE = new WorkingDirectoryProvider();

    private WorkingDirectoryProvider(){}

    /**
     * Returns the current working directory.
     *
     * @return current working directory.
     */
    public String getCurrentWorkingDirectory() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    /**
     *
     * @return
     */
    public Path getCurrentWorkingDirectoryPath() {
        return Paths.get(".").toAbsolutePath().normalize();
    }

}
