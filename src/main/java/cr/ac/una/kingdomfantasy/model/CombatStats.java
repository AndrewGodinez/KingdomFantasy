package cr.ac.una.kingdomfantasy.model;

public class CombatStats {

    private double maxHealth;
    private double damage;
    private double speed;
    private double attackRange;
    private double attackCooldown;
    private double projectileSpeed;
    private int scoreValue;
    private double knockbackResistance;

    public CombatStats(double maxHealth, double damage, double speed, double attackRange,
            double attackCooldown, double projectileSpeed, int scoreValue, double knockbackResistance) {
        this.maxHealth = Math.max(1, maxHealth);
        this.damage = Math.max(0, damage);
        this.speed = Math.max(0, speed);
        this.attackRange = Math.max(0, attackRange);
        this.attackCooldown = Math.max(0.05, attackCooldown);
        this.projectileSpeed = Math.max(0, projectileSpeed);
        this.scoreValue = Math.max(0, scoreValue);
        this.knockbackResistance = clamp(knockbackResistance, 0, 0.95);
    }

    public CombatStats copy() {
        return new CombatStats(maxHealth, damage, speed, attackRange, attackCooldown,
                projectileSpeed, scoreValue, knockbackResistance);
    }

    public CombatStats scaled(double healthScale, double damageScale, double speedScale, double scoreScale) {
        return new CombatStats(
                maxHealth * healthScale,
                damage * damageScale,
                speed * speedScale,
                attackRange,
                attackCooldown,
                projectileSpeed,
                (int) Math.round(scoreValue * scoreScale),
                knockbackResistance);
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = Math.max(0, damage);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(0, speed);
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(double attackRange) {
        this.attackRange = Math.max(0, attackRange);
    }

    public double getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(double attackCooldown) {
        this.attackCooldown = Math.max(0.05, attackCooldown);
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(double projectileSpeed) {
        this.projectileSpeed = Math.max(0, projectileSpeed);
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = Math.max(0, scoreValue);
    }

    public double getKnockbackResistance() {
        return knockbackResistance;
    }

    public void setKnockbackResistance(double knockbackResistance) {
        this.knockbackResistance = clamp(knockbackResistance, 0, 0.95);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
