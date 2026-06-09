package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.ControlScheme;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import cr.ac.una.kingdomfantasy.util.PlayerRegistry;
import cr.ac.una.kingdomfantasy.util.SpriteAnimationSpec;
import cr.ac.una.kingdomfantasy.util.SpriteCatalog;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;


public class AjustesController extends Controller implements Initializable {

    private static final String CONTROL_SCHEME_KEY = "controlScheme";
    private static final String REVIEW_MODE_KEY = "reviewModeEnabled";
    private static final String REVIEW_LEVEL_KEY = "reviewLevel";
    private static final String REVIEW_ACCESS_UNLOCKED_KEY = "reviewAccessUnlocked";
    private static final String REVIEW_UPGRADE_PROFILE_KEY = "reviewUpgradeProfile";
    private static final String REVIEW_PASSWORD = "SAA2026";

    @FXML
    private VBox vbCardMouse;
    @FXML
    private VBox vbCardKeyboard;
    @FXML
    private VBox vbCardReview;
    @FXML
    private VBox vbReviewToolsBox;
    @FXML
    private Label lbReviewStatus;
    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnMouse;
    @FXML
    private MFXButton btnKeyboard;
    @FXML
    private MFXButton btnReviewUnlock;
    @FXML
    private MFXButton btnReviewToggle;
    @FXML
    private MFXButton btnReviewApply;
    @FXML
    private MFXButton btnReviewLevel100;
    @FXML
    private MFXButton btnToggleAudio;
    @FXML
    private ImageView imvAudioToggle;
    @FXML
    private MFXTextField txfReviewLevel;
    @FXML
    private MFXPasswordField txfReviewPassword;
    @FXML
    private HBox hbReviewAccessBox;
    private Image volumeOnImage;
    private Image volumeOffImage;
    @FXML
    private ImageView imvFondo;
    @FXML
    private ImageView imvReviewIcon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setNombreVista("AjustesView");
        ensureDefaults();
        loadAudioToggleImages();
        applyWindowMinimumSize();
        refresh();
        refreshAudioToggle();
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }

    @Override
    public void initialize() {
        ensureDefaults();
        loadAudioToggleImages();
        applyWindowMinimumSize();
        refresh();
        refreshAudioToggle();
    }

    @FXML
    private void onActionBtnMouse(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.MOUSE);
        refresh();
    }

    @FXML
    private void onActionBtnKeyboard(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.KEYBOARD);
        refresh();
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
       MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
       FlowController.getInstance().goViewInStage("PrincipalView", getStage());
   }

    @FXML
    private void onActionBtnToggleAudio(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        MusicManager musicManager = MusicManager.getInstance();
        boolean muted = !musicManager.isMuted();
        musicManager.setMuted(muted);
        refreshAudioToggle();
    }

    @FXML
    private void onActionBtnReviewUnlock(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        String password = txfReviewPassword.getText() == null ? "" : txfReviewPassword.getText().trim();
        if (!REVIEW_PASSWORD.equals(password)) {
            AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, false);
            AppContext.getInstance().set(REVIEW_MODE_KEY, false);
            AppContext.getInstance().delete(REVIEW_UPGRADE_PROFILE_KEY);
            refresh();
            lbReviewStatus.setText("Clave incorrecta.");
            return;
        }
        txfReviewPassword.setText("");
        AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, true);
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        if (!(AppContext.getInstance().get(REVIEW_LEVEL_KEY) instanceof Number)) {
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, getRealCurrentLevel());
        }
        PlayerRegistry.getOrCreateCurrentPlayer();
        refresh();
    }

    @FXML
    private void onActionBtnReviewToggle(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (!isReviewAccessUnlocked()) {
            lbReviewStatus.setText("Ingresa la clave para desbloquear.");
            return;
        }
        boolean enabled = !isReviewModeEnabled();
        AppContext.getInstance().set(REVIEW_MODE_KEY, enabled);
        if (enabled && !(AppContext.getInstance().get(REVIEW_LEVEL_KEY) instanceof Number)) {
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, getRealCurrentLevel());
        }
        if (!enabled) {
            AppContext.getInstance().delete(REVIEW_UPGRADE_PROFILE_KEY);
        }
        refresh();
    }

    @FXML
    private void onActionBtnReviewApply(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (!isReviewAccessUnlocked()) {
            lbReviewStatus.setText("Ingresa la clave para desbloquear.");
            return;
        }
        Integer level = parseReviewLevel();
        if (level == null) {
            return;
        }
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        PlayerRegistry.getOrCreateCurrentPlayer();
        AppContext.getInstance().set(REVIEW_LEVEL_KEY, level);
        refresh();
    }

    @FXML
    private void onActionBtnReviewLevel100(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        if (!isReviewAccessUnlocked()) {
            lbReviewStatus.setText("Ingresa la clave para desbloquear.");
            return;
        }
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        PlayerRegistry.getOrCreateCurrentPlayer();
        AppContext.getInstance().set(REVIEW_LEVEL_KEY, 100);
        refresh();
    }

    private void refresh() {
        ControlScheme scheme = getScheme();
        vbCardMouse.getStyleClass().remove("jfx-selected-control");
        vbCardKeyboard.getStyleClass().remove("jfx-selected-control");
        btnMouse.getStyleClass().remove("jfx-primary-action");
        btnMouse.getStyleClass().remove("jfx-ghost-action");
        btnKeyboard.getStyleClass().remove("jfx-primary-action");
        btnKeyboard.getStyleClass().remove("jfx-ghost-action");
        vbCardReview.getStyleClass().remove("jfx-selected-control");
        btnReviewToggle.getStyleClass().remove("jfx-primary-action");
        btnReviewToggle.getStyleClass().remove("jfx-ghost-action");
        if (scheme == ControlScheme.KEYBOARD) {
            vbCardKeyboard.getStyleClass().add("jfx-selected-control");
            btnKeyboard.getStyleClass().add("jfx-primary-action");
            btnMouse.getStyleClass().add("jfx-ghost-action");
        } else {
            vbCardMouse.getStyleClass().add("jfx-selected-control");
            btnMouse.getStyleClass().add("jfx-primary-action");
            btnKeyboard.getStyleClass().add("jfx-ghost-action");
        }
        boolean reviewUnlocked = isReviewAccessUnlocked();
        setVisibleAndManaged(hbReviewAccessBox, !reviewUnlocked);
        setVisibleAndManaged(vbReviewToolsBox, reviewUnlocked);
        if (!reviewUnlocked) {
            lbReviewStatus.setText("Acceso bloqueado.");
            return;
        }
        boolean reviewMode = isReviewModeEnabled();
        if (reviewMode) {
            vbCardReview.getStyleClass().add("jfx-selected-control");
            btnReviewToggle.getStyleClass().add("jfx-primary-action");
            btnReviewToggle.setText("Desactivar");
        } else {
            btnReviewToggle.getStyleClass().add("jfx-ghost-action");
            btnReviewToggle.setText("Activar");
        }
        int currentLevel = getCurrentLevel();
        if (txfReviewLevel.getText() == null || txfReviewLevel.getText().isBlank()) {
            txfReviewLevel.setText(String.valueOf(currentLevel));
        }
        lbReviewStatus.setText("Acceso concedido.");
    }

    private void loadAudioToggleImages() {
        if (volumeOnImage == null) {
            volumeOnImage = SpriteAnimationSpec.loadCachedResource(SpriteCatalog.resourcePath("VolumeOn.png"));
        }
        if (volumeOffImage == null) {
            volumeOffImage = SpriteAnimationSpec.loadCachedResource(SpriteCatalog.resourcePath("VolumeOff.png"));
        }
    }

    private void refreshAudioToggle() {
        if (imvAudioToggle == null || btnToggleAudio == null) {
            return;
        }
        boolean muted = MusicManager.getInstance().isMuted();
        imvAudioToggle.setImage(muted ? volumeOffImage : volumeOnImage);
        btnToggleAudio.setTooltip(new Tooltip(muted ? "Activar audio" : "Silenciar audio"));
    }

    private void applyWindowMinimumSize() {
        if (getStage() != null && root != null) {
            getStage().setMinWidth(root.getMinWidth());
            getStage().setMinHeight(root.getMinHeight());
        }
    }

    private void ensureDefaults() {
        getScheme();
        if (!(AppContext.getInstance().get(REVIEW_MODE_KEY) instanceof Boolean)) {
            AppContext.getInstance().set(REVIEW_MODE_KEY, false);
        }
        if (!(AppContext.getInstance().get(REVIEW_ACCESS_UNLOCKED_KEY) instanceof Boolean)) {
            AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, false);
        }
        if (!isReviewAccessUnlocked()) {
            AppContext.getInstance().set(REVIEW_MODE_KEY, false);
            AppContext.getInstance().delete(REVIEW_UPGRADE_PROFILE_KEY);
        }
    }

    private ControlScheme getScheme() {
        Object value = AppContext.getInstance().get(CONTROL_SCHEME_KEY);
        if (value instanceof ControlScheme) {
            return (ControlScheme) value;
        }
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.MOUSE);
        return ControlScheme.MOUSE;
    }


    private boolean isReviewModeEnabled() {
        Object value = AppContext.getInstance().get(REVIEW_MODE_KEY);
        return value instanceof Boolean && (Boolean) value;
    }

    private boolean isReviewAccessUnlocked() {
        Object value = AppContext.getInstance().get(REVIEW_ACCESS_UNLOCKED_KEY);
        return value instanceof Boolean && (Boolean) value;
    }

    private int getCurrentLevel() {
        if (isReviewModeEnabled()) {
            Object reviewLevel = AppContext.getInstance().get(REVIEW_LEVEL_KEY);
            if (reviewLevel instanceof Number) {
                return Math.max(1, Math.min(100, ((Number) reviewLevel).intValue()));
            }
        }
        return getRealCurrentLevel();
    }

    private int getRealCurrentLevel() {
        Object value = AppContext.getInstance().get(PlayerRegistry.CURRENT_LEVEL_KEY);
        if (value instanceof Number) {
            return Math.max(1, Math.min(100, ((Number) value).intValue()));
        }
        PlayerRegistry.setCurrentLevel(1);
        return 1;
    }

    private Integer parseReviewLevel() {
        String text = txfReviewLevel.getText();
        if (text == null) {
            return null;
        }
        try {
            int level = Integer.parseInt(text.trim());
            if (level < 1 || level > 100) {
                return null;
            }
            return level;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void setVisibleAndManaged(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }

    @FXML
    private void onKeyPressedTxfReviewPassword(KeyEvent event) {
       if(event.getCode() == KeyCode.ENTER && !txfReviewPassword.getText().isBlank()){
        String password = txfReviewPassword.getText();
        if (!REVIEW_PASSWORD.equals(password)) {
            AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, false);
            AppContext.getInstance().set(REVIEW_MODE_KEY, false);
            AppContext.getInstance().delete(REVIEW_UPGRADE_PROFILE_KEY);
            refresh();
            lbReviewStatus.setText("Clave incorrecta.");
            return;
        }
        txfReviewPassword.setText("");
        AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, true);
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        if (!(AppContext.getInstance().get(REVIEW_LEVEL_KEY) instanceof Number)) {
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, getRealCurrentLevel());
        }
        PlayerRegistry.getOrCreateCurrentPlayer();
        refresh();
       } 
    }
}

