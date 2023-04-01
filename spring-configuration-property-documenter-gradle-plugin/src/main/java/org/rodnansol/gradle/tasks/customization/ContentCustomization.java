package org.rodnansol.gradle.tasks.customization;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class containing header customization data.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class ContentCustomization implements Serializable {

    /**
     * Field defines if the `class` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeClass = true;
    /**
     * Field defines if the `key` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeKey = true;
    /**
     * Field defines if the `type` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeType = true;
    /**
     * Field defines if the `description` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeDescription = true;
    /**
     * Field defines if the `defaultValue` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeDefaultValue = true;
    /**
     * Field defines if the `deprecation` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeDeprecation = true;
    /**
     * Field defines if the `envFormat` should be rendered in the final documentation or not.
     *
     * @since 0.6.0
     */
    private boolean includeEnvFormat = false;

    public boolean isIncludeClass() {
        return includeClass;
    }

    public void setIncludeClass(boolean includeClass) {
        this.includeClass = includeClass;
    }

    public boolean isIncludeKey() {
        return includeKey;
    }

    public void setIncludeKey(boolean includeKey) {
        this.includeKey = includeKey;
    }

    public boolean isIncludeType() {
        return includeType;
    }

    public void setIncludeType(boolean includeType) {
        this.includeType = includeType;
    }

    public boolean isIncludeDescription() {
        return includeDescription;
    }

    public void setIncludeDescription(boolean includeDescription) {
        this.includeDescription = includeDescription;
    }

    public boolean isIncludeDefaultValue() {
        return includeDefaultValue;
    }

    public void setIncludeDefaultValue(boolean includeDefaultValue) {
        this.includeDefaultValue = includeDefaultValue;
    }

    public boolean isIncludeDeprecation() {
        return includeDeprecation;
    }

    public void setIncludeDeprecation(boolean includeDeprecation) {
        this.includeDeprecation = includeDeprecation;
    }

    public boolean isIncludeEnvFormat() {
        return includeEnvFormat;
    }

    public void setIncludeEnvFormat(boolean includeEnvFormat) {
        this.includeEnvFormat = includeEnvFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentCustomization that = (ContentCustomization) o;
        return includeClass == that.includeClass && includeKey == that.includeKey && includeType == that.includeType && includeDescription == that.includeDescription && includeDefaultValue == that.includeDefaultValue && includeDeprecation == that.includeDeprecation && includeEnvFormat == that.includeEnvFormat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(includeClass, includeKey, includeType, includeDescription, includeDefaultValue, includeDeprecation, includeEnvFormat);
    }

    @Override
    public String toString() {
        return "ContentCustomization{" +
            "includeClass=" + includeClass +
            ", includeKey=" + includeKey +
            ", includeType=" + includeType +
            ", includeDescription=" + includeDescription +
            ", includeDefaultValue=" + includeDefaultValue +
            ", includeDeprecation=" + includeDeprecation +
            ", includeEnvFormat=" + includeEnvFormat +
            '}';
    }
}
