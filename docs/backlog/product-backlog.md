# Product Backlog — Slime Hunter

Cada PBI sigue los criterios SMART:
- **S**pecífico: qué se hace concreto
- **M**edible: cómo se mide que está terminado
- **A**lcanzable: es factible en un sprint
- **R**elevante: aporta al objetivo del juego
- **T**emporizado: estimado en story points (1 SP ≈ 1 hora de trabajo)

---

## PBI-01: Movimiento lateral del personaje

**Como** jugador,
**quiero** mover al caballero hacia la izquierda con A y hacia la derecha con D,
**para** poder explorar el escenario.

**Criterio de aceptación:**
- El personaje se desplaza horizontalmente al mantener presionada A o D.
- La velocidad de movimiento es constante y uniforme.
- Al soltar la tecla, el personaje se detiene inmediatamente.
- El sprite cambia de dirección según el sentido del movimiento.

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte

**Cómo probar:**
1. Ejecutar el juego con `gradlew.bat lwjgl3:run`.
2. Presionar A y verificar que el personaje se mueve a la izquierda.
3. Presionar D y verificar que el personaje se mueve a la derecha.
4. Soltar la tecla y verificar que se detiene.
5. Verificar que el sprite se voltea correctamente según la dirección.

---

## PBI-02: Salto del personaje

**Como** jugador,
**quiero** saltar con la barra espaciadora,
**para** poder superar obstáculos y plataformas.

**Criterio de aceptación:**
- Al presionar Espacio, el personaje se impulsa hacia arriba.
- El personaje cae por gravedad después del salto.
- No se puede saltar más de una vez en el aire (sin doble salto aún).
- El salto tiene una altura consistente.

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte

**Cómo probar:**
1. Ejecutar el juego.
2. Presionar Espacio y verificar que el personaje sale disparado hacia arriba.
3. Verificar que cae de vuelta por gravedad.
4. Intentar saltar en el aire y verificar que no se puede (solo un salto).
5. Repetir varias veces y verificar que la altura del salto es siempre la misma.

---

## PBI-03: Colisiones con el escenario

**Como** jugador,
**quiero** que el personaje no atraviese el suelo ni las plataformas,
**para** que el juego tenga física realista.

**Criterio de aceptación:**
- El personaje no atraviesa el suelo.
- El personaje puede pararse sobre plataformas.
- El personaje no puede saltar a través de plataformas desde abajo.
- Las colisiones laterales funcionan (no atraviesa paredes).

**Prioridad:** Alta
**Story Points:** 5
**Área:** Código

**Cómo probar:**
1. Ejecutar el juego con un mapa de prueba que tenga suelo y plataformas.
2. Caminar sobre el suelo y verificar que no se cae.
3. Saltar sobre una plataforma y verificar que se posa correctamente.
4. Intentar saltar desde abajo de una plataforma y verificar que no la atraviesa.
5. Caminar contra una pared y verificar que no la atraviesa.

---

## PBI-04: Ataque con espada

**Como** jugador,
**quiero** atacar con click izquierdo,
**para** poder derrotar enemigos.

**Criterio de aceptación:**
- Al hacer click izquierdo, el personaje realiza una animación de ataque.
- El ataque tiene un rango de alcance determinado.
- Hay un tiempo de enfriamiento entre ataques (no se puede spamear).
- El ataque solo se activa cuando el personaje está en el suelo (opcional).

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte

**Cómo probar:**
1. Ejecutar el juego.
2. Hacer click izquierdo y verificar que se reproduce la animación de ataque.
3. Verificar que hay un intervalo mínimo entre ataques consecutivos.
4. Verificar que el ataque tiene un alcance visible (hitbox).

---

## PBI-05: Barra de vida del personaje

**Como** jugador,
**quiero** ver mi barra de vida en la esquina superior izquierda,
**para** saber cuánta vida me queda.

**Criterio de aceptación:**
- La barra de vida se muestra en la esquina superior izquierda.
- La barra se reduce cuando el personaje recibe daño.
- La barra es de color verde (llena) y cambia a rojo a medida que baja.
- Cuando la barra llega a cero, el jugador muere.

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte (UI)

**Cómo probar:**
1. Ejecutar el juego con un enemigo en pantalla.
2. Verificar que la barra de vida aparece en la esquina superior izquierda.
3. Tocar al enemigo y verificar que la barra baja.
4. Repetir hasta que la barra llegue a cero y verificar que se muestra pantalla de derrota.

---

## PBI-06: Enemigo patrulla (slime azul)

