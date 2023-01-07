package org.rodnansol.core.generator.reader;

/**
 * Exception to be thrown when an error occurs at the metadata conversion.
 *
 * @author nandorholozsnyak
 * @since 0.1.2
 */
public class MetadataConversionException extends RuntimeException {

    public MetadataConversionException(String message) {
        super(message);
    }

    public MetadataConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
