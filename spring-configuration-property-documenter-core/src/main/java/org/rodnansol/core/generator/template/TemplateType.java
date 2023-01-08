package org.rodnansol.core.generator.template;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public enum TemplateType {
    MARKDOWN(".md", HeaderTemplateConstants.MARKDOWN, ContentTemplateConstants.MARKDOWN, FooterTemplateConstants.MARKDOWN, SingleTemplateConstants.MARKDOWN),
    ADOC(".adoc", HeaderTemplateConstants.ADOC, ContentTemplateConstants.ADOC, FooterTemplateConstants.ADOC, SingleTemplateConstants.ADOC),
    HTML(".html", HeaderTemplateConstants.HTML, ContentTemplateConstants.HTML, FooterTemplateConstants.HTML, SingleTemplateConstants.HTML),
    XML(".xml", HeaderTemplateConstants.XML, ContentTemplateConstants.XML, FooterTemplateConstants.XML, SingleTemplateConstants.XML)
    ;

    private final String extension;
    private final String headerTemplate;
    private final String contentTemplate;
    private final String footerTemplate;

    private final String singleTemplate;

    TemplateType(String extension, String headerTemplate, String contentTemplate, String footerTemplate, String singleTemplate) {
        this.extension = extension;
        this.headerTemplate = headerTemplate;
        this.contentTemplate = contentTemplate;
        this.footerTemplate = footerTemplate;
        this.singleTemplate = singleTemplate;
    }

    public String getSingleTemplate() {
        return singleTemplate;
    }

    public String getExtension() {
        return extension;
    }

    public String getHeaderTemplate() {
        return headerTemplate;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public String getFooterTemplate() {
        return footerTemplate;
    }

    static class SingleTemplateConstants {

        static final String ADOC = "templates/single/single-document-template.adoc";
        static final String MARKDOWN = "templates/single/single-document-template.md";
        static final String HTML = "templates/single/single-document-template.html";
        static final String XML = "templates/single/single-document-template.xml";
    }

    static class HeaderTemplateConstants {

        static final String ADOC = "templates/aggregated/adoc/header.adoc";
        static final String MARKDOWN = "templates/aggregated/md/header.md";
        static final String HTML = "templates/aggregated/html/header.html";
        static final String XML = "templates/aggregated/xml/header.xml";
    }

    static class ContentTemplateConstants {

        static final String ADOC = "templates/partials/adoc/content.adoc";
        static final String MARKDOWN = "templates/partials/md/content.md";
        static final String HTML = "templates/partials/html/content.html";
        static final String XML = "templates/partials/xml/content.xml";
    }

    static class FooterTemplateConstants {

        static final String ADOC = "templates/aggregated/adoc/footer.adoc";
        static final String MARKDOWN = "templates/aggregated/md/footer.md";
        static final String HTML = "templates/aggregated/html/footer.html";
        static final String XML = "templates/aggregated/xml/footer.xml";
    }
}