**Como** jugador,
**quiero** encontrar enemigos que patrullan una zona,
**para** que el juego tenga desafío.

**Criterio de aceptación:**
- El enemigo se mueve de izquierda a derecha en un rango definido.
- Al llegar al límite de su patrulla, cambia de dirección.
- El enemigo recibe daño al ser golpeado por la espada.
- El enemigo desaparece (muere) tras recibir una cantidad determinada de golpes.
- El enemigo le da daño al jugador al contacto.

**Prioridad:** Alta
**Story Points:** 5
**Área:** Código + Arte + GD

**Cómo probar:**
1. Ejecutar el juego con un enemigo patrulla en el mapa.
2. Observar que el enemigo se mueve de ida y vuelta.
3. Acercarse y recibir daño por contacto.
4. Atacar al enemigo y verificar que recibe daño (feedback visual).
5. Golpear suficientes veces y verificar que muere.

---

## PBI-07: Enemigo salto (slime azul)

**Como** jugador,
**quiero** encontrar enemigos que saltan periódicamente,
**para** tener una variante de desafío.

**Criterio de aceptación:**
- El enemigo salta periódicamente cada cierto intervalo.
- Al caer, sigue en su posición original (no se desplaza horizontalmente).
- Recibe daño al ser golpeado.
- Muere tras recibir suficientes golpes.
- Le da daño al jugador al contacto.

**Prioridad:** Alta
**Story Points:** 5
**Área:** Código + Arte + GD

**Cómo probar:**
1. Ejecutar el juego con un enemigo salto en el mapa.
2. Observar que salta cada ciertos segundos.
3. Verificar que cae en el mismo lugar.
4. Atacarlo y verificar que recibe daño y muere.

---

## PBI-08: Mapa de ciudad inicial

**Como** jugador,
**quiero** comenzar en una ciudad que funcione como zona segura y menú principal,
**para** tener un punto de partida seguro.

**Criterio de aceptación:**
- La ciudad tiene un diseño de tileset propio en pixel art.
- No hay enemigos en la ciudad.
- El jugador puede moverse libremente por la ciudad.
- Hay al menos 2 salidas (una hacia el bosque, una hacia la mazmorra).
- El mapa está diseñado en Tiled y cargado correctamente en el juego.

**Prioridad:** Alta
**Story Points:** 8
**Área:** Arte + GD + Código

**Cómo probar:**
1. Ejecutar el juego.
2. Verificar que el jugador aparece en la ciudad.
3. Verificar que no hay enemigos.
4. Explorar la ciudad completa y verificar que no hay bugs de colisión.
5. Llegar a las salidas y verificar que conectan con otras zonas.

---

## PBI-09: Mapa de bosque

**Como** jugador,
**quiero** explorar un bosque con enemigos y obstáculos,
**para** avanzar hacia la mazmorra.

**Criterio de aceptación:**
- El bosque tiene un diseño de tileset inspirado en Monster Boy.
- Hay enemigos patrulla y salto distribuidos por el mapa.
- El mapa tiene plataformas que requieren saltos para avanzar.
- Hay un punto donde se obtiene el doble salto.
- El mapa está diseñado en Tiled y cargado correctamente.

**Prioridad:** Alta
**Story Points:** 8
**Área:** Arte + GD + Código

**Cómo probar:**
1. Ejecutar el juego y llegar al bosque desde la ciudad.
2. Explorar el mapa completo.
3. Verificar que los enemigos funcionan correctamente.
4. Verificar que las plataformas y colisiones están bien.
5. Encontrar el punto de obtención del doble salto.

---

## PBI-10: Mapa de mazmorra final

**Como** jugador,
**quiero** llegar a una mazmorra con enemigos difíciles y el jefe final,
**para** completar el juego.

**Criterio de aceptación:**
- La mazmorra tiene un diseño propio en pixel art.
- Hay enemigos distribuidos por el mapa.
- Hay un punto donde se obtiene el dash.
- El jefe final está al final de la mazmorra.
- El mapa está diseñado en Tiled y cargado correctamente.

**Prioridad:** Alta
**Story Points:** 8
**Área:** Arte + GD + Código

**Cómo probar:**
1. Ejecutar el juego y llegar a la mazmorra.
2. Explorar el mapa completo.
3. Encontrar y obtener el dash.
4. Llegar al jefe final.
5. Verificar que el jefe aparece y funciona.

---

## PBI-11: Doble salto

**Como** jugador,
**quiero** obtener el doble salto en el bosque,
**para** acceder a zonas que antes no podía.

