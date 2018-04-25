
package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;

/**
 * UrlEditor is an extension of TextEditor used for editing
 * LinkElements and their extensions.
 */
@Getter
public class UrlEditor extends TextEditor {

    /**
     * Browse icon used for opening system file browser
     */
    private final ImageView browseIcon = new ImageView(Icons.BROWSE.getImage());
    /**
     * The url editors grid, containing the url field and the browse icon
     */
    private final GridPane urlEditor = new GridPane();
    /**
     * The url field used for manually editing URL
     */
    private final TextField urlField = new TextField();
    /**
     * The url label
     */
    private final Label urlLabel = new Label("Link");
    /**
     * The editable URL
     */
    private Observable<String> url;
    /**
     * Url edit lock, used for locking url changes, when it's getting edited by this editor
     */
    private boolean urlEditLock;

    /**
     * Constructs a new UrlEditor
     *
     * @param holder - The holder of the editable element
     * @param text   - The editable text
     * @param url    - The editable url
     */
    public UrlEditor(ElementHolderScene<? extends ElementHolder> holder, Observable<String> text, Observable<String> url) {
        super(holder, text);
        this.url = url;
    }

    @Override
    protected void addNodesToGrid() {
        urlEditor.add(urlField, 0, 0);
        urlEditor.add(browseIcon, 1, 0);

        super.addNodesToGrid();
        grid.add(urlEditor, 1, 4);
        grid.add(urlLabel, 1, 3);
    }

    @Override
    protected void createNodes() {
        super.createNodes();
        createUrlLabel();
        createUrlField();
        createUrlIcon();
    }

    @Override
    protected void createScene() {
        createResizableScene(0.3, "Url Editor");
    }

    /**
     * Creates the dynamically changing url editor field
     */
    public void createUrlField() {
        attach(url, () -> {
            if (!urlEditLock)
                urlField.setText(url.getData());
        });
        urlField.setOnKeyTyped((e) -> Platform.runLater(() -> {
            urlEditLock = true;
            url.setData(urlField.getText());
            urlEditLock = false;
        }));
    }

    /**
     * Creates the dynamically resizable browse icon
     */
    public void createUrlIcon() {
        browseIcon.setPreserveRatio(true);
        attach(screenWidth, () -> {
            double width = screenWidth.getData();
            browseIcon.setFitWidth(width * 0.07);
        });
        browseIcon.setOnMouseReleased((e) -> {
            if (e.getButton() != MouseButton.PRIMARY)
                return;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            File f = fileChooser.showOpenDialog(stage);
            if (f != null)
                url.setData("file:///" + f.getAbsoluteFile().toString());
        });
    }

    /**
     * Creates the url label
     */
    public void createUrlLabel() {
        urlLabel.setAlignment(Pos.CENTER);
        urlLabel.setPrefWidth(Double.MAX_VALUE);
    }

    @Override
    protected void makeGridColumns() {
        super.makeGridColumns();
        urlEditor.setHgap(5);
        urlEditor.getColumnConstraints().addAll(pctCol(90), pctCol(10));
    }

    @Override
    protected void makeGridRows() {
        RowConstraints side = pctRow(5);
        grid.getRowConstraints().addAll(side, side, pctRow(60), new RowConstraints(), side, side);
    }
}
