package cr.ac.una.kingdomfantasy.model;

import cr.ac.una.kingdomfantasy.util.HitBox;

public abstract class GameEntity {

    private double x;
    private double y;
    private double width;
    private double height;
    private Direction facing = Direction.LEFT;
    private EntityState state = EntityState.IDLE;
    private final HitBox hitBox;

    protected GameEntity(double x, double y, double width, double height, double hitBoxWidth, double hitBoxHeight, double hitBoxOffsetX, double hitBoxOffsetY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitBox = new HitBox(x, y, hitBoxWidth, hitBoxHeight, hitBoxOffsetX, hitBoxOffsetY);
    }

    public abstract void update(double deltaSeconds);

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        hitBox.updatePosition(x, y);
    }

    public void moveBy(Vector2D delta) {
        setPosition(x + delta.getX(), y + delta.getY());
        if (delta.length() > 0) {
            facing = Direction.fromDelta(delta.getX(), delta.getY(), facing);
        }
    }

    public Vector2D getPosition() {
        return new Vector2D(x, y);
    }

    public Vector2D getCenter() {
        return new Vector2D(getCenterX(), getCenterY());
    }

    public double distanceTo(GameEntity other) {
        return getCenter().distanceTo(other.getCenter());
    }

    public boolean collidesWith(GameEntity other) {
        return other != null && hitBox.intersects(other.hitBox);
    }

    public void configureHitBox(double width, double height, double offsetX, double offsetY) {
        hitBox.setGeometry(width, height, offsetX, offsetY);
        hitBox.updatePosition(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = Math.max(1, width);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = Math.max(1, height);
    }

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing == null ? this.facing : facing;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state == null ? EntityState.IDLE : state;
    }

    public HitBox getHitBox() {
        return hitBox;
    }
}
