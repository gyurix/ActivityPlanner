package gyurix.activityplanner.gui;

import gyurix.activityplanner.gui.scenes.main.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class ActivityPlannerLauncher extends Application {
    private static LoginScene loginScene = new LoginScene();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        loginScene.apply(stage);
    }
}
