package gyurix.activityplanner.gui;

import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.scenes.main.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;
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
        DataStorage.load(new File("conf.json"));
        CookieHandler.setDefault(new CookieManager());
        new LoginScene(stage).start();
    }
}
