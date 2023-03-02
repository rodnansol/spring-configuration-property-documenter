package org.rodnansol.core.generator.template.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.springframework.boot.context.properties.source.SystemEnvironmentPropertyMapperDelegator;

/**
 * Handlebars helpers transforming property to ENV_VARIABLE
 *
 * @author allsimon
 * @since 0.3.1
 */
public class EnvironmentVariableHelper implements Helper<String> {

    @Override
    public Object apply(String context, Options options) {
        return SystemEnvironmentPropertyMapperDelegator.convertToEnvVariable(context);
    }
}
