package org.rodnansol.maven;

import com.soebes.itf.extension.assertj.MavenProjectResultAssert;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

import java.io.File;
import java.util.Map;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@MavenJupiterExtension
class GeneratePropertyDocumentMojoIT {

    private static final Map<String, File> expectedFileMap = ofEntries(
        entry("property-docs/single-module-example-property-docs.adoc", new File("src/test/resources/single-module-test/property-docs/single-module-example-property-docs.adoc")),
        entry("property-docs/single-module-example-property-docs.html", new File("src/test/resources/single-module-test/property-docs/single-module-example-property-docs.html")),
        entry("property-docs/single-module-example-property-docs.md", new File("src/test/resources/single-module-test/property-docs/single-module-example-property-docs.md")),
        entry("compact-mode.adoc", new File("src/test/resources/single-module-test/compact-mode.adoc")),
        entry("compact-mode.html", new File("src/test/resources/single-module-test/compact-mode.html")),
        entry("compact-mode.md", new File("src/test/resources/single-module-test/compact-mode.md")),
        entry("exclude-group-sample.adoc", new File("src/test/resources/single-module-test/exclude-group-sample.adoc")),
        entry("exclude-property-sample.adoc", new File("src/test/resources/single-module-test/exclude-property-sample.adoc")),
        entry("include-group-sample.adoc", new File("src/test/resources/single-module-test/include-group-sample.adoc")),
        entry("include-property-sample.adoc", new File("src/test/resources/single-module-test/include-property-sample.adoc")),
        entry("without-type-and-deprecation.adoc", new File("src/test/resources/single-module-test/without-type-and-deprecation.adoc")),
        entry("without-type-and-deprecation.html", new File("src/test/resources/single-module-test/without-type-and-deprecation.html")),
        entry("without-type-and-deprecation.md", new File("src/test/resources/single-module-test/without-type-and-deprecation.md"))
    );

    @MavenTest
    void single_module_test(MavenExecutionResult result) {
        MavenProjectResultAssert mavenProjectResultAssert = assertThat(result).project()
            .hasTarget();
        expectedFileMap.forEach((key, value) -> mavenProjectResultAssert
            .withFile(key)
            .hasSameTextualContentAs(value.toPath()));
    }
}