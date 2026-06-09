package cr.ac.una.kingdomfantasy.model;

public abstract class LivingEntity extends GameEntity {

    private final CombatStats stats;
    private double health;
    private double attackCooldownRemaining;
    private double frozenRemaining;

    protected LivingEntity(double x, double y, double width, double height, double hitBoxWidth, double hitBoxHeight, double hitBoxOffsetX, double hitBoxOffsetY, CombatStats stats) {
        super(x, y, width, height, hitBoxWidth, hitBoxHeight, hitBoxOffsetX, hitBoxOffsetY);
        this.stats = stats.copy();
        this.health = stats.getMaxHealth();
    }

    @Override
    public void update(double deltaSeconds) {
        updateTimers(deltaSeconds);
    }

    protected void updateTimers(double deltaSeconds) {
        double delta = Math.max(0, deltaSeconds);
        attackCooldownRemaining = Math.max(0, attackCooldownRemaining - delta);
        frozenRemaining = Math.max(0, frozenRemaining - delta);
        if (isDead()) {
            setState(EntityState.DEAD);
        } else if (isFrozen()) {
            setState(EntityState.FROZEN);
        }
    }

    public void takeDamage(double amount) {
        takeDamage(amount, Vector2D.ZERO);
    }

    public void takeDamage(double amount, Vector2D knockback) {
        if (isDead() || amount <= 0) {
            return;
        }
        health = Math.max(0, health - amount);
        if (knockback != null && knockback.length() > 0) {
            moveBy(knockback.multiply(1.0 - stats.getKnockbackResistance()));
        }
        setState(health <= 0 ? EntityState.DEAD : EntityState.HURT);
    }

    public void heal(double amount) {
        if (amount <= 0 || isDead()) {
            return;
        }
        health = Math.min(stats.getMaxHealth(), health + amount);
    }

    public void freeze(double seconds) {
        if (!isDead()) {
            frozenRemaining = Math.max(frozenRemaining, seconds);
            setState(EntityState.FROZEN);
        }
    }

    public boolean canAttack() {
        return !isDead() && !isFrozen() && attackCooldownRemaining <= 0;
    }

    protected void resetAttackCooldown() {
        attackCooldownRemaining = stats.getAttackCooldown();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isAlive() {
        return !isDead();
    }

    public boolean isFrozen() {
        return frozenRemaining > 0;
    }

    public double getHealthPercent() {
        return health / stats.getMaxHealth();
    }

    public CombatStats getStats() {
        return stats;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = Math.max(0, Math.min(stats.getMaxHealth(), health));
        if (this.health == 0) {
            setState(EntityState.DEAD);
        }
    }

    public double getAttackCooldownRemaining() {
        return attackCooldownRemaining;
    }

    public double getFrozenRemaining() {
        return frozenRemaining;
    }
}
