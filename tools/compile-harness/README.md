# Compile harness

A standalone, Android-SDK-free Gradle build that compiles and tests the parts of c:geo that have
no Android dependency. Today that is the Wherigo/OpenWIG Lua engine:
`cgeo.geocaching.wherigo.openwig` and `cgeo.geocaching.wherigo.kahlua`.

## Why this exists

The full app (`./gradlew assembleBasicDebug` from the repo root) needs the Android SDK and
Google's Maven repository (`dl.google.com`) for the Android Gradle Plugin, AndroidX, etc. In
sandboxed/offline environments without an Android SDK install or with `dl.google.com` blocked,
that build cannot run at all - not even to catch a typo.

This harness sidesteps that: it points a plain `java` Gradle project (JDK 17, Maven Central only)
directly at the existing, unmoved source files for the one meaningful subtree of the app that is
almost entirely pure Java. It does not copy or fork any code - `build.gradle` just adds
`../../main/src/main/java` (filtered to the two packages above) as a source directory. Editing a
file under `main/src/main/java/cgeo/geocaching/wherigo/openwig` or `.../kahlua` and re-running the
harness compiles/tests the real, current file.

## Usage

```sh
gradle -p tools/compile-harness compileJava   # compile-only sanity check
gradle -p tools/compile-harness test          # + run the pure-JUnit tests for this code
```

Use the system `gradle` (or any local Gradle 8.x install), not the repo's root `./gradlew` -
the root wrapper is pinned to a distribution intended for the Android build and this project is
intentionally excluded from the root `settings.gradle` so that running it never triggers
configuration of the Android application modules.

## Caveats

- Two files (`openwig/EventTable.java`, `openwig/Media.java`) use `androidx.annotation.NonNull`.
  Since `androidx.*` artifacts are only published to Google's Maven repo, `stubs/` ships a
  do-nothing local re-declaration of that one annotation so these files can compile here. It is
  never part of the real app - the real app keeps resolving the genuine dependency.
- This only proves the covered files compile and their existing pure-JUnit tests pass. It is not
  a substitute for `./gradlew assembleBasicDebug` / `testBasicDebug` / `checkstyle` wherever those
  are actually runnable - use it as a fast, partial check when they are not.
- If `main/build.gradle` changes the version of `org.apache.commons:commons-collections4`, `junit`,
  or `org.assertj:assertj-core`, update the matching version here too.
- If new files are added under the two covered packages that introduce a new external
  (non-JDK, non-`cgeo.*`) import, this harness's `dependencies {}` block needs the matching
  library added, or it will fail to compile.
