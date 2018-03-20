package gyurix.activityplanner.gui;

import gyurix.activityplanner.gui.scenes.main.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.CookieHandler;
import java.net.CookieManager;

public class ActivityPlannerLauncher extends Application {
    @Getter
    private static ActivityPlannerLauncher instance;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        instance = this;
        CookieHandler.setDefault(new CookieManager());
        new LoginScene(stage).start();
    }
}
