package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.rodnansol.core.generator.template.TemplateCompilerMemoryStoreConstants;
import org.rodnansol.core.generator.template.ThreadLocalTemplateCompilerStore;
import org.rodnansol.core.generator.template.customization.TemplateCustomization;

import java.io.IOException;

/**
 * Helper that decides if the compact mode is enabled or not.
 *
 * @author nandorholozsnyak
 * @since 0.6.0
 */
public class IsCompactModeHelper implements Helper<Object> {

    private final ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore;

    public IsCompactModeHelper(ThreadLocalTemplateCompilerStore threadLocalTemplateCompilerStore) {
        this.threadLocalTemplateCompilerStore = threadLocalTemplateCompilerStore;
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        TemplateCustomization templateCustomization = threadLocalTemplateCompilerStore.getItem(TemplateCompilerMemoryStoreConstants.TEMPLATE_CUSTOMIZATION);
        if (templateCustomization != null && templateCustomization.isCompactMode()) {
            return options.fn(context);
        }
        return options.inverse(context);
    }
}
