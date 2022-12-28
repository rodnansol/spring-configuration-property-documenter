package org.rodnansol;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "this.is.my.nested")
class TopLevelClassNestedProperty {

    /**
     * Nested value.
     */
    private String nestedValue;

    public String getNestedValue() {
        return nestedValue;
    }

    public void setNestedValue(String nestedValue) {
        this.nestedValue = nestedValue;
    }
}
