package cr.ac.una.kingdomfantasy.model;

public enum SpecialPowerType {
    METEOR(90, 96, 0, 5.0),
    ICE(0, 112, 3.0, 5.0);

    private final double baseDamage;
    private final double baseRadius;
    private final double baseFreezeSeconds;
    private final double baseCooldown;

    SpecialPowerType(double baseDamage, double baseRadius, double baseFreezeSeconds, double baseCooldown) {
        this.baseDamage = baseDamage;
        this.baseRadius = baseRadius;
        this.baseFreezeSeconds = baseFreezeSeconds;
        this.baseCooldown = baseCooldown;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getBaseRadius() {
        return baseRadius;
    }

    public double getBaseFreezeSeconds() {
        return baseFreezeSeconds;
    }

    public double getBaseCooldown() {
        return baseCooldown;
    }
}
