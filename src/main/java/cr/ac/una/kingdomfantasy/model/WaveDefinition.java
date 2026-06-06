package cr.ac.una.kingdomfantasy.model;

public class WaveDefinition {

    private final MonsterType monsterType;
    private final int count;
    private final double spawnInterval;
    private final double startDelay;

    public WaveDefinition(MonsterType monsterType, int count, double spawnInterval, double startDelay) {
        this.monsterType = monsterType;
        this.count = Math.max(1, count);
        this.spawnInterval = Math.max(0.2, spawnInterval);
        this.startDelay = Math.max(0, startDelay);
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public int getCount() {
        return count;
    }

    public double getSpawnInterval() {
        return spawnInterval;
    }

    public double getStartDelay() {
        return startDelay;
    }
}
