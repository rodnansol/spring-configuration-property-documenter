package org.rodnansol.core.generator.resolver;

/**
 * Represents the strategies for resolving input file operations.
 *
 * @author nandorholozsnyak
 * @since 0.7.0
 */
public enum InputFileResolutionStrategy {

    /**
     * Throw an exception if the input file is not found.
     *
     * @since 0.7.0
     */
    THROW_EXCEPTION,

    /**
     * Return an empty input stream is the file is not found.
     *
     * @since 0.7.0
     */
    RETURN_EMPTY;

    /**
     * Creates an instance of InputFileResolutionStrategy based on the boolean value provided.
     *
     * @param value the boolean value to determine the resolution strategy
     * @return the corresponding InputFileResolutionStrategy instance:
     *         - THROW_EXCEPTION if the value is true
     *         - RETURN_EMPTY if the value is false
     */
    public static InputFileResolutionStrategy ofBooleanValue(boolean value) {
        return value ? THROW_EXCEPTION : RETURN_EMPTY;
    }

}