**Criterio de aceptación:**
- Hay un objeto/coleccionable visible en el bosque que otorga el doble salto.
- Al recogerlo, el jugador puede saltar una segunda vez en el aire.
- El doble salto tiene un efecto visual o sonoro al activarse.
- La mejora persiste durante toda la partida.

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte + GD

**Cómo probar:**
1. Llegar al bosque y encontrar el objeto del doble salto.
2. Recogerlo y verificar que aparece un feedback (visual/sonoro).
3. Saltar en el aire y presionar Espacio nuevamente.
4. Verificar que el personaje salta una segunda vez.
5. Verificar que funciona en diferentes situaciones.

---

## PBI-12: Dash (impulso horizontal)

**Como** jugador,
**quiero** obtener el dash en la mazmorra,
**para** cruzar gaps y llegar a zonas nuevas.

**Criterio de aceptación:**
- Hay un objeto visible en la mazmorra que otorga el dash.
- Al presionar Shift, el personaje se impulsa rápidamente en la dirección que mira.
- El dash tiene un tiempo de enfriamiento.
- El dash permite cruzar plataformas amplias.
- La mejora persiste durante toda la partida.

**Prioridad:** Alta
**Story Points:** 3
**Área:** Código + Arte + GD

**Cómo probar:**
1. Llegar a la mazmorra y encontrar el objeto del dash.
2. Recogerlo y verificar feedback.
3. Presionar Shift y verificar el impulso rápido.
4. Intentar cruzar un gap amplio con el dash.
5. Verificar que hay cooldown entre dashes consecutivos.

---

## PBI-13: Jefe final (slime grande)

**Como** jugador,
**quiero** enfrentar un jefe final que sea un desafío,
**para** tener una batalla épica.

**Criterio de aceptación:**
- El jefe es un slime azul grande con corona.
- Tiene más vida que los enemigos normales.
- Ataca saltando y causando daño por contacto.
- Invoca enemigos pequeños durante la pelea.
- Al morir, se muestra la pantalla de victoria.

**Prioridad:** Alta
**Story Points:** 8
**Área:** Código + Arte + GD

**Cómo probar:**
1. Llegar al jefe final en la mazmorra.
2. Observar que tiene más vida que los enemigos normales.
3. Esquivar sus ataques de salto.
4. Verificar que invoca enemigos.
5. Derrotarlo y verificar que aparece pantalla de victoria.

---

## PBI-14: Sistema de reinicio (roguelike)

**Como** jugador,
**quiero** que al morir todo el progreso se reinicie,
**para** tener la experiencia roguelike.

**Criterio de aceptación:**
- Al llegar a 0 de vida, el juego se reinicia completamente.
- El jugador vuelve a la ciudad inicial.
- Las habilidades obtenidas se pierden.
- Los enemigos derrotados reaparecen.
- El nombre del jugador se mantiene en la BD.

**Prioridad:** Alta
**Story Points:** 5
**Área:** Código + GD

**Cómo probar:**
1. Jugar hasta morir.
2. Verificar que aparece pantalla de derrota.
3. Verificar que el jugador vuelve a la ciudad.
4. Verificar que no tiene doble salto ni dash.
5. Verificar que los enemigos están de vuelta.

---

## PBI-15: SQLite — registro de nombre y tiempo

**Como** jugador,
**quiero** ingresar mi nombre al iniciar y ver mi tiempo de partida,
**para** comparar mi rendimiento.

**Criterio de aceptación:**
- Al iniciar el juego, aparece una pantalla para ingresar el nombre.
- El nombre se guarda en SQLite.
- Se muestra un cronómetro durante la partida.
- Al ganar o perder, se guarda nombre, tiempo y resultado.
- El mejor tiempo se calcula solo entre victorias.

**Prioridad:** Media
**Story Points:** 5
**Área:** Código + BD

**Cómo probar:**
1. Iniciar el juego e ingresar un nombre.
2. Verificar que el cronómetro avanza.
3. Ganar o perder y verificar que se guarda en la BD.
4. Consultar la BD y verificar que el registro es correcto.
5. Jugar varias veces y verificar que el mejor tiempo es el de la victoria más rápida.

---

## PBI-16: Pantalla de victoria

**Como** jugador,
**quiero** ver una pantalla de victoria al derrotar al jefe,
**para** saber que completé el juego.

**Criterio de aceptación:**
- Al derrotar al jefe final, se muestra una pantalla de victoria.
- Se muestra el tiempo que tardó el jugador.
- Hay una opción para jugar de nuevo.
- Se muestra el nombre del jugador.

**Prioridad:** Media
**Story Points:** 2
**Área:** Código + Arte (UI)

