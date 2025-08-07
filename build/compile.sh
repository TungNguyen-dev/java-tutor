#!/bin/bash

# compile.sh - Compile all Java files in the src directory
# Usage: ./compile.sh

set -e # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SRC_DIR="src"
BUILD_DIR="target/classes"
CLASSPATH="."

echo -e "${BLUE}Java Compilation Script${NC}"
echo "========================="

# Check if src directory exists
if [ ! -d "$SRC_DIR" ]; then
  echo -e "${RED}Error: $SRC_DIR directory not found!${NC}"
  exit 1
fi

# Create build directory if it doesn't exist
if [ ! -d "$BUILD_DIR" ]; then
  echo -e "${YELLOW}Creating build directory...${NC}"
  mkdir -p "$BUILD_DIR"
fi

# Find all Java files
echo -e "${BLUE}Finding Java files...${NC}"
JAVA_FILES=$(find "$SRC_DIR" -name "*.java" -type f)

if [ -z "$JAVA_FILES" ]; then
  echo -e "${YELLOW}No Java files found in $SRC_DIR directory.${NC}"
  exit 0
fi

# Count Java files
FILE_COUNT=$(echo "$JAVA_FILES" | wc -l | tr -d ' ')
echo -e "${GREEN}Found $FILE_COUNT Java file(s) to compile:${NC}"
echo "$JAVA_FILES" | sed 's/^/  /'

echo ""
echo -e "${BLUE}Starting compilation...${NC}"

# Compile all Java files
# Using -d to specify output directory and -cp for classpath
if javac -d "$BUILD_DIR" -cp "$CLASSPATH" $JAVA_FILES; then
  echo -e "${GREEN}✅ Compilation successful!${NC}"
  echo -e "${GREEN}Compiled classes are in the $BUILD_DIR directory.${NC}"

  # Show compiled classes
  echo ""
  echo -e "${BLUE}Compiled classes:${NC}"
  find "$BUILD_DIR" -name "*.class" -type f | sed 's/^/  /'

  # Count compiled classes
  CLASS_COUNT=$(find "$BUILD_DIR" -name "*.class" -type f | wc -l | tr -d ' ')
  echo -e "${GREEN}Total: $CLASS_COUNT class file(s) generated.${NC}"

else
  echo -e "${RED}❌ Compilation failed!${NC}"
  exit 1
fi

echo ""
echo -e "${BLUE}Compilation complete!${NC}"
