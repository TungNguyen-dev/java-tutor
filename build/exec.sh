#!/bin/bash

# Java Runner Script - Compile and run Java programs using FQN
# Usage: ./run-java.sh <fully.qualified.ClassName> [additional-args...]
# Example: ./run-java.sh com.tungnn.tutor.java.core.foundations.types.JavaTypes

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if FQN is provided
if [ $# -eq 0 ]; then
    print_error "No Fully Qualified Name provided!"
    echo "Usage: $0 <fully.qualified.ClassName> [additional-args...]"
    echo ""
    echo "Available Java classes:"
    find src -name "*.java" -type f | while read file; do
        # Extract package and class name from Java file
        package=$(grep -E "^package " "$file" 2>/dev/null | sed 's/package //;s/;//' || echo "")
        class=$(basename "$file" .java)
        if [ -n "$package" ]; then
            echo "  $package.$class"
        else
            echo "  $class"
        fi
    done
    exit 1
fi

FQN="$1"
shift  # Remove the FQN from arguments, leaving any additional args

# Extract class name and package from FQN
CLASS_NAME=$(echo "$FQN" | awk -F. '{print $NF}')
if [[ "$FQN" == "$CLASS_NAME" ]]; then
    # No package, class is in default package
    PACKAGE_PATH=""
else
    # Has package
    PACKAGE_PATH=$(echo "$FQN" | sed "s/\.$CLASS_NAME$//" | tr '.' '/')
fi

# Define paths
SRC_DIR="src/main/java"
BUILD_DIR="target/classes"
if [ -n "$PACKAGE_PATH" ]; then
    JAVA_FILE="$SRC_DIR/$PACKAGE_PATH/$CLASS_NAME.java"
else
    JAVA_FILE="$SRC_DIR/$CLASS_NAME.java"
fi

# Check if Java file exists
if [ ! -f "$JAVA_FILE" ]; then
    print_error "Java file not found: $JAVA_FILE"
    print_info "Looking for similar files..."
    find src -name "*$CLASS_NAME*.java" -type f | head -5
    exit 1
fi

print_info "Found Java file: $JAVA_FILE"

# Create build directory if it doesn't exist
mkdir -p "$BUILD_DIR"

# Compile the Java file
print_info "Compiling $FQN..."
if javac -d "$BUILD_DIR" -cp "$SRC_DIR" "$JAVA_FILE"; then
    print_success "Compilation successful!"
else
    print_error "Compilation failed!"
    exit 1
fi

# Check if compiled class exists
if [ -n "$PACKAGE_PATH" ]; then
    CLASS_FILE="$BUILD_DIR/$PACKAGE_PATH/$CLASS_NAME.class"
else
    CLASS_FILE="$BUILD_DIR/$CLASS_NAME.class"
fi
if [ ! -f "$CLASS_FILE" ]; then
    print_error "Compiled class file not found: $CLASS_FILE"
    exit 1
fi

# Run the Java program
print_info "Running $FQN..."
echo "----------------------------------------"
if java -cp "$BUILD_DIR" "$FQN" "$@"; then
    echo "----------------------------------------"
    print_success "Program executed successfully!"
else
    echo "----------------------------------------"
    print_warning "Program finished with non-zero exit code"
fi
