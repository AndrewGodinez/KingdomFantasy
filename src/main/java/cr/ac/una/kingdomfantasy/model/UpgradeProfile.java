package cr.ac.una.kingdomfantasy.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UpgradeProfile {

    public static final int MAX_LEVEL = 10;
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

    public int getUpgradeCost(UpgradeType type) {
        int level = getLevel(type);
        if (type == UpgradeType.CROSSBOW_DAMAGE || type == UpgradeType.CROSSBOW_SPEED) {
            double base = type == UpgradeType.CROSSBOW_DAMAGE ? 18 : 16;
            double linear = (level - 1) * (type == UpgradeType.CROSSBOW_DAMAGE ? 10.0 : 9.0);
            double curve = Math.pow(level - 1, 1.65) * (type == UpgradeType.CROSSBOW_DAMAGE ? 3.2 : 3.0);
            return (int) Math.round(base + linear + curve);
        }
        int baseCost;
        switch (type) {
            case CROSSBOW_DAMAGE:
                baseCost = 9;
                break;
            case CROSSBOW_SPEED:
                baseCost = 8;
                break;
            case METEOR_DAMAGE:
            case METEOR_RADIUS:
            case ICE_DURATION:
            case ICE_RADIUS:
                baseCost = 12;
                break;
            case CASTLE_HEALTH:
                baseCost = 10;
                break;
            case ELIXIR_CAPACITY:
                baseCost = 8;
                break;
            default:
                baseCost = 10;
                break;
        }
        return baseCost + (level - 1) * 5;
    }

    public double getCastleHealthBonus() {
        return 1500.0 * (Math.pow(1.10, getLevel(UpgradeType.CASTLE_HEALTH) - 1) - 1);
    }

    public double getElixirCapacityBonus() {
        return (getLevel(UpgradeType.ELIXIR_CAPACITY) - 1) * 12;
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
