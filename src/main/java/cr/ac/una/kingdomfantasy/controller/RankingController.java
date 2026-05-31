package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.PartidaService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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
    private TableView<PartidaDto> tvRanking;
    @FXML
    private TableColumn<PartidaDto, Integer> colRank;
    @FXML
    private TableColumn<PartidaDto, ImageView> colAvatar;
    @FXML
    private TableColumn<PartidaDto, String> colName;
    @FXML
    private TableColumn<PartidaDto, Integer> colLevel;
    @FXML
    private TableColumn<PartidaDto, Long> colPoints;
    @FXML
    private Label lblCurrentPlayerName;
    @FXML
    private Label lblCurrentPlayerRank;
    @FXML
    private Label lblCurrentPlayerLevel;
    @FXML
    private Label lblCurrentPlayerPoints;
    @FXML
    private ImageView imvJugadorActual;

    private PartidaService partidaService = new PartidaService();
    
    private ObservableList<PartidaDto> partidas = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }

    @Override
    public void initialize() {
        cargarRanking();
        cargarJugadorActual();
    }

    @FXML
    private void onActionBack(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }
    
    private void configurarTabla() {
        tvRanking.setItems(partidas);
        colRank.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                tvRanking.getItems().indexOf(data.getValue()) + 1
        ));
        colAvatar.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                crearImagen(data.getValue().getIdply().getFotoPerfil(), 42, 42)
        ));
        colName.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                data.getValue().getIdply().getNombre()
        ));
        colLevel.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                numero(data.getValue().getNivelActual())
        ));
        colPoints.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                numero(data.getValue().getIdply().getPuntosTotales())
        ));
    }

    private void cargarRanking() {
        partidas.clear();
        Respuesta respuesta = partidaService.getRanking();
        if (respuesta.getEstado()) {
            List<PartidaDto> ranking = (List<PartidaDto>) respuesta.getResultado("Partidas");
            partidas.addAll(ranking);
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
        int posicion = partidas.indexOf(partidaActual) + 1;
        lblCurrentPlayerName.setText(partidaActual.getIdply().getNombre());
        lblCurrentPlayerRank.setText("#" + posicion);
        lblCurrentPlayerLevel.setText(String.valueOf(numero(partidaActual.getNivelActual())));
        lblCurrentPlayerPoints.setText(String.valueOf(numero(partidaActual.getIdply().getPuntosTotales())));
        if (imvJugadorActual != null) {
            cargarImagenActual(partidaActual.getIdply().getFotoPerfil());
        }
    }

    private PartidaDto buscarPartidaJugador(Long idPlayer) {
        for (PartidaDto partida : partidas) {
            if (partida.getIdply().getId().equals(idPlayer)) {
                return partida;
            }
        }
        return null;
    }

    private ImageView crearImagen(byte[] foto, double ancho, double alto) {
        if (foto == null || foto.length == 0) {
            return null;
        }
        ImageView imagen = new ImageView(new Image(new ByteArrayInputStream(foto)));
        imagen.setFitWidth(ancho);
        imagen.setFitHeight(alto);
        imagen.setPreserveRatio(false);
        return imagen;
    }

    private void cargarImagenActual(byte[] foto) {
        if (foto == null || foto.length == 0) {
            imvJugadorActual.setImage(null);
            return;
        }
        imvJugadorActual.setImage(new Image(new ByteArrayInputStream(foto)));
    }

    private void limpiarJugadorActual() {
        lblCurrentPlayerName.setText("Jugador Actual");
        lblCurrentPlayerRank.setText("#—");
        lblCurrentPlayerLevel.setText("—");
        lblCurrentPlayerPoints.setText("—");
        if (imvJugadorActual != null) {
            imvJugadorActual.setImage(null);
        }
    }

    private int numero(Integer valor) {
        return valor == null ? 0 : valor;
    }

    private Long numero(Long valor) {
        return valor == null ? 0L : valor;
    }
    
}