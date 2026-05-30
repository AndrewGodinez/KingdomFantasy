package cr.ac.una.kingdomfantasy.controller;

import cr.ac.una.kingdomfantasy.model.MejoraDto;
import cr.ac.una.kingdomfantasy.model.PartidaDto;
import cr.ac.una.kingdomfantasy.model.PlayerDto;
import cr.ac.una.kingdomfantasy.service.MejoraService;
import cr.ac.una.kingdomfantasy.service.PartidaService;
import cr.ac.una.kingdomfantasy.util.AppContext;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.Respuesta;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MejorasController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label lbCurrentLevel;
    @FXML
    private Label lbGold;
    @FXML
    private Label lbTotalPoints;
    @FXML
    private MFXButton btnBackMenu;
    @FXML
    private MFXButton btnReviewMaxAll;
    @FXML
    private MFXButton btnStartGame;
    @FXML
    private TabPane tabPane;
    @FXML
    private ProgressBar pgCrossbowDamage;
    @FXML
    private Label lbCrossbowDamageLevel;
    @FXML
    private Label lbCrossbowDamageCost;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowDamage;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowDamage;
    @FXML
    private MFXButton btnUpgradeCrossbowDamage;
    @FXML
    private ProgressBar pgCrossbowSpeed;
    @FXML
    private Label lbCrossbowSpeedLevel;
    @FXML
    private Label lbCrossbowSpeedCost;
    @FXML
    private MFXButton btnReviewDecreaseCrossbowSpeed;
    @FXML
    private MFXButton btnReviewIncreaseCrossbowSpeed;
    @FXML
    private MFXButton btnUpgradeCrossbowSpeed;
    @FXML
    private MFXButton btnCrossBowGreen;
    @FXML
    private MFXButton btnCrossBowPurple;
    @FXML
    private Label lbSelectedSkin;
    @FXML
    private ProgressBar pgCastleHealth;
    @FXML
    private Label lbCastleHealthLevel;
    @FXML
    private Label lbCastleHealthCost;
    @FXML
    private MFXButton btnReviewDecreaseCastleHealth;
    @FXML
    private MFXButton btnReviewIncreaseCastleHealth;
    @FXML
    private MFXButton btnUpgradeCastleHealth;
    @FXML
    private ProgressBar pgElixir;
    @FXML
    private Label lbElixirLevel;
    @FXML
    private Label lbElixirCost;
    @FXML
    private MFXButton btnReviewDecreaseElixir;
    @FXML
    private MFXButton btnReviewIncreaseElixir;
    @FXML
    private MFXButton btnUpgradeElixir;
    @FXML
    private ProgressBar pgMeteor;
    @FXML
    private Label lbMeteorLevel;
    @FXML
    private Label lbMeteorCost;
    @FXML
    private MFXButton btnReviewDecreaseMeteor;
    @FXML
    private MFXButton btnReviewIncreaseMeteor;
    @FXML
    private MFXButton btnUpgradeMeteor;
    @FXML
    private ProgressBar pgMeteorRadius;
    @FXML
    private Label lblMeteorRadiusLevel;
    @FXML
    private Label lbMeteorRadiusCost;
    @FXML
    private MFXButton btnReviewDecreaseMeteorRadius;
    @FXML
    private MFXButton btnReviewIncreaseMeteorRadius;
    @FXML
    private MFXButton btnUpgradeMeteorRadius;
    @FXML
    private ProgressBar pgIce;
    @FXML
    private Label lblIceLevel;
    @FXML
    private Label lbIceCost;
    @FXML
    private MFXButton btnReviewDecreaseIce;
    @FXML
    private MFXButton btnReviewIncreaseIce;
    @FXML
    private MFXButton btnUpgradeIce;
    @FXML
    private ProgressBar pgIceRadius;
    @FXML
    private Label lblIceRadiusLevel;
    @FXML
    private Label lbIceRadiusCost;
    @FXML
    private MFXButton btnReviewDecreaseIceRadius;
    @FXML
    private MFXButton btnReviewIncreaseIceRadius;
    @FXML
    private MFXButton btnUpgradeIceRadius;
    
    private PlayerDto playerDto;
    
    private PartidaDto partidaDto;
    
    private MejoraDto mejoraDto;
    
    private PartidaService partidaService = new PartidaService();
    
    private MejoraService mejoraService = new MejoraService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @Override
    public void initialize() {
        cargarSesion();
        if(playerDto==null){
        FlowController.getInstance().goViewInStage("LoginView",getStage());
        return;
        }
     cargarDatosPantalla();   
    }

    @FXML
    private void onActionBtnBackMenu(ActionEvent event) {
        FlowController.getInstance().goViewInStage("PrincipalView", getStage());
    }

    @FXML
    private void onActionBtnReviewMaxAll(ActionEvent event) {
    }

    @FXML
    private void onActionBtnStartGame(ActionEvent event) {
        FlowController.getInstance().goViewInStage("JuegoView", getStage());
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCrossbowDamage(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCrossbowSpeed(ActionEvent event) {
    }

    @FXML
    private void onActionBtnCrossBowGreen(ActionEvent event) {
    playerDto.setIdBallesta(1);
    AppContext.getInstance().set("Player", playerDto);
    cargarBallesta();
    }

    @FXML
    private void onActionBtnCrossBowPurple(ActionEvent event) {
    playerDto.setIdBallesta(2);
    AppContext.getInstance().set("Player", playerDto);
    cargarBallesta();
    }

    @FXML
    private void onActionBtnReviewDecreaseCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeCastleHealth(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeElixir(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeMeteor(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeMeteorRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeIce(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewDecreaseIceRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnReviewIncreaseIceRadius(ActionEvent event) {
    }

    @FXML
    private void onActionBtnUpgradeIceRadius(ActionEvent event) {
    }
    
    private void cargarSesion(){
    playerDto = (PlayerDto) AppContext.getInstance().get("Player");
    partidaDto = (PartidaDto) AppContext.getInstance().get("Partida");
    mejoraDto = (MejoraDto) AppContext.getInstance().get("Mejora");
    
    if(playerDto != null && partidaDto == null){
    Respuesta respuestaPartida = partidaService.getPartida(playerDto.getId());
        if (respuestaPartida.getEstado()) {
            partidaDto = (PartidaDto) respuestaPartida.getResultado("Partida");
            AppContext.getInstance().set("Partida", partidaDto);
        }
    }
    if(partidaDto != null && mejoraDto == null){
    Respuesta respuestaMejora = mejoraService.getMejora(partidaDto.getIdmej().getId());
        if (respuestaMejora.getEstado()) {
            mejoraDto = (MejoraDto) respuestaMejora.getResultado("Mejora");
            AppContext.getInstance().set("Mejora", mejoraDto);
        }
    }
 }
    private void cargarDatosPantalla(){
    if (partidaDto == null || mejoraDto == null) {
        return;
    }
    lbCurrentLevel.setText("Nivel actual: " + numero(partidaDto.getNivelActual()));
    lbGold.setText(String.valueOf(numero(partidaDto.getPuntosActuales())));
    lbTotalPoints.setText(String.valueOf(numero(playerDto.getPuntosTotales())));

    cargarMejora(pgCrossbowDamage, lbCrossbowDamageLevel, lbCrossbowDamageCost, mejoraDto.getNivelDanoBallesta(), 25, 18);
    cargarMejora(pgCrossbowSpeed, lbCrossbowSpeedLevel, lbCrossbowSpeedCost, mejoraDto.getNivelVelocidadBallesta(), 25, 16);
    cargarMejora(pgMeteor, lbMeteorLevel, lbMeteorCost, mejoraDto.getNivelEfectoMeteoro(), 10, 12);
    cargarMejora(pgMeteorRadius, lblMeteorRadiusLevel, lbMeteorRadiusCost, mejoraDto.getNivelRangoMeteoro(), 10, 12);
    cargarMejora(pgIce, lblIceLevel, lbIceCost, mejoraDto.getNivelEfectoHielo(), 10, 12);
    cargarMejora(pgIceRadius, lblIceRadiusLevel, lbIceRadiusCost, mejoraDto.getNivelRangoHielo(), 10, 12);
    cargarMejora(pgCastleHealth, lbCastleHealthLevel, lbCastleHealthCost, mejoraDto.getNivelCastillo(), 10, 10);
    cargarMejora(pgElixir, lbElixirLevel, lbElixirCost, mejoraDto.getNivelElixir(), 10, 8);
    
    cargarBallesta();
    
 } 
    private void cargarMejora(ProgressBar barra, Label nivelTexto, Label costoTexto, Integer nivel, int maximo, int costo) {
    int valor = numero(nivel);
    barra.setProgress(Math.min(1.0, (double) valor / maximo));
    nivelTexto.setText("Nivel " + valor + "/" + maximo);
    costoTexto.setText((costo * valor) + " monedas");
 }

    private void cargarBallesta() {
    if (playerDto.getIdBallesta() != null && playerDto.getIdBallesta() == 2) {
        lbSelectedSkin.setText("Seleccionada: Morada");
        btnCrossBowPurple.setStyle("-fx-border-color: purple; -fx-border-width: 3");
        btnCrossBowGreen.setStyle("");
    } else {
        lbSelectedSkin.setText("Seleccionada: Verde");
        btnCrossBowGreen.setStyle("-fx-border-color: green; -fx-border-width: 3");
        btnCrossBowPurple.setStyle("");
    }
 }

    private int numero(Integer valor) {
    return valor == null ? 1 : valor;
 }

    private Long numero(Long valor) {
    return valor == null ? 0L : valor;
 }
    
}
