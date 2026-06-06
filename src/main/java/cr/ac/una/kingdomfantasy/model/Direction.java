package cr.ac.una.kingdomfantasy.model;

public enum Direction {
    DOWN(0, 0, 1),
    LEFT(1, -1, 0),
    RIGHT(2, 1, 0),
    UP(3, 0, -1);

    private final int spriteRow;
    private final double dx;
    private final double dy;

    Direction(int spriteRow, double dx, double dy) {
        this.spriteRow = spriteRow;
        this.dx = dx;
        this.dy = dy;
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public Vector2D asVector() {
        return new Vector2D(dx, dy);
    }

    public static Direction fromDelta(double dx, double dy, Direction fallback) {
        if (Math.abs(dx) >= Math.abs(dy)) {
            if (dx > 0) {
                return RIGHT;
            }
            if (dx < 0) {
                return LEFT;
            }
        } else {
            if (dy > 0) {
                return DOWN;
            }
            if (dy < 0) {
                return UP;
            }
        }
        return fallback == null ? DOWN : fallback;
    }
}
