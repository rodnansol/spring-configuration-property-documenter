package org.rodnansol.core.generator;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public enum TemplateType {
    MARKDOWN(".md"),
    ADOC(".adoc"),
    HTML(".html");

    private static final String TEMPLATES_TEMPLATE_ADOC = "templates/template.adoc";
    private static final String TEMPLATES_TEMPLATE_MD = "templates/template.md";
    private static final String TEMPLATES_TEMPLATE_HTML = "templates/template.html";
    private final String extension;

    TemplateType(String extension) {
        this.extension = extension;
    }

    public String calculateTemplate() {
        switch (this) {
            case ADOC:
                return TEMPLATES_TEMPLATE_ADOC;
            case MARKDOWN:
                return TEMPLATES_TEMPLATE_MD;
            case HTML:
                return TEMPLATES_TEMPLATE_HTML;
            default:
                throw new IllegalStateException("Unknown template type");
        }
    }

    public String getExtension() {
        return extension;
    }
}
