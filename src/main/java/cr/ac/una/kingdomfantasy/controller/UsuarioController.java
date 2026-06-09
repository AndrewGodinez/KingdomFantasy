package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.CrossbowDesign;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.PlayerService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Mensaje;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class UsuarioController extends Controller implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imvFondo;
    @FXML
    private MFXButton btnCrossBowGreen;
    @FXML
    private MFXButton btnCrossBowPurple;
    @FXML
    private Label lbJugador;
    @FXML
    private ImageView imvImagenJugador;
    @FXML
    private MFXButton btnCambiarImagen;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnGuardar;

    private PlayerService playerService = new PlayerService();

    private CrossbowDesign selectedDesign = CrossbowDesign.GREEN;
    private byte[] imagenPerfilBytes = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }

    @Override
    public void initialize() {
        limpiarVista();
        cargarJugadorActual();
    }

    @FXML
    private void onActionBtnCrossBowGreen(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        selectedDesign = CrossbowDesign.GREEN;
        actualizarSeleccionBallesta();
    }

    @FXML
    private void onActionBtnCrossBowPurple(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        selectedDesign = CrossbowDesign.PURPLE;
        actualizarSeleccionBallesta();
    }

    @FXML
    private void onActionBtnCambiarImagen(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                imvImagenJugador.setImage(new Image(file.toURI().toString()));
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
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, "Error leyendo imagen", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(), "No se pudo cargar la imagen seleccionada.");
            }
        }
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
        MusicManager.getInstance().playEffect(MusicManager.SoundEffect.BUTTON_CLICK);
        PlayerDto playerDto = (PlayerDto) AppContext.getInstance().get("Player");
        if (playerDto == null) {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Sin sesión", getStage(), "No hay un jugador con sesión activa.");
            return;
        }

        if (imagenPerfilBytes != null) {
            playerDto.setFotoPerfil(imagenPerfilBytes);
        }
        playerDto.setIdBallesta(selectedDesign == CrossbowDesign.PURPLE ? 2 : 1);

        Respuesta respuesta = playerService.guardarPlayer(playerDto);
        if (respuesta.getEstado()) {
            PlayerDto actualizado = (PlayerDto) respuesta.getResultado("Jugador");
            AppContext.getInstance().set("Player", actualizado);
            imagenPerfilBytes = null;
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardado", getStage(), "Perfil actualizado correctamente.");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(), "No se pudieron guardar los cambios.");
        }
    }

    
    private void cargarJugadorActual() {
        PlayerDto playerDto = (PlayerDto) AppContext.getInstance().get("Player");
        if (playerDto == null) {
            limpiarVista();
            return;
        }

        lbJugador.setText(playerDto.getNombre());
        cargarImagenActual(playerDto.getFotoPerfil());
        selectedDesign = playerDto.getIdBallesta() != null && playerDto.getIdBallesta() == 2  ? CrossbowDesign.PURPLE   : CrossbowDesign.GREEN;
        actualizarSeleccionBallesta();
    }

    private void cargarImagenActual(byte[] foto) {
        if (foto == null || foto.length == 0) {
            imvImagenJugador.setImage(null);
            return;
        }
        imvImagenJugador.setImage(new Image(new ByteArrayInputStream(foto)));
    }

    private void actualizarSeleccionBallesta() {
        setStyleClassEnabled(btnCrossBowGreen, "jfx-selected-skin-green", selectedDesign == CrossbowDesign.GREEN);
        setStyleClassEnabled(btnCrossBowPurple, "jfx-selected-skin-purple", selectedDesign == CrossbowDesign.PURPLE);
    }

    private void limpiarVista() {
        lbJugador.setText("Jugador");
        if (imvImagenJugador != null) {
            imvImagenJugador.setImage(null);
        }
        imagenPerfilBytes = null;
        selectedDesign = CrossbowDesign.GREEN;
        actualizarSeleccionBallesta();
    }

    private void setStyleClassEnabled(MFXButton button, String styleClass, boolean enabled) {
        button.getStyleClass().remove(styleClass);
        if (enabled) {
            button.getStyleClass().add(styleClass);
        }
    }

}
