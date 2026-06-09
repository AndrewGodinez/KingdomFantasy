package cr.ac.una.kingdomfantasy.util;

import cr.ac.una.kingdomfantasy.model.EntityState;
import cr.ac.una.kingdomfantasy.model.Monster;
import cr.ac.una.kingdomfantasy.model.MonsterType;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public final class MonsterNode {

    private static final double HEALTH_BAR_WIDTH = 86;
    private static final double HEALTH_BAR_HEIGHT = 8;

    private final Monster monster;
    private final Pane node = new Pane();
    private final ImageView sprite = new ImageView();
    private final Pane health = createLifeBar("jfx-fill-life-red");
    private SpriteAnimator animator;
    private SpriteAnimationId animationId;
    private SpriteAnimationSpec currentSpec;
    private int deathHurtLoops;

    public MonsterNode(Monster monster) {
        this.monster = monster;
        node.setPickOnBounds(false);
        node.getChildren().addAll(sprite, health);
        switchAnimation(SpriteAnimationId.IDLE);
        animator.setFlipX(true);
        update();
    }

    public void update() {
        SpriteAnimationId desired = monsterAnimationFor(monster);
        if (desired != animationId) {
            switchAnimation(desired);
        }
        if (monster.getState() == EntityState.FROZEN) {
            animator.setAnimationFps(2);
        } else if (currentSpec != null) {
            animator.setAnimationFps(currentSpec.getFps());
        }
        double visualOffset = currentSpec != null && currentSpec.getFrameWidth() > 128 ? -128 : 0;
        node.setLayoutX(monster.getX() + visualOffset);
        node.setLayoutY(monster.getY() - 10);
        health.setLayoutX((currentSpec == null ? 128 : currentSpec.getFrameWidth()) / 2.0 - HEALTH_BAR_WIDTH / 2.0);
        health.setLayoutY(3);
        health.setVisible(monster.isAlive());
        setLifeBarProgress(health, monster.getHealthPercent());
    }

    private void switchAnimation(SpriteAnimationId newAnimationId) {
        SpriteAnimationSpec spec = SpriteCatalog.getMonsterAnimation(monster.getType(), newAnimationId);
        if (spec == null) {
            if (monster.isDead()) {
                monster.markDeathAnimationComplete();
                return;
            }
            spec = SpriteCatalog.getMonsterAnimation(monster.getType(), SpriteAnimationId.IDLE);
            newAnimationId = SpriteAnimationId.IDLE;
        }
        currentSpec = spec;
        animationId = newAnimationId;
        if (animator == null) {
            animator = new SpriteAnimator(sprite, spec);
        } else {
            animator.setAnimation(spec);
        }
        animator.setFlipX(true);
        if (spec.isLoop()) {
            animator.playLoop();
        } else {
            animator.playOnce(() -> {
                if (monster.isDead()) {
                    deathHurtLoops++;
                    if (deathHurtLoops < 3) {
                        animationId = null;
                        switchAnimation(SpriteAnimationId.HURT);
                    } else {
                        monster.markDeathAnimationComplete();
                    }
                    return;
                }
                if (monster.isAlive() && !monster.isFrozen()
                        && (monster.getState() == EntityState.ATTACKING || monster.getState() == EntityState.HURT)) {
                    monster.setState(EntityState.IDLE);
                }
                animationId = null;
                switchAnimation(SpriteAnimationId.IDLE);
            });
        }
    }

    public void pause() { animator.pause(); }
    public void resume() { animator.resume(); }
    public void stop() { animator.stop(); }
    public Node getNode() { return node; }

    private static SpriteAnimationId monsterAnimationFor(Monster monster) {
        EntityState state = monster.getState();
        if (monster.isDead() && SpriteCatalog.getMonsterAnimation(monster.getType(), SpriteAnimationId.HURT) != null) {
            return SpriteAnimationId.HURT;
        }
        if (state == EntityState.HURT && SpriteCatalog.getMonsterAnimation(monster.getType(), SpriteAnimationId.HURT) != null) {
            return SpriteAnimationId.HURT;
        }
        if (state == EntityState.ATTACKING) {
            return attackAnimationFor(monster.getType());
        }
        if (state == EntityState.MOVING) {
            return SpriteAnimationId.MOVE;
        }
        return SpriteAnimationId.IDLE;
    }

    private static SpriteAnimationId attackAnimationFor(MonsterType type) {
        switch (type) {
            case DINO_REX: return SpriteAnimationId.ATTACK_A;
            case BADGER:   return SpriteAnimationId.ATTACK_B;
            case GOLLUX:   return SpriteAnimationId.ATTACK_B;
            case PENGU:    return SpriteAnimationId.ATTACK_RAY;
            case CAT:      return SpriteAnimationId.SHOOTING;
            default:       return SpriteAnimationId.IDLE;
        }
    }

    private static Pane createLifeBar(String fillClass) {
        Pane track = new Pane();
        Region fill = new Region();
        track.getStyleClass().add("jfx-track-life");
        fill.getStyleClass().add(fillClass);
        track.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        track.setMinSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        track.setMaxSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        fill.setPrefSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        fill.resize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        track.getChildren().add(fill);
        return track;
    }

    private static void setLifeBarProgress(Pane bar, double progress) {
        if (bar.getChildren().isEmpty()) {
            return;
        }
        double width = HEALTH_BAR_WIDTH * Math.max(0, Math.min(1, progress));
        Region fill = (Region) bar.getChildren().get(0);
        fill.setPrefWidth(width);
        fill.resize(width, HEALTH_BAR_HEIGHT);
    }
}
