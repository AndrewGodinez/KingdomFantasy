package cr.ac.una.kingdomfantasy.util;

import cr.ac.una.kingdomfantasy.App;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import cr.ac.una.kingdomfantasy.controller.Controller;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;


public class FlowController {
    
    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();
    private Object parameter;
    private static Boolean fullScr;

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
        Platform.runLater(() -> warmUpViews(
            "PrincipalView", "AcercaDeView", "LoginView",
            "JuegoView", "RankingView", "MejorasView", "AjustesView", "RegistroView"
        ));
    }

 
    private void warmUpViews(String... viewNames) {
        for (String name : viewNames) {
            try {
                getLoader(name);
            } catch (Exception ex) {
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
                                "/cr/ac/una/kingdomfantasy/view/" + name + ".fxml"), this.idioma);
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

        Stage stage = controller.getStage();
        if (stage == null) {
            stage = this.mainStage;
            controller.setStage(stage);
        }

        if ("Center".equals(location)) {
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
            VBox vBox = (VBox) borderPane.getCenter();
            vBox.getChildren().clear();
            Parent root = loader.getRoot();
            VBox.setVgrow(root, Priority.ALWAYS);
            vBox.getChildren().add(root);
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
                getClass().getResourceAsStream("/cr/ac/una/kingdomfantasy/resource/GameLogo.png")));
        stage.setTitle(controller.getNombreVista());
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
            cr.ac.una.kingdomfantasy.util.MusicManager.getInstance().shutdown();
            Platform.exit();
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

        if (root instanceof Region region) {
            double pw = region.getPrefWidth();
            double ph = region.getPrefHeight();
            if (pw > 0 && ph > 0) {
                stage.setWidth(pw);
                stage.setHeight(ph);
            }
        }

        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(540);
        stage.centerOnScreen();
        stage.show();

        if (fullScr != null && fullScr) {
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreen(true);
        }

    scene.setOnKeyPressed(e -> {
    if (e.getCode() == KeyCode.F11) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            stage.setWidth(950);
            stage.setHeight(800);
            stage.centerOnScreen();
        } else {
            stage.setFullScreen(true);
        }
    }
});
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
    
    public void setFullScreen(Boolean full) {
        this.fullScr = full;
    }
    
}
