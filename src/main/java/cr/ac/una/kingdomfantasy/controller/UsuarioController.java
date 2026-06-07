/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.PartidaService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author andrew
 */
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
    @FXML
    private MFXButton btnCerrarSesion;
    private byte[] imagenPerfilBytes = null;
    private PartidaService partidaService = new PartidaService();
    private ObservableList<PartidaDto> partidas = FXCollections.observableArrayList();

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
        cargarJugadorActual();
    }

    @FXML
    private void onActionBtnCrossBowGreen(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCrossBowPurple(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCambiarImagen(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar foto de perfil");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );
    File file = fileChooser.showOpenDialog(getStage());
    if (file != null) {
        try {
            Image image = new Image(file.toURI().toString());
            imvImagenJugador.setImage(image);
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
           
        }
    }
    }
    
     private void cargarJugadorActual() {
        PlayerDto playerDto = (PlayerDto) AppContext.getInstance().get("Player");
        if (playerDto == null) {
            limpiarJugadorActual();
            return;
        }

        PartidaDto partidaActual = buscarPartidaJugador(playerDto.getId());
        if (partidaActual == null) {
            limpiarJugadorActual();
            return;
        }
        lbJugador.setText(partidaActual.getIdply().getNombre());

            cargarImagenActual(partidaActual.getIdply().getFotoPerfil());
 
    }
     
     private void cargarImagenActual(byte[] foto) {
        if (foto == null || foto.length == 0) {
            imvImagenJugador.setImage(null);
            return;
        }
        imvImagenJugador.setImage(new Image(new ByteArrayInputStream(foto)));
    }
     
    private PartidaDto buscarPartidaJugador(Long idPlayer) {
        for (PartidaDto partida : partidas) {
            if (partida.getIdply().getId().equals(idPlayer)) {
                return partida;
            }
        }
        return null;
    }
     
    private void limpiarJugadorActual() {
        lbJugador.setText("Jugador Actual");
        if (imvImagenJugador != null) {
            imvImagenJugador.setImage(null);
        }
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCerrarSesion(ActionEvent event) {
    }

    
}
