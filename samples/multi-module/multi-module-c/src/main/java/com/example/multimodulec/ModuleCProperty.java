package com.example.multimodulec;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "this.is.module.c")
class ModuleCProperty {

    /**
     * This is my variable from module C.
     */
    private String variableC = "Default value for module C";

    public String getVariableC() {
        return variableC;
    }

    public void setVariableC(String variableC) {
        this.variableC = variableC;
    }
}
