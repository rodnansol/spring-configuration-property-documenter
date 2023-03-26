package com.example.multimoduleb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "this.is.module.b")
class ModuleBProperty {

    /**
     * This is my variable from module B.
     */
    private String variableB = "Default value for module B";

    public String getVariableB() {
        return variableB;
    }

    public void setVariableB(String variableB) {
        this.variableB = variableB;
    }
}
