package cr.ac.una.kingdomfantasy.util;

import cr.ac.una.kingdomfantasy.model.EntityState;
import cr.ac.una.kingdomfantasy.model.Hero;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public final class HeroNode {

    private static final double HEALTH_BAR_WIDTH = 86;
    private static final double HEALTH_BAR_HEIGHT = 8;

    private final Hero hero;
    private final Pane node = new Pane();
    private final List<ImageView> layers = new ArrayList<>();
    private final Pane health = createLifeBar("jfx-fill-life-blue");
    private SpriteAnimator animator;
    private SpriteAnimationId animationId;

    public HeroNode(Hero hero) {
        this.hero = hero;
        node.setPickOnBounds(false);
        node.setScaleX(Hero.VISUAL_SCALE);
        node.setScaleY(Hero.VISUAL_SCALE);
        for (int i = 0; i < 8; i++) {
            ImageView layer = new ImageView();
            layers.add(layer);
            node.getChildren().add(layer);
        }
        node.getChildren().add(health);
        health.setScaleX(1.0 / Hero.VISUAL_SCALE);
        health.setScaleY(1.0 / Hero.VISUAL_SCALE);
        switchAnimation(SpriteAnimationId.IDLE);
        update();
    }

    public void update() {
        SpriteAnimationId desired;
        if (hero.isDead()) {
            desired = SpriteAnimationId.DEATH;
        } else if (animationId == SpriteAnimationId.ATTACK && animator != null && animator.isPlaying()) {
            desired = SpriteAnimationId.ATTACK;
        } else if (hero.getState() == EntityState.ATTACKING) {
            desired = SpriteAnimationId.ATTACK;
        } else if (hero.getState() == EntityState.MOVING) {
            desired = SpriteAnimationId.MOVE;
        } else {
            desired = SpriteAnimationId.IDLE;
        }
        if (desired != animationId) {
            switchAnimation(desired);
        }
        animator.setRow(hero.getFacing().getSpriteRow());
        node.setLayoutX(hero.getX());
        node.setLayoutY(hero.getY());
        health.setLayoutX(21);
        health.setLayoutY(18);
        health.setVisible(hero.isAlive());
        setLifeBarProgress(health, hero.getHealthPercent());
    }

    public void setSelected(boolean selected) {
        node.getStyleClass().remove("jfx-hero-selected");
        if (selected) {
            node.getStyleClass().add("jfx-hero-selected");
        }
    }

    private void switchAnimation(SpriteAnimationId newAnimationId) {
        List<SpriteAnimationSpec> stack = SpriteCatalog.getHeroAnimationStack(hero.getLoadout(), newAnimationId);
        if (stack.isEmpty()) {
            stack = SpriteCatalog.getHeroAnimationStack(hero.getLoadout(), SpriteAnimationId.IDLE);
            newAnimationId = SpriteAnimationId.IDLE;
        }
        animationId = newAnimationId;
        if (animator == null) {
            animator = SpriteAnimator.fromSpecs(layers, stack, hero.getFacing().getSpriteRow());
        } else {
            animator.setAnimationStack(stack);
        }
        animator.setRow(hero.getFacing().getSpriteRow());
        if (stack.get(0).isLoop()) {
            animator.playLoop();
        } else {
            final SpriteAnimationId playedAnimationId = newAnimationId;
            animator.playOnce(() -> {
                if (hero.isDead() && playedAnimationId == SpriteAnimationId.DEATH) {
                    return;
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
