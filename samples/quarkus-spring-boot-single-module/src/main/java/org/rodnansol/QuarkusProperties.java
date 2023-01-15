package org.rodnansol;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "this.is.quarkus")
public class QuarkusProperties {

    /**
     * This is a propety in Quarkus.
     */
    private String property;

    /**
     * Deprecated property, we are not able to use the <code>org.springframework.boot.context.properties.DeprecatedConfigurationProperty</code> annotation because it is in the <code>org.springframework.boot:spring-boot</code> artifact
     */
    @Deprecated(since = "Since you are a pilot")
    private String anotherVariable = "with default value";

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getAnotherVariable() {
        return anotherVariable;
    }

    public void setAnotherVariable(String anotherVariable) {
        this.anotherVariable = anotherVariable;
    }
}
