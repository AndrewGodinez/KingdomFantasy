package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
import cr.ac.una.kingdomfantasy.model.UpgradeProfile;
import cr.ac.una.kingdomfantasy.model.UpgradeType;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import cr.ac.una.kingdomfantasy.util.PlayerRegistry;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MejorasController extends Controller implements Initializable {

    private static final String GOLD_KEY = PlayerRegistry.GOLD_KEY;
    private static final String UPGRADE_PROFILE_KEY = PlayerRegistry.UPGRADE_PROFILE_KEY;
    private static final String CROSSBOW_DESIGN_KEY = PlayerRegistry.CROSSBOW_DESIGN_KEY;
    private static final String CURRENT_LEVEL_KEY = PlayerRegistry.CURRENT_LEVEL_KEY;
    private static final String REVIEW_MODE_KEY = "reviewModeEnabled";
    private static final String REVIEW_UPGRADE_PROFILE_KEY = "reviewUpgradeProfile";

    @FXML
    private AnchorPane root;
    @FXML
    private Label lbGold;
    @FXML
    private Label lbCurrentLevel;
    @FXML
    private Label lbTotalPoints;
    @FXML
    private Label lbCrossbowDamageLevel;
    @FXML
    private Label lbCrossbowDamageCost;
    @FXML
    private Label lbCrossbowSpeedLevel;
    @FXML
    private Label lbCrossbowSpeedCost;
    @FXML
    private Label lbCastleHealthLevel;
    @FXML
    private Label lbCastleHealthCost;
    @FXML
    private Label lbElixirLevel;
    @FXML
    private Label lbElixirCost;
    @FXML
    private Label lbMeteorLevel;
    @FXML
    private Label lbMeteorCost;
    @FXML
    private Label lbMeteorRadiusLevel;
    @FXML
    private Label lbMeteorRadiusCost;
    @FXML
    private Label lbIceLevel;
    @FXML
    private Label lbIceCost;
    @FXML
    private Label lbIceRadiusLevel;
    @FXML
    private Label lbIceRadiusCost;
    @FXML
    private ProgressBar pgCrossbowDamage;
    @FXML
    private ProgressBar pgCrossbowSpeed;
    @FXML
    private ProgressBar pgCastleHealth;
    @FXML
    private ProgressBar pgElixir;
    @FXML
    private ProgressBar pgMeteor;
    @FXML
    private ProgressBar pgMeteorRadius;
    @FXML
    private ProgressBar pgIce;
    @FXML
    private ProgressBar pgIceRadius;
    @FXML
    private MFXButton btnUpgradeCrossbowDamage;
    @FXML
    private MFXButton btnUpgradeCrossbowSpeed;
    @FXML
    private MFXButton btnUpgradeCastleHealth;
    @FXML
    private MFXButton btnUpgradeElixir;
    @FXML
    private MFXButton btnUpgradeMeteor;
    @FXML
    private MFXButton btnUpgradeMeteorRadius;
    @FXML
    private MFXButton btnUpgradeIce;
    @FXML
    private MFXButton btnUpgradeIceRadius;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowDamage;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowDamage;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowSpeed;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowSpeed;
    @FXML
    private MFXButton btnReviewDecreaseCastleHealth;
    @FXML
    private MFXButton btnReviewIncreaseCastleHealth;
    @FXML
    private MFXButton btnReviewDecreaseElixir;
    @FXML
    private MFXButton btnReviewIncreaseElixir;
    @FXML
    private MFXButton btnReviewDecreaseMeteor;
    @FXML
    private MFXButton btnReviewIncreaseMeteor;
    @FXML
    private MFXButton btnReviewDecreaseMeteorRadius;
    @FXML
    private MFXButton btnReviewIncreaseMeteorRadius;
    @FXML
    private MFXButton btnReviewDecreaseIce;
    @FXML
    private MFXButton btnReviewIncreaseIce;
    @FXML
    private MFXButton btnReviewDecreaseIceRadius;
    @FXML
    private MFXButton btnReviewIncreaseIceRadius;
    @FXML
    private MFXButton btnReviewMaxAll;
    @FXML
    private Button btnStartGame;

    private UpgradeProfile profile;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private TabPane tabPane;
    @FXML
    private ImageView imvFondo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setNombreVista("Kingdom Fantasy - Mejoras");
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }

    @Override
    public void initialize() {
        MusicManager.getInstance().playTrack(MusicManager.MusicTrack.IMPROVEMENTS);
        PlayerRegistry.getOrCreateCurrentPlayer();
        PlayerRegistry.syncCurrentFromContext();
        profile = getActiveProfile();
        refresh();
        
    }

    @FXML
    private void onActionBtnUpgradeCrossbowDamage(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.CROSSBOW_DAMAGE);
    }

    @FXML
    private void onActionBtnUpgradeCrossbowSpeed(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.CROSSBOW_SPEED);
    }

    @FXML
    private void onActionBtnUpgradeCastleHealth(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.CASTLE_HEALTH);
    }

    @FXML
    private void onActionBtnUpgradeElixir(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.ELIXIR_CAPACITY);
    }

    @FXML
    private void onActionBtnUpgradeMeteor(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.METEOR_DAMAGE);
    }

    @FXML
    private void onActionBtnUpgradeMeteorRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.METEOR_RADIUS);
    }

    @FXML
    private void onActionBtnUpgradeIce(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.ICE_DURATION);
    }

    @FXML
    private void onActionBtnUpgradeIceRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        upgrade(UpgradeType.ICE_RADIUS);
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowDamage(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CROSSBOW_DAMAGE, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowDamage(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CROSSBOW_DAMAGE, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowSpeed(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CROSSBOW_SPEED, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowSpeed(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CROSSBOW_SPEED, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseCastleHealth(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CASTLE_HEALTH, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseCastleHealth(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.CASTLE_HEALTH, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseElixir(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ELIXIR_CAPACITY, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseElixir(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ELIXIR_CAPACITY, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteor(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.METEOR_DAMAGE, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteor(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.METEOR_DAMAGE, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteorRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.METEOR_RADIUS, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteorRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.METEOR_RADIUS, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseIce(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ICE_DURATION, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseIce(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ICE_DURATION, 1);
    }

    @FXML
    private void onActionBtnReviewDecreaseIceRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ICE_RADIUS, -1);
    }

    @FXML
    private void onActionBtnReviewIncreaseIceRadius(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        adjustReviewUpgrade(UpgradeType.ICE_RADIUS, 1);
    }

    @FXML
    private void onActionBtnReviewMaxAll(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (!isReviewModeEnabled()) {
            return;
        }
        profile = getReviewProfile();
        for (UpgradeType type : UpgradeType.values()) {
            profile.setLevel(type, profile.getMaxLevel(type));
        }
        AppContext.getInstance().set(REVIEW_UPGRADE_PROFILE_KEY, profile);
        refresh();
    }


    @FXML
    private void onActionBtnStartGame(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (isReviewModeEnabled()) {
            AppContext.getInstance().set(REVIEW_UPGRADE_PROFILE_KEY, profile);
        } else {
            PlayerRegistry.setUpgradeProfile(profile);
        }
        FlowController.getInstance().goViewInStage("JuegoView", getStage());
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        PlayerRegistry.syncCurrentFromContext();
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    private void upgrade(UpgradeType type) {
        if (isReviewModeEnabled()) {
            adjustReviewUpgrade(type, 1);
            return;
        }
        int gold = getGold();
        int cost = profile.getUpgradeCost(type);
        if (profile.upgrade(type, gold)) {
            setGold(gold - cost);
        } 
        PlayerRegistry.setUpgradeProfile(profile);
        refresh();
    }

    private void adjustReviewUpgrade(UpgradeType type, int delta) {
        if (!isReviewModeEnabled() || type == null) {
            return;
        }
        profile = getReviewProfile();
        int level = profile.getLevel(type);
        profile.setLevel(type, level + delta);
        AppContext.getInstance().set(REVIEW_UPGRADE_PROFILE_KEY, profile);
        refresh();
    }

    private boolean areAllUpgradesMaxed() {
        for (UpgradeType type : UpgradeType.values()) {
            if (profile.getLevel(type) < profile.getMaxLevel(type)) {
                return false;
            }
        }
        return true;
    }

    private void refresh() {
        profile = getActiveProfile();
        if (profile == null) {
            return;
        }
        int gold = getGold();
        boolean reviewMode = isReviewModeEnabled();
        lbGold.setText(String.valueOf(gold));
        lbCurrentLevel.setText(reviewMode ? "Modo revision" : "Nivel actual: " + getCurrentLevel());
        if (lbTotalPoints != null) {
            lbTotalPoints.setText(String.valueOf(PlayerRegistry.getHistoricalPoints()));
        }
        setVisibleAndManaged(btnReviewMaxAll, reviewMode);
        if (btnReviewMaxAll != null) {
            boolean allMaxed = reviewMode && areAllUpgradesMaxed();
            btnReviewMaxAll.setDisable(allMaxed);
            btnReviewMaxAll.setText(allMaxed ? "Mejoras al maximo" : "Maxear mejoras");
        }
        refreshUpgrade(UpgradeType.CROSSBOW_DAMAGE, lbCrossbowDamageLevel, lbCrossbowDamageCost,
                pgCrossbowDamage, btnUpgradeCrossbowDamage, btnReviewDecreaseCrossbowDamage,
                btnReviewIncreaseCrossbowDamage, gold, reviewMode);
        refreshUpgrade(UpgradeType.CROSSBOW_SPEED, lbCrossbowSpeedLevel, lbCrossbowSpeedCost,
                pgCrossbowSpeed, btnUpgradeCrossbowSpeed, btnReviewDecreaseCrossbowSpeed,
                btnReviewIncreaseCrossbowSpeed, gold, reviewMode);
        refreshUpgrade(UpgradeType.CASTLE_HEALTH, lbCastleHealthLevel, lbCastleHealthCost,
                pgCastleHealth, btnUpgradeCastleHealth, btnReviewDecreaseCastleHealth,
                btnReviewIncreaseCastleHealth, gold, reviewMode);
        refreshUpgrade(UpgradeType.ELIXIR_CAPACITY, lbElixirLevel, lbElixirCost,
                pgElixir, btnUpgradeElixir, btnReviewDecreaseElixir,
                btnReviewIncreaseElixir, gold, reviewMode);
        refreshUpgrade(UpgradeType.METEOR_DAMAGE, lbMeteorLevel, lbMeteorCost,
                pgMeteor, btnUpgradeMeteor, btnReviewDecreaseMeteor,
                btnReviewIncreaseMeteor, gold, reviewMode);
        refreshUpgrade(UpgradeType.METEOR_RADIUS, lbMeteorRadiusLevel, lbMeteorRadiusCost,
                pgMeteorRadius, btnUpgradeMeteorRadius, btnReviewDecreaseMeteorRadius,
                btnReviewIncreaseMeteorRadius, gold, reviewMode);
        refreshUpgrade(UpgradeType.ICE_DURATION, lbIceLevel, lbIceCost,
                pgIce, btnUpgradeIce, btnReviewDecreaseIce,
                btnReviewIncreaseIce, gold, reviewMode);
        refreshUpgrade(UpgradeType.ICE_RADIUS, lbIceRadiusLevel, lbIceRadiusCost,
                pgIceRadius, btnUpgradeIceRadius, btnReviewDecreaseIceRadius,
                btnReviewIncreaseIceRadius, gold, reviewMode);
        getDesign();
        btnStartGame.setDisable(false);
    }

    private void refreshUpgrade(UpgradeType type, Label levelLabel, Label costLabel,
            ProgressBar progressBar, MFXButton button, MFXButton decreaseButton,
            MFXButton increaseButton, int gold, boolean reviewMode) {
        int level = profile.getLevel(type);
        int maxLevel = profile.getMaxLevel(type);
        levelLabel.setText("Nivel " + level + "/" + maxLevel);
        progressBar.setProgress(level / (double) maxLevel);
        if (reviewMode) {
            costLabel.setText("Revision libre");
            setVisibleAndManaged(button, false);
            setVisibleAndManaged(decreaseButton, true);
            setVisibleAndManaged(increaseButton, true);
            decreaseButton.setDisable(level <= 1);
            increaseButton.setDisable(level >= maxLevel);
            return;
        }
        setVisibleAndManaged(button, true);
        setVisibleAndManaged(decreaseButton, false);
        setVisibleAndManaged(increaseButton, false);
        if (profile.canUpgrade(type)) {
            int cost = profile.getUpgradeCost(type);
            costLabel.setText(cost + " monedas");
            button.setDisable(gold < cost);
        } else {
            costLabel.setText("Maximo");
            button.setDisable(true);
        }
    }

    private UpgradeProfile getActiveProfile() {
        return isReviewModeEnabled() ? getReviewProfile() : getProfile();
    }

    private UpgradeProfile getReviewProfile() {
        Object value = AppContext.getInstance().get(REVIEW_UPGRADE_PROFILE_KEY);
        if (value instanceof UpgradeProfile) {
            return (UpgradeProfile) value;
        }
        UpgradeProfile reviewProfile = copyProfile(getProfile());
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

    private UpgradeProfile getProfile() {
        Object value = AppContext.getInstance().get(UPGRADE_PROFILE_KEY);
        if (value instanceof UpgradeProfile) {
            return (UpgradeProfile) value;
        }
        UpgradeProfile newProfile = new UpgradeProfile();
        PlayerRegistry.setUpgradeProfile(newProfile);
        return newProfile;
    }

    private boolean isReviewModeEnabled() {
        Object value = AppContext.getInstance().get(REVIEW_MODE_KEY);
        return value instanceof Boolean && (Boolean) value;
    }

    private CrossbowDesign getDesign() {
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

    private int getCurrentLevel() {
        Object value = AppContext.getInstance().get(CURRENT_LEVEL_KEY);
        if (value instanceof Number) {
            return Math.max(1, Math.min(100, ((Number) value).intValue()));
        }
        PlayerRegistry.setCurrentLevel(1);
        return 1;
    }

    private void setStyleClassEnabled(MFXButton button, String styleClass, boolean enabled) {
        button.getStyleClass().remove(styleClass);
        if (enabled) {
            button.getStyleClass().add(styleClass);
        }
    }

    private void setVisibleAndManaged(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }
}

