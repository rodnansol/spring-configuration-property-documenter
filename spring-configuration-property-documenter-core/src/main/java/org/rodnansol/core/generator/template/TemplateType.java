package org.rodnansol.core.generator.template;

import java.util.Map;
import java.util.Optional;

/**
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public enum TemplateType {

    MARKDOWN(".md",
        Map.of(
            TemplateMode.STANDARD, new TemplateResource(StandardTemplates.HeaderTemplateConstants.MARKDOWN, StandardTemplates.ContentTemplateConstants.MARKDOWN, StandardTemplates.FooterTemplateConstants.MARKDOWN, StandardTemplates.SingleTemplateConstants.MARKDOWN),
            TemplateMode.COMPACT, new TemplateResource(CompactTemplates.HeaderTemplateConstants.MARKDOWN, CompactTemplates.ContentTemplateConstants.MARKDOWN, CompactTemplates.FooterTemplateConstants.MARKDOWN, CompactTemplates.SingleTemplateConstants.MARKDOWN))),
    ADOC(".adoc",
        Map.of(
            TemplateMode.STANDARD, new TemplateResource(StandardTemplates.HeaderTemplateConstants.ADOC, StandardTemplates.ContentTemplateConstants.ADOC, StandardTemplates.FooterTemplateConstants.ADOC, StandardTemplates.SingleTemplateConstants.ADOC),
            TemplateMode.COMPACT, new TemplateResource(CompactTemplates.HeaderTemplateConstants.ADOC, CompactTemplates.ContentTemplateConstants.ADOC, CompactTemplates.FooterTemplateConstants.ADOC, CompactTemplates.SingleTemplateConstants.ADOC))),
    HTML(".html",
        Map.of(
            TemplateMode.STANDARD, new TemplateResource(StandardTemplates.HeaderTemplateConstants.HTML, StandardTemplates.ContentTemplateConstants.HTML, StandardTemplates.FooterTemplateConstants.HTML, StandardTemplates.SingleTemplateConstants.HTML),
            TemplateMode.COMPACT, new TemplateResource(CompactTemplates.HeaderTemplateConstants.HTML, CompactTemplates.ContentTemplateConstants.HTML, CompactTemplates.FooterTemplateConstants.HTML, CompactTemplates.SingleTemplateConstants.HTML))),
    XML(".xml",
        Map.of(
            TemplateMode.STANDARD, new TemplateResource(StandardTemplates.HeaderTemplateConstants.XML, StandardTemplates.ContentTemplateConstants.XML, StandardTemplates.FooterTemplateConstants.XML, StandardTemplates.SingleTemplateConstants.XML)));

    private final String extension;
    private final Map<TemplateMode, TemplateResource> templateResourcesMap;

    TemplateType(String extension, Map<TemplateMode, TemplateResource> templateResourcesMap) {
        this.extension = extension;
        this.templateResourcesMap = templateResourcesMap;
    }

    public String getSingleTemplate(TemplateMode templateMode) {
        return Optional.ofNullable(templateResourcesMap.get(templateMode))
            .map(TemplateResource::getCombinedTemplate)
            .orElseThrow(() -> new TemplateNotFoundException("Header template for type is not found:[" + templateMode + "]"));
    }

    public String getExtension() {
        return extension;
    }

    public String getHeaderTemplate(TemplateMode templateMode) {
        return Optional.ofNullable(templateResourcesMap.get(templateMode))
            .map(TemplateResource::getHeaderTemplate)
            .orElseThrow(() -> new TemplateNotFoundException("Header template for type is not found:[" + templateMode + "]"));
    }

    public String getContentTemplate(TemplateMode templateMode) {
        return Optional.ofNullable(templateResourcesMap.get(templateMode))
            .map(TemplateResource::getContentTemplate)
            .orElseThrow(() -> new TemplateNotFoundException("Content template for type is not found:[" + templateMode + "]"));
    }

    public String getFooterTemplate(TemplateMode templateMode) {
        return Optional.ofNullable(templateResourcesMap.get(templateMode))
            .map(TemplateResource::getFooterTemplate)
            .orElseThrow(() -> new TemplateNotFoundException("Footer template for type is not found:[" + templateMode + "]"));
    }

    static class CompactTemplates {

        CompactTemplates() {
        }

        static class SingleTemplateConstants {

            static final String ADOC = "templates/single/compact-single-document-template.adoc";
            static final String MARKDOWN = "templates/single/compact-single-document-template.md";
            static final String HTML = "templates/single/compact-single-document-template.html";

            SingleTemplateConstants() {
            }
        }

        static class HeaderTemplateConstants {

            static final String ADOC = "templates/aggregated/adoc/compact-header.adoc";
            static final String MARKDOWN = "templates/aggregated/md/compact-header.md";
            static final String HTML = "templates/aggregated/html/compact-header.html";

            HeaderTemplateConstants() {
            }
        }

        static class ContentTemplateConstants {

            static final String ADOC = "templates/partials/adoc/compact-content.adoc";
            static final String MARKDOWN = "templates/partials/md/compact-content.md";
            static final String HTML = "templates/partials/html/compact-content.html";

            ContentTemplateConstants() {
            }
        }

        static class FooterTemplateConstants {

            static final String ADOC = "templates/aggregated/adoc/compact-footer.adoc";
            static final String MARKDOWN = "templates/aggregated/md/compact-footer.md";
            static final String HTML = "templates/aggregated/html/compact-footer.html";

            FooterTemplateConstants() {
            }
        }
    }

    static class StandardTemplates {

        StandardTemplates() {
        }

        static class SingleTemplateConstants {

            static final String ADOC = "templates/single/single-document-template.adoc";
            static final String MARKDOWN = "templates/single/single-document-template.md";
            static final String HTML = "templates/single/single-document-template.html";
            static final String XML = "templates/single/single-document-template.xml";

            SingleTemplateConstants() {
            }
        }

        static class HeaderTemplateConstants {

            static final String ADOC = "templates/aggregated/adoc/header.adoc";
            static final String MARKDOWN = "templates/aggregated/md/header.md";
            static final String HTML = "templates/aggregated/html/header.html";
            static final String XML = "templates/aggregated/xml/header.xml";

            HeaderTemplateConstants() {
            }
        }

        static class ContentTemplateConstants {

            static final String ADOC = "templates/partials/adoc/content.adoc";
            static final String MARKDOWN = "templates/partials/md/content.md";
            static final String HTML = "templates/partials/html/content.html";
            static final String XML = "templates/partials/xml/content.xml";

            ContentTemplateConstants() {
            }
        }

        static class FooterTemplateConstants {

            static final String ADOC = "templates/aggregated/adoc/footer.adoc";
            static final String MARKDOWN = "templates/aggregated/md/footer.md";
            static final String HTML = "templates/aggregated/html/footer.html";
            static final String XML = "templates/aggregated/xml/footer.xml";

            FooterTemplateConstants() {
            }
        }
    }
}
