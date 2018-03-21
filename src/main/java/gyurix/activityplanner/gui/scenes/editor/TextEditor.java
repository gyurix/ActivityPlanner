package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class TextEditor extends AbstractScene {
    private Observable<String> text;
    private TextArea textArea = new TextArea();
    private Label textLabel = new Label("Text");
    private boolean editLock;

    public TextEditor(Observable<String> text) {
        super(new Stage());
        this.text = text;
    }

    public void addNodesToGrid() {
        grid.add(textLabel, 1, 1);
        grid.add(textArea, 1, 2);
    }

    public void createNodes() {
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setPrefWidth(Double.MAX_VALUE);
        attach(text, () -> {
            if (!editLock) textArea.setText(text.getData());
        });
        textArea.setOnKeyTyped((e) -> Platform.runLater(() -> {
            editLock = true;
            text.setData(textArea.getText());
            editLock = false;
        }));
    }

    @Override
    public void createScene() {
        createResizableScene(0.3, "Text Editor");
    }

    public void makeGrid() {
        grid.setVgap(5);
        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(90);
        grid.getColumnConstraints().addAll(side, center, side);
    }

    public void makeGridRows() {
        RowConstraints side = new RowConstraints();
        side.setPercentHeight(5);
        RowConstraints label = new RowConstraints();
        label.setPercentHeight(5);
        RowConstraints editor = new RowConstraints();
        editor.setPercentHeight(85);
        grid.getRowConstraints().addAll(side, label, editor, side);
    }
}
