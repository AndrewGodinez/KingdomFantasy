package cr.ac.una.kingdomfantasy;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import cr.ac.una.kingdomfantasy.util.FlowController;
import cr.ac.una.kingdomfantasy.util.MusicManager;
import javafx.scene.text.Font;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(App.class.getResourceAsStream("resource/Kingthings_Exeter.ttf"), 14);
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

    public static void main(String[] args) {
        launch();
    }

}