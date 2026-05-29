/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class LoginController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lbLogin;
    @FXML
    private MFXButton btnCancel;
    @FXML
    private MFXTextField txfName;
    @FXML
    private Label lblMessage;
    @FXML
    private MFXButton btnLogin;
    @FXML
    private MFXButton btnCreatePlayer;

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
    private void onActionBtnCancel(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnLogin(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCreatePlayer(ActionEvent event) {
        FlowController.getInstance().goViewInStage("RegistroView", getStage());
    }
    
}
