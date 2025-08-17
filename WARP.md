# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Repository overview
- This repo is a set of Java tutorials/examples grouped by topic in subdirectories:
  - java-module: JPMS (Java Platform Module System) example with multiple modules (m.A, m.B, m.C, m.D).
  - java-core, java-collection, java-IO: skeletal folders with brief READMEs. Some files are placeholders; not all code is present.
- There is no build tool configured (no Maven/Gradle). Compilation and execution are done directly with javac/java.

Prerequisites
- JDK 9+ (JPMS is used: javac/java with --module/--module-path). Verify with: java -version
- Shell: zsh on macOS (commands below are POSIX-compatible).

Common commands
- Clean compiled artifacts
  - rm -rf target/modules target/classes 2>/dev/null || true

- Compile JPMS modules (run from java-module/)
  - mkdir -p target/modules
  - javac --module-source-path src/main/java -d target/modules --module m.A
  - javac --module-source-path src/main/java -d target/modules --module m.B
  - javac --module-source-path src/main/java -d target/modules --module m.C
  - javac --module-source-path src/main/java -d target/modules --module m.D
  - One-liner loop (equivalent): for M in m.A m.B m.C m.D; do javac --module-source-path src/main/java -d target/modules --module "$M"; done

- Run the JPMS sample (run from java-module/)
  - java --module-path target/modules --module m.A/client.Test
  - Note: The main class path (client.Test) is based on the existing README; ensure that class exists after compilation.

- Lint during compilation (adds javac warnings)
  - Add -Xlint to the compile commands, for example:
    - javac -Xlint:all --module-source-path src/main/java -d target/modules --module m.A

- Run a single-module compile (helpful during focused edits)
  - Example: recompile only m.D
    - javac --module-source-path src/main/java -d target/modules --module m.D

High-level architecture
- java-module demonstrates JPMS with explicit module descriptors:
  - m.A: requires m.D.
  - m.B: requires transitive m.C.
  - m.C: requires transitive m.D.
  - m.D: exports package p.
- Build outputs are written under java-module/target/modules; these are then placed on the module-path when running.
- Other topic folders (java-core, java-collection, java-IO) currently contain minimal READMEs. The java-core README references running a class from target/classes, implying a conventional non-modular layout for that topic, but corresponding sources are not present in this snapshot.

Important notes from existing READMEs
- java-module/READEME.md documents the exact javac/java commands used above.
- java-core/READEME.md shows an example of running a class with java -cp target/classes ...; ensure compiled classes exist if you use that pattern.
- Filenames are READEME.md (note the spelling) in each topic directory.

What’s not configured
- No test framework is present (e.g., JUnit) and no build automation (Maven/Gradle). Use the raw javac/java commands above for development in this repo.

