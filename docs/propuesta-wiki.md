# Propuesta del Proyecto - Slime Hunter

## Tema: Slime Hunter

**Videojuego 2D de plataformas / Metroidvania**

5to año - Laboratorio y Programación

---

## Introducción

El juego tratará de un caballero cuya misión será derrotar al jefe de una mazmorra. Contará con una ambientación medieval y fantasiosa, el mundo contará con una ciudad inicial, un bosque y la mazmorra final. Al llegar a la mazmorra el jugador podrá conseguir distintas habilidades que le permitirán acceder a lugares a los que antes era imposible acceder. Al quedarse sin vida el jugador perderá y podrá volver a empezar desde el principio.

La experiencia busca generar en el jugador una sensación de desafío constante y de superación personal al incluir una mecánica como el reinicio al perder, en la cual cada intento sea una oportunidad de aprendizaje.

---

## 1.1 Objetivo

El objetivo del proyecto es desarrollar un videojuego utilizando Java y el framework LibGDX.

Se buscará utilizar los contenidos aprendidos como herencia para los distintos personajes; además, si es necesario, se agregan otros temas como una tabla de estados que muestra qué va a hacer un personaje según el estado en que se encuentre.

### Base de datos SQL

Se utilizará SQLite para guardar el nombre del usuario y su tiempo de partida. La base de datos funcionará de forma local. El guardado del tiempo se realizará al derrotar al jefe final o cuando el jugador se quede sin vida.

La base de datos guardará todos los intentos (nombre y tiempo), indicando si terminaron en victoria o derrota. El mejor tiempo se calculará únicamente entre las partidas completadas con victoria.

El videojuego a desarrollar va a consistir en un mapa grande e interconectado y una progresión basada en conseguir nuevas habilidades para avanzar (metroidvania), en el cual cada vez que el jugador pierda todo su progreso se reiniciará (roguelike). La perspectiva del juego será en 2D con plataformas.

---

## 1.2 Alcance

El videojuego va a consistir en un mapa grande con salas interconectadas, dos tipos de enemigos, un jefe, un personaje principal con un ataque y un salto más 2 habilidades para obtener. La ciudad principal actuará como menú principal.

### Mundo

El juego va a tener como mapa una ciudad principal que actuará como menú principal, donde el jugador comenzará y volverá a aparecer cada vez que pierda, una zona intermedia con temática de bosque con enemigos y obstáculos, y una mazmorra final con enemigos, obstáculos y el jefe.

### Jugador

El personaje principal se moverá únicamente de izquierda a derecha, podrá atacar con una espada, tendrá salto, doble salto, un impulso hacia la derecha o la izquierda (dash), una cantidad de vida y podrá recibir daño. La vitalidad del personaje estará compuesta por una barra de vida colocada en la esquina superior izquierda que irá bajando de izquierda a derecha a medida que el personaje sufre daño; cuando la vida del personaje llegue a cero, el jugador perderá.

Las habilidades que va a conseguir a lo largo del juego van a ser el doble salto (le permite saltar en el aire luego de un salto previo) y el dash (le permite llegar a lugares a los que antes no podía acceder). El doble salto se obtendrá en el bosque (zona intermedia) y será necesario para acceder a ciertas áreas de la mazmorra final. El dash se obtendrá dentro de la mazmorra final.

### Enemigos

Habrá dos tipos de enemigos básicos: uno que patrulla y uno que salta, ambos hechos como slimes azules.

El jefe final va a ser un slime azul grande que ataca saltando e invocando enemigos; al derrotarlo se completa el juego (condición de victoria). La condición de derrota se da cuando la vida del jugador llega a cero.

Si se logra terminar el desarrollo principal, se agregará un jefe adicional: un caballero de armadura negra con dificultad superior, que ataca con proyectiles que cubren gran parte de la pantalla. Será opcional y su recompensa será una habilidad que permite ataques más fuertes. Su diseño está inspirado en el antagonista principal de *DELTARUNE* como referencia visual, no como copia directa del personaje.

### 1.2.1 Alcance mínimo

El alcance mínimo contará con:

- Un mapa principal con una ciudad inicial, un bosque como zona intermedia y una mazmorra final.
- El personaje principal con movimiento lateral, salto, ataque con espada, barra de vida y recepción de daño.
- Dos enemigos básicos: uno que ataca patrullando y otro que ataca saltando.
- Un jefe final que ataca saltando e invocando a los enemigos anteriormente mencionados.
- Dos habilidades: doble salto (segundo salto en el aire, obtenible en el bosque) y dash (impulso hacia izquierda o derecha, obtenible en la mazmorra final).
- Mecánica de reinicio: al llegar a 0 de vida el juego termina y vuelve a empezar.
- El jugador al iniciar el juego ingresa un nombre; al perder o terminar el juego se muestra ese nombre junto con el tiempo que tardó en morir o ganar.
- Cuando el jugador gana o pierda, todo el progreso se reinicia (enemigos derrotados incluyendo jefes, habilidades obtenidas). Lo único que se guarda en la base de datos es el nombre, el tiempo y si terminó en victoria o derrota.
- Pantalla de victoria al derrotar al jefe final y pantalla de derrota cuando la vida llega a cero.
- La ciudad inicial funciona como zona segura (sin enemigos) y como menú principal. Se puede volver a ella libremente.

### 1.2.2 Alcance deseable

El alcance deseable contará con:

- Un jefe adicional con una dificultad mayor a la de los enemigos y el jefe del alcance mínimo.
- Una habilidad extra que el jugador recibirá al vencer a este jefe adicional.
- Dos enemigos extras: un slime que ataca con una espada y otro que lanza proyectiles.

---

## 2. Descripción de la Propuesta

