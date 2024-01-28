package org.rodnansol.samples.gradle;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Big first level nested property.
 */
public class FirstLevelNestedProperty {

    /**
     * Name of the custom property.
     */
    private String name = "ABC";

    /**
     * Description of this thing.
     */
    private String desc = "123";

    /**
     * Nested custom properties.
     */
    @NestedConfigurationProperty
    private SecondLevelNestedClass secondLevelNestedClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SecondLevelNestedClass getSecondLevelNestedClass() {
        return secondLevelNestedClass;
    }

    public void setSecondLevelNestedClass(SecondLevelNestedClass secondLevelNestedClass) {
        this.secondLevelNestedClass = secondLevelNestedClass;
    }

    static class SecondLevelNestedClass {

        /**
         * Custom nested
         */
        private String secondLevelValue;

        public String getSecondLevelValue() {
            return secondLevelValue;
        }

        public void setSecondLevelValue(String secondLevelValue) {
            this.secondLevelValue = secondLevelValue;
        }
    }
}
