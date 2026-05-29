/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    private AnchorPane root;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      imvBattlefield.fitHeightProperty().bind(root.heightProperty());
      imvBattlefield.fitWidthProperty().bind(root.widthProperty());
    }    

    @Override
    public void initialize() {
    }
    
}
