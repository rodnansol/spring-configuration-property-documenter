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
