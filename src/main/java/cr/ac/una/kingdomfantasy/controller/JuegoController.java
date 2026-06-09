package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.ControlScheme;
import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
import cr.ac.una.kingdomfantasy.model.Direction;
import cr.ac.una.kingdomfantasy.model.EntityState;
import cr.ac.una.kingdomfantasy.model.GameSession;
import cr.ac.una.kingdomfantasy.model.Hero;
import cr.ac.una.kingdomfantasy.model.LevelDefinition;
import cr.ac.una.kingdomfantasy.model.LevelFactory;
import cr.ac.una.kingdomfantasy.model.Monster;
import cr.ac.una.kingdomfantasy.model.Projectile;
import cr.ac.una.kingdomfantasy.model.SpecialPower;
import cr.ac.una.kingdomfantasy.model.SpecialPowerCast;
import cr.ac.una.kingdomfantasy.model.SpecialPowerType;
import cr.ac.una.kingdomfantasy.model.UpgradeProfile;
import cr.ac.una.kingdomfantasy.model.UpgradeType;
import cr.ac.una.kingdomfantasy.model.Vector2D;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Mensaje;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import cr.ac.una.kingdomfantasy.util.SpriteAnimationId;
import cr.ac.una.kingdomfantasy.util.SpriteAnimationSpec;
import cr.ac.una.kingdomfantasy.util.SpriteAnimator;
import cr.ac.una.kingdomfantasy.util.SpriteCatalog;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.transform.Scale;
import io.github.palexdev.materialfx.controls.MFXButton;

import cr.ac.una.kingdomfantasy.util.HeroNode;
import cr.ac.una.kingdomfantasy.util.MonsterNode;
import cr.ac.una.kingdomfantasy.util.OverlayAnimationSequence;
import cr.ac.una.kingdomfantasy.util.OverlayAnimationType;
import cr.ac.una.kingdomfantasy.util.OverlaySpriteAnimator;
import cr.ac.una.kingdomfantasy.util.PlayerRegistry;
import cr.ac.una.kingdomfantasy.util.SpellButtonVisualState;

public class JuegoController extends Controller implements Initializable {

    private static final String GOLD_KEY = PlayerRegistry.GOLD_KEY;
    private static final String UPGRADE_PROFILE_KEY = PlayerRegistry.UPGRADE_PROFILE_KEY;
    private static final String CONTROL_SCHEME_KEY = "controlScheme";
    private static final String REVIEW_MODE_KEY = "reviewModeEnabled";
    private static final String REVIEW_LEVEL_KEY = "reviewLevel";
    private static final String REVIEW_UPGRADE_PROFILE_KEY = "reviewUpgradeProfile";
    private static final String CROSSBOW_DESIGN_KEY = PlayerRegistry.CROSSBOW_DESIGN_KEY;
    private static final String CURRENT_LEVEL_KEY = PlayerRegistry.CURRENT_LEVEL_KEY;
    private static final double WORLD_WIDTH = GameSession.WORLD_WIDTH;
    private static final double WORLD_HEIGHT = GameSession.WORLD_HEIGHT;
    private static final double MANA_REGEN_PER_SECOND = 2.0;
    private static final double CROSSBOW_X = 220;
    private static final double CROSSBOW_Y = 275;
    private static final double CROSSBOW_SOURCE_X = 320; 
    private static final double CROSSBOW_SOURCE_Y = 328; 
    private static final double CROSSBOW_FIT_WIDTH = 130;
    private static final double CROSSBOW_FIT_HEIGHT = 110;
    private static final double CROSSBOW_KEYBOARD_AIM_SPEED = 1100;
    private static final double HERO_REGEN_DELAY_SECONDS = 3.0;
    private static final double HERO_REGEN_PER_SECOND = 0.045;
    private static final double HERO_ATTACK_PROXIMITY_PADDING = 14;
    private static final double WALKABLE_LEFT_TOP_Y = 204;
    private static final double WALKABLE_FIELD_TOP_Y = 152;
    private static final double WALKABLE_RAMP_WIDTH = 430;
    private static final double HUD_UPDATE_INTERVAL_SECONDS = 0.10;
    private static final double OVERLAY_ANIMATION_FPS = 12.0;
    private static final int OVERLAY_ATLAS_COLUMNS = 4;
    private static final int OVERLAY_ATLAS_ROWS = 4;
    private static final int OVERLAY_FRAME_COUNT = 16;
    private static final int DEFEAT_TEXT_FRAME_COUNT = 13;
    private static boolean gameplayAssetsWarmed;
    @FXML
    private MFXButton btnBackToMenu;
    @FXML
    private ImageView imvHeroPortrait;

    @FXML
    private AnchorPane root;
    @FXML
    private StackPane battlefieldFrame;
    @FXML
    private Pane gameWorld;
    @FXML
    private ImageView imvBattlefield;
    @FXML
    private Pane worldPane;
    @FXML
    private Pane actorPane;
    @FXML
    private Pane projectilePane;
    @FXML
    private Pane effectsPane;
    @FXML
    private ImageView imvCrossbow;
    @FXML
    private Rectangle castleZone;
    @FXML
    private StackPane pauseOverlay;
    @FXML
    private Label lbLevel;
    @FXML
    private Label lbTimer;
    @FXML
    private Label lbLevelPoints;
    @FXML
    private Label lbScoreMultiplier;
    @FXML
    private Label lbCastleValue;
    @FXML
    private Label lbManaValue;
    @FXML
    private Label lbWaveValue;
    @FXML
    private Label lbReviewMode;
    @FXML
    private ImageView imvOverlayAnimation;
    @FXML
    private ProgressBar prgWave;
    @FXML
    private ProgressBar prgLevelTime;
    @FXML
    private ProgressBar prgCastle;
    @FXML
    private ProgressBar prgMana;
    @FXML
    private MFXButton btnPause;
    @FXML
    private MFXButton btnResumeOverlay;
    @FXML
    private MFXButton btnBackToImprovements;
    @FXML
    private MFXButton btnHeroPortrait;
    @FXML
    private MFXButton btnMeteorSpell;
    @FXML
    private MFXButton btnIceSpell;
    @FXML
    private HBox hbReviewHud;
    @FXML
    private MFXButton btnReviewPreviousLevel;
    @FXML
    private MFXButton btnReviewCompleteLevel;
    @FXML
    private MFXButton btnReviewNextLevel;
    @FXML
    private MFXButton btnReviewLevel100;
    @FXML
    private ImageView imvMeteorSpell;
    @FXML
    private ImageView imvIceSpell;

    private final Map<Monster, MonsterNode> monsterNodes = new HashMap<>();
    private final Map<Projectile, ImageView> projectileNodes = new HashMap<>();
    private final List<SpriteAnimator> effectAnimators = new ArrayList<>();
    private final List<Timeline> effectTimelines = new ArrayList<>();
    private final Map<SpecialPowerType, Image[]> spellIconImages = new EnumMap<>(SpecialPowerType.class);
    private final Map<SpecialPowerType, SpellButtonVisualState> spellButtonStates = new EnumMap<>(SpecialPowerType.class);
    private final Map<OverlayAnimationType, OverlayAnimationSequence> overlaySequences = new EnumMap<>(OverlayAnimationType.class);
    private final Set<Monster> activeMonsterScratch = new HashSet<>();
    private final List<Monster> removedMonsterScratch = new ArrayList<>();
    private final Set<Projectile> activeProjectileScratch = new HashSet<>();
    private final List<Projectile> removedProjectileScratch = new ArrayList<>();
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final EnumSet<KeyCode> movementKeys = EnumSet.of(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D);
    private final java.util.ArrayDeque<KeyCode> movementKeyOrder = new java.util.ArrayDeque<>();

