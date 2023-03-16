package org.rodnansol.samples.gradle;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.test:true}")
    private boolean myBoolean;
}
