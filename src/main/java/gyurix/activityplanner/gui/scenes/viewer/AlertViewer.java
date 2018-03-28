package gyurix.activityplanner.gui.scenes.viewer;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import gyurix.activityplanner.gui.scenes.editor.DateEditor;
import gyurix.activityplanner.gui.scenes.editor.TextEditor;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

@Getter
public class AlertViewer extends ElementHolderScene<Alert> {
    private Label title, subtitle, date;

    public AlertViewer(UserScene userScene, Alert info, Stage stage) {
        super(userScene, info, stage);
    }

    @Override
    public void addNodesToGrid() {
        grid.add(title, 1, 1);
        grid.add(subtitle, 1, 2);
        grid.add(date, 1, 3);
        grid.add(elementsWrapper, 1, 4);
    }

    @Override
    public void createNodes() {
        title = renderText(24, info.getTitle());
        title.setPrefWidth(MAX_VALUE);
        title.setAlignment(Pos.BOTTOM_CENTER);
        title.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2)
                new TextEditor(this, info.getTitle()).start();
        });

        subtitle = renderText(16, info.getSubtitle());
        subtitle.setAlignment(Pos.TOP_LEFT);
        subtitle.setPrefWidth(MAX_VALUE);
        subtitle.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2)
                new TextEditor(this, info.getSubtitle()).start();
        });

        date = renderDate(info.getDueDate());
        date.setPrefWidth(MAX_VALUE);
        date.setAlignment(Pos.BOTTOM_RIGHT);

        attach(info.getElements(), this::createElementsGrid);
    }

    @Override
    public void createScene() {
        createResizableScene(0.5, "Alert Viewer");
    }

    @Override
    public void makeGridRows() {
        RowConstraints top = new RowConstraints();
        RowConstraints title = new RowConstraints();
        RowConstraints subtitle = new RowConstraints();
        RowConstraints date = new RowConstraints();
        RowConstraints elements = new RowConstraints();
        RowConstraints bottom = new RowConstraints();
        date.setMinHeight(20);
        bottom.setMinHeight(5);
        grid.getRowConstraints().addAll(top, title, subtitle, date, elements, bottom);
    }

    @Override
    public void destroy() {
        super.destroy();
        elementRenderer.destroy();
    }

    private Label renderDate(Observable<Long> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(formatTime(obs.getData())));
        label.setOnMouseReleased((e) -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2)
                new DateEditor(this, info.getDueDate()).start();
        });
        return label;
    }
}
