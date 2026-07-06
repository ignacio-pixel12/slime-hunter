# AGENTS.md

## Stack

- **Language:** Java 25
- **Framework:** libgdx (game framework)
- **IDE:** Eclipse
- **Build:** Gradle (expected — standard libgdx toolchain)

## Directory layout

```
slime-hunter/
├── core/                     # Cross-platform game code
│   ├── build.gradle
│   └── src/main/java/com/slimehunter/
├── lwjgl3/                   # Desktop launcher (LWJGL3)
│   ├── build.gradle
│   └── src/main/java/com/slimehunter/desktop/
├── assets/                   # Runtime game assets (packed textures, maps, audio)
├── art/
│   ├── tiled/                # Tiled map editor source files (.tmx, .tsx)
│   └── aseprite/             # Aseprite sprite source files (.aseprite)
├── docs/                     # Design documents, definitions, specs
├── raw/                      # Permanent staging folder — NEVER delete
├── build.gradle              # Root Gradle build
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
- Art sources live in `art/tiled/` and `art/aseprite/` — **not** in `assets/`. Use a build/pack step to move runtime-ready files into `assets/`.
- Game entrypoint: `lwjgl3/` launcher class (`Lwjgl3Launcher.java`).
- Core game logic goes under `core/src/main/java/com/slimehunter/`.
- Desktop module is `lwjgl3/` (not `desktop/`) — this is the Liftoff convention.
