# Sprint Backlog — Sprint 1

**Duración:** 2 semanas
**Fecha de inicio:** 20/07/2026
**Fecha de fin:** 03/08/2026
**Objetivo del sprint:** Implementar el movimiento, salto, colisiones y primer enemigo del personaje jugable.

**PBIs incluidos:**
- PBI-01: Movimiento lateral del personaje (3 SP)
- PBI-02: Salto del personaje (3 SP)
- PBI-03: Colisiones con el escenario (5 SP)
- PBI-06: Enemigo patrulla (5 SP)

**Total del sprint:** 16 SP

---

## Tareas por área

### Código

| # | Tarea | PBI | Horas est. | Estado |
|---|-------|-----|------------|--------|
| C1 | Crear clase `Player` que extienda `Sprite` con posición, velocidad y dirección | 01 | 2h | Pendiente |
| C2 | Implementar detección de teclado (A/D) en `GameScreen` | 01 | 1h | Pendiente |
| C3 | Mover al personaje horizontalmente según input del teclado | 01 | 2h | Pendiente |
| C4 | Voltear sprite al cambiar de dirección | 01 | 1h | Pendiente |
| C5 | Crear clase `PlayerController` para separar input de lógica | 01-02 | 1h | Pendiente |
| C6 | Implementar salto con barra espaciadora (velocidad vertical + gravedad) | 02 | 3h | Pendiente |
| C7 | Crear clase `World` o `GameMap` para manejar tilesets y colisiones | 03 | 3h | Pendiente |
| C8 | Implementar colisiones del personaje con el suelo (Tiled collision layers) | 03 | 3h | Pendiente |
| C9 | Implementar colisiones laterales (paredes) | 03 | 2h | Pendiente |
| C10 | Crear clase `Enemy` con posición, velocidad y IA básica | 06 | 3h | Pendiente |
| C11 | Implementar IA de patrulla (caminar de ida y vuelta en rango definido) | 06 | 2h | Pendiente |
| C12 | Implementar colisiones entre personaje y enemigo (daño por contacto) | 06 | 2h | Pendiente |
| C13 | Crear clase `HealthSystem` para manejar vida del jugador | 06 | 2h | Pendiente |
| C14 | Reducir vida al contacto con enemigo | 06 | 1h | Pendiente |
| C15 | Agregar cooldown de invulnerabilidad tras recibir daño (1-2 segundos) | 06 | 1h | Pendiente |
| | **Subtotal Código** | | **30h** | |

### Game Design (GD)

| # | Tarea | PBI | Horas est. | Estado |
|---|-------|-----|------------|--------|
| GD1 | Definir velocidad de movimiento del personaje (px/frame) | 01 | 1h | Pendiente |
| GD2 | Definir fuerza de salto y valor de gravedad | 02 | 1h | Pendiente |
| GD3 | Definir tamaño del hitbox del personaje | 01-03 | 1h | Pendiente |
| GD4 | Diseñar mapa de prueba para testear movimiento y salto | 01-03 | 3h | Pendiente |
| GD5 | Definir vida del personaje (cantidad de golpes que resiste) | 06 | 0.5h | Pendiente |
| GD6 | Definir daño que causa el enemigo por contacto | 06 | 0.5h | Pendiente |
| GD7 | Definir velocidad de patrulla del enemigo | 06 | 0.5h | Pendiente |
| GD8 | Definir rango de patrulla del enemigo (distancia de ida y vuelta) | 06 | 0.5h | Pendiente |
| GD9 | Definir vida del enemigo (cuántos golpes resiste) | 06 | 0.5h | Pendiente |
| GD10 | Crear documento de métricas de gameplay | 01-06 | 1h | Pendiente |
| | **Subtotal GD** | | **9.5h** | |

### Arte

| # | Tarea | PBI | Horas est. | Estado |
|---|-------|-----|------------|--------|
| A1 | Crear sprite del personaje (idle) en Aseprite | 01 | 3h | Pendiente |
| A2 | Crear animación de caminar (walk cycle) del personaje | 01 | 3h | Pendiente |
| A3 | Crear animación de salto del personaje | 02 | 2h | Pendiente |
| A4 | Crear sprite del enemigo slime azul (idle) | 06 | 2h | Pendiente |
| A5 | Crear animación de caminar del slime | 06 | 2h | Pendiente |
| A6 | Crear tileset de prueba para el mapa de testing | 03 | 3h | Pendiente |
| A7 | Definir paleta de colores del proyecto | Todos | 1h | Pendiente |
| | **Subtotal Arte** | | **16h** | |

---

## Mapa de prueba del Sprint 1

El mapa de prueba debe tener:
- Suelo sólido (varios tilesets para probar colisiones)
- 2-3 plataformas a diferentes alturas
- 1-2 paredes laterales
- 2-3 enemigos patrulla distribuidos
- Espacio suficiente para probar movimiento, salto y combate

**Herramienta:** Tiled
**Formato:** `.tmx` con layer de colisiones separado

---

## Criterio de aceptación del Sprint 1

El sprint se considera completado cuando:

1. El personaje se mueve horizontalmente con A/D.
2. El personaje salta con Espacio y cae por gravedad.
3. El personaje no atraviesa suelo, paredes ni plataformas.
4. El enemigo patrulla se mueve de ida y vuelta.
5. El enemigo recibe daño al ser golpeado y muere tras N golpes.
6. El personaje recibe daño al tocar al enemigo.
7. La barra de vida se reduce al recibir daño.
8. Todo funciona en el mapa de prueba sin bugs críticos.

---

## Riesgos identificados

| Riesgo | Impacto | Mitigación |
|--------|---------|------------|
| Las colisiones de Tiled no funcionan correctamente | Alto | Investigar la API de Tiled de LibGDX antes de empezar |
| Las animaciones no se ven bien | Medio | Usar sprites simples primero, pulir después |
| El salto se siente "raro" | Medio | Ajustar valores de gravedad y fuerza iterativamente |
| El enemigo patrulla se queda pegado | Bajo | Implementar detección de bordes de plataforma |

---

## Notas

- Las horas son estimaciones aproximadas (1 SP ≈ 1h de trabajo efectivo).
- El arte puede hacerse en paralelo con el código.
- El GD puede hacerse en paralelo con todo.
- Al final del sprint, hacer una demo funcional con el mapa de prueba.
