#!/usr/bin/env zsh

#######################################
# Global Defaults
#######################################
: "${LOG_FILE:=app.log}"

#######################################
# Logging
#######################################
log_info() {
  echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') - $*" | tee -a "$LOG_FILE"
}

log_warn() {
  echo "[WARN] $(date '+%Y-%m-%d %H:%M:%S') - $*" | tee -a "$LOG_FILE"
}

log_error() {
  echo "[ERROR] $(date '+%Y-%m-%d %H:%M:%S') - $*" | tee -a "$LOG_FILE" >&2
}

#######################################
# Exit Handling
#######################################
die() {
  log_error "$*"
  exit 1
}

#######################################
# Command Utilities
#######################################
require_command() {
  local cmd="$1"
  command -v "$cmd" >/dev/null 2>&1 || die "Required command not found: $cmd"
}

#######################################
# File & Directory Validation
#######################################
require_file() {
  local file="$1"
  [[ -f "$file" ]] || die "File not found: $file"
}

require_dir() {
  local dir="$1"
  [[ -d "$dir" ]] || die "Directory not found: $dir"
}

ensure_dir() {
  local dir="$1"
  if [[ ! -d "$dir" ]]; then
    log_info "Creating directory: $dir"
    mkdir -p "$dir"
  fi
}

#######################################
# OS Detection
#######################################
is_macos() {
  [[ "$OSTYPE" == "darwin"* ]]
}

is_linux() {
  [[ "$OSTYPE" == "linux-gnu"* ]]
}

#######################################
# Temp Utilities
#######################################
make_temp_dir() {
  mktemp -d 2>/dev/null || mktemp -d -t 'tmp'
}

#######################################
# String Utilities
#######################################
is_empty() {
  [[ -z "${1:-}" ]]
}

#######################################
# Debug
#######################################
enable_debug() {
  set -x
}

#######################################
# Trap (optional usage in main script)
#######################################
setup_trap() {
  trap 'log_error "Script failed at line $LINENO"; exit 1' ERR
}