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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class AcercaDeController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnBack;
    @FXML
    private ImageView imvFondo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnBack(ActionEvent event) {
         FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }
    
}
