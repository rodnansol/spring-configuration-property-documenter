package org.rodnansol.core.generator.template;

/**
 * Class collecting all the necessary resources for the different {@link TemplateType}s.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class TemplateResource {

    private final String headerTemplate;
    private final String contentTemplate;
    private final String footerTemplate;
    private final String combinedTemplate;

    public TemplateResource(String headerTemplate, String contentTemplate, String footerTemplate, String combinedTemplate) {
        this.headerTemplate = headerTemplate;
        this.contentTemplate = contentTemplate;
        this.footerTemplate = footerTemplate;
        this.combinedTemplate = combinedTemplate;
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

    public String getCombinedTemplate() {
        return combinedTemplate;
    }
}
