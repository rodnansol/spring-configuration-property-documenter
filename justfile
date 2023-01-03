#!/usr/bin/env just --justfile

# maven build without tests
build:
   mvn -DskipTests clean install

# Build samples
build-samples: build
  cd samples && mvn clean install

# Build samples
debug-samples: build
  cd samples && mvnDebug clean install -X

# Dry full-release
dry-release:
  mvn clean -Prelease deploy -DaltDeploymentRepository=local::file:./target/staging-deploy
  mvn jreleaser:full-release -Prelease -Djreleaser.dry.run

# JReleaser config
dry-release-config:
  mvn -Prelease jreleaser:config -Djreleaser.dry.run
