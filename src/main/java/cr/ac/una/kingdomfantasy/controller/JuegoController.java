/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class JuegoController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblWaveValue;
    @FXML
    private ProgressBar prgWave;
    @FXML
    private Label lblTimer;
    @FXML
    private ProgressBar prgLevelTime;
    @FXML
    private Label lblLevelPoints;
    @FXML
    private Label lblScoreMultiplier;
    @FXML
    private MFXButton btnPause;
    @FXML
    private MFXButton btnBackToMenu;
    @FXML
    private Label lblCastleValue;
    @FXML
    private ProgressBar prgCastle;
    @FXML
    private Label lblManaValue;
    @FXML
    private ProgressBar prgMana;
    @FXML
    private HBox reviewHud;
    @FXML
    private Label lblReviewMode;
    @FXML
    private MFXButton btnReviewPreviousLevel;
    @FXML
    private MFXButton btnReviewCompleteLevel;
    @FXML
    private MFXButton btnReviewNextLevel;
    @FXML
    private MFXButton btnReviewLevel100;
    @FXML
    private MFXButton btnHeroPortrait;
    @FXML
    private ImageView imvHeroPortrait;
    @FXML
    private MFXButton btnMeteorSpell;
    @FXML
    private ImageView imvMeteorSpell;
    @FXML
    private MFXButton btnIceSpell;
    @FXML
    private ImageView imvIceSpell;
    @FXML
    private StackPane battlefieldFrame;
    @FXML
    private Pane gameWorld;
    @FXML
    private ImageView imvBattlefield;
    @FXML
    private Pane worldPane;
    @FXML
    private Rectangle castleZone;
    @FXML
    private ImageView imvCrossbow;
    @FXML
    private Pane actorPane;
    @FXML
    private Pane projectilePane;
    @FXML
    private Pane effectsPane;
    @FXML
    private StackPane pauseOverlay;
    @FXML
    private ImageView imvOverlayAnimation;
    @FXML
    private Label lblOverlayTitle;
    @FXML
    private Label lblOverlayMessage;
    @FXML
    private MFXButton btnResumeOverlay;
    @FXML
    private MFXButton btnBackToImprovements;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imvBattlefield.fitHeightProperty().bind(battlefieldFrame.heightProperty());
        imvBattlefield.fitWidthProperty().bind(battlefieldFrame.widthProperty());
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionPause(ActionEvent event) {
    }

    @FXML
    private void onActionBackToMenu(ActionEvent event) {
    }

    @FXML
    private void onActionReviewPreviousLevel(ActionEvent event) {
    }

    @FXML
    private void onActionReviewCompleteLevel(ActionEvent event) {
    }

    @FXML
    private void onActionReviewNextLevel(ActionEvent event) {
    }

    @FXML
    private void onActionReviewLevel100(ActionEvent event) {
    }

    @FXML
    private void onActionHeroPortrait(ActionEvent event) {
    }

    @FXML
    private void onActionMeteorSpell(ActionEvent event) {
    }

    @FXML
    private void onActionIceSpell(ActionEvent event) {
    }

    @FXML
    private void onActionResume(ActionEvent event) {
    }

    @FXML
    private void onActionBackToImprovements(ActionEvent event) {
    }
    
}
