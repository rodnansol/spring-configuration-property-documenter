package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.rodnansol.core.generator.template.TemplateCompilerMemoryStoreConstants;
import org.rodnansol.core.generator.template.ThreadLocalTemplateCompilerStore;
import org.rodnansol.core.generator.template.customization.ContentCustomization;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

/**
 * Helper that decides whether a property should be rendered or not.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class IsIncludedHelper implements Helper<String> {

    private static final Map<Object, Function<ContentCustomization, Boolean>> FUNCTION_MAP = ofEntries(
        entry("class", ContentCustomization::isIncludeClass),
        entry("key", ContentCustomization::isIncludeKey),
        entry("type", ContentCustomization::isIncludeType),
        entry("description", ContentCustomization::isIncludeDescription),
        entry("defaultValue", ContentCustomization::isIncludeDefaultValue),
        entry("deprecation", ContentCustomization::isIncludeDeprecation),
        entry("envFormat", ContentCustomization::isIncludeEnvFormat)
    );

    private final ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore;

    public IsIncludedHelper(ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore) {
        this.threadLocalTemplateCompilerStore = threadLocalTemplateCompilerStore;
    }

    @Override
    public Object apply(String context, Options options) throws IOException {
        TemplateCustomization templateCustomization = threadLocalTemplateCompilerStore.getItem(TemplateCompilerMemoryStoreConstants.TEMPLATE_CUSTOMIZATION);
        if (templateCustomization != null && FUNCTION_MAP.get(context).apply(templateCustomization.getContentCustomization())) {
            return options.fn(context);
        }
        return options.inverse(context);
    }
}
