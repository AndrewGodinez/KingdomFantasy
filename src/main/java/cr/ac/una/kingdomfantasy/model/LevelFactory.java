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
        int totalMonsters = 7 + safeLevel / 3;
        int perType = totalMonsters / availableTypes.size();
        int remainder = totalMonsters % availableTypes.size();
        for (int i = 0; i < availableTypes.size(); i++) {
            int count = Math.max(1, perType + (i < remainder ? 1 : 0));
            waves.add(new WaveDefinition(availableTypes.get(i), count, 0, 0));
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
}
