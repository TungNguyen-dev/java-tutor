#!/usr/bin/env bash

set -euo pipefail

#######################################
# Configuration
#######################################

STORAGE_DIR="storage"
CHROMEDRIVER_VERSION="145.0.7632.160"
CHROMEDRIVER_DIR="$STORAGE_DIR/chromedriver-mac-x64"
CHROMEDRIVER_PATH="$CHROMEDRIVER_DIR/chromedriver"
CHROMEDRIVER_ZIP="$STORAGE_DIR/chromedriver-mac-x64.zip"

CHROMEDRIVER_URL="https://storage.googleapis.com/chrome-for-testing-public/${CHROMEDRIVER_VERSION}/mac-x64/chromedriver-mac-x64.zip"

JAR_FILE="target/java-selenium-1.0-SNAPSHOT.jar"

export CRAWLER_POOL_SIZE="${CRAWLER_POOL_SIZE:-6}"

#######################################
# Utility
#######################################

require_command() {
  command -v "$1" >/dev/null 2>&1 || {
    echo "Error: required command '$1' not found." >&2
    exit 1
  }
}

#######################################
# Check dependencies
#######################################

require_command curl
require_command unzip
require_command java

#######################################
# Ensure chromedriver exists
#######################################

if [[ ! -x "$CHROMEDRIVER_PATH" ]]; then
  echo "Chromedriver not found. Downloading..."

  mkdir -p "$STORAGE_DIR"

  curl -fsSL "$CHROMEDRIVER_URL" -o "$CHROMEDRIVER_ZIP"

  unzip -qo "$CHROMEDRIVER_ZIP" -d "$STORAGE_DIR"

  chmod +x "$CHROMEDRIVER_PATH"

  rm -f "$CHROMEDRIVER_ZIP"

  echo "Chromedriver installed at $CHROMEDRIVER_PATH"
fi

#######################################
# Verify application
#######################################

if [[ ! -f "$JAR_FILE" ]]; then
  echo "Error: JAR file not found: $JAR_FILE"
  echo "Run 'mvn package' first."
  exit 1
fi

#######################################
# Run application
#######################################

java -jar "$JAR_FILE" 2>&1 | tee result.log