### 2.1 Tácticas de Trabajo

El trabajo se realizará de manera continua. Si es posible, se trabajará al menos una hora por día, cumpliendo distintos objetivos. Se utilizará una tabla con tareas (product backlog) en Trello para llevar control de las tareas realizadas y a realizar. Se utilizará Git y GitHub para el control de versiones y respaldo del proyecto.

El desarrollo del trabajo se dividirá en las siguientes etapas:

1. Proyecto base en Java / LibGDX.
2. Movimiento lateral y salto.
3. Ataque con espada y daño.
4. Enemigos básicos (patrulla y salta).
5. Ciudad inicial.
6. Bosque (zona intermedia).
7. Mazmorra final.
8. Doble salto (obtenible en el bosque).
9. Dash (obtenible en la mazmorra).
10. Jefe final.
11. SQLite: registro de nombre, tiempo y condición de victoria/derrota.
12. Pantallas de victoria y derrota.
13. Pulido visual y pruebas finales.

Cuando se termine el desarrollo de un objetivo se va a probar; si presenta algún error se corregirá y volverá a probar hasta que funcione de forma correcta, y así sucesivamente con cada objetivo.

### 2.2 Gráficos y Bocetos

Todos los diseños del videojuego, como el mundo, el personaje y los enemigos, van a ser hechos únicamente con píxeles (pixel art) utilizando **Aseprite** como herramienta de dibujo. Para el desarrollo del mapa se utilizará **Tiled**.

- El diseño del personaje principal va a ser propio, sin basarse en ningún personaje existente, al igual que todas sus animaciones.
- Los enemigos están basados en los enemigos del videojuego *Monster Boy and the Cursed Kingdom* como inspiración visual para un proyecto académico.
- El jefe principal va a ser una versión de estos enemigos pero con un tamaño considerablemente mayor, con más vida y ataque.
- La ciudad donde va a empezar el jugador (que también va a servir como zona segura) es un diseño propio. El bosque está basado en zonas del videojuego *Monster Boy and the Cursed Kingdom*.
- El diseño de la mazmorra final va a ser propio.
- El diseño del jefe adicional se basará en el antagonista principal de *DELTARUNE* como referencia de silueta, tono oscuro y presencia.

Todas las imágenes de otros juegos se incluyen como referencia visual para un trabajo académico.

### 2.3 Mecánicas y Controles

Las mecánicas del juego serán:

- **Atacar**: el jugador le hará daño a los enemigos y viceversa.
- **Recibir daño**: al contacto con un enemigo, una trampa o al recibir un ataque. Los enemigos solo recibirán daño al ser golpeados.
- **Saltar**: con esta habilidad el jugador podrá impulsarse hacia arriba. Si consigue la mejora de doble salto, podrá realizar esta acción hasta un total de 2 veces.
- **Impulso (dash)**: con esta mecánica el jugador podrá impulsarse hacia la izquierda o la derecha cada cierto período de tiempo.

**Controles:**

| Tecla | Acción |
|-------|--------|
| A | Moverse hacia la izquierda |
| D | Moverse hacia la derecha |
| Espacio | Saltar / Doble salto |
| Click izquierdo | Atacar |
| Shift | Dash (una vez conseguido) |
| F | Interacciones con elementos del escenario |

No va a haber un menú por el cual el jugador puede pausar el juego una vez empezado.

### 2.4 Temas a Investigar

- Uso de LibGDX para videojuegos 2D.
- Manejo de pantallas en LibGDX.
- Movimiento y colisiones.
- Animaciones en pixel art.
- Uso de Tiled para creación de mapas.
- Carga de mapas en el juego.
- Uso de Aseprite para sprites y animaciones.
- Implementación de SQLite con Java.
- JDBC para conectar Java con SQLite.
- Consultas SQL básicas: INSERT, SELECT, UPDATE.
- Guardado y lectura de datos.
- Organización del proyecto con Git y GitHub.
- Diseño de niveles tipo metroidvania.
- Mecánicas de reinicio tipo roguelike.
- Estructura de clases para personaje, enemigos, jefe, habilidades y mapa.

---

## 3. Aportes

El juego combina exploración metroidvania con reinicio tipo roguelike y registro de tiempos, buscando que cada intento sea una carrera por mejorar el recorrido, llegar más lejos y completar el juego más rápido.

A diferencia de los mapas de un juego roguelike que son aleatorios, en este proyecto se busca tener un mapa fijo de un metroidvania. El sistema de progresión se diferencia de otros al no poder mejorar características como la vida o el ataque a menos que exista una habilidad para eso.

La ciudad principal funciona como zona segura al no tener enemigos, además de que se va a poder volver a entrar las veces que el jugador lo desee.

Las habilidades como doble salto y dash cumplen la función de abrir zonas a las que antes no se podía acceder; por ejemplo, sin la habilidad del doble salto (obtenida en el bosque) no se va a poder llegar a la zona de la mazmorra donde se consigue el dash.

La base de datos SQLite le permite al jugador ver su tiempo de partida e incentivarlo a superar su propio récord o el de otros. El mejor tiempo se calcula únicamente entre las partidas ganadas.

El diseño del personaje principal es propio; los enemigos son del videojuego *Monster Boy and the Cursed Kingdom* solo en diseño, ya que va a haber variantes de ese mismo enemigo con distintas habilidades que son de diseño propio. El jefe final es un diseño propio, la ciudad es un diseño propio, el bosque está inspirado en una zona de *Monster Boy and the Cursed Kingdom* y el jefe adicional se va a basar en el antagonista principal de *DELTARUNE* como referencia visual.

---

## 4. Aprobación

Esto deja constancia de la aceptación del tema propuesto.
