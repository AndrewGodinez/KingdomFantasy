package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.util.AppContext;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Mensaje;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

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
    @FXML
    private Label lblOnline;
    
    PlayerDto playerDto= new PlayerDto();
    @FXML
    private MFXButton btnPerfil;
    @FXML
    private MFXButton btnCerrarSesion;
    @FXML
    private MFXButton btnComoJugar;
    
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
     MusicManager.getInstance().playTrack(MusicManager.MusicTrack.MAIN_MENU);
     playerDto= (PlayerDto) AppContext.getInstance().get("Player");
     if(playerDto!=null){
        lblOnline.setText("Online");
        btnCerrarSesion.setVisible(true);
        btnPerfil.setVisible(true);
        btnIniciarSesion.setDisable(true);
        btnNuevoJugador.setDisable(true);
     }
     else{
        lblOnline.setText("OffLine");
        btnCerrarSesion.setVisible(false);
        btnPerfil.setVisible(false);
        btnIniciarSesion.setDisable(false);
        btnNuevoJugador.setDisable(false);
     }
     
    }

    @FXML
    private void onActionBtnIniciarSesion(ActionEvent event) {
        if(AppContext.getInstance().get("Player")== null){
        FlowController.getInstance().goViewInStage("LoginView", getStage());
        } else {
        new Mensaje().showModal(Alert.AlertType.WARNING, "Inicio de sesión anteriormente realizado", getStage(), "Debes de cerrar la sesión actual antes de realizar una nueva ");
        }
    }

    @FXML
    private void onActionBtnRanking(ActionEvent event) {
        FlowController.getInstance().goViewInStage("RankingView", getStage());
    }

    @FXML
    private void onActionBtnComenzar(ActionEvent event) {
        if(playerDto!=null){
        FlowController.getInstance().goViewInStage("MejorasView", getStage());
        } else {
        new Mensaje().showModal(Alert.AlertType.WARNING, "Jugador Sin Registrar", getStage(), "Por Favor Inicie Sesión Antes de Comenzar");
        }
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

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        cr.ac.una.kingdomfantasy.util.MusicManager.getInstance().shutdown();
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(150));
        delay.setOnFinished(e -> System.exit(0));
        delay.play();
    }

    @FXML
    private void onActionBtnPerfil(ActionEvent event) {
        if(playerDto!=null){
            FlowController.getInstance().goViewInStage("UsuarioView", getStage());
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Jugador Sin Registrar", getStage(), "Por Favor Inicie Sesión Antes de Comenzar");
        }
    }

    @FXML
    private void onActionBtnCerrarSesion(ActionEvent event) {
      if(new Mensaje ().showConfirmation("Cerrar Sesión", getStage(), "¿Desea Cerrar Sesión?")){
      AppContext.getInstance().set("Player", null);
      AppContext.getInstance().set("Partida", null);
      AppContext.getInstance().set("Mejora", null);
      initialize();
    }
      
    }

    @FXML
    private void onActionBtnComoJugar(ActionEvent event) {
        FlowController.getInstance().goViewInStage("InstruccionesView", getStage());
    }
    
}
