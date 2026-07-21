# AGENTS.md

## Stack

- **Language:** Java 25
- **Framework:** libgdx (game framework)
- **IDE:** Eclipse
- **Build:** Gradle 9.4.0

## Directory layout

```
slime-hunter/
├── src/main/java/com/slimehunter/   # All Java source code
│   ├── SlimeHunter.java             # Game entrypoint
│   ├── GameScreen.java              # Game screen
│   └── desktop/
│       └── Lwjgl3Launcher.java      # Desktop launcher
├── src/main/resources/              # Resources
├── assets/                          # Runtime game assets (textures, maps, audio)
├── art/
│   ├── tiled/                       # Tiled map editor source files (.tmx, .tsx)
│   └── aseprite/                    # Aseprite sprite source files (.aseprite)
├── docs/                            # Design documents, definitions, specs
├── raw/                             # Permanent staging folder — NEVER delete
├── build.gradle                     # Single Gradle build (Shadow for fat jar)
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat
├── AGENTS.md
├── README.md
├── CHANGELOG.md
└── .gitignore
```

## Key conventions

- **`raw/`** is a permanent staging folder. Never delete it. User drops files here for sorting.
- Art sources live in `art/tiled/` and `art/aseprite/` — **not** in `assets/`.
- Single Gradle module — Eclipse imports as one project.
- Fat jar: `./gradlew shadowJar` → `build/libs/slime-hunter.jar` (run anywhere with `java -jar`).
- Run dev: `./gradlew run` (Linux/Mac) or `.\gradlew.bat run` (Windows).
