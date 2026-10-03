#!/usr/bin/env bash

set -euo pipefail

targets=$(find . -type d -name "target")

echo "Found target directories:"
echo "$targets"
echo "$targets" | xargs rm -rf
echo "Removed."
