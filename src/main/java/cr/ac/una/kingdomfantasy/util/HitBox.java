package cr.ac.una.kingdomfantasy.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * ═══════════════════════════════════════════════════════════════════
 *  HitBox — Sistema de detección de colisiones para entidades del juego
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTO:
 *   El HitBox es un rectángulo invisible más pequeño que el sprite visible.
 *   Esto es fundamental porque los sprite sheets siempre tienen espacios
 *   vacíos alrededor del personaje. Si usáramos el tamaño del ImageView
 *   directamente como hitbox, detectaríamos colisiones en el aire.
 *
 *   Sprite visual (64×64):   HitBox real (30×50):
 *   ┌──────────────────┐      ┌──────────────────┐
 *   │  espacio vacío   │      │                  │
 *   │   ┌────────┐     │      │    ┌──────┐      │
 *   │   │        │     │  →   │    │ HIT  │      │
 *   │   │ sprite │     │      │    │ BOX  │      │
 *   │   │        │     │      │    └──────┘      │
 *   │   └────────┘     │      │                  │
 *   └──────────────────┘      └──────────────────┘
 *
 * POSICIONAMIENTO:
 *   El HitBox tiene un offset relativo al origen del sprite (posX, posY).
 *   Al mover la entidad, solo cambias posX/posY y el hitbox se actualiza solo.
 *
 * TIPOS DE COLISIÓN que soporta:
 *   1. AABB (Axis-Aligned Bounding Box) — Rectángulo vs Rectángulo
 *      Rápido, ideal para monstruos, castillo, héroe
 *   2. Círculo vs AABB — Para poderes especiales (meteoro, hielo)
 *      El radio del poder se compara contra el hitbox del monstruo
 *   3. Punto vs AABB — Para clicks del mouse (seleccionar héroe, etc.)
 *
 * USO:
 * <pre>
 *   // Monstruo con sprite de 64x64 pero hitbox de 40x50 centrado
 *   HitBox hb = new HitBox(
 *       entityPosX, entityPosY,   // posición actual de la entidad
 *       40, 50,                    // tamaño del hitbox
 *       12, 7                      // offset: 12px desde la izquierda, 7px desde arriba
 *   );
 *
 *   // Mover la entidad (el hitbox se actualiza automáticamente)
 *   hb.updatePosition(newX, newY);
 *
 *   // Detectar si colisiona con otro hitbox
 *   if (hb.intersects(otraEntidad.getHitBox())) { ... }
 *
 *   // Detectar si un click del mouse cayó dentro
 *   if (hb.containsPoint(mouseX, mouseY)) { ... }
 * </pre>
 */
public class HitBox {

    // Posición actual de la ENTIDAD (esquina superior izquierda del sprite)
    private double entityX;
    private double entityY;

    // Tamaño del hitbox en píxeles
    private double width;
    private double height;

    // Offset respecto al origen del sprite (para centrar el hitbox correctamente)
    private double offsetX;
    private double offsetY;

    // Rectángulo calculado (la posición real del hitbox en el mundo)
    private Rectangle2D bounds;

    // Para debug visual: mostrar el hitbox en pantalla
    private boolean debugVisible = false;

