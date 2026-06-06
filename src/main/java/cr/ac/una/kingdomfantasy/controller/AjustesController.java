package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.ControlScheme;
import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
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
import javafx.scene.layout.BorderPane;

public class AjustesController extends Controller implements Initializable {

    private static final String CONTROL_SCHEME_KEY = "controlScheme";
    private static final String CROSSBOW_DESIGN_KEY = "crossbowDesign";
    private static final String REVIEW_MODE_KEY = "reviewModeEnabled";
    private static final String REVIEW_LEVEL_KEY = "reviewLevel";
    private static final String REVIEW_ACCESS_UNLOCKED_KEY = "reviewAccessUnlocked";
    private static final String REVIEW_UPGRADE_PROFILE_KEY = "reviewUpgradeProfile";
    private static final String REVIEW_PASSWORD = "SAA2026";

    @FXML
    private VBox cardMouse;
    @FXML
    private VBox cardKeyboard;
    @FXML
    private VBox cardReview;
    @FXML
    private VBox reviewToolsBox;
    @FXML
    private Label lblSelection;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblReviewStatus;
    @FXML
    private Label lblReviewUnlockedStatus;
    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnConfirm;
    @FXML
    private MFXButton btnMouse;
    @FXML
    private MFXButton btnKeyboard;
    @FXML
    private MFXButton btnInstructions;
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
    private javafx.scene.layout.HBox reviewAccessBox;
    private Image volumeOnImage;
    private Image volumeOffImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setNombreVista("Kingdom Fantasy - Controles");
        ensureDefaults();
        loadAudioToggleImages();
        applyWindowMinimumSize();
        refresh();
        refreshAudioToggle();
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
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.MOUSE);
        lblMessage.setText("Control con mouse seleccionado.");
        refresh();
    }

    @FXML
    private void onActionBtnKeyboard(ActionEvent event) {
        AppContext.getInstance().set(CONTROL_SCHEME_KEY, ControlScheme.KEYBOARD);
        lblMessage.setText("Control con teclado seleccionado.");
        refresh();
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
       FlowController.getInstance().goViewInStage("PrincipalView", getStage());
   }

    @FXML
    private void onActionBtnInstructions(ActionEvent event) {
        FlowController.getInstance().goViewInStage("InstruccionesView", getStage());
    }

    @FXML
    private void onActionBtnToggleAudio(ActionEvent event) {
        MusicManager musicManager = MusicManager.getInstance();
        boolean muted = !musicManager.isMuted();
        musicManager.setMuted(muted);
        lblMessage.setText(muted ? "Audio desactivado." : "Audio activado.");
        refreshAudioToggle();
    }

    @FXML
    private void onActionBtnReviewUnlock(ActionEvent event) {
        String password = txfReviewPassword.getText() == null ? "" : txfReviewPassword.getText().trim();
        if (!REVIEW_PASSWORD.equals(password)) {
            AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, false);
            AppContext.getInstance().set(REVIEW_MODE_KEY, false);
            AppContext.getInstance().delete(REVIEW_UPGRADE_PROFILE_KEY);
            lblMessage.setText("Acceso avanzado bloqueado.");
            refresh();
            lblReviewStatus.setText("Clave incorrecta.");
            return;
        }
        txfReviewPassword.setText("");
        AppContext.getInstance().set(REVIEW_ACCESS_UNLOCKED_KEY, true);
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        if (!(AppContext.getInstance().get(REVIEW_LEVEL_KEY) instanceof Number)) {
            AppContext.getInstance().set(REVIEW_LEVEL_KEY, getRealCurrentLevel());
        }
        PlayerRegistry.getOrCreateCurrentPlayer();
        lblMessage.setText("Modo revision desbloqueado.");
        refresh();
    }

    @FXML
    private void onActionBtnReviewToggle(ActionEvent event) {
        if (!isReviewAccessUnlocked()) {
            lblReviewStatus.setText("Ingresa la clave para desbloquear.");
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
        lblMessage.setText(enabled
                ? "Modo revision activado. Puedes saltar niveles para probar."
                : "Modo revision desactivado.");
        refresh();
    }

    @FXML
    private void onActionBtnReviewApply(ActionEvent event) {
        if (!isReviewAccessUnlocked()) {
            lblReviewStatus.setText("Ingresa la clave para desbloquear.");
            return;
        }
        Integer level = parseReviewLevel();
        if (level == null) {
            lblReviewUnlockedStatus.setText("Escribe un nivel entre 1 y 100.");
            return;
        }
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        PlayerRegistry.getOrCreateCurrentPlayer();
        AppContext.getInstance().set(REVIEW_LEVEL_KEY, level);
        lblMessage.setText("Modo revision listo en el nivel " + level + ".");
        refresh();
    }

    @FXML
    private void onActionBtnReviewLevel100(ActionEvent event) {
        if (!isReviewAccessUnlocked()) {
            lblReviewStatus.setText("Ingresa la clave para desbloquear.");
            return;
        }
        AppContext.getInstance().set(REVIEW_MODE_KEY, true);
        PlayerRegistry.getOrCreateCurrentPlayer();
        AppContext.getInstance().set(REVIEW_LEVEL_KEY, 100);
        lblMessage.setText("Modo revision listo en el nivel 100.");
        refresh();
    }

    private void refresh() {
        ControlScheme scheme = getScheme();
        cardMouse.getStyleClass().remove("selected-control");
        cardKeyboard.getStyleClass().remove("selected-control");
        btnMouse.getStyleClass().remove("primary-action");
        btnMouse.getStyleClass().remove("ghost-action");
        btnKeyboard.getStyleClass().remove("primary-action");
        btnKeyboard.getStyleClass().remove("ghost-action");
        cardReview.getStyleClass().remove("selected-control");
        btnReviewToggle.getStyleClass().remove("primary-action");
        btnReviewToggle.getStyleClass().remove("ghost-action");
        if (scheme == ControlScheme.KEYBOARD) {
            cardKeyboard.getStyleClass().add("selected-control");
            btnKeyboard.getStyleClass().add("primary-action");
            btnMouse.getStyleClass().add("ghost-action");
        } else {
            cardMouse.getStyleClass().add("selected-control");
            btnMouse.getStyleClass().add("primary-action");
            btnKeyboard.getStyleClass().add("ghost-action");
        }
        boolean reviewUnlocked = isReviewAccessUnlocked();
        setVisibleAndManaged(reviewAccessBox, !reviewUnlocked);
        setVisibleAndManaged(reviewToolsBox, reviewUnlocked);
        if (!reviewUnlocked) {
            lblReviewStatus.setText("Acceso bloqueado.");
            if (lblReviewUnlockedStatus != null) {
                lblReviewUnlockedStatus.setText("");
            }
            return;
        }
        boolean reviewMode = isReviewModeEnabled();
        if (reviewMode) {
            cardReview.getStyleClass().add("selected-control");
            btnReviewToggle.getStyleClass().add("primary-action");
            btnReviewToggle.setText("Desactivar");
        } else {
            btnReviewToggle.getStyleClass().add("ghost-action");
            btnReviewToggle.setText("Activar");
        }
        int currentLevel = getCurrentLevel();
        if (txfReviewLevel.getText() == null || txfReviewLevel.getText().isBlank()) {
            txfReviewLevel.setText(String.valueOf(currentLevel));
        }
        lblReviewUnlockedStatus.setText(reviewMode
                ? "Activo: los saltos no guardan monedas, puntos ni progreso real."
                : "Inactivo: progreso normal.");
        lblReviewStatus.setText("Acceso concedido.");
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
        getDesign();
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

    private CrossbowDesign getDesign() {
        Object value = AppContext.getInstance().get(CROSSBOW_DESIGN_KEY);
        if (value instanceof CrossbowDesign) {
            return (CrossbowDesign) value;
        }
        AppContext.getInstance().set(CROSSBOW_DESIGN_KEY, CrossbowDesign.GREEN);
        PlayerRegistry.setCrossbowDesign(CrossbowDesign.GREEN);
        return CrossbowDesign.GREEN;
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

    private String displayName(CrossbowDesign design) {
        if (design == CrossbowDesign.PURPLE) {
            return "Morada";
        }
        return "Verde";
    }

    private void setVisibleAndManaged(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }
}

