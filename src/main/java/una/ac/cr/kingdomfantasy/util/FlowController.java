/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package una.ac.cr.kingdomfantasy.util;

import una.ac.cr.kingdomfantasy.App;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import una.ac.cr.kingdomfantasy.controller.Controller;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 *
 * @author andrew
 */
public class FlowController {
    
    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();
    private Object parameter;

    private FlowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        this.mainStage = stage;
        this.idioma = idioma;
        // Precarga en background las vistas más usadas para que el primer cambio sea instantáneo
        javafx.application.Platform.runLater(() -> warmUpViews(
            "PrincipalView", "AcercaDeView", "LoginView",
            "JuegoView", "RankingView", "MejorasView", "AjustesView", "RegistroView"
        ));
    }

    /** Carga silenciosamente los FXMLs sin mostrarlos, para que el loader los cachee. */
    private void warmUpViews(String... viewNames) {
        for (String name : viewNames) {
            try {
                getLoader(name);
            } catch (Exception ex) {
                // Silencioso — si falla el warmup no es bloqueante
            }
        }
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource(
                                "/una/ac/cr/kingdomfantasy/view/" + name + ".fxml"), this.idioma);
                        loader.load();
                        loaders.put(name, loader);
                    } catch (Exception ex) {
                        loader = null;
                        java.util.logging.Logger.getLogger(FlowController.class.getName())
                                .log(Level.SEVERE, "Creando loader [" + name + "].", ex);
                    }
                }
            }
        }
        return loader;
    }

    public void goMain() {
        try {
            Parent root = FXMLLoader.load(
            App.class.getResource("view/PrincipalView.fxml"), this.idioma);
            AnchorPane rootContainer = new AnchorPane(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            this.mainStage.setScene(new Scene(rootContainer));
            MFXThemeManager.addOn(this.mainStage.getScene(), Themes.DEFAULT, Themes.LEGACY);
            this.mainStage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName())
                    .log(Level.SEVERE, "Error inicializando la vista base.", ex);
        }
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
    }

    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        this.parameter = null;

        Stage stage = controller.getStage();
        if (stage == null) {
            stage = this.mainStage;
            controller.setStage(stage);
        }

        switch (location) {
            case "Center":
                BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
                VBox vBox = (VBox) borderPane.getCenter();
                vBox.getChildren().clear();
                Parent root = loader.getRoot();
                VBox.setVgrow(root, Priority.ALWAYS);
                if (root instanceof Region region) {
                    
                }
                vBox.getChildren().add(root);
                break;

            case "Top":
            case "Bottom":
            case "Right":
            case "Left":
            default:
                break;
        }
    }

    public void goView(String viewName, Object parameter) {
        this.parameter = parameter;
        goView(viewName, "Center", null);
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        Parent root = loader.getRoot();
        // Guardar dimensiones actuales para no alterar el tamaño de la ventana al cambiar vista
        double currentW = stage.getWidth();
        double currentH = stage.getHeight();
        Parent sceneRoot = stage.getScene().getRoot();
        if (sceneRoot instanceof AnchorPane container) {
        container.getChildren().setAll(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
      } else {
    stage.getScene().setRoot(root);
}
        // Aplicar tamaño minimo si la ventana es mas pequena que el minimo soportado
        if (root instanceof Region region) {
            double minW = region.getMinWidth();
            double minH = region.getMinHeight();
            if (minW > 0 && currentW < minW) stage.setWidth(minW);
            if (minH > 0 && currentH < minH) stage.setHeight(minH);
        }
        controller.initialize();
    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/una/ac/cr/kingdomfantasy/resource/GameLogo.png")));
        stage.setTitle(controller.getNombreVista());
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        AnchorPane rootContainer = new AnchorPane(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        Scene scene = new Scene(rootContainer);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        // Usar prefSize del FXML como tamaño inicial de la ventana
        if (root instanceof Region region) {
            double pw = region.getPrefWidth();
            double ph = region.getPrefHeight();
            if (pw > 0 && ph > 0) {
                stage.setWidth(pw);
                stage.setHeight(ph);
            }
        }
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(
                getClass().getResourceAsStream("/cr/ac/una/kingdomfantasy/resource/GameLogo.png")));
        stage.setTitle(controller.getNombreVista());
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        AnchorPane rootContainer = new AnchorPane(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        Scene scene = new Scene(rootContainer);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }

    public Object getParameter() {
        return parameter;
    }

    public void limpiarLoader(String view) {
        this.loaders.remove(view);
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }

    public void initialize() {
        this.loaders.clear();
    }

    public void salir() {
        this.mainStage.close();
    }
    
}
