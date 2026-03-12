#!/usr/bin/env bash

#######################################
# Logging
#######################################
log() {
  local level="$1"
  shift
  printf "[%s] [%s] %s\n" "$(date '+%Y-%m-%d %H:%M:%S')" "$level" "$*"
}

#######################################
# Load env
#######################################
load_env() {
  local env_file="$1"

  if [[ -f "$env_file" ]]; then
    log INFO "Loading env from $env_file"
    set -a
    # shellcheck disable=SC1090
    source "$env_file"
    set +a
  else
    log WARN "Env file not found: $env_file"
  fi
}

#######################################
# Validate required commands
# Usage:
#   require_cmd java git curl
#######################################
require_cmd() {
  local missing=()

  for cmd in "$@"; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
      missing+=("$cmd")
    fi
  done

  if (( ${#missing[@]} > 0 )); then
    log ERROR "Missing required command(s): ${missing[*]}"
    exit 1
  fi
}

#######################################
# Error trap
#######################################
setup_error_trap() {
  trap 'log ERROR "Failed at line $LINENO"; exit 1' ERR
}