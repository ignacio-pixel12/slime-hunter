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

- **JDK 25:** Se requiere Eclipse Adoptium JDK 25. Se puede descargar desde [Adoptium](https://adoptium.net/).
- **Git:** Para clonar el repositorio. Se puede descargar desde [git-scm.com](https://git-scm.com/downloads).

Para verificar la instalación del JDK, ejecutá en una terminal:

```bash
java -version
```

Debe mostrar `25`.

## Cómo compilar y ejecutar

### Windows

```bash
# 1. Clonar el repositorio
git clone https://github.com/ignacio-pixel12/slime-hunter.git

# 2. Entrar a la carpeta del proyecto
cd slime-hunter

# 3. Ejecutar
.\gradlew.bat run
```

### Linux / macOS

```bash
# 1. Clonar el repositorio
git clone https://github.com/ignacio-pixel12/slime-hunter.git

# 2. Entrar a la carpeta del proyecto
cd slime-hunter

# 3. Ejecutar
./gradlew run
```

**Nota:** En macOS es posible que necesites dar permisos de ejecución al wrapper la primera vez:

```bash
chmod +x gradlew
```

### Exportar sprites (requiere Aseprite)

Si se instala Aseprite, se puede exportar los spritesheets de forma automática:

```bash
.\gradlew.bat exportarSprites
```

**Nota:** Esta tarea busca Aseprite en la ruta de instalación por defecto de Steam en Windows. Si Aseprite no está instalado, la tarea se omite automáticamente y se ejecutan los spritesheets ya exportados en `src/main/resources/`.

## Documentación

- [Propuesta formal del proyecto (Wiki)](https://github.com/ignacio-pixel12/slime-hunter/wiki/Home)

## Estado actual

Prototipo jugable — segunda pre-entrega. Implementado:

### Jugador
- Movimiento lateral con aceleración y fricción
- Salto con gravedad (altura ajustable)
- Control aéreo (50% de aceleración en el aire)
- Ataque con espada (hitbox activo, cooldown, animación)
- Sistema de vida (5 puntos), barra de HUD e invulnerabilidad temporal
- Bajar de plataformas con flecha abajo

### Mapa y entorno
- Mapa diseñado en Tiled (`diseño.tmx`) con múltiples capas de tiles
- Colisiones sólidas y plataformas unidireccionales leídas desde el mapa
- Spawn del jugador configurado desde el mapa

### Enemigos
- Enemigo patrulla (slime) con animaciones y sistema de vida
- Colisión jugador ↔ enemigo: daño por contacto
- Colisión ataque ↔ enemigo: el golpe reduce la vida del enemigo

### Pantallas
- Pantalla de nombre (el jugador ingresa su nombre)
- Pantalla de juego
- Pantalla de derrota (muestra nombre y tiempo)
- Pantalla de victoria (muestra nombre y tiempo)
- Transiciones entre pantallas con `setScreen()`

### Cámara y viewport
- Cámara con zoom (1/3) centrada en el jugador
- Viewport con `FitViewport` para adaptación a diferentes resoluciones
- Redimensionamiento dinámico al cambiar el tamaño de la ventana

### Controles

| Tecla | Acción |
|-------|--------|
| A / ← | Moverse a la izquierda |
| D / → | Moverse a la derecha |
| Espacio | Saltar |
| Click izquierdo | Atacar |
| S / ↓ | Bajar de plataforma |
| F3 | Mostrar/ocultar debug de colisiones |
| Enter | Confirmar / continuar en pantallas de menú |