**Cómo probar:**
1. Derrotar al jefe final.
2. Verificar que aparece la pantalla de victoria.
3. Verificar que muestra el tiempo y el nombre.
4. Hacer clic en "Jugar de nuevo" y verificar que reinicia.

---

## PBI-17: Pantalla de derrota

**Como** jugador,
**quiero** ver una pantalla de derrota al morir,
**para** saber que perdí y poder reintentar.

**Criterio de aceptación:**
- Al llegar a 0 de vida, se muestra una pantalla de derrota.
- Se muestra el tiempo que tardó el jugador.
- Hay una opción para reintentar.
- Se muestra el nombre del jugador.

**Prioridad:** Media
**Story Points:** 2
**Área:** Código + Arte (UI)

**Cómo probar:**
1. Morir en el juego.
2. Verificar que aparece la pantalla de derrota.
3. Verificar que muestra el tiempo y el nombre.
4. Hacer clic en "Reintentar" y verificar que reinicia.

---

## PBI-18: Jefe adicional (caballero de armadura negra) — ALCANCE DESEABLE

**Como** jugador,
**quiero** enfrentar un jefe opcional más difícil,
**para** tener un desafío extra.

**Criterio de aceptación:**
- El jefe es un caballero de armadura negra.
- Ataca con proyectiles que cubren gran parte de la pantalla.
- Tiene más vida que el jefe final.
- Al derrotarlo, se obtiene una habilidad extra (ataques más fuertes).

**Prioridad:** Baja
**Story Points:** 13
**Área:** Código + Arte + GD

**Cómo probar:**
1. Encontrar al jefe adicional (zona oculta).
2. Observar que lanza proyectiles.
3. Derrotarlo y verificar que se obtiene la habilidad.
4. Verificar que los ataques son más fuertes.

---

## PBI-19: Enemigo extra — slime con espada — ALCANCE DESEABLE

**Como** jugador,
**quiero** encontrar un enemigo que ataque con espada,
**para** tener una variante más peligrosa.

**Criterio de aceptación:**
- El enemigo es un slime que ataca con espada.
- Tiene un rango de ataque mayor que el contacto normal.
- Recibe daño al ser golpeado.
- Muere tras suficientes golpes.

**Prioridad:** Baja
**Story Points:** 5
**Área:** Código + Arte + GD

**Cómo probar:**
1. Encontrar al enemigo con espada.
2. Observar que ataca con un golpe de espada.
3. Recibir daño y verificar el rango.
4. Derrotarlo y verificar que muere.

---

## PBI-20: Enemigo extra — slime con proyectiles — ALCANCE DESEABLE

**Como** jugador,
**quiero** encontrar un enemigo que lance proyectiles,
**para** tener un desafío a distancia.

**Criterio de aceptación:**
- El enemigo lanza proyectiles periódicamente.
- Los proyectiles causan daño al jugador.
- El enemigo recibe daño al ser golpeado.
- Muere tras suficientes golpes.

**Prioridad:** Baja
**Story Points:** 5
**Área:** Código + Arte + GD

**Cómo probar:**
1. Encontrar al enemigo con proyectiles.
2. Observar que lanza proyectiles.
3. Recibir daño de un proyectil.
4. Derrotarlo y verificar que muere.

---

## Resumen

| PBI | Descripción | SP | Prioridad |
|-----|-------------|-----|-----------|
| 01 | Movimiento lateral | 3 | Alta |
| 02 | Salto | 3 | Alta |
| 03 | Colisiones con escenario | 5 | Alta |
| 04 | Ataque con espada | 3 | Alta |
| 05 | Barra de vida | 3 | Alta |
| 06 | Enemigo patrulla | 5 | Alta |
| 07 | Enemigo salto | 5 | Alta |
| 08 | Mapa ciudad inicial | 8 | Alta |
| 09 | Mapa bosque | 8 | Alta |
| 10 | Mapa mazmorra final | 8 | Alta |
| 11 | Doble salto | 3 | Alta |
| 12 | Dash | 3 | Alta |
| 13 | Jefe final | 8 | Alta |
| 14 | Sistema de reinicio | 5 | Alta |
| 15 | SQLite — registro | 5 | Media |
| 16 | Pantalla victoria | 2 | Media |
| 17 | Pantalla derrota | 2 | Media |
| 18 | Jefe adicional (deseable) | 13 | Baja |
| 19 | Enemigo espada (deseable) | 5 | Baja |
| 20 | Enemigo proyectiles (deseable) | 5 | Baja |
| | **Total** | **107** | |
