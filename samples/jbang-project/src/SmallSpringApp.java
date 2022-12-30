///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.boot:spring-boot-starter-parent:3.0.0@pom
//DEPS org.springframework.boot:spring-boot-starter-web
//DEPS org.springframework.boot:spring-boot-configuration-processor
//JAVAC_OPTIONS -processor org.springframework.boot.configurationprocessor.ConfigurationMetadataAnnotationProcessor
//JAVA 17

package org.rodnansol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SmallSpringApp {

    public static void main(String[] args) {
        SpringApplication.run(SmallSpringApp.class, args);
    }

}

@Component
@ConfigurationProperties(prefix = "jbang.app.properties")
class JbangAppProperties {

    /**
     * Variable in a property class.
     */
    private String variable = "Hello from JBang!";

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
