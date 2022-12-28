package org.rodnansol.core.generator;

/**
 * Exception to be thrown when the document generation flow can not be finished.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class DocumentGenerationException extends RuntimeException {

    public DocumentGenerationException(String message) {
        super(message);
    }

    public DocumentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
