#!/usr/bin/env bash

set -euo pipefail

# ==============================================================================
# Configuration
# ==============================================================================

LOCAL_REPOSITORY="$HOME/.m2/repository"

# ==============================================================================
# Functions
# ==============================================================================

clean_local_repository() {
    echo "==> Cleaning local Maven repository..."

    rm -rf "$LOCAL_REPOSITORY/tungnn"
}

package_project() {
    echo "==> Packaging Maven project..."

    mvn clean package
}

# ==============================================================================
# Main
# ==============================================================================

clean_local_repository
package_project

echo "==> Build completed successfully."