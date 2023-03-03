package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.rodnansol.core.generator.template.MainTemplateData;

import java.io.IOException;

/**
 * Helper class to determine whether to include the enviroment variable format or not.
 *
 * @author nandorholozsnyak
 * @since 0.4.0
 */
public class IncludeEnvironmentVariableFormatHelper implements Helper<MainTemplateData> {

    @Override
    public Object apply(MainTemplateData context, Options options) throws IOException {
        if (context.getTemplateCustomization().isIncludeEnvFormat()) {
            return options.fn(context);
        }

        return options.inverse(context);
    }
}
