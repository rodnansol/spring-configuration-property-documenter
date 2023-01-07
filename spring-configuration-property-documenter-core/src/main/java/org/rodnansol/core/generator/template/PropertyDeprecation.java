package org.rodnansol.core.generator.template;

/**
 * Class represnting a deprecation info.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class PropertyDeprecation {

    private final String reason;
    private final String replacement;

    public PropertyDeprecation(String reason, String replacement) {
        this.reason = reason;
        this.replacement = replacement;
    }

    public String getReason() {
        return reason;
    }

    public String getReplacement() {
        return replacement;
    }

    @Override
    public String toString() {
        return "Reason: " + reason + ", use for replacement: " + replacement;
    }
}
