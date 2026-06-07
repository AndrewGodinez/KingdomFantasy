package cr.ac.una.kingdomfantasy.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UpgradeProfile {

    public static final int MAX_LEVEL = 20;
    public static final int CROSSBOW_MAX_LEVEL = Crossbow.MAX_LEVEL;

    private final EnumMap<UpgradeType, Integer> levels = new EnumMap<>(UpgradeType.class);

    public UpgradeProfile() {
        for (UpgradeType type : UpgradeType.values()) {
            levels.put(type, 1);
        }
    }

    public int getLevel(UpgradeType type) {
        return levels.getOrDefault(type, 1);
    }

    public boolean canUpgrade(UpgradeType type) {
        return getLevel(type) < getMaxLevel(type);
    }

    public boolean upgrade(UpgradeType type, int availablePoints) {
        if (type == null || !canUpgrade(type) || availablePoints < getUpgradeCost(type)) {
            return false;
        }
        levels.put(type, getLevel(type) + 1);
        return true;
    }

    private static final double COST_EXPONENT = 1.7;

    public int getUpgradeCost(UpgradeType type) {
        int level = getLevel(type);
        double base;
        double k;
        switch (type) {
            case CROSSBOW_DAMAGE:
                base = 18; k = 2.62; break;
            case CROSSBOW_SPEED:
                base = 16; k = 2.35; break;
            case CASTLE_HEALTH:
                base = 16; k = 2.25; break;
            case ELIXIR_CAPACITY:
                base = 12; k = 1.51; break;
            case METEOR_DAMAGE:
                base = 12; k = 1.56; break;
            case METEOR_RADIUS:
                base = 11; k = 1.37; break;
            case ICE_DURATION:
                base = 12; k = 1.56; break;
            case ICE_RADIUS:
                base = 11; k = 1.37; break;
            default:
                base = 12; k = 1.50; break;
        }
        return (int) Math.round(base + k * Math.pow(level - 1, COST_EXPONENT));
    }

    public double getCastleHealthBonus() {
        return 1500.0 + (getLevel(UpgradeType.ELIXIR_CAPACITY) - 1) * 500;
    }

    public double getElixirCapacityBonus() {
        return (getLevel(UpgradeType.ELIXIR_CAPACITY) - 1) * 10;
    }

    public Crossbow createCrossbow(CrossbowDesign design) {
        return new Crossbow(design, getLevel(UpgradeType.CROSSBOW_DAMAGE), getLevel(UpgradeType.CROSSBOW_SPEED));
    }

    public SpecialPower createPower(SpecialPowerType type) {
        if (type == SpecialPowerType.ICE) {
            return new SpecialPower(type, getLevel(UpgradeType.ICE_DURATION), getLevel(UpgradeType.ICE_RADIUS));
        }
        return new SpecialPower(type, getLevel(UpgradeType.METEOR_DAMAGE), getLevel(UpgradeType.METEOR_RADIUS));
    }

    public Map<UpgradeType, Integer> getLevels() {
        return Collections.unmodifiableMap(levels);
    }

    public void setLevel(UpgradeType type, int level) {
        if (type != null) {
            levels.put(type, Math.max(1, Math.min(getMaxLevel(type), level)));
        }
    }

    public int getMaxLevel(UpgradeType type) {
        if (type == UpgradeType.CROSSBOW_DAMAGE || type == UpgradeType.CROSSBOW_SPEED) {
            return CROSSBOW_MAX_LEVEL;
        }
        return MAX_LEVEL;
    }
}
