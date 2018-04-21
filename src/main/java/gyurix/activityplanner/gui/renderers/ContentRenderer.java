package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import gyurix.activityplanner.gui.scenes.viewer.ContentViewer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientTop;

/**
 * ContentRenderer used for rendering Contents (Tables and Alerts)
 */
public class ContentRenderer extends DataRenderer implements ContentVisitor {
    /**
     * Multiplier of the remove button size
     */
    private static final double REMOVE_ICON_SIZE = 0.025;
    /**
     * Next Alerts index
     */
    private int alertIndex;
    /**
     * The parent UserScene
     */
    private UserScene scene;
    /**
     * Next Tables index
     */
    private int tableIndex;

    /**
     * Constructs a new ContentRenderer with the given UserScene
     *
     * @param scene - The parent UserScene
     */
    public ContentRenderer(UserScene scene) {
        this.scene = scene;
        scene.getAlerts().getChildren().clear();
        scene.getTables().getChildren().clear();
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return scene.getScreenWidth();
    }

    /**
     * Makes the grid for a content
     *
     * @param content - The showable content
     * @return The grid made for showing the given content
     */
    public GridPane makeContentGrid(Table content) {
        GridPane grid = new GridPane();
        makeDynamicBackground(grid, content.getColor());
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getColumnConstraints().addAll(pctCol(5), pctCol(85), pctCol(5), pctCol(5));
        return grid;
    }

    /**
     * Makes the dynamic background of a grid pane
     *
     * @param grid  - The changeable grid
     * @param color - The grids dynamically changeable color
     */
    public void makeDynamicBackground(GridPane grid, Observable<String> color) {
        attach(color, () -> grid.setBackground(bgColorGradientTop(Color.web("#" + color.getData()))));
    }

    /**
     * Renders the given alert
     *
     * @param a - The renderable alert
     * @return The rendered alert
     */
    private GridPane renderAlert(Alert a) {
        GridPane grid = renderTable(a);
        Label date = renderDate(a.getDueDate());
        grid.add(date, 1, 2, 2, 1);
        return grid;
    }

    /**
     * Renders the given table
     *
     * @param t - The renderable table
     * @return The rendered table
     */
    private GridPane renderTable(Table t) {
        GridPane grid = makeContentGrid(t);
        Label title = renderText(24, t.getTitle());
        Label subtitle = renderText(16, t.getSubtitle());

        grid.add(title, 1, 0);
        if (scene.getInfo().isContentEditable(t.getId().getData())) {
            Pane removeIcon = createClickablePicture(Icons.REMOVE, REMOVE_ICON_SIZE, () ->
                    DataStorage.getInstance().removeContent(scene.getInfo(), t.getId().getData()));
            grid.add(removeIcon, 2, 0);
        }
        grid.add(subtitle, 1, 1, 2, 1);
        grid.setOnMouseReleased((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                Platform.runLater(() -> {
                    if (t.getId().getData() != 0)
                        new ContentViewer(scene, t, new Stage()).start();
                });
            }
        });

        return grid;
    }

    @Override
    public void visit(Alert a) {
        Platform.runLater(() -> scene.getAlerts().add(renderAlert(a), 1, alertIndex++));
    }

    @Override
    public void visit(Table t) {
        Platform.runLater(() -> scene.getTables().add(renderTable(t), 1, tableIndex++));
    }
}