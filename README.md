# Kingdom Fantasy

Juego de estilo *tower-defense* inspirado en [Defender II](https://play.google.com/store/apps/details?id=com.droidhen.defender2) con combate en tiempo real, desarrollado en **JavaFX 25** con persistencia **JPA / Oracle**.

Defiende tu castillo de oleadas de monstruos, mejora tu ballesta y poderes especiales, y escala hasta el nivel 100.

---

## Características

- **100 niveles generados proceduralmente** con dificultad progresiva y 5 tipos de monstruos
- **Sistema de progresión**: 8 categorías de mejora (ballesta, castillo, meteorito, hielo), cada una hasta nivel 20
- **Combate en tiempo real**: control WASD o ratón, ataque cuerpo a cuerpo y ballesta automática
- **Poderes especiales**: Meteorito (daño en área) y Hielo (congelamiento)
- **Persistencia con JPA / Oracle**: jugadores, partidas y mejoras guardadas en base de datos
- **Ranking global** de puntuaciones
- **Sprites animados** con soporte para accesorios del héroe
- **Audio** con música de fondo y efectos de sonido
- **Modo review**

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 25 |
| UI | JavaFX 25, MaterialFX |
| Persistencia | EclipseLink JPA, Oracle 21c |
| Build | Maven |
| Sprites | Animación por `AnimationTimer` con grid de sprites |

---

## Requisitos

- Java 25+
- Docker (para la base de datos Oracle)
- Maven 3.9+

## Ejecución

```bash
mvn clean javafx:run
```

## Debug

```bash
mvn clean javafx:run@debug
```

---

## Créditos

Desarrollado como proyecto del curso de Programación II en la Universidad Nacional de Costa Rica (UNA).

- [AndrewGodinez](https://github.com/AndrewGodinez)
- [AndresCortesVictor](https://github.com/AndresCortesVictor)
- [SemMora](https://github.com/SemMora)

---

## Licencia

MIT — ver [LICENSE](LICENSE) para más detalles.
