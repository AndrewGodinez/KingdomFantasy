package cr.ac.una.kingdomfantasy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import javafx.scene.text.Font;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(App.class.getResourceAsStream("resource/Kingthings_Exeter.ttf"), 14);
        stage.setMinHeight(600);
        stage.setMinWidth(800);

        stage.setOnCloseRequest(event -> {
            MusicManager.getInstance().shutdown();
        });

        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().setFullScreen(true);
        FlowController.getInstance().goViewInWindow("PrincipalView");
    }

    @Override
    public void stop() {
        MusicManager.getInstance().shutdown();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}