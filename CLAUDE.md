# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## About c:geo

c:geo is an open-source Android geocaching app (unofficial client for geocaching.com, with support
for several other geocaching platforms). Written in Java, built with Gradle.

## Sources of truth

When instructions conflict, apply this priority (highest first):

1. `.github/copilot-instructions.md` — agent behavior rules (very detailed; read it)
2. `checkstyle.xml` and `.editorconfig` — authoritative code style/formatting rules
3. `README.md` / `CONTRIBUTING.md` — project and contributor context
4. `.github/workflows/` — what the real CI actually runs

Only rely on files tracked in git (not excluded by `.gitignore`).

## Build & test commands

Use `./gradlew` from the repo root. Prefer `--offline` only when running as an agent with a
pre-populated cache (e.g. CI); omit it for normal local development.

| Purpose | Command |
|---|---|
| Compile & build (debug APK, basic flavor) | `./gradlew assembleBasicDebug` |
| Pure unit tests | `./gradlew testBasicDebugUnitTest` (or `testBasicDebug`) |
| Checkstyle | `./gradlew checkstyle` |
| All unit tests (pure + instrumentation) | `./gradlew testDebug` |
| Instrumented tests (needs emulator/device) | `./gradlew connectedBasicDebugAndroidTest` |
| Install on connected device/emulator | `./gradlew installBasicDebug` |
| Run on connected device/emulator | `./gradlew runBasicDebug` |
| Check dependency updates | `./gradlew dependencyUpdates --no-parallel` |
| Help / list of common commands | `./gradlew` (default task `cgeoHelp`) |

**Mandatory quality gates**: after every code change, run and fix failures for all three:
`assembleBasicDebug`, `testBasicDebug` (or `testBasicDebugUnitTest`), `checkstyle`.
Do **not** run instrumented tests or PMD checks automatically — they require an emulator and/or a
configured (premium) geocaching.com account and take a long time; only run them when explicitly asked.

Running a single test class: use the IDE's "Run '\<class name\>'" (Android JUnit Test), or from the
command line target it with Gradle's test filter, e.g.
`./gradlew testBasicDebugUnitTest --tests "cgeo.geocaching.SomeClassTest"`.

### Product flavors and build types

- Flavor dimension `javaCompilerTime` has flavors `basic` (default/normal), `nojit`, `foss` (no
  proprietary/Google-dependent code — sources under `src/nofoss/java` are excluded for this flavor).
  Prefer the `basic` flavor for everyday builds/tests (`assembleBasicDebug`, `testBasicDebug`).
- Build types: `debug`, `nightly` (needs env var `NB`), `rc` (needs env var `RC`), `release`, `legacy`.

### API keys

