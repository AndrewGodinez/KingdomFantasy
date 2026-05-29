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
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import cr.ac.una.kingdomfantasy.util.FlowController;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class PrincipalController extends Controller implements Initializable {

    @FXML
    private StackPane root;
    @FXML
    private ImageView imvFondo;
    @FXML
    private MFXButton btnIniciarSesion;
    @FXML
    private MFXButton btnRanking;
    @FXML
    private ImageView imvLogo;
    @FXML
    private MFXButton btnComenzar;
    @FXML
    private MFXButton btnAcercaDe;
    @FXML
    private MFXButton btnNuevoJugador;
    @FXML
    private MFXButton btnControles;
    @FXML
    private MFXButton btnSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }    

    @FXML
    private void onActionBtnIniciarSesion(ActionEvent event) {
        FlowController.getInstance().goViewInStage("LoginView", getStage());
    }

    @FXML
    private void onActionBtnRanking(ActionEvent event) {
        FlowController.getInstance().goViewInStage("RankingView", getStage());
    }

    @FXML
    private void onActionBtnComenzar(ActionEvent event) {
        FlowController.getInstance().goViewInStage("MejorasView", getStage());
    }

    @FXML
    private void onActionBtnAcercaDe(ActionEvent event) {
        FlowController.getInstance().goViewInStage("AcercaDeView", getStage());
    }

    @FXML
    private void onActionBtnNuevoJugador(ActionEvent event) {
        FlowController.getInstance().goViewInStage("RegistroView", getStage());
    }

    @FXML
    private void onActionBtnControles(ActionEvent event) {
        FlowController.getInstance().goViewInStage("AjustesView", getStage());
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        getStage().close();
    }
    
}
