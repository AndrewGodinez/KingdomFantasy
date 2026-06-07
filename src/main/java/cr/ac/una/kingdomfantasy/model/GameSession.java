package cr.ac.una.kingdomfantasy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameSession {

    public static final double WORLD_WIDTH = 1584;
    public static final double WORLD_HEIGHT = 672;
    public static final double CASTLE_WIDTH = 260;
    public static final double LEVEL_DURATION_SECONDS = 150;
    public static final double HERO_RESPAWN_SECONDS = 15;
    private static final double SPAWN_START_SECONDS = 1.15;
    private static final double SPAWN_MIN_WINDOW_SECONDS = 32;
    private static final double SPAWN_MAX_WINDOW_SECONDS = 76;
    private static final double HERO_MELEE_AGGRO_PADDING = 12;
    private static final double HERO_MELEE_AGGRO_DISTANCE = 72;
    private static final double HERO_RANGED_AGGRO_DISTANCE = 138 * 3;
    private static final double HERO_RANGED_AGGRO_VERTICAL = 26;
    private static final double HERO_ATTACK_PROXIMITY_PADDING = 14;

    private final LevelDefinition levelDefinition;
    private final Hero hero;
    private final Castle castle;
    private final Crossbow crossbow;
    private final Map<SpecialPowerType, SpecialPower> specialPowers = new EnumMap<>(SpecialPowerType.class);
    private final List<Monster> monsters = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<SpawnEvent> spawnEvents;
    private final Random spawnRandom = new Random();
    private final int totalMonsters;
    private final double heroSpawnX;
    private final double heroSpawnY;

    private int score;
    private int defeatedMonsters;
    private double elixir;
    private double elapsedSeconds;
    private boolean paused;
    private boolean won;
    private boolean lost;
    private int nextSpawnEventIndex;
    private double heroRespawnRemaining;
    private int projectileHitEvents;
    private int monsterSpawnEvents;

    public GameSession(LevelDefinition levelDefinition) {
        this(levelDefinition, new Hero(CASTLE_WIDTH + 28, WORLD_HEIGHT / 2.0 - 64), new Crossbow());
    }

    public GameSession(LevelDefinition levelDefinition, Hero hero, Crossbow crossbow) {
        this(levelDefinition, hero, crossbow, null);
    }

    public GameSession(LevelDefinition levelDefinition, Hero hero, Crossbow crossbow, UpgradeProfile upgradeProfile) {
        this.levelDefinition = levelDefinition == null ? LevelFactory.createLevel(1) : levelDefinition;
        this.hero = hero == null ? new Hero(CASTLE_WIDTH + 28, WORLD_HEIGHT / 2.0 - 64) : hero;
        UpgradeProfile profile = upgradeProfile == null ? new UpgradeProfile() : upgradeProfile;
        double castleHp = this.levelDefinition.getCastleHealth() + (profile.getLevel(UpgradeType.CASTLE_HEALTH) - 1) * 250.0;
        this.castle = new Castle(0, 0, CASTLE_WIDTH, WORLD_HEIGHT, castleHp);
        this.crossbow = crossbow == null ? new Crossbow() : crossbow;
        this.elixir = this.levelDefinition.getMaxElixir();
        this.heroSpawnX = this.hero.getX();
        this.heroSpawnY = this.hero.getY();
        this.specialPowers.put(SpecialPowerType.METEOR, profile.createPower(SpecialPowerType.METEOR));
        this.specialPowers.put(SpecialPowerType.ICE, profile.createPower(SpecialPowerType.ICE));
        this.spawnEvents = buildSpawnEvents(this.levelDefinition);
        this.totalMonsters = countMonsters(this.spawnEvents);
    }

    public void update(double deltaSeconds) {
        if (paused || won || lost) {
            return;
        }
        double delta = Math.max(0, deltaSeconds);
        elapsedSeconds = Math.min(LEVEL_DURATION_SECONDS, elapsedSeconds + delta);
        regenerateElixir(delta);
        crossbow.update(delta);
        hero.update(delta);
        updateHeroRespawn(delta);
        castle.update(delta);
        for (SpecialPower power : specialPowers.values()) {
            power.update(delta);
        }
        updateSpawning(delta);
        updateMonsters(delta);
        updateProjectiles(delta);
        collectDeadMonsterScores();
        checkEndState();
    }

    public Projectile fireCrossbow(Vector2D target) {
        Vector2D start = new Vector2D(castle.getX() + castle.getWidth() - 24, target == null ? castle.getCenterY() : target.getY());
        return fireCrossbow(start, target);
    }

    public Projectile fireCrossbow(Vector2D start, Vector2D target) {
        Projectile projectile = crossbow.fire(start, target);
        if (projectile != null) {
            projectiles.add(projectile);
        }
        return projectile;
    }

    public boolean heroAttackNearestMonster() {
        if (hero.isDead()) {
            return false;
        }
        Monster nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Monster monster : monsters) {
            if (monster.isDead()) {
                continue;
            }
            double distance = hero.distanceTo(monster);
            boolean reachable = hero.getHitBox().expanded(hero.getStats().getAttackRange() + HERO_ATTACK_PROXIMITY_PADDING)
                    .intersects(monster.getHitBox().getBounds());
            if (reachable && distance < nearestDistance) {
                nearest = monster;
                nearestDistance = distance;
            }
        }
        return nearest != null && hero.attack(nearest);
    }

    public SpecialPowerCast usePower(SpecialPowerType type, double x, double y) {
        SpecialPower power = specialPowers.get(type);
        if (power == null || !power.canUse(elixir, levelDefinition.getMaxElixir())) {
            return new SpecialPowerCast(type, 0, 0, 0);
        }
        beginPowerCast(type);
        return resolvePowerCast(type, x, y);
    }

    public boolean beginPowerCast(SpecialPowerType type) {
        SpecialPower power = specialPowers.get(type);
        if (power == null || !power.canUse(elixir, levelDefinition.getMaxElixir())) {
            return false;
        }
        elixir -= power.getManaCost(levelDefinition.getMaxElixir());
        power.beginCast();
        return true;
    }

    public SpecialPowerCast resolvePowerCast(SpecialPowerType type, double x, double y) {
        SpecialPower power = specialPowers.get(type);
        if (power == null) {
            return new SpecialPowerCast(type, 0, 0, 0);
        }
        SpecialPowerCast cast = power.applyAt(x, y, monsters);
        collectDeadMonsterScores();
        return cast;
    }

    public Monster spawnMonster(MonsterType type, double x, double y) {
        Monster monster = new Monster(type, levelDefinition.getNumber(), x, y);
        monsters.add(monster);
        return monster;
    }

    private void updateSpawning(double delta) {
        while (nextSpawnEventIndex < spawnEvents.size()
                && elapsedSeconds >= spawnEvents.get(nextSpawnEventIndex).getTimeSeconds()) {
            spawnEvent(spawnEvents.get(nextSpawnEventIndex));
            nextSpawnEventIndex++;
        }
    }

    private void updateMonsters(double delta) {
        List<Projectile> spawnedProjectiles = new ArrayList<>();
        for (Monster monster : monsters) {
            if (monster.isDead()) {
                continue;
            }
            GameEntity target = chooseTargetFor(monster);
            monster.advanceToward(target, delta);
            if (monster.getType().getAttackStyle() == AttackStyle.RANGED) {
                Projectile projectile = monster.createRangedProjectile(target);
                if (projectile != null) {
                    spawnedProjectiles.add(projectile);
                }
            } else if (target == hero) {
                monster.attackLiving(hero);
            } else {
                monster.attackCastle(castle);
            }
        }
        projectiles.addAll(spawnedProjectiles);
    }

    private void updateProjectiles(double delta) {
        for (Projectile projectile : projectiles) {
            projectile.update(delta);
            if (!projectile.isActive()) {
                continue;
            }
            if (projectile.getOwner() == ProjectileOwner.CROSSBOW || projectile.getOwner() == ProjectileOwner.HERO) {
                applyProjectileToMonsters(projectile);
            } else if (projectile.getOwner() == ProjectileOwner.MONSTER) {
                if (!projectile.applyTo(hero, heroDamagePerHit())) {
                    projectile.applyTo(castle);
                }
            }
        }
        projectiles.removeIf(projectile -> !projectile.isActive());
    }

    private GameEntity chooseTargetFor(Monster monster) {
        if (monster.getType().getAttackStyle() == AttackStyle.RANGED) {
            return isHeroAlignedWith(monster) ? hero : castle;
        }
        if (!hero.isDead()
                && (monster.getHitBox().intersectsExpanded(hero.getHitBox(), HERO_MELEE_AGGRO_PADDING)
                || monster.distanceTo(hero) <= HERO_MELEE_AGGRO_DISTANCE)) {
            return hero;
        }
        return castle;
    }

    private boolean isHeroAlignedWith(Monster monster) {
        if (hero.isDead()) {
            return false;
        }
        double horizontalDistance = monster.getHitBox().getCenterX() - hero.getHitBox().getCenterX();
        double verticalDistance = Math.abs(hero.getHitBox().getCenterY() - monster.getHitBox().getCenterY());
        return horizontalDistance > 0
                && verticalDistance <= HERO_RANGED_AGGRO_VERTICAL
                && horizontalDistance <= Math.min(monster.getStats().getAttackRange(), HERO_RANGED_AGGRO_DISTANCE);
    }

    private void applyProjectileToMonsters(Projectile projectile) {
        for (Monster monster : monsters) {
            if (projectile.applyTo(monster)) {
                projectileHitEvents++;
                break;
            }
        }
    }

    private void collectDeadMonsterScores() {
        Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            int value = monster.claimScoreValue();
            if (value > 0) {
                score += value;
                defeatedMonsters++;
            }
            if (monster.isDead() && monster.isScoreClaimed() && monster.isDeathAnimationComplete()) {
                iterator.remove();
            }
        }
    }

    private void checkEndState() {
        lost = castle.isDead();
        if (!won && elapsedSeconds >= LEVEL_DURATION_SECONDS && defeatedMonsters < totalMonsters) {
            lost = true;
        }
        won = !lost && nextSpawnEventIndex >= spawnEvents.size() && monsters.isEmpty();
    }

    private void regenerateElixir(double delta) {
        elixir = Math.min(levelDefinition.getMaxElixir(),
                elixir + levelDefinition.getElixirRegenPerSecond() * delta);
    }

    private void updateHeroRespawn(double delta) {
        if (!hero.isDead()) {
            heroRespawnRemaining = 0;
            return;
        }
        if (heroRespawnRemaining <= 0) {
            heroRespawnRemaining = HERO_RESPAWN_SECONDS;
        }
        heroRespawnRemaining = Math.max(0, heroRespawnRemaining - delta);
        if (heroRespawnRemaining <= 0) {
            hero.setPosition(heroSpawnX, heroSpawnY);
            hero.setHealth(hero.getStats().getMaxHealth());
            hero.setFacing(Direction.RIGHT);
            hero.setState(EntityState.IDLE);
        }
    }

    private void spawnEvent(SpawnEvent event) {
        List<Double> lanes = shuffledSpawnLanes();
        int laneIndex = 0;
        int spawned = 0;
        for (MonsterType monsterType : event.getMonsterTypes()) {
            double laneY = lanes.get(laneIndex % lanes.size());
            laneIndex++;
            double y = clamp(laneY + randomBetween(-16, 16), 34, WORLD_HEIGHT - Monster.SPRITE_HEIGHT - 10);
            double x = WORLD_WIDTH - 190 - randomBetween(0, 80);
            spawnMonster(monsterType, x, y);
            spawned++;
        }
        if (spawned > 0) {
            monsterSpawnEvents++;
        }
    }

    private List<Double> shuffledSpawnLanes() {
        double[] laneValues = {66, 150, 234, 318, 402, 486, 570};
        List<Double> lanes = new ArrayList<>();
        for (double lane : laneValues) {
            lanes.add(lane);
        }
        Collections.shuffle(lanes, spawnRandom);
        return lanes;
    }

    private List<SpawnEvent> buildSpawnEvents(LevelDefinition levelDefinition) {
        List<MonsterType> monsterPool = new ArrayList<>();
        for (WaveDefinition wave : levelDefinition.getWaves()) {
            for (int i = 0; i < wave.getCount(); i++) {
                monsterPool.add(wave.getMonsterType());
            }
        }
        Collections.shuffle(monsterPool, spawnRandom);

        List<Integer> groupSizes = buildGroupSizes(levelDefinition.getNumber(), monsterPool.size());
        List<SpawnEvent> events = new ArrayList<>();
        double spawnWindow = spawnWindowForLevel(levelDefinition.getNumber());
        double interval = groupSizes.size() <= 1 ? 0 : spawnWindow / (groupSizes.size() - 1);
        int monsterIndex = 0;
        for (int i = 0; i < groupSizes.size(); i++) {
            int groupSize = groupSizes.get(i);
            List<MonsterType> groupTypes = new ArrayList<>();
            for (int j = 0; j < groupSize && monsterIndex < monsterPool.size(); j++) {
                groupTypes.add(monsterPool.get(monsterIndex));
                monsterIndex++;
            }
            if (!groupTypes.isEmpty()) {
                events.add(new SpawnEvent(SPAWN_START_SECONDS + interval * i, groupTypes));
            }
        }
        return events;
    }

    private List<Integer> buildGroupSizes(int level, int totalMonsters) {
        List<Integer> groupSizes = new ArrayList<>();
        Random levelPattern = new Random(73_891L + level * 10_007L);
        int remaining = totalMonsters;
        double burstChance = clamp(0.10 + level * 0.0055, 0.12, 0.62);
        while (remaining > 0) {
            int groupSize = 1;
            if (remaining >= 3 && levelPattern.nextDouble() < burstChance * 0.45) {
                groupSize = 3;
            } else if (remaining >= 2 && levelPattern.nextDouble() < burstChance) {
                groupSize = 2;
            }
            groupSizes.add(Math.min(groupSize, remaining));
            remaining -= groupSize;
        }
        return groupSizes;
    }

    private double spawnWindowForLevel(int level) {
        return clamp(30 + level * 0.46, SPAWN_MIN_WINDOW_SECONDS, SPAWN_MAX_WINDOW_SECONDS);
    }

    private double randomBetween(double min, double max) {
        return min + spawnRandom.nextDouble() * (max - min);
    }

    private double heroDamagePerHit() {
        return hero.getStats().getMaxHealth() / 6.0;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private int countMonsters(List<SpawnEvent> events) {
        int total = 0;
        for (SpawnEvent event : events) {
            total += event.getMonsterTypes().size();
        }
        return total;
    }

    private static final class SpawnEvent {

        private final double timeSeconds;
        private final List<MonsterType> monsterTypes;

        private SpawnEvent(double timeSeconds, List<MonsterType> monsterTypes) {
            this.timeSeconds = Math.max(0, timeSeconds);
            this.monsterTypes = Collections.unmodifiableList(new ArrayList<>(monsterTypes));
        }

        private double getTimeSeconds() {
            return timeSeconds;
        }

        private List<MonsterType> getMonsterTypes() {
            return monsterTypes;
        }
    }

    public LevelDefinition getLevelDefinition() {
        return levelDefinition;
    }

    public Hero getHero() {
        return hero;
    }

    public Castle getCastle() {
        return castle;
    }

    public Crossbow getCrossbow() {
        return crossbow;
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    public SpecialPower getPower(SpecialPowerType type) {
        return specialPowers.get(type);
    }

    public int getScore() {
        return score;
    }

    public int getDefeatedMonsters() {
        return defeatedMonsters;
    }

    public int getTotalMonsters() {
        return totalMonsters;
    }

    public double getProgress() {
        return totalMonsters == 0 ? 1.0 : Math.min(1.0, defeatedMonsters / (double) totalMonsters);
    }

    public double getTimeProgress() {
        return Math.min(1.0, elapsedSeconds / LEVEL_DURATION_SECONDS);
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public double getRemainingSeconds() {
        return Math.max(0, LEVEL_DURATION_SECONDS - elapsedSeconds);
    }

    public double getElixir() {
        return elixir;
    }

    public void setElixir(double elixir) {
        this.elixir = Math.max(0, Math.min(levelDefinition.getMaxElixir(), elixir));
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isLost() {
        return lost;
    }

    public double getHeroRespawnRemaining() {
        return heroRespawnRemaining;
    }

    public int consumeProjectileHitEvents() {
        int events = projectileHitEvents;
        projectileHitEvents = 0;
        return events;
    }

    public int consumeMonsterSpawnEvents() {
        int events = monsterSpawnEvents;
        monsterSpawnEvents = 0;
        return events;
    }
}
