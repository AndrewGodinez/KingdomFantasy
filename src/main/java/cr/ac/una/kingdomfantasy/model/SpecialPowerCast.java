package cr.ac.una.kingdomfantasy.model;

public class SpecialPowerCast {

    private final SpecialPowerType type;
    private final int affectedMonsters;
    private final double totalDamage;
    private final double freezeSeconds;

    public SpecialPowerCast(SpecialPowerType type, int affectedMonsters, double totalDamage, double freezeSeconds) {
        this.type = type;
        this.affectedMonsters = affectedMonsters;
        this.totalDamage = totalDamage;
        this.freezeSeconds = freezeSeconds;
    }

    public SpecialPowerType getType() {
        return type;
    }

    public int getAffectedMonsters() {
        return affectedMonsters;
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    public double getFreezeSeconds() {
        return freezeSeconds;
    }
}
