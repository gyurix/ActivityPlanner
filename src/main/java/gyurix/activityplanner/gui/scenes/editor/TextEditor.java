package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Text editor used for editing TextElements, and other String properties
 * like titles or subtitles of Contents.
 */
@Getter
public class TextEditor extends Editor {

    /**
     * The text area used for editing the text
     */
    private final TextArea textArea = new TextArea();
    /**
     * The text label
     */
    private final Label textLabel = new Label("Text");
    /**
     * Edit lock, used for locking text changes, when it's getting edited by this editor
     */
    private boolean editLock;
    /**
     * The editable text
     */
    private Observable<String> text;

    /**
     * Constructs a new TextEditor
     *
     * @param holder - The holder of the editable element
     * @param text   - The editable text
     */
    public TextEditor(ElementHolderScene<? extends ElementHolder> holder, Observable<String> text) {
        super(holder, new Stage());
        this.text = text;
    }

    @Override
    protected void addNodesToGrid() {
        grid.add(textLabel, 1, 1);
        grid.add(textArea, 1, 2);
    }

    @Override
    public void createNodes() {
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setPrefWidth(Double.MAX_VALUE);

        //Make text dynamically changeable
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
}