    /**
     * Constructor completo.
     *
     * @param entityX  posición X actual de la entidad (origen del sprite)
     * @param entityY  posición Y actual de la entidad (origen del sprite)
     * @param width    ancho del hitbox en píxeles
     * @param height   alto del hitbox en píxeles
     * @param offsetX  desplazamiento horizontal desde el origen del sprite
     * @param offsetY  desplazamiento vertical desde el origen del sprite
     */
    public HitBox(double entityX, double entityY,
                  double width, double height,
                  double offsetX, double offsetY) {
        this.entityX = entityX;
        this.entityY = entityY;
        this.width   = width;
        this.height  = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    /**
     * Constructor simplificado — hitbox del mismo tamaño que el sprite, sin offset.
     *
     * @param entityX posición X
     * @param entityY posición Y
     * @param width   ancho del hitbox
     * @param height  alto del hitbox
     */
    public HitBox(double entityX, double entityY, double width, double height) {
        this(entityX, entityY, width, height, 0, 0);
    }

    /**
     * Actualiza la posición del hitbox cuando la entidad se mueve.
     * LLAMAR EN CADA FRAME desde el método move() de la entidad.
     *
     * @param newEntityX nueva posición X de la entidad
     * @param newEntityY nueva posición Y de la entidad
     */
    public void updatePosition(double newEntityX, double newEntityY) {
        this.entityX = newEntityX;
        this.entityY = newEntityY;
        recalculate();
    }

    public void moveBy(double deltaX, double deltaY) {
        updatePosition(entityX + deltaX, entityY + deltaY);
    }

    public void setGeometry(double width, double height, double offsetX, double offsetY) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    public void setSize(double width, double height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
        recalculate();
    }

    public void setOffset(double offsetX, double offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        recalculate();
    }

    public HitBox copy() {
        HitBox copy = new HitBox(entityX, entityY, width, height, offsetX, offsetY);
        copy.setDebugVisible(debugVisible);
        return copy;
    }

    public HitBox translated(double deltaX, double deltaY) {
        HitBox copy = copy();
        copy.moveBy(deltaX, deltaY);
        return copy;
    }

    public Rectangle2D expanded(double amount) {
        return new Rectangle2D(
                bounds.getMinX() - amount,
                bounds.getMinY() - amount,
                bounds.getWidth() + amount * 2,
                bounds.getHeight() + amount * 2);
    }

    /**
     * Recalcula el Rectangle2D interno según la posición y offset actuales.
     * Privado — se llama automáticamente.
     */
    private void recalculate() {
        double x = entityX + offsetX;
        double y = entityY + offsetY;
        bounds   = new Rectangle2D(x, y, width, height);
    }

    // ═══ DETECCIÓN DE COLISIONES ═══

    /**
     * Detecta colisión AABB (rectángulo vs rectángulo).
     * Es el método principal para flechas vs monstruos, monstruos vs castillo,
     * monstruos vs héroe.
     *
     * @param other el HitBox de la otra entidad
     * @return true si los dos rectángulos se superponen
     */
    public boolean intersects(HitBox other) {
        if (other == null) return false;
        return bounds.intersects(other.bounds);
    }

    /**
     * Detecta colisión entre este hitbox y un Rectangle2D.
     *
     * @param rect el rectángulo a comparar
     * @return true si se superponen
     */
    public boolean intersects(Rectangle2D rect) {
        if (rect == null) return false;
        return bounds.intersects(rect);
    }

    public boolean intersectsExpanded(HitBox other, double padding) {
        if (other == null) return false;
        return expanded(padding).intersects(other.bounds);
    }

    public double overlapX(HitBox other) {
        if (!intersects(other)) return 0;
        return Math.min(getMaxX(), other.getMaxX()) - Math.max(getX(), other.getX());
    }

    public double overlapY(HitBox other) {
        if (!intersects(other)) return 0;
        return Math.min(getMaxY(), other.getMaxY()) - Math.max(getY(), other.getY());
    }

    /**
     * Detecta colisión CIRCULAR (radio vs rectángulo).
     * Usada para poderes especiales (meteoro, hielo) que tienen un radio de efecto.
     *
     * Algoritmo: calcula el punto del rectángulo más cercano al centro del círculo,
     * luego verifica si esa distancia es menor que el radio.
     *
     * @param circleX  posición X del centro del círculo (donde cayó el poder)
     * @param circleY  posición Y del centro del círculo
     * @param radius   radio del efecto del poder
     * @return true si el hitbox está dentro del radio del poder
     */
    public boolean intersectsCircle(double circleX, double circleY, double radius) {
        // Punto del rectángulo más cercano al centro del círculo
        double nearestX = clamp(circleX, bounds.getMinX(), bounds.getMaxX());
        double nearestY = clamp(circleY, bounds.getMinY(), bounds.getMaxY());

        // Distancia entre ese punto y el centro del círculo
        double dx = circleX - nearestX;
        double dy = circleY - nearestY;
        double distanceSquared = dx * dx + dy * dy;

        return distanceSquared <= (radius * radius);
    }

    /**
     * Detecta si un punto (ej: click del mouse) está dentro del hitbox.
     * Usado para: seleccionar al héroe, clickear el castillo, etc.
     *
     * @param pointX coordenada X del punto (ej: mouseX)
     * @param pointY coordenada Y del punto (ej: mouseY)
     * @return true si el punto está dentro del hitbox
     */
    public boolean containsPoint(double pointX, double pointY) {
        return bounds.contains(pointX, pointY);
    }

    /**
     * Calcula la distancia entre el centro de este hitbox y el centro de otro.
     * Útil para lógica de rango de ataque del héroe y monstruos ranged.
     *
     * @param other el otro HitBox
     * @return distancia en píxeles entre los dos centros
     */
    public double distanceTo(HitBox other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcula la distancia desde el centro de este hitbox a un punto.
     *
     * @param px coordenada X del punto
     * @param py coordenada Y del punto
     * @return distancia en píxeles
     */
    public double distanceToPoint(double px, double py) {
        double dx = getCenterX() - px;
        double dy = getCenterY() - py;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // ═══ DEBUG VISUAL ═══

    /**
     * Dibuja el contorno del hitbox en el canvas para depuración.
     * Desactivar en producción con setDebugVisible(false).
     *
     * @param gc     GraphicsContext del canvas del juego
     * @param color  color del contorno (ej: Color.RED para monstruos,
     *               Color.GREEN para flechas, Color.CYAN para el héroe)
     */
    public void debugDraw(GraphicsContext gc, Color color) {
        if (!debugVisible) return;
        gc.setStroke(color);
        gc.setLineWidth(1.5);
        gc.strokeRect(bounds.getMinX(), bounds.getMinY(), width, height);
        // Marcar el centro con un punto
        gc.setFill(color);
        gc.fillOval(getCenterX() - 2, getCenterY() - 2, 4, 4);
    }

    // ═══ GETTERS ═══

    /** @return el Rectangle2D con la posición y tamaño reales del hitbox */
    public Rectangle2D getBounds() { return bounds; }

    /** @return posición X real del hitbox en el mundo (incluyendo offset) */
    public double getX()       { return bounds.getMinX(); }

    /** @return posición Y real del hitbox en el mundo (incluyendo offset) */
    public double getY()       { return bounds.getMinY(); }

    /** @return borde derecho del hitbox */
    public double getMaxX()    { return bounds.getMaxX(); }

    /** @return borde inferior del hitbox */
    public double getMaxY()    { return bounds.getMaxY(); }

    /** @return centro horizontal del hitbox */
    public double getCenterX() { return bounds.getMinX() + width  / 2.0; }

    /** @return centro vertical del hitbox */
    public double getCenterY() { return bounds.getMinY() + height / 2.0; }

    public double getWidth()   { return width; }
    public double getHeight()  { return height; }
    public double getOffsetX() { return offsetX; }
    public double getOffsetY() { return offsetY; }
    public double getEntityX() { return entityX; }
    public double getEntityY() { return entityY; }

    public boolean isDebugVisible() { return debugVisible; }
    public void setDebugVisible(boolean debugVisible) { this.debugVisible = debugVisible; }

    // ═══ HELPERS INTERNOS ═══

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public String toString() {
        return String.format("HitBox[x=%.1f, y=%.1f, w=%.1f, h=%.1f]",
            bounds.getMinX(), bounds.getMinY(), width, height);
    }
}
