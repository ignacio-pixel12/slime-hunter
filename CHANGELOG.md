# Changelog

Todos los cambios notables de este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es/1.1.0/).

## [0.1.0] - 2026-07-06

### Agregado

- Estructura inicial del proyecto con libGDX (módulo único, sin separación core/lwjgl3).
- Configuración de Gradle 9.4.0 con Java 25 (Eclipse Adoptium JDK 25).
- Dependencias: libGDX 1.12.1, backend LWJGL3.
- Clase principal `SlimeHunter.java` y launcher `Lwjgl3Launcher.java`.
- Repositorio en GitHub con remote configurado.
- Archivo `.gitignore` para Eclipse, Gradle, IntelliJ y sistema operativo.

## [0.2.0] - 2026-07-10

### Agregado

- Archivo `README.md` con descripción del proyecto, integrantes, tecnologías e instrucciones de ejecución.
- Archivo `CHANGELOG.md` con registro de cambios.
- Enlace a la Wiki del repositorio desde el README.

### Cambiado

- Configuración de Java 25 como toolchain en Gradle (consistente en build.gradle).

## [0.3.0] - 2026-07-13

### Agregado

- Propuesta formal del proyecto en la Wiki del repositorio (Home), con formato Markdown, imagen del escudo de la escuela y links externos formateados.
- Archivo `propuesta-wiki.md` en `docs/` como respaldo local de la propuesta en Markdown.
- Colaborador `jasinski1988` agregado al repositorio.

### Corregido

- Corrección de la configuración de Gradle: eliminación de dependencia innecesaria de Android Gradle Plugin.
- Corrección de encabezados del CHANGELOG al español (Agregado, Cambiado, Corregido).
- Corrección de instrucciones en README: "JDK 25" (sin "o superior").

## [0.4.0] - 2026-07-20

### Agregado

- Jerarquía de entidades: `Entidad` (abstracta) → `EntidadEstatica` / `EntidadDinamica` → `Jugador`.
- Movimiento lateral del jugador con aceleración y fricción (`ACELERACION_JUGADOR`, `FRICCION_JUGADOR`).
- Salto con gravedad (`FUERZA_SALTO`, `GRAVEDAD`).
- Tabla de estados animados con `TablaEstados` y `EstadoAnimacion` (INACTIVO, CAMINANDO, SALTANDO, CAYENDO, ATACANDO, MURIENDO, DESPLAZANDO).
- Input abstracto: interfaz `Entrada` y clase `ManejadorEntrada` (InputAdapter).
- Colisiones AABB con el escenario (mapas Tiled): detección y resolución por eje de menor solape.
- Sistema de sprites: `GestorSprites` carga hojas de sprites PNG + JSON exportados desde Aseprite.
- Integración de mapa Tiled (`mapa-test.tmx`) con capa de colisiones y cámara con seguimiento (`CamaraJuego`).
- Debug visual de colisiones con `DebugColisiones` (ShapeRenderer).

## [0.5.0] - 2026-07-27

### Agregado

- Ataque con espada: hitbox de ataque, cooldown, duración de la animación (0.64s), rango de 80px.
- Detección de click izquierdo en `ManejadorEntrada` para atacar.
- Animación de ataque sin loop (`obtenerFrameSinLoop`).
- Debug visual de la hitbox de ataque (color amarillo).

## [0.6.0] - 2026-08-03

### Agregado

- Sistema de vida del jugador: `vida`, `maxVida` (5 puntos), `recibirDano()`.
- Invulnerabilidad temporal (1s) con parpadeo visual (alpha).
- Barra de vida en HUD: `InterfazHUD` con ShapeRenderer y proyección fija (setToOrtho2D).
- Transición de color de la barra (verde → rojo según porcentaje de vida).
- Estado MURIENDO con animación de muerte y reinicio automático.
- Reinicio al morir: posición (200, 600), vida completa.

## [0.7.0] - 2026-08-05

### Agregado

- Enemigo patrulla: clase `Enemigo` con patrullaje entre dos coordenadas X.
- Colisión jugador ↔ enemigo: daño por contacto (`SLIME_DANO = 1`).
- Colisión ataque del jugador ↔ enemigo: el golpe elimina al slime.
- Sistema de vida del enemigo: `SLIME_VIDA_MAXIMA = 3`.
- Fade-out al morir (0.3s de transparencia).
- Sprite del slime re-exportado con tags de animación (caminar, herido).
- Tabla de estados del enemigo: CAMINANDO ↔ RECIBIENDO_DANO.
- Animación de hit sin loop con retorno automático a CAMINANDO.

### Corregido

- Caja de colisión del slime ajustada a 20x13 píxeles.
- Debug visual de colisiones ahora muestra también los enemigos vivos.
- Render del slime centrado horizontal y verticalmente (igual que el caballero).
- Aseprite export task: ruta configurable, se omite si Aseprite no está instalado.
- Tarea `run` ya no depende automáticamente de `exportarSprites`.
