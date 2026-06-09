package cr.ac.una.kingdomfantasy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelDefinition {

    private final int number;
    private final List<WaveDefinition> waves;
    private final double castleHealth;
    private final double maxElixir;
    private final double elixirRegenPerSecond;

    public LevelDefinition(int number, List<WaveDefinition> waves, double castleHealth, double maxElixir, double elixirRegenPerSecond) {
        this.number = Math.max(1, Math.min(100, number));
        List<WaveDefinition> safeWaves = waves == null ? Collections.emptyList() : waves;
        this.waves = Collections.unmodifiableList(new ArrayList<>(safeWaves));
        this.castleHealth = Math.max(1, castleHealth);
        this.maxElixir = Math.max(1, maxElixir);
        this.elixirRegenPerSecond = Math.max(0, elixirRegenPerSecond);
    }

    public int getNumber() {
        return number;
    }

    public List<WaveDefinition> getWaves() {
        return waves;
    }

    public double getCastleHealth() {
        return castleHealth;
    }

    public double getMaxElixir() {
        return maxElixir;
    }

    public double getElixirRegenPerSecond() {
        return elixirRegenPerSecond;
    }
}
