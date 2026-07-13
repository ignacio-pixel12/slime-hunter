# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es/1.1.0/).

## [0.1.0] - 2026-07-06

### Added

- Estructura inicial del proyecto con libGDX (core + lwjgl3).
- Configuración de Gradle 9.4.0 con Java 25 (Eclipse Adoptium JDK 25).
- Dependencias: libGDX 1.12.1, backend LWJGL3.
- Clase principal `SlimeHunter.java` y launcher `Lwjgl3Launcher.java`.
- Repositorio en GitHub con remote configurado.
- Archivo `.gitignore` para Eclipse, Gradle, IntelliJ y sistema operativo.

## [0.2.0] - 2026-07-10

### Added

- Archivo `README.md` con descripción del proyecto, integrantes, tecnologías e instrucciones de ejecución.
- Archivo `CHANGELOG.md` con registro de cambios.
- Enlace a la Wiki del repositorio desde el README.

### Changed

- Configuración de Java 25 como toolchain en Gradle (consistente en build.gradle).

## [0.3.0] - 2026-07-13

### Added

- Propuesta formal del proyecto en la Wiki del repositorio (Home).
- Archivo `propuesta-wiki.md` en `docs/` como respaldo local de la propuesta en Markdown.
- Colaborador `jasinski1988` agregado al repositorio.

### Fixed

- Corrección de la configuración de Gradle: eliminación de dependencia innecesaria de Android Gradle Plugin.
- Configuración de la tarea `run` en el subproyecto lwjgl3 mediante el plugin `application`.
