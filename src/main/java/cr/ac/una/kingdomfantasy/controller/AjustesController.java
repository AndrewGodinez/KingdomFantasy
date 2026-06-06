/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class AjustesController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnToggleAudio;
    @FXML
    private ImageView imvAudioToggle;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnMouse;
    @FXML
    private MFXButton btnKeyboard;
    @FXML
    private MFXPasswordField txfReviewPassword;
    @FXML
    private MFXButton btnReviewUnlock;
    @FXML
    private Label lblReviewStatus;
    @FXML
    private ImageView imvReviewIcon;
    @FXML
    private ImageView imvFondo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
        // TODO
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnToggleAudio(ActionEvent event) {
    }

    private void onActionBtnInstructions(ActionEvent event) {
        FlowController.getInstance().goViewInStage("InstruccionesView", getStage());
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
        //TODO: Este onAction lo comparten el boton de volver y confirmar, pero creo que 
        //deberían ser onActions separados por la naturaleza del los botones.
    }

    @FXML
    private void onActionBtnMouse(ActionEvent event) {
    }

    @FXML
    private void onActionBtnKeyboard(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewUnlock(ActionEvent event) {
    }

    
}