Full functionality (Google Maps, OpenCaching sites, etc.) requires API keys, but the app builds and
most unit tests run without them:
1. Copy `./templates/private.properties` to `./private.properties` and fill in keys.
2. `main/src/main/res/values/keys.xml` is generated from it during the Gradle build (only if it
   doesn't already exist — delete it to regenerate after changing keys).

### Modules

`settings.gradle` includes three Gradle modules: `main` (the app — almost all code lives here),
`mapswithme-api` and `organicmaps-api` (thin API clients for launching external map apps).

### Compiling without the Android SDK

`./gradlew assembleBasicDebug`/`testBasicDebug`/`checkstyle` require the Android SDK and network
access to Google's Maven repo (`dl.google.com`). In an environment where either is unavailable,
those commands cannot run at all — check this first (e.g. `echo $ANDROID_HOME`, try reaching
`dl.google.com`) before assuming the app is broken.

For that situation, `tools/compile-harness/` is a standalone, Android-free Gradle project (plain
`java` plugin, Maven Central only) that compiles and unit-tests the one large part of the codebase
that has (almost) no Android dependency: `cgeo.geocaching.wherigo.openwig` and
`cgeo.geocaching.wherigo.kahlua` (the Wherigo/OpenWIG Lua engine). It reads the real source files
in place — no copying — so edits there are validated immediately:

```sh
gradle -p tools/compile-harness compileJava   # fast compile-only check
gradle -p tools/compile-harness test          # + run that code's pure-JUnit tests
```

Use a plain system `gradle`, not the root `./gradlew` (whose wrapper distribution is Android-
oriented). See `tools/compile-harness/README.md` for how it's wired up and its limits — it is a
partial, fallback check for when the real quality gates can't run, not a replacement for them.
When they can run, use them instead.

## Code style (see `checkstyle.xml` / `.editorconfig` for authoritative rules)

- Java, 4-space indentation, spaces not tabs, files end with a newline.
- Import order (blank line between groups, alphabetical within a group, static and non-static
  imports in the same group not separated by a blank line):
  1. `cgeo.*`
  2. `android.*`
  3. `androidx.*`
  4. `java.*`
  5. `javax.*`
  6. everything else
- No unused imports, no star imports, always use `@Override`, keep `equals()`/`hashCode()` in sync,
  prefer `final` for local variables and method parameters.
- Java 8 language features/APIs are used (core library desugaring enabled for older API levels).

## Translations

String resources are managed via [Crowdin](https://crowdin.com/project/cgeo). **Never edit the
string resource XML files directly** — such changes are overwritten on the next Crowdin sync.

## Branching

- `master` — new feature development; nightly builds are cut from here.
- `release` — bug fixes for already-released versions (merge back to `master` regularly).

Bug fixes should target `release`; new features should target `master`.

## Architecture overview

The app module lives under `main/src/main/java/cgeo/geocaching/` (plus a small `btools.routingapp`
package for BRouter integration, and `nojit` for the nojit flavor). Source sets:
`src/main/java` (app), `src/test/java` (pure JUnit unit tests, no Android framework), `src/androidTest/java`
(instrumented tests requiring an Android device/emulator). Test classes live in the same package as
the class under test.

### Multi-connector design (core abstraction)

c:geo talks to several geocaching services (geocaching.com, various OpenCaching sites, GeoKrety,
Wherigo, Adventure Labs, etc.) through a common abstraction in `connector/`:
- `IConnector` — capability interface every geocaching-service connector implements (naming,
  `canHandle(geocode)`, SQL-like geocode filtering, etc.); `AbstractConnector` provides a base
  implementation. Optional capabilities live in `connector/capability/`.
- `ConnectorFactory` — registry/dispatcher that picks the right connector for a given geocode/URL.
- Per-service subpackages: `gc` (geocaching.com), `oc` (OpenCaching), `ec` (extremcaching), `su`
  (geocaching.su), `al` (Adventure Labs), `ga`/`ge` (GeoKrety trackables etc.), `wm` (Waymarking),
  `tc` (TerraCaching), `unknown`/`internal` (fallbacks). `trackable/` handles Trackables specifically.
- `ILoggingManager`/`AbstractLoggingManager` — per-connector logic for submitting logs (finds, notes).

When adding support for a new geocaching source or feature that varies by source, this is the
pattern to extend, not a special case bolted onto UI code.

### Storage

- `storage/DataStore.java` — the central SQLite persistence layer (single large class); holds the
  DB schema version (`dbVersion`) and upgrade/migration logic in `DbHelper` (a `SQLiteOpenHelper`).
  Schema changes require bumping `dbVersion` and adding an upgrade path.
- `storage/CacheCache.java` — in-memory cache of recently used geocaches, backed by `DataStore`.
- `storage/ContentStorage.java`, `PersistableFolder.java`, `PersistableUri.java`, `LocalStorage.java`
  — abstraction over SAF (Storage Access Framework) vs. plain file access for user-chosen folders
  (GPX exports, offline maps, etc.), with `FileContentAccessor`/`DocumentContentAccessor` as the two
  concrete backends.
- `storage/extension/` — small, independently-versioned auxiliary tables/extensions to the DB.

### Domain models

`models/` holds the core domain objects: `Geocache` (the central entity — coordinates, waypoints,
logs, attributes, connector-specific state), `Waypoint`, `Trackable`, `Route`/`IndividualRoute`/
`RouteSegment`/`RouteItem` (route building/navigation), `CalculatedCoordinate` (projections/formulas
for mystery caches), `Image`, `PersonalNote`. `ICoordinate`/ `INamedGeoCoordinate`/`IGeoObject` are
the coordinate abstractions shared across maps, models and connectors.

### Maps

`maps/` contains legacy/shared map support; `unifiedmap/` is the newer unified map architecture that
abstracts over multiple map engines/tile providers:
- `UnifiedMapActivity` + `UnifiedMapViewModel` drive the map screen.
- `AbstractMapFragment` / `DefaultMap` / `UnifiedMapType` — pluggable map engine abstraction, with
  concrete engines in `unifiedmap/googlemaps/`, `unifiedmap/mapsforge/`, `unifiedmap/mapsforgevtm/`.
- `unifiedmap/layers/`, `geoitemlayer/`, `tileproviders/` — overlay layers (tracks, waypoints,
  position) and tile source management, engine-independent where possible.
- `brouter/` integrates offline routing (BRouter, via the `btools.routingapp` AIDL package).

### Other key areas

- `connector/` interacts closely with `network/` (HTTP clients/parsers per site) and `filters/`
  (search/list filtering logic).
- `settings/` — app preferences, backed by Android `SharedPreferences`; most feature areas read
  config through this package rather than accessing preferences directly.
- `activity/`, `ui/` — shared base Activities/Fragments and reusable UI widgets used across feature
  screens (list, log, search, calendar, etc. each have their own package).
- `downloader/` — offline map/routing-data download management.
- `backup/`, `export/`, `files/` — GPX/backup import-export functionality.
- `sensors/`, `location/`, `playservices/` — GPS/sensor abstraction, with `playservices/` isolating
  Google Play Services dependent code (relevant for the `foss` flavor).
- `wherigo/` — Wherigo cartridge support.

## AI-assisted contributions

Per `CONTRIBUTING.md`: AI tool use is allowed but must be disclosed in the PR description, the
contributor remains responsible for reviewing/testing everything submitted, and bulk/mass AI
generation of issues, comments, or PRs is not allowed.
