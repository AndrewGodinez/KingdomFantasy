package cr.ac.una.kingdomfantasy.model;

import javafx.geometry.Rectangle2D;

public class Hero extends LivingEntity {

    public static final double SPRITE_WIDTH = 128;
    public static final double SPRITE_HEIGHT = 128;
    public static final double VISUAL_SCALE = 2.08;

    private final HeroLoadout loadout;

    public Hero(double x, double y) {
        this(x, y, HeroLoadout.defaultKnightNoHelmetNoBeard());
    }

    public Hero(double x, double y, HeroLoadout loadout) {
        super(x, y, SPRITE_WIDTH, SPRITE_HEIGHT,
                84, 96, 22, 22,
                new CombatStats(430, 1, 168, 42, 1.05, 0, 0, 0.25));
        this.loadout = loadout == null ? HeroLoadout.defaultKnightNoHelmetNoBeard() : loadout;
        setFacing(Direction.RIGHT);
    }

    @Override
    public void update(double deltaSeconds) {
        super.update(deltaSeconds);
        if (isAlive() && !isFrozen() && getState() == EntityState.HURT) {
            setState(EntityState.IDLE);
        }
    }

    public void move(Direction direction, double deltaSeconds) {
        if (direction == null || isDead() || isFrozen()) {
            return;
        }
        Vector2D delta = direction.asVector().multiply(getStats().getSpeed() * Math.max(0, deltaSeconds));
        moveBy(delta);
        setFacing(direction);
        setState(EntityState.MOVING);
    }

    public void stopMoving() {
        if (isAlive() && !isFrozen()) {
            setState(EntityState.IDLE);
        }
    }

    public boolean attack(Monster monster) {
        if (monster == null || !canAttack() || monster.isDead()) {
            return false;
        }
        setFacing(Direction.fromDelta(monster.getCenterX() - getCenterX(), monster.getCenterY() - getCenterY(), getFacing()));
        if (!getAttackArea().intersects(monster.getHitBox().getBounds())) {
            return false;
        }
        if (monster.getType() == MonsterType.CAT) {
            resetAttackCooldown();
            setState(EntityState.ATTACKING);
            return true;
        }
        Vector2D knockback = Vector2D
                .fromPoints(getCenterX(), getCenterY(), monster.getCenterX(), monster.getCenterY())
                .normalize()
                .multiply(18);
        monster.takeDamage(damageAgainst(monster), knockback);
        resetAttackCooldown();
        setState(EntityState.ATTACKING);
        return true;
    }

    private Rectangle2D getAttackArea() {
        Rectangle2D body = getHitBox().getBounds();
        double reach = getStats().getAttackRange();
        switch (getFacing()) {
            case LEFT:
                return new Rectangle2D(
                        body.getMinX() - reach,
                        body.getMinY() - 22,
                        reach + 18,
                        body.getHeight() + 44);
            case UP:
                return new Rectangle2D(
                        body.getMinX() - 28,
                        body.getMinY() - reach,
                        body.getWidth() + 56,
                        reach + 18);
            case DOWN:
                return new Rectangle2D(
                        body.getMinX() - 28,
                        body.getMaxY() - 18,
                        body.getWidth() + 56,
                        reach + 18);
            case RIGHT:
            default:
                return new Rectangle2D(
                        body.getMaxX() - 18,
                        body.getMinY() - 22,
                        reach + 18,
                        body.getHeight() + 44);
        }
    }

    private double damageAgainst(Monster monster) {
        switch (monster.getType()) {
            case BADGER:
                return monster.getStats().getMaxHealth() / 3.0;
            case GOLLUX:
                return monster.getStats().getMaxHealth() / 6.0;
            case PENGU:
            case DINO_REX:
            default:
                return monster.getStats().getMaxHealth() / 4.0;
        }
    }

    public HeroLoadout getLoadout() {
        return loadout;
    }
}
