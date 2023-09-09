package org.rodnansol.core.generator.writer;

import java.util.StringJoiner;

/**
 * Class representing a customization for the templates.
 * <p>
 * It should store the path to the custom templates.
 *
 * @author nandorholozsnyak
 * @since 0.2.1
 */
public class CustomTemplate {

    private String customHeaderTemplate;
    private String customContentTemplate;
    private String customFooterTemplate;

    public CustomTemplate(String customHeaderTemplate, String customContentTemplate, String customFooterTemplate) {
        this.customHeaderTemplate = customHeaderTemplate;
        this.customContentTemplate = customContentTemplate;
        this.customFooterTemplate = customFooterTemplate;
    }

    public String getCustomHeaderTemplate() {
        return customHeaderTemplate;
    }

    public void setCustomHeaderTemplate(String customHeaderTemplate) {
        this.customHeaderTemplate = customHeaderTemplate;
    }

    public String getCustomContentTemplate() {
        return customContentTemplate;
    }

    public void setCustomContentTemplate(String customContentTemplate) {
        this.customContentTemplate = customContentTemplate;
    }

    public String getCustomFooterTemplate() {
        return customFooterTemplate;
    }

    public void setCustomFooterTemplate(String customFooterTemplate) {
        this.customFooterTemplate = customFooterTemplate;
    }

    @Override
    public String toString() {
        return new StringJoiner(",\n\t", CustomTemplate.class.getSimpleName() + "[", "]")
            .add("customHeaderTemplate='" + customHeaderTemplate + "'")
            .add("customContentTemplate='" + customContentTemplate + "'")
            .add("customFooterTemplate='" + customFooterTemplate + "'")
            .toString();
    }
}
