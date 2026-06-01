package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.MejoraDto;
import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.MejoraService;
import cr.ac.una.kingdomfantasy.service.PartidaService;
import cr.ac.una.kingdomfantasy.service.PlayerService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Formato;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

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

    private PlayerService playerService = new PlayerService();
    private PartidaService partidaService = new PartidaService();
    private MejoraService mejoraService = new MejoraService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txfName.setTextFormatter(Formato.getInstance().letrasFormat(40));
        lblMessage.setText("");
    }

    @Override
    public void initialize() {
        lblMessage.setText("");
        txfName.clear();
    }

    @FXML
    private void onActionBtnCancel(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnLogin(ActionEvent event) {
        String nombre = txfName.getText().trim();
        if (nombre.isEmpty()) {
            lblMessage.setText("Debe ingresar el nombre del jugador.");
            return;
        }
        verificarJugador(nombre);
    }

    @FXML
    private void onActionBtnCreatePlayer(ActionEvent event) {
        FlowController.getInstance().goViewInStage("RegistroView", getStage());
    }

    @FXML
    private void onKeyPressedTxfName(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER && !txfName.getText().isBlank()){
        String nombre = txfName.getText().trim();   
        verificarJugador(nombre);
        }
    }
    
    private void verificarJugador(String nombre){
    Respuesta respuestaPlayer = playerService.getPlayerByName(nombre);
        if (!respuestaPlayer.getEstado()) {
            lblMessage.setText("No existe un jugador con ese nombre.");
            return;
        }
        PlayerDto playerDto = (PlayerDto) respuestaPlayer.getResultado("Player");
        Respuesta respuestaPartida = partidaService.getPartida(playerDto.getId());
        if (!respuestaPartida.getEstado()) {
            lblMessage.setText("El jugador no tiene partida registrada.");
            return;
        }
        PartidaDto partidaDto = (PartidaDto) respuestaPartida.getResultado("Partida");
        Respuesta respuestaMejora = mejoraService.getMejora(partidaDto.getIdmej().getId());
        if (!respuestaMejora.getEstado()) {
            lblMessage.setText("No se encontraron las mejoras del jugador.");
            return;
        }
        MejoraDto mejoraDto = (MejoraDto) respuestaMejora.getResultado("Mejora");
        AppContext.getInstance().set("Player", playerDto);
        AppContext.getInstance().set("Partida", partidaDto);
        AppContext.getInstance().set("Mejora", mejoraDto);
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }
}