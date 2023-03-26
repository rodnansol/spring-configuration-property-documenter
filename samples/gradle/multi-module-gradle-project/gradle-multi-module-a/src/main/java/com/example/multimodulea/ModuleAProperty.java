package com.example.multimodulea;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "this.is.module.a")
class ModuleAProperty {

    /**
     * This is my variable from module A.
     */
    private String variableA = "Default value for module A";

    public String getVariableA() {
        return variableA;
    }

    public void setVariableA(String variableA) {
        this.variableA = variableA;
    }
}
