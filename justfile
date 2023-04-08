#!/usr/bin/env just --justfile

set export

home_dir  := env_var('HOME')
JAVA_HOME := home_dir + "/.sdkman/candidates/java/11.0.17-tem"
MAVEN_HOME := home_dir + "/.sdkman/candidates/maven/3.9.0"

# maven build without tests
build:
   mvn -DskipTests clean install -Pbuild

# maven build without tests
verify:
   mvn clean verify

mvn-version:
  mvn --version

# Build samples
build-samples: build
  cd samples && mvn clean install

# Debug samples
debug-samples: build
  cd samples && mvnDebug clean install -X

# Debug Multi Module Docs sample
debug-multi-module-docs: build
  cd samples/multi-module/multi-module-docs && mvnDebug clean install -X

# Build gradle plugin
build-gradle-plugin:
  rm -rf ~/.m2/repository/org/rodnansol/spring-configuration-property-documenter-gradle-plugin
  rm -rf ~/.gradle/caches
  mvn clean install -Pbuild -pl 'spring-configuration-property-documenter-gradle-plugin'

# Dry full-release
dry-release:
  mvn clean -Prelease deploy -DaltDeploymentRepository=local::file:./target/staging-deploy
  mvn jreleaser:full-release -Prelease -Djreleaser.dry.run

# Snapshot release
#snapshot-release version:
#  mvn versions:set -DnewVersion={{version}}
#  mvn clean -Prelease deploy -DaltDeploymentRepository=local::file:./target/staging-deploy
#  mvn jreleaser:full-release -Prelease -N
#  mvn versions:set -DnewVersion=999-SNAPSHOT

# Draft release
draft-release version: (create-jbang-release version)
  mvn versions:set -DnewVersion={{version}}
  mvn jreleaser:release -Prelease -Djreleaser-github-release.draft=true -Djreleaser-nexus-deploy.active=NEVER -N
  mvn versions:set -DnewVersion=999-SNAPSHOT

# Full-release
full-release version:
  mvn versions:set -DnewVersion={{version}}
  mvn clean -Prelease deploy -DaltDeploymentRepository=local::file:./target/staging-deploy
  mvn jreleaser:full-release -Prelease N
  mvn versions:set -DnewVersion=999-SNAPSHOT

announce-release version:
  mvn versions:set -DnewVersion={{version}}
  #mvn clean -Prelease deploy -DaltDeploymentRepository=local::file:./target/staging-deploy
  mvn jreleaser:announce -Prelease -X -N
  mvn versions:set -DnewVersion=999-SNAPSHOT

# JReleaser config
dry-release-config:
  mvn -Prelease jreleaser:config -Djreleaser.dry.run

# Creates a JBang "release"
create-jbang-release version:
  ./scripts/jbang-version-release.sh {{version}}

# Snapshot release
snapshot-release:
  mvn versions:set -DnewVersion=999-SNAPSHOT
  mvn clean -Prelease,build deploy -DaltDeploymentRepository=local::file:./target/staging-deploy  -pl '!spring-configuration-property-documenter-report'
  mvn jreleaser:deploy -Prelease -N -Djreleaser-nexus-deploy.active=SNAPSHOT

only-snapshot-release:
  mvn jreleaser:deploy -Prelease -N -Djreleaser-nexus-deploy.active=SNAPSHOT -X

generate-antora:
  antora local-playbook.yml
