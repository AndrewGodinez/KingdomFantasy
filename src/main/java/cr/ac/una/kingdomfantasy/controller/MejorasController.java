/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MejorasController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lbCurrentLevel;
    @FXML
    private Label lbGold;
    @FXML
    private Label lbTotalPoints;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnReviewMaxAll;
    @FXML
    private MFXButton btnStartGame;
    @FXML
    private TabPane tabPane;
    @FXML
    private ProgressBar pgCrossbowDamage;
    @FXML
    private Label lbCrossbowDamageLevel;
    @FXML
    private Label lbCrossbowDamageCost;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowDamage;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowDamage;
    @FXML
    private MFXButton btnUpgradeCrossbowDamage;
    @FXML
    private ProgressBar pgCrossbowSpeed;
    @FXML
    private Label lbCrossbowSpeedLevel;
    @FXML
    private Label lbCrossbowSpeedCost;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowSpeed;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowSpeed;
    @FXML
    private MFXButton btnUpgradeCrossbowSpeed;
    @FXML
    private MFXButton btnCrossBowGreen;
    @FXML
    private MFXButton btnCrossBowPurple;
    @FXML
    private Label lbSelectedSkin;
    @FXML
    private ProgressBar pgCastleHealth;
    @FXML
    private Label lbCastleHealthLevel;
    @FXML
    private Label lbCastleHealthCost;
    @FXML
    private MFXButton btnReviewDecreaseCastleHealth;
    @FXML
    private MFXButton btnReviewIncreaseCastleHealth;
    @FXML
    private MFXButton btnUpgradeCastleHealth;
    @FXML
    private ProgressBar pgElixir;
    @FXML
    private Label lbElixirLevel;
    @FXML
    private Label lbElixirCost;
    @FXML
    private MFXButton btnReviewDecreaseElixir;
    @FXML
    private MFXButton btnReviewIncreaseElixir;
    @FXML
    private MFXButton btnUpgradeElixir;
    @FXML
    private ProgressBar pgMeteor;
    @FXML
    private Label lbMeteorLevel;
    @FXML
    private Label lbMeteorCost;
    @FXML
    private MFXButton btnReviewDecreaseMeteor;
    @FXML
    private MFXButton btnReviewIncreaseMeteor;
    @FXML
    private MFXButton btnUpgradeMeteor;
    @FXML
    private ProgressBar pgMeteorRadius;
    @FXML
    private Label lblMeteorRadiusLevel;
    @FXML
    private Label lbMeteorRadiusCost;
    @FXML
    private MFXButton btnReviewDecreaseMeteorRadius;
    @FXML
    private MFXButton btnReviewIncreaseMeteorRadius;
    @FXML
    private MFXButton btnUpgradeMeteorRadius;
    @FXML
    private ProgressBar pgIce;
    @FXML
    private Label lblIceLevel;
    @FXML
    private Label lbIceCost;
    @FXML
    private MFXButton btnReviewDecreaseIce;
    @FXML
    private MFXButton btnReviewIncreaseIce;
    @FXML
    private MFXButton btnUpgradeIce;
    @FXML
    private ProgressBar pgIceRadius;
    @FXML
    private Label lblIceRadiusLevel;
    @FXML
    private Label lbIceRadiusCost;
    @FXML
    private MFXButton btnReviewDecreaseIceRadius;
    @FXML
    private MFXButton btnReviewIncreaseIceRadius;
    @FXML
    private MFXButton btnUpgradeIceRadius;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnReviewMaxAll(ActionEvent event) {
    }

    @FXML
    private void onActionBtnStartGame(ActionEvent event) {
        FlowController.getInstance().goViewInStage("JuegoView", getStage());
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCrossBowGreen(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCrossBowPurple(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseIceRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseIceRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeIceRadius(ActionEvent event) {
    }
    
}
