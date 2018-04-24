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

/**
 * Abstract scene contains all the utility methods required
 * for making every scene. It also contains some abstract methods
 * which should be implemented by the classes extending it
 */
@Getter
public abstract class AbstractScene extends DataRenderer {

    /**
     * The stage of the window, showing this scene
     */
    protected final Stage stage;

    /**
     * The main grid of the scene, containing all the main nodes,
     * which should be rendered
     */
    protected GridPane grid = new GridPane();

    /**
     * The scene of the window
     */
    protected Scene scene;

    /**
     * The dynamically changing screen width, which can be observed
     * by auto resizable elements
     */
    protected Observable<Double> screenWidth = new Observable<>();

    /**
     * Creates a new AbstractScene
     *
     * @param stage - The stage of the window
     */
    protected AbstractScene(Stage stage) {
        this.stage = stage;
    }

    /**
     * Add renderable nodes to the main grid
     */
    protected abstract void addNodesToGrid();

    /**
     * Creates all the renderable nodes
     */
    protected abstract void createNodes();

    /**
     * Creates a resizable screen with the given title and the
     * given size multiplier. The minimal screen size is the 60%
     * of the given size multiplier.
     * You can exit from scenes created by this method by pressing ESC
     *
     * @param sizeMultiplier - Multiplier of the scene size relative to the
     *                       total screen size of the main screen
     * @param title          - Title, shown in the titlebar of the window.
     */
    protected void createResizableScene(double sizeMultiplier, String title) {
        Screen screen = Screen.getPrimary();

        //Create scene
        Rectangle2D bounds = screen.getVisualBounds();
        double maxx = bounds.getWidth();
        double maxy = bounds.getHeight();
        scene = new Scene(grid, maxx * sizeMultiplier, maxy * sizeMultiplier);
        screenWidth.setData(sizeMultiplier * maxx);

        //Prepare stage
        stage.setScene(scene);
        stage.setTitle("• Activity Planner - " + title + " •");
        stage.getIcons().setAll(Icons.LOGO.getImage());
        stage.setResizable(true);

        //Set minimal window size
        stage.setMinWidth(0.6 * sizeMultiplier * maxx);
        stage.setMinHeight(0.6 * sizeMultiplier * maxy);

        //Center window
        stage.setX((1 - sizeMultiplier) / 2 * maxx);
        stage.setY((1 - sizeMultiplier) / 2 * maxy);

        //Make ESC close the window
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
                destroy();
            }
        });

        //Listen to window resizing
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            screenWidth.setData(newVal.doubleValue());
        });
    }

    /**
     * Creates the scene, usually delegates scene creation to
     * createResizableScene method
     */
    protected abstract void createScene();

    /**
     * Makes the main grid of the window
     */
    protected abstract void makeGrid();

    /**
     * Makes the columns of the main grid
     */
    protected void makeGridColumns() {
    }

    /**
     * Makes the rows of the main grid
     */
    protected void makeGridRows() {
    }

    /**
     * Makes the final preparations for showing the scene
     */
    protected void prepareScene() {
        stage.setOnCloseRequest(event -> destroy());
        stage.show();
    }

    /**
     * Starts the rendering process
     */
    public void start() {
        createScene();
        createNodes();
        makeGrid();
        addNodesToGrid();
        prepareScene();
    }
}
