#!/usr/bin/env zsh

#######################################
# Strict Mode
#######################################
set -euo pipefail

#######################################
# Load common
#######################################
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
source "$SCRIPT_DIR/lib/common.sh"

#######################################
# Configuration
#######################################
LOG_FILE="${LOG_FILE:-storage/app.log}"
JAR_FILE="${JAR_FILE:-tools/bin/crawler.jar}"

#######################################
# Main Logic
#######################################
main() {
  require_command java

  log_info "Starting app..."
  export CRAWLER_POOL_SIZE="${CRAWLER_POOL_SIZE:-3}"
  caffeinate -i java -jar "$JAR_FILE" 2>&1 | tee -a "$LOG_FILE"
}

main "$@"