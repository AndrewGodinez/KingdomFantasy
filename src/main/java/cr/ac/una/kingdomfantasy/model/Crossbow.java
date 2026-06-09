package cr.ac.una.kingdomfantasy.model;

public class Crossbow {

    public static final int MAX_LEVEL = 20;
    private static final double MIN_DAMAGE = 18;
    private static final double MAX_DAMAGE = 72;
    private static final double MAX_COOLDOWN = 0.72;
    private static final double MIN_COOLDOWN = 0.34;
    private static final double MIN_PROJECTILE_SPEED = 350;
    private static final double MAX_PROJECTILE_SPEED = 564;
    private static final double MIN_KNOCKBACK = 26;
    private static final double MAX_KNOCKBACK = 47.6;
    private CrossbowDesign design;
    private int damageLevel;
    private int speedLevel;
    private double cooldownRemaining;

    public Crossbow() {
        this(CrossbowDesign.GREEN, 1, 1);
    }

    public Crossbow(CrossbowDesign design, int level) {
        this(design, level, level);
    }

    public Crossbow(CrossbowDesign design, int damageLevel, int speedLevel) {
        this.design = design == null ? CrossbowDesign.GREEN : design;
        this.damageLevel = clampLevel(damageLevel);
        this.speedLevel = clampLevel(speedLevel);
    }

    public void update(double deltaSeconds) {
        cooldownRemaining = Math.max(0, cooldownRemaining - Math.max(0, deltaSeconds));
    }

    public Projectile fire(Vector2D start, Vector2D target) {
        if (!canFire() || start == null || target == null) {
            return null;
        }
        Vector2D direction = target.subtract(start).normalize();
        if (direction.length() == 0) {
            return null;
        }
        cooldownRemaining = getCooldown();
        Vector2D vel = direction.multiply(getProjectileSpeed());
        return new Projectile(
                ProjectileOwner.CROSSBOW,
                start.getX(),
                start.getY(),
                220,
                80,
                vel,
                getDamage(),
                1600,
                getKnockbackForce());
    }

    public boolean canFire() {
        return cooldownRemaining <= 0;
    }

    public void upgrade() {
        damageLevel = Math.min(MAX_LEVEL, damageLevel + 1);
    }

    public double getDamage() {
        return interpolate(MIN_DAMAGE, MAX_DAMAGE, damageLevel);
    }

    public double getCooldown() {
        return interpolate(MAX_COOLDOWN, MIN_COOLDOWN, speedLevel);
    }

    public double getProjectileSpeed() {
        return interpolate(MIN_PROJECTILE_SPEED, MAX_PROJECTILE_SPEED, speedLevel);
    }

    public double getKnockbackForce() {
        return interpolate(MIN_KNOCKBACK, MAX_KNOCKBACK, damageLevel);
    }

    public CrossbowDesign getDesign() {
        return design;
    }

    public void setDesign(CrossbowDesign design) {
        this.design = design == null ? this.design : design;
    }

    public int getLevel() {
        return Math.max(damageLevel, speedLevel);
    }

    public void setLevel(int level) {
        this.damageLevel = clampLevel(level);
        this.speedLevel = clampLevel(level);
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public void setDamageLevel(int damageLevel) {
        this.damageLevel = clampLevel(damageLevel);
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = clampLevel(speedLevel);
    }

    public double getCooldownRemaining() {
        return cooldownRemaining;
    }

    private int clampLevel(int value) {
        return Math.max(1, Math.min(MAX_LEVEL, value));
    }

    private double interpolate(double min, double max, int level) {
        if (MAX_LEVEL <= 1) {
            return max;
        }
        double progress = (clampLevel(level) - 1) / (double) (MAX_LEVEL - 1);
        return min + (max - min) * progress;
    }
}
