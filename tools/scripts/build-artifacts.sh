#!/usr/bin/env bash

set -euo pipefail

# ==============================================================================
# Configuration & Environment
# ==============================================================================

# Xác định thư mục chứa file script để load file .env ở cùng vị trí
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"

load_env() {
    if [[ -f "$ENV_FILE" ]]; then
        echo "==> Loading environment variables from .env..."
        # Export các biến trong .env (bỏ qua dòng trống và comment)
        set -o allexport
        # shellcheck disable=SC1090
        source <(grep -v '^#' "$ENV_FILE" | grep -v '^\s*$')
        set +o allexport
    fi
}

# Gọi hàm load .env trước
load_env

LOCAL_REPOSITORY="${LOCAL_REPOSITORY:-$HOME/.m2/repository}"

# ==============================================================================
# Functions
# ==============================================================================

clean_local_repository() {
    echo "==> Cleaning local Maven repository..."

    rm -rf "$LOCAL_REPOSITORY/tungnn/java/tutor/core"
    rm -rf "$LOCAL_REPOSITORY/tungnn/java/tutor/infrastructure"
    rm -rf "$LOCAL_REPOSITORY/tungnn/java/tutor/tool"
    rm -rf "$LOCAL_REPOSITORY/tungnn/java/tutor/benchmark"
}

package_project() {
    echo "==> Packaging Maven project..."

    mvn clean install
}

# ==============================================================================
# Main
# ==============================================================================

clean_local_repository
package_project

echo "==> Build completed successfully."