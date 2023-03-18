package org.rodnansol.gradle.tasks;

import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.IOException;

public abstract class GenerateTask implements Task {

    @Input
    @Optional
    @Option(option = "imageName", description = "Input file name")
    public abstract Property<String> getInputFileName();


    @TaskAction
    void generateDocument() throws IOException {
        System.out.println("Hello");
    }
}
