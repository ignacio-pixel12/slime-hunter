# Slime Hunter

**Videojuego 2D de plataformas / Metroidvania** — Java 25 + libGDX

Slime Hunter es un videojuego de acción y aventura en perspectiva 2D donde el jugador controla a un caballero que debe enfrentar oleadas de slimes en diferentes escenarios. El juego combina exploración tipo metroidvania con una mecánica de reinicio tipo roguelike: al perder toda la vida, el progreso se reinicia y el jugador debe volver a intentarlo. Cada intento se registra con nombre y tiempo, incentivando la superación personal.

El mundo está compuesto por una ciudad inicial (zona segura y menú principal), un bosque intermedio con enemigos y obstáculos, y una mazmorra final donde se encuentra el jefe principal. A lo largo de la aventura, el jugador obtiene habilidades como el doble salto y el dash que desbloquean nuevas zonas del mapa.

## Integrantes

- Ignacio García Belvedere

## Tecnologías

- **Lenguaje:** Java 25 ([Eclipse Adoptium JDK 25](https://adoptium.net/))
- **Framework:** libGDX 1.12.1
- **Backend:** LWJGL3 (escritorio)
- **Build:** Gradle 9.4.0
- **Arte:** Aseprite (sprites), Tiled (mapas)
- **Base de datos:** SQLite (planeada)

## Requisitos previos

- **JDK 25:** Se requiere Eclipse Adoptium JDK 25 o superior. Se puede descargar desde [Adoptium](https://adoptium.net/).
- **Git:** Para clonar el repositorio. Se puede descargar desde [git-scm.com](https://git-scm.com/downloads).

Para verificar la instalación del JDK, ejecutá en una terminal:

```bash
java -version
```

Debe mostrar `25` o superior.

## Cómo compilar y ejecutar

### Windows

```bash
# 1. Clonar el repositorio
git clone https://github.com/ignacio-pixel12/slime-hunter.git

# 2. Entrar a la carpeta del proyecto
cd slime-hunter

# 3. Ejecutar
.\gradlew.bat lwjgl3:run
```

### Linux / macOS

```bash
# 1. Clonar el repositorio
git clone https://github.com/ignacio-pixel12/slime-hunter.git

# 2. Entrar a la carpeta del proyecto
cd slime-hunter

# 3. Ejecutar
./gradlew lwjgl3:run
```

**Nota:** En macOS es posible que necesites dar permisos de ejecución al wrapper la primera vez:

```bash
chmod +x gradlew
```

## Documentación

- [Propuesta formal del proyecto (Wiki)](https://github.com/ignacio-pixel12/slime-hunter/wiki/Home)

## Estado actual

Primera pre-entrega: configuración inicial del proyecto y repositorio.
