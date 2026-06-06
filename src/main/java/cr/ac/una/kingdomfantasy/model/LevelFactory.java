package cr.ac.una.kingdomfantasy.model;

import java.util.ArrayList;
import java.util.List;

public final class LevelFactory {

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 100;

    private LevelFactory() {
    }

    public static LevelDefinition createLevel(int level) {
        int safeLevel = Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, level));
        List<MonsterType> availableTypes = MonsterType.availableForLevel(safeLevel);
        List<WaveDefinition> waves = new ArrayList<>();

        int totalMonsters = monsterCountForLevel(safeLevel, availableTypes.size());
        double totalWeight = 0;
        for (MonsterType type : availableTypes) {
            totalWeight += weightFor(type, safeLevel);
        }

        List<Integer> counts = new ArrayList<>();
        int assigned = 0;
        for (MonsterType type : availableTypes) {
            int count = Math.max(1, (int) Math.round(totalMonsters * weightFor(type, safeLevel) / totalWeight));
            if (type == MonsterType.CAT) {
                count = Math.min(count, maxCatsForLevel(safeLevel));
            }
            counts.add(count);
            assigned += count;
        }
        int rebalanceIndex = 0;
        while (assigned < totalMonsters) {
            MonsterType type = availableTypes.get(rebalanceIndex % availableTypes.size());
            if (type != MonsterType.CAT || counts.get(rebalanceIndex % counts.size()) < maxCatsForLevel(safeLevel)) {
                counts.set(rebalanceIndex % counts.size(), counts.get(rebalanceIndex % counts.size()) + 1);
                assigned++;
            }
            rebalanceIndex++;
        }
        rebalanceIndex = availableTypes.size() - 1;
        while (assigned > totalMonsters) {
            int index = Math.floorMod(rebalanceIndex, availableTypes.size());
            if (counts.get(index) > 1) {
                counts.set(index, counts.get(index) - 1);
                assigned--;
            }
            rebalanceIndex--;
        }

        for (int i = 0; i < availableTypes.size(); i++) {
            waves.add(new WaveDefinition(availableTypes.get(i), counts.get(i), 0, 0));
        }

        double castleHealth = 1500.0;
        double maxElixir = 100;
        double elixirRegen = 2.0;
        return new LevelDefinition(safeLevel, waves, castleHealth, maxElixir, elixirRegen);
    }

    public static List<LevelDefinition> createCampaign() {
        List<LevelDefinition> levels = new ArrayList<>();
        for (int level = 1; level <= 100; level++) {
            levels.add(createLevel(level));
        }
        return levels;
    }

    private static int monsterCountForLevel(int level, int availableTypeCount) {
        return 7 + level / 3 + Math.max(0, availableTypeCount - 1);
    }

    private static double weightFor(MonsterType type, int level) {
        switch (type) {
            case BADGER:
                return 0.95;
            case GOLLUX:
                return 0.62 + Math.min(0.20, Math.max(0, level - type.getIntroLevel()) * 0.01);
            case PENGU:
                return 0.68;
            case CAT:
                return 0.20;
            case DINO_REX:
            default:
                return 1.15;
        }
    }

    private static int maxCatsForLevel(int level) {
        if (level < MonsterType.CAT.getIntroLevel()) {
            return 0;
        }
        return Math.min(3, 1 + (level - MonsterType.CAT.getIntroLevel()) / 9);
    }
}
