#!/bin/bash

set -euo pipefail

mvn clean package -f src/java-tool/pom.xml

cp "src/java-tool/target/java-tool-1.0-SNAPSHOT.jar" "tools/bin/java-tool-1.0-SNAPSHOT.jar"