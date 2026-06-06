package cr.ac.una.kingdomfantasy.model;

import java.util.Objects;

public final class Vector2D {

    public static final Vector2D ZERO = new Vector2D(0, 0);

    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double distanceTo(Vector2D other) {
        return subtract(other).length();
    }

    public Vector2D normalize() {
        double length = length();
        if (length == 0) {
            return ZERO;
        }
        return new Vector2D(x / length, y / length);
    }

    public Vector2D clampLength(double maxLength) {
        double length = length();
        if (length <= maxLength || length == 0) {
            return this;
        }
        return normalize().multiply(maxLength);
    }

    public static Vector2D fromPoints(double startX, double startY, double endX, double endY) {
        return new Vector2D(endX - startX, endY - startY);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vector2D)) {
            return false;
        }
        Vector2D vector2D = (Vector2D) object;
        return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2D{x=" + x + ", y=" + y + '}';
    }
}
