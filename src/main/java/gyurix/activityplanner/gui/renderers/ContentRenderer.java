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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientTop;
import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

public class ContentRenderer extends DataRenderer implements ContentVisitor {
    private static final double REMOVE_ICON_SIZE = 0.025;
    private int alertIndex, tableIndex;
    private UserScene scene;

    public ContentRenderer(UserScene scene) {
        this.scene = scene;
        scene.getChat().getChildren().clear();
        scene.getAlerts().getChildren().clear();
        scene.getTables().getChildren().clear();
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return scene.getScreenWidth();
    }

    public GridPane makeContentGrid(Table content) {
        GridPane grid = new GridPane();
        makeDynamicBackground(grid, content.getColor());
        grid.setHgap(10);
        grid.setVgap(10);
        makeContentGridColumns(grid);
        return grid;
    }

    public void makeContentGridColumns(GridPane grid) {
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(85);
        grid.getColumnConstraints().addAll(side, center, side, side);
    }

    public void makeDynamicBackground(GridPane grid, Observable<String> obs) {
        attach(obs, () -> grid.setBackground(bgColorGradientTop(Color.web("#" + obs.getData()))));
    }

    private GridPane renderAlert(Alert a) {
        GridPane grid = renderTable(a);
        Label date = renderDate(a.getDueDate());
        date.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(date, 1, 2, 2, 1);
        return grid;
    }

    private Label renderDate(Observable<Long> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(formatTime(obs.getData())));
        return label;
    }

    private GridPane renderTable(Table t) {
        GridPane grid = makeContentGrid(t);
        Label title = renderText(24, t.getTitle());
        Label subtitle = renderText(16, t.getSubtitle());

        grid.add(title, 1, 0);
        if (scene.getInfo().isContentEditable(t.getId().getData())) {
            Pane removeIcon = createClickableImage(Icons.REMOVE, REMOVE_ICON_SIZE, () ->
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