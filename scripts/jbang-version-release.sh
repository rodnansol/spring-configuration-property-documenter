#!/usr/bin/env bash
RELEASE_VERSION=$1
echo "Creating JBang script with ${RELEASE_VERSION} version"
mkdir -p jbang/${RELEASE_VERSION}
cp jbang/src/PropertyDocumenter.java jbang/${RELEASE_VERSION}/
sed -i 's/999-SNAPSHOT/'"${RELEASE_VERSION}"'/' jbang/${RELEASE_VERSION}/PropertyDocumenter.java
