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
# Working Directory
#######################################
WORKING_DIR="$(pwd)"
ENV_FILE="$WORKING_DIR/.env"

if [[ -f "$ENV_FILE" ]]; then
  log_info "Loading environment from $ENV_FILE"
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
else
  log_info "No .env file found in $WORKING_DIR"
fi

#######################################
# Configuration
#######################################
LOG_FILE="${LOG_FILE:-storage/app.log}"
JAR_FILE="${JAR_FILE:-tools/bin/crawler-tool-1.0.0.jar}"

#######################################
# Main Logic
#######################################
main() {
  require_command java

  log_info "Working directory: $WORKING_DIR"
  log_info "Starting app..."

  export CRAWLER_POOL_SIZE="${CRAWLER_POOL_SIZE:-3}"

  caffeinate -i java -jar "$JAR_FILE" crawl 2>&1 | tee -a "$LOG_FILE"
}

main "$@"