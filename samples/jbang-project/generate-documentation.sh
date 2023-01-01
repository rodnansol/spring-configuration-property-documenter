#!/usr/bin/env bash
JBANG_EXECUTABLE=~/.jbang/bin/jbang

JAR_LOCATION=$(${JBANG_EXECUTABLE} info tools src/SmallSpringApp.java | jq -r .applicationJar)
echo "Jar file location:${JAR_LOCATION}"

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java generate \
                           -n "Simple JBang Application" -i ${JAR_LOCATION} \
                           -tt HTML \
                           -o spring-app-simple-property-docs.html

${JBANG_EXECUTABLE} ../../jbang/src/PropertyDocumenter.java aggregate \
                           -n "Simple JBang Application" \
                           -mn "Spring Application example 1" -i ${JAR_LOCATION} \
                           -mn "Spring Application example 2" -i ${JAR_LOCATION} \
                           -tt HTML \
                           -o spring-app-aggregated-property-docs.html
