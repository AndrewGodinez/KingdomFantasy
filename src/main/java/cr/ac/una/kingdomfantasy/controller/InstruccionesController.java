package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class InstruccionesController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private MFXButton btnBackControls;
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
    private void onActionBackControls(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    
    
}
