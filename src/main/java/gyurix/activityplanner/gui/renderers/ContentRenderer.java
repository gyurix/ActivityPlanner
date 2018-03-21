package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import gyurix.activityplanner.gui.scenes.viewer.AlertViewer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientTop;
import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

public class ContentRenderer extends DataRenderer implements ContentVisitor {
    private int alertIndex, tableIndex;
    private UserScene scene;

    public ContentRenderer(UserScene scene) {
        this.scene = scene;
    }

    public GridPane makeAlertGrid(Alert alert) {
        GridPane grid = new GridPane();
        makeDynamicBackground(grid, alert.getColor());
        grid.setHgap(10);
        grid.setVgap(10);
        makeAlertGridColumns(grid);
        return grid;
    }

    public void makeAlertGridColumns(GridPane grid) {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(15);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(15);

        grid.getColumnConstraints().addAll(column1, column2, column3);
    }

    public void makeDynamicBackground(GridPane grid, Observable<String> obs) {
        attach(obs, () -> grid.setBackground(bgColorGradientTop(Color.web("#" + obs.getData()))));
    }

    private GridPane renderAlert(Alert a) {
        GridPane grid = makeAlertGrid(a);

        Label title = renderText(24, a.getTitle());
        title.setPrefWidth(MAX_VALUE);
        title.setAlignment(Pos.BOTTOM_CENTER);

        Label subtitle = renderText(16, a.getSubtitle());
        subtitle.setAlignment(Pos.TOP_LEFT);
        subtitle.setPrefWidth(MAX_VALUE);

        Label date = renderDate(a.getDueDate());
        date.setPrefWidth(MAX_VALUE);
        date.setAlignment(Pos.BOTTOM_RIGHT);

        grid.add(title, 0, 0, 3, 1);
        grid.add(subtitle, 0, 1, 3, 1);
        grid.add(date, 1, 2, 3, 1);
        grid.setOnMouseReleased((e) -> {
            if (e.getButton() == MouseButton.PRIMARY)
                new AlertViewer(a, new Stage()).start();
        });

        return grid;
    }

    private Label renderDate(Observable<Long> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(formatTime(obs.getData())));
        return label;
    }

    private GridPane renderTable(Table t) {
        return new GridPane();
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