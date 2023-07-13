package org.rodnansol.core.generator.template.data;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Objects;

/**
 * Class represnting a deprecation info.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class PropertyDeprecation {

    @Nullable
    private final String reason;
    @Nullable
    private final String replacement;

    public PropertyDeprecation() {
        reason = null;
        replacement = null;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyDeprecation that = (PropertyDeprecation) o;
        return Objects.equals(reason, that.reason) && Objects.equals(replacement, that.replacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, replacement);
    }

    @Override
    public String toString() {
        return "Reason: " + reason + ", use for replacement: " + replacement;
    }
}
