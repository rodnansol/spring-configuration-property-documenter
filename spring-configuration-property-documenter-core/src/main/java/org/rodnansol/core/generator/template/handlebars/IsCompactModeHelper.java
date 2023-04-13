package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.rodnansol.core.generator.template.compiler.TemplateCompilerMemoryStoreConstants;
import org.rodnansol.core.generator.template.TemplateMode;
import org.rodnansol.core.generator.template.compiler.ThreadLocalTemplateCompilerStore;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.io.IOException;

/**
 * Helper that decides if the compact mode is enabled or not.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
class IsCompactModeHelper implements Helper<Object> {

    private final ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore;

    public IsCompactModeHelper(ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore) {
        this.threadLocalTemplateCompilerStore = threadLocalTemplateCompilerStore;
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        TemplateCustomization templateCustomization = threadLocalTemplateCompilerStore.getItem(TemplateCompilerMemoryStoreConstants.TEMPLATE_CUSTOMIZATION);
        if (templateCustomization != null && templateCustomization.getTemplateMode() == TemplateMode.COMPACT) {
            return options.fn(context);
        }
        return options.inverse(context);
    }
}