    private GameSession session;
    private HeroNode heroNode;
    private AnimationTimer gameLoop;
    private long lastUpdate;
    private Vector2D lastAim = new Vector2D(WORLD_WIDTH * 0.75, WORLD_HEIGHT * 0.5);
    // Last known mouse position in world coordinates. The spell aim circle
    // always follows this point, in BOTH control schemes, so the player aims
    // spells with the mouse even while moving the hero with the keyboard.
    private Vector2D spellPointer = new Vector2D(WORLD_WIDTH * 0.75, WORLD_HEIGHT * 0.5);
    private Vector2D heroMoveTarget;
    private ControlScheme controlScheme;
    private CrossbowDesign crossbowDesign;
    private Image crossbowImage;
    private Image arrowImage;
    private int lastScore;
    private int lastAwardedLevelPoints;
    private boolean rewardSaved;
    private boolean endShown;
    private boolean heroSelected;
    private double heroOutOfCombatSeconds;
    private double lastHeroHealth;
    private boolean worldConfigured;
    private boolean sceneInputConfigured;
    private SpecialPowerType selectedPower;
    private int currentLevel;
    private double hudUpdateAccumulator;
    private boolean hudDirty = true;
    private double lastCrossbowAngle = Double.NaN;
    private boolean mouseFireHeld;
    private boolean pauseToggleReady = true;
    private OverlaySpriteAnimator overlayAnimator;
    private OverlayAnimationSequence currentOverlaySequence;
    private Image spellAimImage;
    private ImageView aimOverlay;
    private boolean debugHitboxes = false;
    private final Map<Object, Rectangle> debugRectMap = new java.util.LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setNombreVista("Kingdom Fantasy - Partida");
        warmUpGameplayAssets();
        loadSpellIconImages();
        controlScheme = getControlScheme();
        crossbowDesign = getCrossbowDesign();
        arrowImage = loadArrowImage(crossbowDesign);
        imvBattlefield.setImage(loadResourceImage("game_field.png"));
        configureWorld();
        Platform.runLater(this::configureSceneInput);
    }

    @Override
    public void initialize() {
        warmUpGameplayAssets();
        loadSpellIconImages();
        controlScheme = getControlScheme();
        crossbowDesign = getCrossbowDesign();
        arrowImage = loadArrowImage(crossbowDesign);
        startLevel();
        Platform.runLater(this::configureSceneInput);
    }

    private static synchronized void warmUpGameplayAssets() {
        if (gameplayAssetsWarmed) {
            return;
        }
        SpriteCatalog.preloadGameplaySprites();
        SpriteCatalog.preloadResourceImages(
                "game_field.png",
                "Garrick.png",
                "Arrow-Green.png",
                "Arrow-Purple.png",
                "Meteor-Spell-Normal.png",
                "Meteor-Spell-Selected.png",
                "Meteor-Spell-Cooldown.png",
                "Ice-Spell-Normal.png",
                "Ice-Spell-Selected.png",
                "Ice-Spell-Cooldown.png",
                "VictoryAnimation.png",
                "DefeatAnimation.png",
                "PauseAnimation.png",
                "Spell-Aim-Area.png");
        gameplayAssetsWarmed = true;
    }

    @FXML
    private void onActionPause(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        pauseGame();
    }

    @FXML
    private void onActionResume(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        resumeGame();
    }

    @FXML
    private void onActionBackToImprovements(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (session != null && session.isPaused()) {
             if(new Mensaje().showConfirmation("Volver a Mejoras", getStage(), "¿Estás seguro de querer volver a el apartado de mejoras? "
                + "¡Aviso perderás todo tu progreso del nivel si vuelves a las mejoras!")){
        stopCurrentLevelRuntime();
        FlowController.getInstance().goViewInStage("MejorasView", getStage());
        }
      } else {
        FlowController.getInstance().goViewInStage("MejorasView", getStage());    
    }
    }

    @FXML
    private void onActionBackToMenu(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
         if (session != null && session.isPaused()) {
             if(new Mensaje().showConfirmation("Volver al menú", getStage(), "¿Estás seguro de querer volver al menú? "
                + "¡Aviso perderás todo tu progreso del nivel si vuelves al menú!")){
        stopCurrentLevelRuntime();
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
        }
      } else {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());    
    }
    }

    @FXML
    private void onActionHeroPortrait(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        selectHeroForMovement();
    }

    @FXML
    private void onActionMeteorSpell(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        selectPower(SpecialPowerType.METEOR);
    }

    @FXML
    private void onActionIceSpell(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        selectPower(SpecialPowerType.ICE);
    }

    @FXML
    private void onActionReviewPreviousLevel(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        jumpToReviewLevel(currentLevel - 1, "Nivel anterior cargado para revision.");
    }

    @FXML
    private void onActionReviewCompleteLevel(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        jumpToReviewLevel(Math.min(100, currentLevel + 1), "Nivel marcado como completado en modo revision.");
    }

    @FXML
    private void onActionReviewNextLevel(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        jumpToReviewLevel(currentLevel + 1, "Siguiente nivel cargado para revision.");
    }

    @FXML
    private void onActionReviewLevel100(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        jumpToReviewLevel(100, "Nivel 100 cargado para revision.");
    }

    private void configureWorld() {
        if (worldConfigured) {
            return;
        }
        worldConfigured = true;
        castleZone.setWidth(GameSession.CASTLE_WIDTH);
        castleZone.setHeight(WORLD_HEIGHT);
        gameWorld.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gameWorld.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        imvBattlefield.setPreserveRatio(false);
        imvBattlefield.setSmooth(true);
        imvBattlefield.fitWidthProperty().bind(gameWorld.widthProperty());
        imvBattlefield.fitHeightProperty().bind(gameWorld.heightProperty());
        worldPane.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        worldPane.setMinSize(WORLD_WIDTH, WORLD_HEIGHT);
        worldPane.setMaxSize(WORLD_WIDTH, WORLD_HEIGHT);
        worldPane.setClip(new Rectangle(WORLD_WIDTH, WORLD_HEIGHT));
        actorPane.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        projectilePane.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);
        effectsPane.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);

        spellAimImage = loadResourceImage("Spell-Aim-Area.png");
        aimOverlay = new ImageView(spellAimImage);
        aimOverlay.setMouseTransparent(true);
        aimOverlay.setManaged(false);
        aimOverlay.setPreserveRatio(true);
        aimOverlay.setVisible(false);
        gameWorld.widthProperty().addListener((obs, oldValue, newValue) -> resizeWorld());
        gameWorld.heightProperty().addListener((obs, oldValue, newValue) -> resizeWorld());

        gameWorld.addEventHandler(MouseEvent.MOUSE_MOVED, this::onWorldMouseMoved);
        gameWorld.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onWorldMousePressed);
        gameWorld.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onWorldMouseDragged);
        gameWorld.addEventHandler(MouseEvent.MOUSE_RELEASED, this::onWorldMouseReleased);
        if (pauseOverlay != null) {
            pauseOverlay.widthProperty().addListener((obs, oldValue, newValue) -> resizeOverlayAnimation());
            pauseOverlay.heightProperty().addListener((obs, oldValue, newValue) -> resizeOverlayAnimation());
        }
        Platform.runLater(this::resizeWorld);
    }

    private void configureSceneInput() {
        if (root.getScene() == null) {
            Platform.runLater(this::configureSceneInput);
            return;
        }
        if (sceneInputConfigured) {
            return;
        }
        sceneInputConfigured = true;
        root.setFocusTraversable(true);
        root.requestFocus();
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        root.addEventFilter(KeyEvent.KEY_RELEASED, this::onKeyReleased);
        root.addEventFilter(MouseEvent.MOUSE_RELEASED, this::onWorldMouseReleased);
    }

    private void startLevel() {
        stopCurrentLevelRuntime();
        MusicManager.getInstance().playTrack(MusicManager.MusicTrack.GAME);
        PlayerRegistry.getOrCreateCurrentPlayer();
        PlayerRegistry.syncCurrentFromContext();
        UpgradeProfile profile = getProfile();
        currentLevel = getCurrentLevel();
        LevelDefinition base = LevelFactory.createLevel(currentLevel);
        LevelDefinition adjusted = new LevelDefinition(
                base.getNumber(),
                base.getWaves(),
                base.getCastleHealth(),
                base.getMaxElixir() + profile.getElixirCapacityBonus(),
                MANA_REGEN_PER_SECOND);

        Hero hero = new Hero(GameSession.CASTLE_WIDTH + 36, WORLD_HEIGHT * 0.5 - Hero.SPRITE_HEIGHT * 0.5);
        session = new GameSession(adjusted, hero, profile.createCrossbow(crossbowDesign), profile);
        crossbowImage = loadCrossbowImage(crossbowDesign);
        imvCrossbow.setImage(crossbowImage);
        imvCrossbow.setViewport(null);
        imvCrossbow.setFitWidth(CROSSBOW_FIT_WIDTH);
        imvCrossbow.setFitHeight(CROSSBOW_FIT_HEIGHT);
        imvCrossbow.setPreserveRatio(true);
        imvCrossbow.setSmooth(true);
        imvCrossbow.setLayoutX(CROSSBOW_X);
        imvCrossbow.setLayoutY(CROSSBOW_Y);
        actorPane.getChildren().clear();
        projectilePane.getChildren().clear();
        effectsPane.getChildren().clear();
        hideSpellAim();
        monsterNodes.clear();
        projectileNodes.clear();
        activeMonsterScratch.clear();
        removedMonsterScratch.clear();
        activeProjectileScratch.clear();
        removedProjectileScratch.clear();
        spellButtonStates.clear();
        heroNode = new HeroNode(hero);
        actorPane.getChildren().add(heroNode.getNode());
        lastScore = 0;
        lastAwardedLevelPoints = 0;
        rewardSaved = false;
        endShown = false;
        heroSelected = false;
        selectedPower = null;
        heroMoveTarget = null;
        pressedKeys.clear();
        movementKeyOrder.clear();
        mouseFireHeld = false;
        pauseToggleReady = true;
        heroOutOfCombatSeconds = 0;
        lastHeroHealth = hero.getHealth();
        lastAim = new Vector2D(WORLD_WIDTH - 80, CROSSBOW_SOURCE_Y);
        spellPointer = new Vector2D(WORLD_WIDTH * 0.5, WORLD_HEIGHT * 0.5);
        lastCrossbowAngle = Double.NaN;
        hudUpdateAccumulator = 0;
        hudDirty = true;
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
            pauseOverlay.setManaged(false);
        }
        stopOverlayAnimation();
        if (btnPause != null) {
            btnPause.setDisable(false);
        }
        updateCrossbowAngle();
        updateHud();
        updateSpellHud();
        updateReviewHud();
        Platform.runLater(root::requestFocus);
        startLoop();
    }

    private void startLoop() {
        stopLoop();
        lastUpdate = 0;
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double delta = Math.min(0.05, (now - lastUpdate) / 1_000_000_000.0);
                lastUpdate = now;
                updateFrame(delta);
            }
        };
        gameLoop.start();
    }

    private void stopLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
        stopEffectAnimators();
    }

    private void stopCurrentLevelRuntime() {
        stopLoop();
        stopEntityAnimators();
        stopOverlayAnimation();
    }

    private void stopEntityAnimators() {
        if (heroNode != null) {
            heroNode.stop();
        }
        for (MonsterNode node : monsterNodes.values()) {
            node.stop();
        }
    }

    private void updateFrame(double deltaSeconds) {
        handleCrossbowAim(deltaSeconds);
        handleContinuousCrossbowFire();
        handleHeroMovement(deltaSeconds);
        session.update(deltaSeconds);
        playQueuedSessionSounds();
        handleHeroCombat(deltaSeconds);
        syncMonsterNodes();
        syncProjectileNodes();
        heroNode.update();
        showScorePopupIfNeeded();
        updateCrossbowAngle();
        updateSpellAim();
        updateHudIfNeeded(deltaSeconds);
        if (debugHitboxes) {
            updateDebugHitboxes();
        } else if (!debugRectMap.isEmpty()) {
            clearDebugHitboxes();
        }
        if ((session.isWon() || session.isLost()) && !endShown) {
            // Force one last HUD refresh so the castle bar is seen reaching 0
            // (or its final value) BEFORE the victory/defeat animation appears.
            updateHud();
            updateSpellHud();
            showEndOverlay();
        }
    }

    private void updateHudIfNeeded(double deltaSeconds) {
        hudUpdateAccumulator += deltaSeconds;
        if (!hudDirty && hudUpdateAccumulator < HUD_UPDATE_INTERVAL_SECONDS) {
            return;
        }
        hudUpdateAccumulator = 0;
        hudDirty = false;
        updateHud();
        updateSpellHud();
    }

    private void handleCrossbowAim(double deltaSeconds) {
        if (controlScheme != ControlScheme.KEYBOARD || session == null || session.isPaused()) {
            return;
        }
        double aimY = lastAim.getY();
        double speed = CROSSBOW_KEYBOARD_AIM_SPEED * deltaSeconds;
        if (pressedKeys.contains(KeyCode.UP)) {
            aimY -= speed;
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            aimY += speed;
        }
        aimY = clamp(aimY, 36, WORLD_HEIGHT - 36);
        lastAim = new Vector2D(CROSSBOW_SOURCE_X + 200, aimY);
    }

    private void handleContinuousCrossbowFire() {
        if (pressedKeys.contains(KeyCode.SPACE) || mouseFireHeld) {
            fireAtLastAim();
        }
    }

    private void handleHeroMovement(double deltaSeconds) {
        if (session == null || session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        if (session.getHero().isDead()) {
            heroMoveTarget = null;
            clearHeroSelection();
            return;
        }
        Direction direction = null;
        KeyCode lastMoveKey = movementKeyOrder.peekLast();
        if (lastMoveKey == KeyCode.A) {
            direction = Direction.LEFT;
        } else if (lastMoveKey == KeyCode.D) {
            direction = Direction.RIGHT;
        } else if (lastMoveKey == KeyCode.W) {
            direction = Direction.UP;
        } else if (lastMoveKey == KeyCode.S) {
            direction = Direction.DOWN;
        }
        if (direction != null) {
            heroMoveTarget = null;
            session.getHero().move(direction, deltaSeconds);
            clampHeroToWorld();
            return;
        }
        if (heroMoveTarget != null) {
            moveHeroTowardTarget(deltaSeconds);
        } else {
            session.getHero().stopMoving();
        }
    }

    private void moveHeroTowardTarget(double deltaSeconds) {
        Hero hero = session.getHero();
        Vector2D current = hero.getCenter();
        Vector2D toTarget = heroMoveTarget.subtract(current);
        if (toTarget.length() <= 8) {
            heroMoveTarget = null;
            hero.stopMoving();
            return;
        }
        Vector2D delta = toTarget.normalize().multiply(hero.getStats().getSpeed() * deltaSeconds);
        hero.moveBy(delta.clampLength(toTarget.length()));
        hero.setFacing(Direction.fromDelta(delta.getX(), delta.getY(), hero.getFacing()));
        hero.setState(EntityState.MOVING);
        clampHeroToWorld();
    }

    private void handleHeroCombat(double deltaSeconds) {
        Hero hero = session.getHero();
        if (hero.isDead()) {
            heroMoveTarget = null;
            clearHeroSelection();
            heroOutOfCombatSeconds = 0;
            lastHeroHealth = hero.getHealth();
            return;
        }
        boolean tookDamage = hero.getHealth() < lastHeroHealth;
        boolean inAttackReach = isMonsterNearHero();
        boolean underAttack = isHeroUnderAttack();
        if (inAttackReach) {
            if (session.heroAttackNearestMonster()) {
                heroOutOfCombatSeconds = 0;
                MusicManager.getInstance().playEffect(MusicManager.SoundEffect.HERO_ATTACK);
            }
            heroOutOfCombatSeconds = 0;
        } else if (tookDamage || underAttack) {
            heroOutOfCombatSeconds = 0;
        } else {
            heroOutOfCombatSeconds += deltaSeconds;
            if (heroOutOfCombatSeconds >= HERO_REGEN_DELAY_SECONDS) {
                hero.heal(hero.getStats().getMaxHealth() * HERO_REGEN_PER_SECOND * deltaSeconds);
            }
        }
        lastHeroHealth = hero.getHealth();
    }

    private void playQueuedSessionSounds() {
        int hits = session.consumeProjectileHitEvents();
        if (hits > 0) {
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.PROJECTILE_HIT);
        }
        int newWaves = session.consumeMonsterSpawnEvents();
        if (newWaves > 0) {
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.NEW_WAVE);
        }
        int enemyAttacks = session.consumeEnemyAttackEvents();
        if (enemyAttacks > 0) {
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.ENEMY_ATTACK);
        }
    }

    private boolean isMonsterNearHero() {
        if (session.getHero().isDead()) {
            return false;
        }
        for (Monster monster : session.getMonsters()) {
            if (monster.isAlive()
                    && session.getHero().getHitBox().expanded(session.getHero().getStats().getAttackRange() + HERO_ATTACK_PROXIMITY_PADDING)
                            .intersects(monster.getHitBox().getBounds())) {
                return true;
            }
        }
        return false;
    }

    private boolean isHeroUnderAttack() {
        Hero hero = session.getHero();
        if (hero.isDead()) {
            return false;
        }
        for (Monster monster : session.getMonsters()) {
            if (monster.isAlive() && monster.isTargetInRange(hero)) {
                return true;
            }
        }
        return false;
    }

    private void clampHeroToWorld() {
        Hero hero = session.getHero();
        double minX = GameSession.CASTLE_WIDTH + 4;
        double maxX = WORLD_WIDTH - Hero.SPRITE_WIDTH - 4;
        double x = clamp(hero.getX(), minX, maxX);
        double minY = walkableTopYForHero(x);
        double maxY = WORLD_HEIGHT - Hero.SPRITE_HEIGHT - 6;
        double y = clamp(hero.getY(), minY, maxY);
        if (x != hero.getX() || y != hero.getY()) {
            hero.setPosition(x, y);
        }
    }

    private Vector2D clampHeroMoveTarget(Vector2D target) {
        double minCenterX = GameSession.CASTLE_WIDTH + 4 + Hero.SPRITE_WIDTH * 0.5;
        double maxCenterX = WORLD_WIDTH - 4 - Hero.SPRITE_WIDTH * 0.5;
        double x = clamp(target.getX(), minCenterX, maxCenterX);
        double minCenterY = walkableTopYForHero(x - Hero.SPRITE_WIDTH * 0.5) + Hero.SPRITE_HEIGHT * 0.5;
        double maxCenterY = WORLD_HEIGHT - 6 - Hero.SPRITE_HEIGHT * 0.5;
        double y = clamp(target.getY(), minCenterY, maxCenterY);
        return new Vector2D(x, y);
    }

    private double walkableTopYForHero(double heroX) {
        double centerX = heroX + Hero.SPRITE_WIDTH * 0.5;
        double t = clamp((centerX - GameSession.CASTLE_WIDTH) / WALKABLE_RAMP_WIDTH, 0, 1);
        return WALKABLE_LEFT_TOP_Y + (WALKABLE_FIELD_TOP_Y - WALKABLE_LEFT_TOP_Y) * t;
    }

    private void clearHeroSelection() {
        if (!heroSelected) {
            return;
        }
        heroSelected = false;
        if (heroNode != null) {
            heroNode.setSelected(false);
        }
    }

    private void onWorldMousePressed(MouseEvent event) {
        mouseFireHeld = false;
        Vector2D worldPoint = worldPointFromMouseEvent(event);
        spellPointer = worldPoint;
        if (session != null && session.getHero().isDead()) {
            heroMoveTarget = null;
            clearHeroSelection();
        }
        if (selectedPower != null) {
            castSelectedPowerAt(worldPoint);
            root.requestFocus();
            return;
        }
        if (isHeroClicked(worldPoint)) {
            selectHeroForMovement();
            root.requestFocus();
            return;
        }
        if (heroSelected && worldPoint.getX() > GameSession.CASTLE_WIDTH + 18) {
            Vector2D target = clampHeroMoveTarget(worldPoint);
            boolean adjustedToGround = Math.abs(target.getY() - worldPoint.getY()) > 1.0;
            heroMoveTarget = target;
            clearHeroSelection();
            root.requestFocus();
            return;
        }
        lastAim = worldPoint;
        if (controlScheme == ControlScheme.MOUSE) {
            mouseFireHeld = true;
            fireAtLastAim();
        }
        
        root.requestFocus();
    }

    private void onWorldMouseDragged(MouseEvent event) {
        if (session == null) {
            return;
        }
        // Always track the mouse for spell aiming, even in keyboard mode.
        spellPointer = worldPointFromMouseEvent(event);
        updateSpellAim();
        if (!mouseFireHeld || controlScheme != ControlScheme.MOUSE
                || session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        lastAim = spellPointer;
        updateCrossbowAngle();
        event.consume();
    }

    private void onWorldMouseMoved(MouseEvent event) {
        if (session == null) {
            return;
        }
        // The spell aim circle follows the mouse in both control schemes.
        spellPointer = worldPointFromMouseEvent(event);
        updateSpellAim();
        if (controlScheme != ControlScheme.MOUSE) {
            return;
        }
        lastAim = spellPointer;
        updateCrossbowAngle();
    }

    private void onWorldMouseReleased(MouseEvent event) {
        mouseFireHeld = false;
    }

    private Vector2D worldPointFromMouseEvent(MouseEvent event) {
        Point2D point = worldPane.sceneToLocal(event.getSceneX(), event.getSceneY());
        return new Vector2D(clamp(point.getX(), 0, WORLD_WIDTH), clamp(point.getY(), 0, WORLD_HEIGHT));
    }

    private boolean isHeroClicked(Vector2D point) {
        Hero hero = session.getHero();
        return hero.isAlive() && hero.getHitBox().expanded(46).contains(point.getX(), point.getY());
    }

    private void selectHeroForMovement() {
        if (session == null || session.isPaused() || session.isWon() || session.isLost()
                || session.getHero().isDead()) {
            clearHeroSelection();
            return;
        }
        selectedPower = null;
        heroSelected = true;
        heroNode.setSelected(true);
        hideSpellAim(); 
        updateSpellHud();
        root.requestFocus();
    }

    private void selectPower(SpecialPowerType type) {
        if (session == null || type == null || session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        SpecialPower power = session.getPower(type);
        if (power == null) {
            return;
        }
        if (!power.canUse(session.getElixir(), session.getLevelDefinition().getMaxElixir())) {
            hideSpellAim();
            updateSpellHud();
            root.requestFocus();
            return;
        }
        selectedPower = selectedPower == type ? null : type;
        heroSelected = false;
        if (heroNode != null) {
            heroNode.setSelected(false);
        }
        if (selectedPower != null) {
            showSpellAim(power.getRadius());
        } else {
            hideSpellAim();
        }
        updateSpellHud();
        root.requestFocus();
    }

    private void castSelectedPowerAt(Vector2D worldPoint) {
        if (selectedPower == null || session == null || worldPoint == null) {
            return;
        }
        SpecialPowerType type = selectedPower;
        SpecialPower power = session.getPower(type);
        if (power == null || !power.canUse(session.getElixir(), session.getLevelDefinition().getMaxElixir())) {
            selectedPower = null;
            hideSpellAim();
            updateSpellHud();
            return;
        }
        if (!session.beginPowerCast(type)) {
            selectedPower = null;
            hideSpellAim();
            updateSpellHud();
            return;
        }
        hideSpellAim();
        double fx = worldPoint.getX();
        double fy = worldPoint.getY() - effectsPane.getLayoutY();
        MusicManager.getInstance().playEffect(type == SpecialPowerType.ICE
                ? MusicManager.SoundEffect.ICE_CAST
                : MusicManager.SoundEffect.METEOR_CAST);
        showSpellEffect(type, fx, fy, power.getRadius(), () -> {
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.SPELL_IMPACT);
            SpecialPowerCast cast = session.resolvePowerCast(type, worldPoint.getX(), worldPoint.getY());
            String detail = type == SpecialPowerType.ICE
                    ? String.format("Congelados: %d por %.1fs.", cast.getAffectedMonsters(), cast.getFreezeSeconds())
                    : "Impactados: " + cast.getAffectedMonsters() + ".";
            
        });
        selectedPower = null;
        updateSpellHud();
    }

    private void loadSpellIconImages() {
        if (!spellIconImages.isEmpty()) {
            return;
        }
        spellIconImages.put(SpecialPowerType.METEOR, new Image[]{
            loadResourceImage("Meteor-Spell-Normal.png"),
            loadResourceImage("Meteor-Spell-Selected.png"),
            loadResourceImage("Meteor-Spell-Cooldown.png")
        });
        spellIconImages.put(SpecialPowerType.ICE, new Image[]{
            loadResourceImage("Ice-Spell-Normal.png"),
            loadResourceImage("Ice-Spell-Selected.png"),
            loadResourceImage("Ice-Spell-Cooldown.png")
        });
    }

    private void updateSpellHud() {
        updateSpellButton(SpecialPowerType.METEOR, btnMeteorSpell, imvMeteorSpell);
        updateSpellButton(SpecialPowerType.ICE, btnIceSpell, imvIceSpell);
    }

    private void updateSpellButton(SpecialPowerType type, MFXButton button, ImageView imageView) {
        if (button == null || imageView == null || session == null) {
            return;
        }
        SpecialPower power = session.getPower(type);
        boolean selected = selectedPower == type;
        boolean cooldown = power != null && power.getCooldownRemaining() > 0;
        boolean enoughMana = power != null && session.getElixir() >= power.getManaCost(session.getLevelDefinition().getMaxElixir());
        Image[] icons = spellIconImages.get(type);
        SpellButtonVisualState visualState = cooldown || !enoughMana
                ? SpellButtonVisualState.COOLDOWN
                : selected ? SpellButtonVisualState.SELECTED : SpellButtonVisualState.READY;
        if (icons != null) {
            Image icon = icons[visualState == SpellButtonVisualState.COOLDOWN ? 2
                    : visualState == SpellButtonVisualState.SELECTED ? 1 : 0];
            if (imageView.getImage() != icon) {
                imageView.setImage(icon);
            }
        }
        if (spellButtonStates.get(type) != visualState) {
            spellButtonStates.put(type, visualState);
            setStyleClassEnabled(button, "jfx-btn-spell-selected", visualState == SpellButtonVisualState.SELECTED);
            setStyleClassEnabled(button, "jfx-btn-spell-cooldown", visualState == SpellButtonVisualState.COOLDOWN);
        }
    }

    private void setStyleClassEnabled(Node node, String styleClass, boolean enabled) {
        if (node == null || styleClass == null) {
            return;
        }
        boolean present = node.getStyleClass().contains(styleClass);
        if (enabled && !present) {
            node.getStyleClass().add(styleClass);
        } else if (!enabled && present) {
            node.getStyleClass().remove(styleClass);
        }
    }

    private void onKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (movementKeys.contains(code)) {
            if (pressedKeys.add(code)) {
                movementKeyOrder.remove(code);
                movementKeyOrder.addLast(code);
            }
            event.consume();
            return;
        }
        if (controlScheme == ControlScheme.KEYBOARD && (code == KeyCode.UP || code == KeyCode.DOWN)) {
            pressedKeys.add(code);
            event.consume();
            return;
        }
        if (code == KeyCode.P || code == KeyCode.ESCAPE) {
            if (pauseToggleReady) {
                pauseToggleReady = false;
                togglePause();
            }
            event.consume();
        } else if (code == KeyCode.SPACE) {
            pressedKeys.add(code);
            fireAtLastAim();
            event.consume();
        } else if (code == KeyCode.E) {
            if (session.heroAttackNearestMonster()) {
                MusicManager.getInstance().playEffect(MusicManager.SoundEffect.HERO_ATTACK);
            }
            event.consume();
        } else if (code == KeyCode.ENTER && session != null && session.isPaused()) {
            resumeGame();
            event.consume();
        } else if (code == KeyCode.F1) {
            debugHitboxes = !debugHitboxes;
            if (!debugHitboxes) clearDebugHitboxes();
            event.consume();
        }
    }

    private void onKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        pressedKeys.remove(code);
        movementKeyOrder.remove(code);
        if (code == KeyCode.P || code == KeyCode.ESCAPE) {
            pauseToggleReady = true;
        }
    }

    private void fireAtLastAim() {
        if (session == null || session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        Projectile projectile = session.fireCrossbow(
                new Vector2D(CROSSBOW_SOURCE_X, CROSSBOW_SOURCE_Y), lastAim);
        if (projectile != null) {
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.CROSSBOW_SHOT);
        }
    }

    private void pauseGame() {
        if (session == null || session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        mouseFireHeld = false;
        session.setPaused(true);
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(true);
            pauseOverlay.setManaged(true);
        }
        if (btnResumeOverlay != null) {
            btnResumeOverlay.setVisible(true);
            btnResumeOverlay.setManaged(true);
        }
        playOverlayAnimation(OverlayAnimationType.PAUSE);
        pauseAnimators();
        MusicManager.getInstance().pauseTrack();
    }

    private void resumeGame() {
        if (session == null || !session.isPaused() || session.isWon() || session.isLost()) {
            return;
        }
        mouseFireHeld = false;
        session.setPaused(false);
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
            pauseOverlay.setManaged(false);
        }
        stopOverlayAnimation();
        resumeAnimators();
        MusicManager.getInstance().resumeTrack();
        root.requestFocus();
    }

    private void togglePause() {
        if (session == null) {
            return;
        }
        if (session.isPaused()) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void syncMonsterNodes() {
        activeMonsterScratch.clear();
        activeMonsterScratch.addAll(session.getMonsters());
        removedMonsterScratch.clear();
        for (Monster monster : monsterNodes.keySet()) {
            if (!activeMonsterScratch.contains(monster)) {
                removedMonsterScratch.add(monster);
            }
        }
        for (Monster monster : removedMonsterScratch) {
            MonsterNode node = monsterNodes.remove(monster);
            actorPane.getChildren().remove(node.getNode());
        }
        for (Monster monster : session.getMonsters()) {
            MonsterNode node = monsterNodes.get(monster);
            if (node == null) {
                node = new MonsterNode(monster);
                monsterNodes.put(monster, node);
                actorPane.getChildren().add(node.getNode());
            }
            node.update();
        }
    }

    private void syncProjectileNodes() {
        activeProjectileScratch.clear();
        activeProjectileScratch.addAll(session.getProjectiles());
        removedProjectileScratch.clear();
        for (Projectile projectile : projectileNodes.keySet()) {
            if (!activeProjectileScratch.contains(projectile)) {
                removedProjectileScratch.add(projectile);
            }
        }
        for (Projectile projectile : removedProjectileScratch) {
            ImageView node = projectileNodes.remove(projectile);
            projectilePane.getChildren().remove(node);
        }
        for (Projectile projectile : session.getProjectiles()) {
            ImageView node = projectileNodes.get(projectile);
            if (node == null) {
                node = createProjectileNode(projectile);
                projectileNodes.put(projectile, node);
                projectilePane.getChildren().add(node);
            }
            updateProjectileNode(projectile, node);
        }
    }

    private ImageView createProjectileNode(Projectile projectile) {
        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        if (projectile.getImageAsset() != null) {
            imageView.setImage(loadResourceImage("enemy/" + projectile.getImageAsset()));
            if ("cat_bullet.png".equals(projectile.getImageAsset())) {
                imageView.setViewport(new Rectangle2D(492, 328, 437, 110));
                imageView.setFitWidth(160);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(false);
                imageView.setScaleX(-1); 
            } else {
                imageView.setViewport(new Rectangle2D(1029, 2, 331, 54));
                imageView.setFitWidth(140);
                imageView.setFitHeight(23);
                imageView.setPreserveRatio(false);
                imageView.setScaleX(1);
            }
        } else {
            imageView.setImage(arrowImage);
            imageView.setViewport(null);
            imageView.setFitWidth(projectile.getWidth());
            imageView.setFitHeight(projectile.getHeight());
            imageView.setPreserveRatio(true);
            imageView.setScaleX(1);
        }
        imageView.setVisible(true);
        return imageView;
    }

    private void updateProjectileNode(Projectile projectile, ImageView imageView) {
        if (projectile.getImageAsset() != null) {
            imageView.setLayoutX(projectile.getX() - imageView.getFitWidth() * 0.5);
            imageView.setLayoutY(projectile.getY() - imageView.getFitHeight() * 0.5);
        } else {
            imageView.setLayoutX(projectile.getX());
            imageView.setLayoutY(projectile.getY() - imageView.getFitHeight() * 0.5);
        }
        Vector2D velocity = projectile.getVelocity();
        imageView.setRotate(Math.toDegrees(Math.atan2(velocity.getY(), velocity.getX())));
    }

    private void updateDebugHitboxes() {
        java.util.Set<Object> seen = new java.util.HashSet<>();
        for (Map.Entry<Projectile, ImageView> e : projectileNodes.entrySet()) {
            Projectile p = e.getKey();
            javafx.geometry.Rectangle2D hb = p.getHitBox().getBounds();
            Rectangle r = debugRectMap.computeIfAbsent(p, k -> makeDebugRect("lime"));
            r.setX(hb.getMinX()); r.setY(hb.getMinY());
            r.setWidth(hb.getWidth()); r.setHeight(hb.getHeight());
            if (!effectsPane.getChildren().contains(r)) effectsPane.getChildren().add(r);
            seen.add(p);
        }
        for (Map.Entry<Monster, MonsterNode> e : monsterNodes.entrySet()) {
            Monster m = e.getKey();
            javafx.geometry.Rectangle2D hb = m.getHitBox().getBounds();
            Rectangle r = debugRectMap.computeIfAbsent(m, k -> makeDebugRect("red"));
            r.setX(hb.getMinX()); r.setY(hb.getMinY());
            r.setWidth(hb.getWidth()); r.setHeight(hb.getHeight());
            if (!effectsPane.getChildren().contains(r)) effectsPane.getChildren().add(r);
            seen.add(m);
        }
        java.util.Iterator<Map.Entry<Object, Rectangle>> it = debugRectMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Rectangle> entry = it.next();
            if (!seen.contains(entry.getKey())) {
                effectsPane.getChildren().remove(entry.getValue());
                it.remove();
            }
        }
    }

    private void clearDebugHitboxes() {
        for (Rectangle r : debugRectMap.values()) {
            effectsPane.getChildren().remove(r);
        }
        debugRectMap.clear();
    }

    private Rectangle makeDebugRect(String colorName) {
        Rectangle r = new Rectangle();
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.web(colorName));
        r.setStrokeWidth(1.5);
        r.setMouseTransparent(true);
        r.setManaged(false);
        return r;
    }

    private void showScorePopupIfNeeded() {
        int score = session.getScore();
        if (score <= lastScore) {
            return;
        }
        int gained = score - lastScore;
        lastScore = score;
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.MONSTER_DEFEATED);
        showFloatingText("+" + gained, lastAim.getX(), lastAim.getY() - 20);
    }

    private void showFloatingText(String text, double x, double y) {
        Label label = new Label(text);
        label.getStyleClass().add("jfx-label-coin-popup");
        label.setLayoutX(clamp(x, 220, WORLD_WIDTH - 120));
        label.setLayoutY(clamp(y, 24, WORLD_HEIGHT - 40));
        effectsPane.getChildren().add(label);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(label.opacityProperty(), 1),
                        new KeyValue(label.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(0.9),
                        new KeyValue(label.opacityProperty(), 0),
                        new KeyValue(label.translateYProperty(), -34)));
        playEffectTimeline(timeline, () -> effectsPane.getChildren().remove(label));
    }

    private void showSpellEffect(SpecialPowerType type, double x, double y, double radius, Runnable onImpact) {
        playSpellSpriteAnimation(type, x, y, radius, onImpact);
    }

    private void showSpellAim(double radius) {
        if (spellAimImage == null || aimOverlay == null) {
            return;
        }
        double srcDiameter = 961.0;
        double scale = (radius * 2.0) / srcDiameter;
        double imgW = 1024.0 * scale;
        double imgH = 1048.0 * scale;
        aimOverlay.setFitWidth(imgW);
        aimOverlay.setFitHeight(imgH);
        double fx = spellPointer.getX();
        double fy = spellPointer.getY() - effectsPane.getLayoutY();
        aimOverlay.setLayoutX(fx - imgW / 2.0);
        aimOverlay.setLayoutY(fy - imgH / 2.0);
        if (!effectsPane.getChildren().contains(aimOverlay)) {
            effectsPane.getChildren().add(aimOverlay);
        }
        aimOverlay.setVisible(true);
        aimOverlay.toFront();
    }

    private void hideSpellAim() {
        if (aimOverlay != null) {
            aimOverlay.setVisible(false);
        }
    }

    private void positionSpellAim(double fx, double fy) {
        if (aimOverlay == null || !aimOverlay.isVisible() || selectedPower == null || session == null) {
            return;
        }
        SpecialPower power = session.getPower(selectedPower);
        if (power == null) {
            return;
        }
        double radius = power.getRadius();
        double srcDiameter = 961.0;
        double scale = (radius * 2.0) / srcDiameter;
        double imgW = 1024.0 * scale;
        double imgH = 1048.0 * scale;
        aimOverlay.setFitWidth(imgW);
        aimOverlay.setFitHeight(imgH);
        aimOverlay.setLayoutX(fx - imgW / 2.0);
        aimOverlay.setLayoutY(fy - imgH / 2.0);
    }

    private void updateSpellAim() {
        if (selectedPower != null && aimOverlay != null && aimOverlay.isVisible()) {
            double fx = spellPointer.getX();
            double fy = spellPointer.getY() - effectsPane.getLayoutY();
            positionSpellAim(fx, fy);
        }
    }

    private void playSpellSpriteAnimation(SpecialPowerType type, double x, double y, double radius, Runnable onImpact) {
        SpriteAnimationSpec spec = SpriteCatalog.getSpellAnimation(spellAnimationIdFor(type));
        if (spec == null) {
            if (onImpact != null) {
                onImpact.run();
            }
            return;
        }
        ImageView view = new ImageView();
        view.setMouseTransparent(true);
        view.setSmooth(true);
        effectsPane.getChildren().add(view);
        SpriteAnimator animator = new SpriteAnimator(view, spec);
        animator.setSmooth(true);
        double scale = spellScaleFor(type, radius, spec);
        animator.setScale(scale);
        view.setLayoutX(x - spec.getFrameWidth() * scale * 0.5);
        view.setLayoutY(y - spec.getFrameHeight() * scale * 0.5);
        effectAnimators.add(animator);

        Timeline impactTimeline = new Timeline(
                new KeyFrame(Duration.seconds(spellImpactFrame(type) / spec.getFps()), event -> {
                    if (onImpact != null) {
                        onImpact.run();
                    }
                }));
        playEffectTimeline(impactTimeline, null);

        animator.playOnce(() -> {
            effectAnimators.remove(animator);
            effectsPane.getChildren().remove(view);
        });
    }

    private SpriteAnimationId spellAnimationIdFor(SpecialPowerType type) {
        return type == SpecialPowerType.ICE
                ? SpriteAnimationId.ICE_SPELL_FX
                : SpriteAnimationId.METEOR_SPELL_FX;
    }

    private int spellImpactFrame(SpecialPowerType type) {
        return type == SpecialPowerType.ICE ? 20 : 30;
    }

    private double spellScaleFor(SpecialPowerType type, double radius, SpriteAnimationSpec spec) {
        double diameter = radius * (type == SpecialPowerType.ICE ? 2.30 : 2.45);
        return clamp(diameter / spec.getFrameWidth(), type == SpecialPowerType.ICE ? 0.42 : 0.38,
                type == SpecialPowerType.ICE ? 0.76 : 0.68);
    }

    private void playEffectTimeline(Timeline timeline, Runnable onFinished) {
        effectTimelines.add(timeline);
        timeline.setOnFinished(event -> {
            effectTimelines.remove(timeline);
            if (onFinished != null) {
                onFinished.run();
            }
        });
        timeline.play();
    }

    private void stopEffectAnimators() {
        for (SpriteAnimator animator : new ArrayList<>(effectAnimators)) {
            animator.stop();
        }
        effectAnimators.clear();
        for (Timeline timeline : new ArrayList<>(effectTimelines)) {
            timeline.stop();
        }
        effectTimelines.clear();
    }

    private void updateHud() {
        if (session == null) {
            return;
        }
        setLabelText(lbLevel, "Nivel " + session.getLevelDefinition().getNumber());
        double multiplier = levelScoreMultiplier(session.getCastle().getHealthPercent());
        setLabelText(lbLevelPoints, calculateLevelPoints(session.getScore(), session.getCastle().getHealthPercent()) + " pts");
        setLabelText(lbScoreMultiplier, formatMultiplier(multiplier));
        setLabelText(lbTimer, formatTime(session.getRemainingSeconds()));
        setLabelText(lbWaveValue, session.getDefeatedMonsters() + "/" + session.getTotalMonsters());
        setLabelText(lbCastleValue, (int) Math.ceil(session.getCastle().getHealth()) + " / "
                + (int) Math.ceil(session.getCastle().getStats().getMaxHealth()));
        setLabelText(lbManaValue, (int) Math.floor(session.getElixir()) + " / "
                + (int) Math.ceil(session.getLevelDefinition().getMaxElixir()));
        setProgress(prgWave, session.getProgress());
        setProgress(prgLevelTime, session.getTimeProgress());
        setProgress(prgCastle, session.getCastle().getHealthPercent());
        setProgress(prgMana, session.getElixir() / session.getLevelDefinition().getMaxElixir());
        updateReviewHud();
    }

    private void setLabelText(Label label, String text) {
        if (label != null && text != null && !text.equals(label.getText())) {
            label.setText(text);
        }
    }

    private void setProgress(ProgressBar progressBar, double progress) {
        if (progressBar == null) {
            return;
        }
        double safeProgress = clamp(progress, 0, 1);
        if (Math.abs(progressBar.getProgress() - safeProgress) > 0.001) {
            progressBar.setProgress(safeProgress);
        }
    }

    private void updateReviewHud() {
        boolean reviewMode = isReviewModeEnabled();
        if (hbReviewHud != null) {
            hbReviewHud.setVisible(reviewMode);
            hbReviewHud.setManaged(reviewMode);
        }
        if (reviewMode && lbReviewMode != null) {
            lbReviewMode.setText("REVISION - NIVEL " + currentLevel);
        }
    }

    private int calculateLevelPoints(int baseScore, double castleHealthPercent) {
        return (int) Math.round(Math.max(0, baseScore) * levelScoreMultiplier(castleHealthPercent));
    }

    private double levelScoreMultiplier(double castleHealthPercent) {
        double safeHealth = clamp(castleHealthPercent, 0, 1);
        if (safeHealth < 0.5) {
            return 1.0;
        }
        return 1.0 + ((safeHealth - 0.5) / 0.5) * 0.5;
    }

    private String formatMultiplier(double multiplier) {
        return String.format("x%.2f", multiplier);
    }

    private void jumpToReviewLevel(int requestedLevel, String statusMessage) {
        if (!isReviewModeEnabled()) {
            return;
        }
        int targetLevel = Math.max(1, Math.min(100, requestedLevel));
        AppContext.getInstance().set(REVIEW_LEVEL_KEY, targetLevel);
        startLevel();
    }

    private void showEndOverlay() {
        endShown = true;
        saveReward();
        stopCurrentLevelRuntime();
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(true);
            pauseOverlay.setManaged(true);
        }
        if (btnResumeOverlay != null) {
            btnResumeOverlay.setVisible(false);
            btnResumeOverlay.setManaged(false);
        }
        if (btnPause != null) {
            btnPause.setDisable(true);
        }
        if (session.isWon()) {
            boolean reviewMode = isReviewModeEnabled();
            int earnedCoins = reviewMode ? 0 : session.getScore();
            double multiplier = levelScoreMultiplier(session.getCastle().getHealthPercent());
            playOverlayAnimation(OverlayAnimationType.VICTORY);
            advanceLevelAfterWin();
            MusicManager.getInstance().playEffect(currentLevel >= 100
                    ? MusicManager.SoundEffect.GAME_COMPLETE
                    : MusicManager.SoundEffect.VICTORY);
        } else {
            playOverlayAnimation(OverlayAnimationType.DEFEAT);
            MusicManager.getInstance().playEffect(MusicManager.SoundEffect.DEFEAT);
        }
    }

    private void saveReward() {
        if (rewardSaved || session == null || !session.isWon()) {
            return;
        }
        if (isReviewModeEnabled()) {
            lastAwardedLevelPoints = 0;
            rewardSaved = true;
            return;
        }
        int earnedCoins = session.getScore();
        lastAwardedLevelPoints = calculateLevelPoints(earnedCoins, session.getCastle().getHealthPercent());
        setGold(getGold() + earnedCoins);
        PlayerRegistry.addHistoricalPoints(lastAwardedLevelPoints);
        rewardSaved = true;
    }

    private void advanceLevelAfterWin() {
        int nextLevel = Math.min(100, currentLevel + 1);
        if (isReviewModeEnabled()) {
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, nextLevel);
        } else {
            PlayerRegistry.setCurrentLevel(nextLevel);
        }
    }

    private void resizeWorld() {
        double w = gameWorld.getWidth();
        double h = gameWorld.getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        double scaleX = w / WORLD_WIDTH;
        double scaleY = h / WORLD_HEIGHT;
        worldPane.getTransforms().setAll(new Scale(scaleX, scaleY, 0, 0));
    }

    private void playOverlayAnimation(OverlayAnimationType type) {
        if (imvOverlayAnimation == null || type == null) {
            return;
        }
        OverlayAnimationSequence sequence = getOverlaySequence(type);
        currentOverlaySequence = sequence;
        if (overlayAnimator == null) {
            overlayAnimator = new OverlaySpriteAnimator(imvOverlayAnimation);
        }
        imvOverlayAnimation.setVisible(true);
        imvOverlayAnimation.setManaged(true);
        overlayAnimator.setSequence(sequence);
        resizeOverlayAnimation();
        overlayAnimator.play(sequence.isLoop(), null);
    }

    private void stopOverlayAnimation() {
        if (overlayAnimator != null) {
            overlayAnimator.stop();
        }
        currentOverlaySequence = null;
        if (imvOverlayAnimation != null) {
            imvOverlayAnimation.setImage(null);
            imvOverlayAnimation.setVisible(false);
            imvOverlayAnimation.setManaged(false);
        }
    }

    private void resizeOverlayAnimation() {
        if (imvOverlayAnimation == null || currentOverlaySequence == null || pauseOverlay == null) {
            return;
        }
        double overlayWidth = pauseOverlay.getWidth() > 0 ? pauseOverlay.getWidth() : battlefieldFrame.getWidth();
        double overlayHeight = pauseOverlay.getHeight() > 0 ? pauseOverlay.getHeight() : battlefieldFrame.getHeight();
        if (overlayWidth <= 0 || overlayHeight <= 0) {
            return;
        }
        double targetWidth = Math.max(170, Math.min(430, overlayWidth * 0.52));
        double targetHeight = Math.max(140, Math.min(300, overlayHeight * 0.62));
        imvOverlayAnimation.setFitWidth(targetWidth);
        imvOverlayAnimation.setFitHeight(targetHeight);
    }

    private OverlayAnimationSequence getOverlaySequence(OverlayAnimationType type) {
        OverlayAnimationSequence sequence = overlaySequences.get(type);
        if (sequence == null) {
            sequence = buildOverlaySequence(type);
            overlaySequences.put(type, sequence);
        }
        return sequence;
    }

    private OverlayAnimationSequence buildOverlaySequence(OverlayAnimationType type) {
        String fileName;
        int frameCount = OVERLAY_FRAME_COUNT;
        if (type == OverlayAnimationType.PAUSE) {
            fileName = "PauseAnimation.png";
        } else if (type == OverlayAnimationType.VICTORY) {
            fileName = "VictoryAnimation.png";
        } else {
            fileName = "DefeatAnimation.png";
            frameCount = DEFEAT_TEXT_FRAME_COUNT;
        }
        Image image = loadResourceImage(fileName);
        return new OverlayAnimationSequence(
                image,
                gridFramesForImage(image, OVERLAY_ATLAS_COLUMNS, OVERLAY_ATLAS_ROWS, frameCount),
                OVERLAY_ANIMATION_FPS,
                false);
    }

    private List<Rectangle2D> gridFramesForImage(Image image, int columns, int rows, int frameCount) {
        List<Rectangle2D> frames = new ArrayList<>();
        double frameWidth = image.getWidth() / columns;
        double frameHeight = image.getHeight() / rows;
        for (int i = 0; i < frameCount; i++) {
            int column = i % columns;
            int row = i / columns;
            frames.add(new Rectangle2D(
                    column * frameWidth,
                    row * frameHeight,
                    frameWidth,
                    frameHeight));
        }
        return frames;
    }

    private void updateCrossbowAngle() {
        Vector2D direction = lastAim.subtract(new Vector2D(CROSSBOW_SOURCE_X, CROSSBOW_SOURCE_Y));
        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        if (Double.isNaN(lastCrossbowAngle) || Math.abs(angle - lastCrossbowAngle) > 0.15) {
            lastCrossbowAngle = angle;
            imvCrossbow.setRotate(angle);
        }
    }

    private void pauseAnimators() {
        if (heroNode != null) {
            heroNode.pause();
        }
        for (MonsterNode node : monsterNodes.values()) {
            node.pause();
        }
        for (SpriteAnimator animator : effectAnimators) {
            animator.pause();
        }
        for (Timeline timeline : effectTimelines) {
            timeline.pause();
        }
    }

    private void resumeAnimators() {
        if (heroNode != null) {
            heroNode.resume();
        }
        for (MonsterNode node : monsterNodes.values()) {
            node.resume();
        }
        for (SpriteAnimator animator : effectAnimators) {
            animator.resume();
        }
        for (Timeline timeline : effectTimelines) {
            timeline.play();
        }
    }

    private UpgradeProfile getProfile() {
        if (isReviewModeEnabled()) {
            return getReviewProfile();
        }
        return getPlayerProfile();
    }

    private UpgradeProfile getReviewProfile() {
        Object value = AppContext.getInstance().get(REVIEW_UPGRADE_PROFILE_KEY);
        if (value instanceof UpgradeProfile) {
            return (UpgradeProfile) value;
        }
        UpgradeProfile reviewProfile = copyProfile(getPlayerProfile());
        AppContext.getInstance().set(REVIEW_UPGRADE_PROFILE_KEY, reviewProfile);
        return reviewProfile;
    }

    private UpgradeProfile copyProfile(UpgradeProfile source) {
        UpgradeProfile copy = new UpgradeProfile();
        if (source != null) {
            for (Map.Entry<UpgradeType, Integer> entry : source.getLevels().entrySet()) {
                copy.setLevel(entry.getKey(), entry.getValue());
            }
        }
        return copy;
    }

    private UpgradeProfile getPlayerProfile() {
        Object value = AppContext.getInstance().get(UPGRADE_PROFILE_KEY);
        if (value instanceof UpgradeProfile) {
            return (UpgradeProfile) value;
        }
        UpgradeProfile profile = new UpgradeProfile();
        PlayerRegistry.setUpgradeProfile(profile);
        return profile;
    }

    private ControlScheme getControlScheme() {
        Object value = AppContext.getInstance().get(CONTROL_SCHEME_KEY);
        if (value instanceof ControlScheme) {
            return (ControlScheme) value;
        }
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.MOUSE);
        return ControlScheme.MOUSE;
    }

    private CrossbowDesign getCrossbowDesign() {
        Object value = AppContext.getInstance().get(CROSSBOW_DESIGN_KEY);
        if (value instanceof CrossbowDesign) {
            return (CrossbowDesign) value;
        }
        PlayerRegistry.setCrossbowDesign(CrossbowDesign.GREEN);
        return CrossbowDesign.GREEN;
    }

    private int getGold() {
        Object value = AppContext.getInstance().get(GOLD_KEY);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        PlayerRegistry.setGold(0);
        return 0;
    }

    private void setGold(int gold) {
        PlayerRegistry.setGold(gold);
    }

    private boolean isReviewModeEnabled() {
        Object value = AppContext.getInstance().get(REVIEW_MODE_KEY);
        return value instanceof Boolean && (Boolean) value;
    }

    private int getCurrentLevel() {
        if (isReviewModeEnabled()) {
            Object reviewLevel = AppContext.getInstance().get(REVIEW_LEVEL_KEY);
            if (reviewLevel instanceof Number) {
                return Math.max(1, Math.min(100, ((Number) reviewLevel).intValue()));
            }
            Object realLevel = AppContext.getInstance().get(CURRENT_LEVEL_KEY);
            int fallback = realLevel instanceof Number ? ((Number) realLevel).intValue() : 1;
            int safeFallback = Math.max(1, Math.min(100, fallback));
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, safeFallback);
            return safeFallback;
        }
        Object value = AppContext.getInstance().get(CURRENT_LEVEL_KEY);
        if (value instanceof Number) {
            return Math.max(1, Math.min(100, ((Number) value).intValue()));
        }
        PlayerRegistry.setCurrentLevel(1);
        return 1;
    }

    private Image loadCrossbowImage(CrossbowDesign design) {
        CrossbowDesign effectiveDesign = design == null ? CrossbowDesign.GREEN : design;
        return loadResourceImage(effectiveDesign.getResourceName());
    }

    private Image loadArrowImage(CrossbowDesign design) {
        if (design == CrossbowDesign.PURPLE) {
            return loadResourceImage("Arrow-Purple.png");
        }
        return loadResourceImage("Arrow-Green.png");
    }

    private Image loadResourceImage(String fileName) {
        return SpriteAnimationSpec.loadCachedResource(SpriteCatalog.resourcePath(fileName));
    }

    private String formatTime(double seconds) {
        int safeSeconds = Math.max(0, (int) Math.ceil(seconds));
        int minutes = safeSeconds / 60;
        int remainder = safeSeconds % 60;
        return String.format("%02d:%02d", minutes, remainder);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}

