package cr.ac.una.kingdomfantasy.model;

import java.util.List;

public class SpecialPower {

    public static final int MAX_LEVEL = 20;

    private final SpecialPowerType type;
    private int primaryLevel;
    private int radiusLevel;
    private double cooldownRemaining;

    public SpecialPower(SpecialPowerType type) {
        this(type, 1, 1);
    }

    public SpecialPower(SpecialPowerType type, int level) {
        this(type, level, level);
    }

    public SpecialPower(SpecialPowerType type, int primaryLevel, int radiusLevel) {
        this.type = type == null ? SpecialPowerType.METEOR : type;
        this.primaryLevel = clampLevel(primaryLevel);
        this.radiusLevel = clampLevel(radiusLevel);
    }

    public void update(double deltaSeconds) {
        cooldownRemaining = Math.max(0, cooldownRemaining - Math.max(0, deltaSeconds));
    }

    public boolean canUse(double availableMana, double maxMana) {
        return cooldownRemaining <= 0 && availableMana >= getManaCost(maxMana);
    }

    public SpecialPowerCast castAt(double x, double y, List<Monster> monsters) {
        beginCast();
        return applyAt(x, y, monsters);
    }

    public void beginCast() {
        cooldownRemaining = getCooldown();
    }

    public SpecialPowerCast applyAt(double x, double y, List<Monster> monsters) {
        int affected = 0;
        double totalDamage = 0;
        double freeze = getFreezeSeconds();
        if (monsters == null) {
            return new SpecialPowerCast(type, 0, 0, freeze);
        }
        for (Monster monster : monsters) {
            if (monster == null || monster.isDead()
                    || !monster.getHitBox().intersectsCircle(x, y, getRadius())) {
                continue;
            }
            affected++;
            if (getDamage() > 0) {
                double damage = getDamage();
                if (damage > 0) {
                    monster.takeDamage(damage);
                    totalDamage += damage;
                }
            }
            if (freeze > 0) {
                monster.freeze(freeze);
            }
        }
        return new SpecialPowerCast(type, affected, totalDamage, freeze);
    }

    public void upgrade() {
        primaryLevel = Math.min(MAX_LEVEL, primaryLevel + 1);
    }

    public double getDamage() {
        if (type.getBaseDamage() <= 0) {
            return 0;
        }
        return type.getBaseDamage() * (1.0 + (primaryLevel - 1) * 0.10);
    }

    public double getRadius() {
        return type.getBaseRadius() + (radiusLevel - 1) * 4;
    }

    public double getFreezeSeconds() {
        return type.getBaseFreezeSeconds() <= 0 ? 0 : type.getBaseFreezeSeconds() + (primaryLevel - 1) * 0.24;
    }

    public double getManaCost(double maxMana) {
        return Math.max(1, maxMana * 0.25);
    }

    public double getCooldown() {
        return type.getBaseCooldown();
    }

    public SpecialPowerType getType() {
        return type;
    }

    public int getLevel() {
        return primaryLevel;
    }

    public void setLevel(int level) {
        this.primaryLevel = clampLevel(level);
    }

    public int getPrimaryLevel() {
        return primaryLevel;
    }

    public void setPrimaryLevel(int primaryLevel) {
        this.primaryLevel = clampLevel(primaryLevel);
    }

    public int getRadiusLevel() {
        return radiusLevel;
    }

    public void setRadiusLevel(int radiusLevel) {
        this.radiusLevel = clampLevel(radiusLevel);
    }

    public double getCooldownRemaining() {
        return cooldownRemaining;
    }

    private int clampLevel(int value) {
        return Math.max(1, Math.min(MAX_LEVEL, value));
    }
}
