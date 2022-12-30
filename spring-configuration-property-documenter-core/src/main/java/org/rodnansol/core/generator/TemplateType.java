package org.rodnansol.core.generator;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public enum TemplateType {
    MARKDOWN(".md"),
    ADOC(".adoc"),
    HTML(".html");

    private final String extension;

    TemplateType(String extension) {
        this.extension = extension;
    }

    public String findSingleTemplate() {
        switch (this) {
            case ADOC:
                return SingleTemplateConstants.ADOC;
            case MARKDOWN:
                return SingleTemplateConstants.MARKDOWN;
            case HTML:
                return SingleTemplateConstants.HTML;
            default:
                throw new IllegalStateException("Unknown template type");
        }
    }

    public String getHeaderTemplate() {
        switch (this) {
            case ADOC:
                return HeaderTemplateConstants.ADOC;
            case MARKDOWN:
                return HeaderTemplateConstants.MARKDOWN;
            case HTML:
                return HeaderTemplateConstants.HTML;
            default:
                throw new IllegalStateException("Unknown template type");
        }
    }

    public String getContentTemplate() {
        switch (this) {
            case ADOC:
                return ContentTemplateConstants.ADOC;
            case MARKDOWN:
                return ContentTemplateConstants.MARKDOWN;
            case HTML:
                return ContentTemplateConstants.HTML;
            default:
                throw new IllegalStateException("Unknown template type");
        }
    }

    public String getFooterTemplate() {
        switch (this) {
            case ADOC:
                return FooterTemplateConstants.ADOC;
            case MARKDOWN:
                return FooterTemplateConstants.MARKDOWN;
            case HTML:
                return FooterTemplateConstants.HTML;
            default:
                throw new IllegalStateException("Unknown template type");
        }
    }

    public String getExtension() {
        return extension;
    }

    static class SingleTemplateConstants {

        static final String ADOC = "templates/single/single-document-template.adoc";
        static final String MARKDOWN = "templates/single/single-document-template.md";
        static final String HTML = "templates/single/single-document-template.html";
    }

    static class HeaderTemplateConstants {

        static final String ADOC = "templates/aggregated/adoc/header.adoc";
        static final String MARKDOWN = "templates/aggregated/md/header.md";
        static final String HTML = "templates/aggregated/html/header.html";
    }

    static class ContentTemplateConstants {

        static final String ADOC = "templates/partials/adoc/content.adoc";
        static final String MARKDOWN = "templates/partials/md/content.md";
        static final String HTML = "templates/partials/html/content.html";
    }

    static class FooterTemplateConstants {

        static final String ADOC = "templates/aggregated/adoc/footer.adoc";
        static final String MARKDOWN = "templates/aggregated/md/footer.md";
        static final String HTML = "templates/aggregated/html/footer.html";
    }
}
