# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Repository overview
- This repo is a set of Java tutorials/examples grouped by topic in subdirectories:
  - java-core, java-collection, java-IO: skeletal folders with brief READMEs. Some files are placeholders; not all code is present.
- There is no build tool configured (no Maven/Gradle). Compilation and execution are done directly with javac/java.

Prerequisites
- JDK 9+ (JPMS is used: javac/java with --module/--module-path). Verify with: java -version
- Shell: zsh on macOS (commands below are POSIX-compatible).

Common commands
- Clean compiled artifacts
  - rm -rf target/modules target/classes 2>/dev/null || true

- Lint during compilation (adds javac warnings)
  - Add -Xlint to the compile commands, for example:
    - javac -Xlint:all --module-source-path src/main/java -d target/modules --module m.A

High-level architecture
- Other topic folders (java-core, java-collection, java-IO) currently contain minimal READMEs. The java-core README references running a class from target/classes, implying a conventional non-modular layout for that topic, but corresponding sources are not present in this snapshot.

Important notes from existing READMEs
- java-core/READEME.md shows an example of running a class with java -cp target/classes ...; ensure compiled classes exist if you use that pattern.
- Filenames are READEME.md (note the spelling) in each topic directory.

What’s not configured
- No test framework is present (e.g., JUnit) and no build automation (Maven/Gradle). Use the raw javac/java commands above for development in this repo.

## Updating Warp

Warp automatically checks for updates on startup. A notification will appear in the top right corner of the Warp window when a new update is available.

To check for updates, search for "update" in the Command Palette or go to `Settings > Accounts` and click "Check for Update".

If nothing happens, it means you already have the latest stable build.

**Auto-Update Issues**

Warp cannot auto-update if it does not have the correct permissions to replace the running version of Warp. If this is the case, a banner will prompt you to manually update Warp.

There are two main causes of this:

1. You opened Warp directly from the mounted volume instead of dragging it into your Applications directory. Quit Warp, drag the application into /Applications, and restart Warp.
2. You are a non-Admin user. Opening the app with an admin user should fix the auto-update issues.

