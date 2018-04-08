package gyurix.activityplannertest;

import gyurix.activityplanner.core.storage.ConfigUtils;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import javafx.application.Application;
import javafx.stage.Stage;

public class ActivityPlannerTest extends Application {
    public static void main(String[] args) {
        ConfigUtils.getInstance().generateDefaultConfig();
        ActivityPlannerLauncher.main(args);
    }

    @Override
    public void start(Stage primaryStage) {
    }
}
