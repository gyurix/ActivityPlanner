package gyurix.activityplanner.gui;

import gyurix.activityplanner.core.storage.ConfigUtils;
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
        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
        ConfigUtils.getInstance().loadConfig();
        CookieHandler.setDefault(new CookieManager());
        new LoginScene(stage).start();
    }
}
