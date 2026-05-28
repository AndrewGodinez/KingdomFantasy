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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RankingController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private MFXButton btnPodium2;
    @FXML
    private MFXButton btnPodium1;
    @FXML
    private MFXButton btnPodium3;
    @FXML
    private MFXButton btnBack;
    @FXML
    private TableView<?> tblRanking;
    @FXML
    private TableColumn<?, ?> colRank;
    @FXML
    private TableColumn<?, ?> colAvatar;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colLevel;
    @FXML
    private TableColumn<?, ?> colPoints;
    @FXML
    private Label lblCurrentPlayerName;
    @FXML
    private Label lblCurrentPlayerRank;
    @FXML
    private Label lblCurrentPlayerLevel;
    @FXML
    private Label lblCurrentPlayerPoints;

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
    private void onActionPodium2(ActionEvent event) {
    }

    @FXML
    private void onActionPodium1(ActionEvent event) {
    }

    @FXML
    private void onActionPodium3(ActionEvent event) {
    }

    @FXML
    private void onActionBack(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }
    
}
