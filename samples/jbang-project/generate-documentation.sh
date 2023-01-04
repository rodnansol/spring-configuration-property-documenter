#!/usr/bin/env bash
JBANG_EXECUTABLE=~/.jbang/bin/jbang

JAR_LOCATION=$(${JBANG_EXECUTABLE} info tools src/SmallSpringApp.java | jq -r .applicationJar)
echo "Jar file location:${JAR_LOCATION}"

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java generate \
                           -n "Simple JBang Application" -i ${JAR_LOCATION} \
                           -tt HTML \
                           -o target/spring-app-simple-property-docs.html

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java generate \
                           -n "Simple JBang Application" -i ${JAR_LOCATION} \
                           -tt ADOC \
                           -o target/spring-app-simple-property-docs.adoc

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java generate \
                           -n "Simple JBang Application" -i ${JAR_LOCATION} \
                           -tt MARKDOWN \
                           -o target/spring-app-simple-property-docs.md


${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java aggregate \
                           -n "Simple JBang Application" \
                           -mn "Spring Application example 1" -i ${JAR_LOCATION} \
                           -mn "Spring Application example 2" -i ${JAR_LOCATION} \
                           -tt ADOC \
                           -o target/spring-app-aggregated-property-docs.adoc


${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java aggregate \
                           -n "Simple JBang Application" \
                           -mn "Spring Application example 1" -i ${JAR_LOCATION} \
                           -mn "Spring Application example 2" -i ${JAR_LOCATION} \
                           -tt MARKDOWN \
                           -o target/spring-app-aggregated-property-docs.md

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java aggregate \
                           -n "Simple JBang Application" \
                           -mn "Spring Application example 1" -i ${JAR_LOCATION} \
                           -mn "Spring Application example 2" -i ${JAR_LOCATION} \
                           -tt HTML \
                           -o target/spring-app-aggregated-property-docs.html
