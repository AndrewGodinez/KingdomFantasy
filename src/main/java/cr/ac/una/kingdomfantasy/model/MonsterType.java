package cr.ac.una.kingdomfantasy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum MonsterType {
    DINO_REX("Dinorex", "dino_rex", AttackStyle.MELEE, 1,
            new CombatStats(50, 25, 29, 34, 1.55, 0, 5, 0.18),
            72, 82, 28, 44),
    BADGER("Badger", "badger", AttackStyle.MELEE, 20,
            new CombatStats(42, 18, 56, 30, 1.05, 0, 10, 0.08),
            90, 46, 18, 80),
    GOLLUX("Gollux", "gollux", AttackStyle.MELEE, 40,
            new CombatStats(86, 43, 23, 38, 1.85, 0, 15, 0.45),
            66, 60, 34, 68),
    PENGU("Pengu", "pengu", AttackStyle.RANGED, 60,
            new CombatStats(48, 29, 18, 235, 2.35, 155, 20, 0.15),
            74, 86, 27, 34),
    CAT("Cat", "cat", AttackStyle.RANGED, 80,
            new CombatStats(138, 50, 16, 280, 2.75, 145, 25, 0.35),
            88, 78, 20, 50);

    private static final double RANGED_ATTACK_RANGE_MULTIPLIER = 3.0;

    private final String displayName;
    private final String assetPrefix;
    private final AttackStyle attackStyle;
    private final int introLevel;
    private final CombatStats baseStats;
    private final double hitBoxWidth;
    private final double hitBoxHeight;
    private final double hitBoxOffsetX;
    private final double hitBoxOffsetY;

    MonsterType(String displayName, String assetPrefix, AttackStyle attackStyle, int introLevel,
            CombatStats baseStats, double hitBoxWidth, double hitBoxHeight,
            double hitBoxOffsetX, double hitBoxOffsetY) {
        this.displayName = displayName;
        this.assetPrefix = assetPrefix;
        this.attackStyle = attackStyle;
        this.introLevel = introLevel;
        this.baseStats = baseStats;
        this.hitBoxWidth = hitBoxWidth;
        this.hitBoxHeight = hitBoxHeight;
        this.hitBoxOffsetX = hitBoxOffsetX;
        this.hitBoxOffsetY = hitBoxOffsetY;
    }

    public CombatStats createStatsForLevel(int level) {
        int safeLevel = Math.max(1, Math.min(100, level));
        int activeLevels = Math.max(1, safeLevel - introLevel + 1);
        int tenLevelSteps = Math.max(0, (safeLevel - 1) / 10);
        double tierBonus = (introLevel - 1) / 20.0 * 0.10;
        double healthScale = 1.0 + tierBonus + tenLevelSteps * 0.18;
        double damageScale = 1.0 + activeLevels * 0.012;
        double speedScale = 1.0 + Math.min(0.18, activeLevels * 0.002);
        double scoreScale = 1.0;
        CombatStats stats = baseStats.scaled(healthScale, damageScale, speedScale, scoreScale);
        if (attackStyle == AttackStyle.RANGED) {
            stats.setAttackRange(stats.getAttackRange() * RANGED_ATTACK_RANGE_MULTIPLIER);
        }
        return stats;
    }

    public static List<MonsterType> availableForLevel(int level) {
        List<MonsterType> types = new ArrayList<>();
        for (MonsterType type : values()) {
            if (level >= type.introLevel) {
                types.add(type);
            }
        }
        return Collections.unmodifiableList(types);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAssetPrefix() {
        return assetPrefix;
    }

    public AttackStyle getAttackStyle() {
        return attackStyle;
    }

    public int getIntroLevel() {
        return introLevel;
    }

    public CombatStats getBaseStats() {
        return baseStats.copy();
    }

    public double getHitBoxWidth() {
        return hitBoxWidth;
    }

    public double getHitBoxHeight() {
        return hitBoxHeight;
    }

    public double getHitBoxOffsetX() {
        return hitBoxOffsetX;
    }

    public double getHitBoxOffsetY() {
        return hitBoxOffsetY;
    }
}
