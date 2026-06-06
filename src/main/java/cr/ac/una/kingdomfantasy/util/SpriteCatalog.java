package cr.ac.una.kingdomfantasy.util;

import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
import cr.ac.una.kingdomfantasy.model.HeroAccessory;
import cr.ac.una.kingdomfantasy.model.HeroLoadout;
import cr.ac.una.kingdomfantasy.model.MonsterType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class SpriteCatalog {

    private static final Map<MonsterType, Map<SpriteAnimationId, SpriteAnimationSpec>> MONSTER_ANIMATIONS = buildMonsterAnimations();
    private static final Map<SpriteAnimationId, SpriteAnimationSpec> HERO_BASE_ANIMATIONS = buildHeroBaseAnimations();
    private static final Map<SpriteAnimationId, SpriteAnimationSpec> SPELL_ANIMATIONS = buildSpellAnimations();
    private static final List<HeroAccessory> HERO_LAYER_ORDER = Collections.unmodifiableList(Arrays.asList(
            HeroAccessory.HAIR,
            HeroAccessory.LEGGINGS,
            HeroAccessory.BOOTS,
            HeroAccessory.CHESTPLATE,
            HeroAccessory.GLOVES,
            HeroAccessory.SWORD,
            HeroAccessory.SWORD_ALT,
            HeroAccessory.HAT,
            HeroAccessory.BEARD));

    private SpriteCatalog() {
    }

    public static SpriteAnimationSpec getMonsterAnimation(MonsterType type, SpriteAnimationId animationId) {
        Map<SpriteAnimationId, SpriteAnimationSpec> animations = MONSTER_ANIMATIONS.get(type);
        if (animations == null) {
            return null;
        }
        return animations.get(animationId);
    }

    public static Map<SpriteAnimationId, SpriteAnimationSpec> getMonsterAnimations(MonsterType type) {
        Map<SpriteAnimationId, SpriteAnimationSpec> animations = MONSTER_ANIMATIONS.get(type);
        return animations == null ? Collections.emptyMap() : Collections.unmodifiableMap(animations);
    }

    public static SpriteAnimationSpec getHeroBaseAnimation(SpriteAnimationId animationId) {
        return HERO_BASE_ANIMATIONS.get(animationId);
    }

    public static SpriteAnimationSpec getSpellAnimation(SpriteAnimationId animationId) {
        return SPELL_ANIMATIONS.get(animationId);
    }

    public static List<SpriteAnimationSpec> getHeroAnimationStack(HeroLoadout loadout, SpriteAnimationId animationId) {
        SpriteAnimationSpec base = getHeroBaseAnimation(animationId);
        if (base == null) {
            return Collections.emptyList();
        }
        List<SpriteAnimationSpec> stack = new ArrayList<>();
        stack.add(base);

        if (animationId == SpriteAnimationId.ATTACK) {
            SpriteAnimationSpec fx = getHeroBaseAnimation(SpriteAnimationId.ATTACK_FX);
            if (fx != null) {
                stack.add(fx);
            }
            return stack;
        }

        if (animationId != SpriteAnimationId.IDLE && animationId != SpriteAnimationId.MOVE) {
            return stack;
        }

        HeroLoadout effectiveLoadout = loadout == null ? HeroLoadout.defaultKnightNoHelmetNoBeard() : loadout;
        for (HeroAccessory accessory : HERO_LAYER_ORDER) {
            if (!effectiveLoadout.has(accessory)) {
                continue;
            }
            SpriteAnimationSpec layer = getHeroAccessoryAnimation(accessory, animationId);
            if (layer != null) {
                stack.add(layer);
            }
        }
        return stack;
    }

    public static String getCrossbowResourcePath(CrossbowDesign design) {
        CrossbowDesign effectiveDesign = design == null ? CrossbowDesign.GREEN : design;
        return SpriteAnimationSpec.RESOURCE_ROOT + effectiveDesign.getResourceName();
    }

    public static String resourcePath(String fileName) {
        return SpriteAnimationSpec.RESOURCE_ROOT + fileName;
    }

    public static void preloadGameplaySprites() {
        for (Map<SpriteAnimationId, SpriteAnimationSpec> animations : MONSTER_ANIMATIONS.values()) {
            for (SpriteAnimationSpec spec : animations.values()) {
                spec.loadImage();
            }
        }
        for (SpriteAnimationSpec spec : HERO_BASE_ANIMATIONS.values()) {
            spec.loadImage();
        }
        for (SpriteAnimationSpec spec : SPELL_ANIMATIONS.values()) {
            spec.loadImage();
        }
        for (HeroAccessory accessory : HERO_LAYER_ORDER) {
            SpriteAnimationSpec idle = getHeroAccessoryAnimation(accessory, SpriteAnimationId.IDLE);
            SpriteAnimationSpec move = getHeroAccessoryAnimation(accessory, SpriteAnimationId.MOVE);
            if (idle != null) {
                idle.loadImage();
            }
            if (move != null) {
                move.loadImage();
            }
        }
    }

    public static void preloadResourceImages(String... fileNames) {
        if (fileNames == null) {
            return;
        }
        for (String fileName : fileNames) {
            if (fileName != null && !fileName.isBlank()) {
                SpriteAnimationSpec.loadCachedResource(resourcePath(fileName));
            }
        }
    }

    private static SpriteAnimationSpec getHeroAccessoryAnimation(HeroAccessory accessory, SpriteAnimationId animationId) {
        if (animationId == SpriteAnimationId.IDLE) {
            return hero(accessory.getIdleResourceName(), 4, 8, true);
        }
        if (animationId == SpriteAnimationId.MOVE) {
            return hero(accessory.getMoveResourceName(), 6, 10, true);
        }
        return null;
    }

    private static Map<MonsterType, Map<SpriteAnimationId, SpriteAnimationSpec>> buildMonsterAnimations() {
        Map<MonsterType, Map<SpriteAnimationId, SpriteAnimationSpec>> result = new EnumMap<>(MonsterType.class);

        Map<SpriteAnimationId, SpriteAnimationSpec> badger = new EnumMap<>(SpriteAnimationId.class);
        badger.put(SpriteAnimationId.IDLE, monster("badger_idle.png", 128, 5, 8, true));
        badger.put(SpriteAnimationId.MOVE, monster("badger_move.png", 384, 8, 12, true));
        badger.put(SpriteAnimationId.ATTACK_A, monster("badger_attack_A.png", 384, 15, 18, false));
        badger.put(SpriteAnimationId.ATTACK_B, monster("badger_attack_B.png", 384, 11, 18, false));
        badger.put(SpriteAnimationId.ABILITY, monster("badger_ability.png", 384, 15, 16, false));
        badger.put(SpriteAnimationId.HURT, monster("badger_hurt.png", 384, 4, 10, false));
        result.put(MonsterType.BADGER, badger);

        Map<SpriteAnimationId, SpriteAnimationSpec> cat = new EnumMap<>(SpriteAnimationId.class);
        cat.put(SpriteAnimationId.IDLE, monster("cat_idle.png", 128, 5, 8, true));
        cat.put(SpriteAnimationId.MOVE, monster("cat_move.png", 384, 8, 10, true));
        cat.put(SpriteAnimationId.SHOOTING, monster("cat_shooting.png", 384, 13, 16, false));
        cat.put(SpriteAnimationId.GRENADE, monster("cat_grenade.png", 384, 16, 16, false));
        cat.put(SpriteAnimationId.RELOAD, monster("cat_reload.png", 384, 41, 18, false));
        cat.put(SpriteAnimationId.OUT_OF_AMMO, monster("cat_outofammo.png", 384, 28, 16, false));
        cat.put(SpriteAnimationId.HURT, monster("cat_hurt.png", 384, 4, 10, false));
        result.put(MonsterType.CAT, cat);

        Map<SpriteAnimationId, SpriteAnimationSpec> dino = new EnumMap<>(SpriteAnimationId.class);
        dino.put(SpriteAnimationId.IDLE, monster("dino_rex_idle.png", 128, 5, 8, true));
        dino.put(SpriteAnimationId.MOVE, monster("dino_rex_move.png", 384, 8, 10, true));
        dino.put(SpriteAnimationId.ATTACK_A, monster("dino_rex_attack_A.png", 384, 21, 18, false));
        dino.put(SpriteAnimationId.ATTACK_B_START, monster("dino_rex_attack_B_1_start.png", 384, 10, 16, false));
        dino.put(SpriteAnimationId.ATTACK_B_LOOP, monster("dino_rex_attack_B_2_loop.png", 384, 5, 12, true));
        dino.put(SpriteAnimationId.ATTACK_B_END, monster("dino_rex_attack_B_3_end.png", 384, 7, 16, false));
        dino.put(SpriteAnimationId.STOMP, monster("dino_rex_stomp.png", 384, 5, 12, false));
        dino.put(SpriteAnimationId.HURT, monster("dino_rex_hurt.png", 384, 4, 10, false));
        result.put(MonsterType.DINO_REX, dino);

        Map<SpriteAnimationId, SpriteAnimationSpec> gollux = new EnumMap<>(SpriteAnimationId.class);
        gollux.put(SpriteAnimationId.IDLE, monster("gollux_idle.png", 128, 5, 8, true));
        gollux.put(SpriteAnimationId.MOVE, monster("gollux_move.png", 384, 8, 9, true));
        gollux.put(SpriteAnimationId.ATTACK_B, monster("gollux_attack_B.png", 384, 19, 15, false));
        gollux.put(SpriteAnimationId.HURT, monster("gollux_hit.png", 384, 4, 9, false));
        result.put(MonsterType.GOLLUX, gollux);

        Map<SpriteAnimationId, SpriteAnimationSpec> pengu = new EnumMap<>(SpriteAnimationId.class);
        pengu.put(SpriteAnimationId.IDLE, monster("pengu_idle.png", 128, 5, 8, true));
        pengu.put(SpriteAnimationId.MOVE, monster("pengu_move.png", 384, 8, 10, true));
        pengu.put(SpriteAnimationId.ATTACK_RAY, monster("pengu_attack_ray.png", 384, 14, 16, false));
        pengu.put(SpriteAnimationId.FREEZE_FX, SpriteAnimationSpec.fromResource("pengu_fx_freeze.png", 16, 128, 15, 1, 16, false));
        pengu.put(SpriteAnimationId.HURT, monster("pengu_hurt.png", 384, 4, 10, false));
        result.put(MonsterType.PENGU, pengu);

        return result;
    }

    private static Map<SpriteAnimationId, SpriteAnimationSpec> buildHeroBaseAnimations() {
        Map<SpriteAnimationId, SpriteAnimationSpec> result = new EnumMap<>(SpriteAnimationId.class);
        result.put(SpriteAnimationId.IDLE, hero("Character_Idle.png", 4, 8, true));
        result.put(SpriteAnimationId.MOVE, hero("Character_Move.png", 6, 10, true));
        result.put(SpriteAnimationId.ATTACK, hero("Character_Attack.png", 6, 18, false));
        result.put(SpriteAnimationId.ATTACK_FX, hero("Character_Attack_FX.png", 6, 18, false));
        result.put(SpriteAnimationId.DEATH, hero("Character_Death.png", 11, 10, false));
        return result;
    }

    private static Map<SpriteAnimationId, SpriteAnimationSpec> buildSpellAnimations() {
        Map<SpriteAnimationId, SpriteAnimationSpec> result = new EnumMap<>(SpriteAnimationId.class);
        result.put(SpriteAnimationId.ICE_SPELL_FX,
                SpriteAnimationSpec.fromResourceGrid("flash_freeze.png", 512, 512, 8, 12, 1, 88, 30, false));
        result.put(SpriteAnimationId.METEOR_SPELL_FX,
                SpriteAnimationSpec.fromResourceGrid("fireball.png", 512, 384, 4, 10, 1, 39, 28, false));
        return result;
    }

    private static SpriteAnimationSpec monster(String fileName, int frameWidth, int frameCount, double fps, boolean loop) {
        return new SpriteAnimationSpec(monsterResourcePath(fileName), frameWidth, 128, frameCount, 1, fps, loop);
    }

    private static SpriteAnimationSpec hero(String fileName, int frameCount, double fps, boolean loop) {
        return SpriteAnimationSpec.fromResource(fileName, 128, 128, frameCount, 4, fps, loop);
    }

    private static String monsterResourcePath(String fileName) {
        if (fileName == null || fileName.startsWith("dino_rex")) {
            return SpriteAnimationSpec.RESOURCE_ROOT + fileName;
        }
        return SpriteAnimationSpec.RESOURCE_ROOT + "enemy/" + fileName;
    }
}
