package gyurix.activityplanner.gui.scenes.core;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.renderers.DataRenderer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public abstract class AbstractScene extends DataRenderer {
    protected final Stage stage;
    protected GridPane grid = new GridPane();
    protected Scene scene;
    protected Observable<Double> screenWidth = new Observable<>();

    public AbstractScene(Stage stage) {
        this.stage = stage;
    }

    public abstract void addNodesToGrid();

    public abstract void createNodes();

    public abstract void createScene();

    public void createResizableScene(double multiplier, String page) {
        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();
        double maxx = bounds.getWidth();
        double maxy = bounds.getHeight();
        scene = new Scene(grid, maxx * multiplier, maxy * multiplier);
        screenWidth.setData(multiplier * maxx);

        stage.setScene(scene);
        stage.setTitle("• Activity Planner - " + page + " •");
        stage.getIcons().setAll(Icons.LOGO.getImage());
        stage.setResizable(true);
        stage.setMinWidth(0.6 * multiplier * maxx);
        stage.setMinHeight(0.6 * multiplier * maxy);
        stage.setX((1 - multiplier) / 2 * maxx);
        stage.setY((1 - multiplier) / 2 * maxy);
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
                destroy();
            }
        });
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            screenWidth.setData(newVal.doubleValue());
        });
    }

    public abstract void makeGrid();

    public void makeGridColumns() {
    }

    public void makeGridRows() {
    }

    public void prepareScene() {
        stage.setScene(scene);
        stage.show();
    }

    public void start() {
        createScene();
        createNodes();
        makeGrid();
        addNodesToGrid();
        prepareScene();
        stage.setOnCloseRequest(event -> destroy());
    }
}
