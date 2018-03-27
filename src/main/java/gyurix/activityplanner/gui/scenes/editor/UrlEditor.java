
package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.assets.Icons;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;

@Getter
public class UrlEditor extends TextEditor {
    private ImageView browseIcon = new ImageView(Icons.BROWSE.getImage());
    private Observable<String> url;
    private boolean urlEditLock;
    private GridPane urlEditor = new GridPane();
    private TextField urlField = new TextField();
    private Label urlLabel = new Label("Link");

    public UrlEditor(Observable<String> text, Observable<String> url) {
        super(text);
        this.url = url;
    }


    public void addNodesToGrid() {
        urlEditor.add(urlField, 0, 0);
        urlEditor.add(browseIcon, 1, 0);

        super.addNodesToGrid();
        grid.add(urlEditor, 1, 4);
        grid.add(urlLabel, 1, 3);
    }

    public void createNodes() {
        super.createNodes();
        createUrlLabel();
        createUrlField();
        createUrlIcon();
    }

    @Override
    public void createScene() {
        createResizableScene(0.3, "Url Editor");
    }

    @Override
    public void makeGridColumns() {
        super.makeGridColumns();

        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(90);
        ColumnConstraints icon = new ColumnConstraints();
        icon.setPercentWidth(10);

        urlEditor.setHgap(5);
        urlEditor.getColumnConstraints().addAll(main, icon);
    }

    public void makeGridRows() {
        RowConstraints side = new RowConstraints();
        side.setPercentHeight(5);
        RowConstraints free = new RowConstraints();
        RowConstraints editor = new RowConstraints();
        editor.setPercentHeight(60);
        grid.getRowConstraints().addAll(side, side, editor, free, side, side);
    }

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

    public void createUrlLabel() {
        urlLabel.setAlignment(Pos.CENTER);
        urlLabel.setPrefWidth(Double.MAX_VALUE);
    }
}
