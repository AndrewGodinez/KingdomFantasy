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
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author andrew
 */
public class JuegoController extends Controller implements Initializable {
    
    private static final Integer BASE_WIDTH = 960;
    private static final Integer BASE_HEIGHT = 540;
    private static final Integer CASTLE_ZONE_W = 215;
    private static final Integer CASTLE_ZONE_H = 540;
    private static final Integer ACTOR_PANE_W = 960;
    private static final Integer ACTOR_PANE_H = 400;

    @FXML
    private AnchorPane root;
    @FXML
    private Pane battlefieldFrame;
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
    private MFXButton btnPause;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblWaveValue;
    @FXML
    private ProgressBar prgWave;
    @FXML
    private Label lblLevelPoints;
    @FXML
    private Label lblScoreMultiplier;
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
    private Label lblCastleValue;
    @FXML
    private ProgressBar prgCastle;
    @FXML
    private Label lblManaValue;
    @FXML
    private ProgressBar prgMana;
    @FXML
    private StackPane pauseOverlay;
    @FXML
    private ImageView imvOverlayAnimation;
    @FXML
    private MFXButton btnResumeOverlay;
    @FXML
    private MFXButton btnBackToImprovements;
    @FXML
    private MFXButton btnBackToMainMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      imvBattlefield.fitHeightProperty().bind(root.heightProperty());
      imvBattlefield.fitWidthProperty().bind(root.widthProperty());
      gameWorld.setManaged(false);
      root.widthProperty().addListener((ov,oldVal,newVal) -> updateScale());
      root.heightProperty().addListener((ov,oldVal,newVal) -> updateScale());
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionPause(ActionEvent event) {
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
    
    private void updateScale() {
        double scaleX = root.getWidth() / (double) BASE_WIDTH;
        double scaleY = root.getHeight() / (double) BASE_HEIGHT;

        gameWorld.setScaleX(scaleX);
        gameWorld.setScaleY(scaleY);
    }
    
}
