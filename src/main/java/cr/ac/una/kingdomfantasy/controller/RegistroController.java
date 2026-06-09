package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.Mejora;
import cr.ac.una.kingdomfantasy.model.Partida;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.PlayerService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Formato;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;


public class RegistroController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lbMessage;
    @FXML
    private MFXButton btnCancel;
    @FXML
    private MFXButton btnCreatePlayer;
    @FXML
    private MFXTextField txfName;
    @FXML
    private MFXButton btnCrossBowGreen;
    @FXML
    private MFXButton btnCrossBowPurple;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private MFXButton btnLoadImage;
    
    private Integer idBallestaSeleccionada = 0; 
    
    private byte[] imagenPerfilBytes = null;
    
    private PlayerService playerService = new PlayerService();
    @FXML
    private ImageView imvFondo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txfName.setTextFormatter(Formato.getInstance().letrasFormat(40));
        lbMessage.setText("");
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }    

    @Override
    public void initialize() {
    }

    @FXML
    private void onActionBtnCancel(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnCreatePlayer(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        String nombre = txfName.getText().trim();
        if (nombre.isEmpty()) {
            lbMessage.setText("Debe ingresar un nombre.");
            return;
        }
        if (idBallestaSeleccionada == 0) {
            lbMessage.setText("Debe seleccionar un tipo de ballesta.");
            return;
        }
        Respuesta existe = playerService.getPlayerByName(nombre);
        if (existe.getEstado()) {
            lbMessage.setText("Ya existe un jugador con ese nombre.");
            return;
        }
        try {
            Mejora mejora = new Mejora();
            mejora.setNivelVelocidadBallesta(1);
            mejora.setNivelDanoBallesta(1);
            mejora.setNivelEfectoMeteoro(1);
            mejora.setNivelRangoMeteoro(1);
            mejora.setNivelEfectoHielo(1);
            mejora.setNivelRangoHielo(1);
            mejora.setNivelCastillo(1);
            mejora.setNivelElixir(1);
            
            Partida partida = new Partida();
            partida.setNivelActual(1);
            partida.setPuntosActuales(0L);
            partida.setFechaGuardado(LocalDate.now());
            partida.setIdmej(mejora);

            List<Partida> partidas = new ArrayList<>();
            partidas.add(partida);

            PlayerDto nuevoPlayer = new PlayerDto();
            nuevoPlayer.setNombre(nombre);
            nuevoPlayer.setFotoPerfil(imagenPerfilBytes);
            nuevoPlayer.setIdBallesta(idBallestaSeleccionada);
            nuevoPlayer.setFechaRegistro(LocalDate.now());
            nuevoPlayer.setPuntosTotales(0L);
            nuevoPlayer.setPartidaList(partidas);

            Respuesta resPlayer = playerService.guardarPlayer(nuevoPlayer);
            if (resPlayer.getEstado()) {
                PlayerDto playerCreado = (PlayerDto) resPlayer.getResultado("Jugador");
                AppContext.getInstance().set("Player", playerCreado);
                FlowController.getInstance().goViewInStage("MejorasView", getStage());
            } else {
                lbMessage.setText(resPlayer.getMensaje());
            }
        } catch (Exception ex) {
            lbMessage.setText("Error inesperado al crear el jugador.");
        }
    }

    @FXML
    private void onActionBtnCrossBowGreen(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        idBallestaSeleccionada = 1;
        setSkinSelected(btnCrossBowGreen, "jfx-selected-skin-green", btnCrossBowPurple, "jfx-selected-skin-purple");
    }

    @FXML
    private void onActionBtnCrossBowPurple(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        idBallestaSeleccionada = 2;
        setSkinSelected(btnCrossBowPurple, "jfx-selected-skin-purple", btnCrossBowGreen, "jfx-selected-skin-green");
    }

    private void setSkinSelected(MFXButton selected, String selectedClass,
                                  MFXButton other, String otherClass) {
        selected.getStyleClass().remove(otherClass);
        if (!selected.getStyleClass().contains(selectedClass)) {
            selected.getStyleClass().add(selectedClass);
        }
        other.getStyleClass().remove(otherClass);
        other.getStyleClass().remove(selectedClass);
    }

    @FXML
    private void onActionBtnLoadImage(ActionEvent event) {
    MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar foto de perfil");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );
    File file = fileChooser.showOpenDialog(getStage());
    if (file != null) {
        try {
            Image image = new Image(file.toURI().toString());
            imgAvatar.setImage(image);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                bos.write(buf, 0, bytesRead);
            }
            fis.close();
            imagenPerfilBytes = bos.toByteArray();
            
        } catch (IOException ex) {
            lbMessage.setText("Error al cargar la imagen.");
        }
    }
    }
    
}
