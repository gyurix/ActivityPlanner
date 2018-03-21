package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.element.TextElement;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.AbstractScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class TextEditor extends AbstractScreen {
    private Observable<String> text;
    private TextArea textArea = new TextArea();
    private Label textLabel = new Label("Text");

    public TextEditor(TextElement el) {
        super(new Stage());
        text = el.getText();
    }

    public void addNodesToGrid() {
        grid.add(textLabel, 1, 1);
        grid.add(textArea, 1, 2);
    }

    public void createNodes() {
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setPrefWidth(Double.MAX_VALUE);
        createResizableScene(0.3, "Text Editor");
        attach(text, () -> textArea.setText(text.getData()));
        textArea.setOnKeyTyped((e) -> {
            text.setData(textArea.getText());
            System.out.println("Set text to " + text.getData());
        });
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
        RowConstraints main = new RowConstraints();
        grid.getRowConstraints().addAll(side, main, main, side);
    }
}
