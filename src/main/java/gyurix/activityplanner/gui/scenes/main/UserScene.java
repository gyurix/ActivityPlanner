package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.gui.renderers.ContentRenderer;
import gyurix.activityplanner.gui.scenes.InfoScreen;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradient;
import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientTop;

@Getter
public class UserScene extends InfoScreen<User> {
    private static final Color alertBackground = Color.web("#ff7070");
    private static final Color chatBackground = Color.web("#a0a0ff");
    private static final Color mainBackground = Color.SILVER;
    private static final Color tableBackground = Color.web("#a07000");
    private GridPane alerts = new GridPane();
    private GridPane chat = new GridPane();
    private GridPane grid = new GridPane();
    private Button logoutButton = new Button("Logout");
    private GridPane tables = new GridPane();
    private Label usernameLabel = new Label();

    public UserScene(User info, Stage stage) {
        super(info, stage);
    }


    @Override
    public void addNodesToGrid() {
        addNodesToMainGrid();
        info.visitCreatedContents(new ContentRenderer(this));
        addNodesToGridChat();
    }

    private void addNodesToGridChat() {
    }


    private void addNodesToMainGrid() {
        grid.add(usernameLabel, 1, 0);
        grid.add(logoutButton, 2, 0);
        grid.add(tables, 0, 1);
        grid.add(alerts, 1, 1);
        grid.add(chat, 2, 1);
    }


    @Override
    public void createNodes() {
        info.visitCreatedContents(new ContentRenderer(this));
        Observable<String> un = info.getUsername();
        usernameLabel = new Label();
        logoutButton.setOnAction((e) -> new LoginScene(stage).start());
        attach(un, () -> usernameLabel.setText(un.getData()));
    }

    public ColumnConstraints makeColumn(double width, HPos alignment) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(width);
        col.setHalignment(alignment);
        return col;
    }

    @Override
    public void makeGrid() {
        grid.setBackground(bgColorGradient(mainBackground));
        grid.setHgap(10);
        grid.setVgap(10);

        chat.setBackground(bgColorGradientTop(chatBackground));
        tables.setBackground(bgColorGradientTop(tableBackground));
        alerts.setBackground(bgColorGradientTop(alertBackground));
        alerts.setHgap(5);
        alerts.setVgap(5);

        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        makeMainGridColumns();
        ColumnConstraints col1 = makeColumn(10, HPos.LEFT);
        ColumnConstraints col2 = makeColumn(80, HPos.CENTER);
        ColumnConstraints col3 = makeColumn(10, HPos.RIGHT);
        tables.getColumnConstraints().addAll(col1, col2, col3);
        alerts.getColumnConstraints().addAll(col1, col2, col3);
    }

    @Override
    public void makeGridRows() {
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        row0.setPercentHeight(10);
        row1.setPercentHeight(90);
        grid.getRowConstraints().addAll(row0, row1);
    }

    public void makeMainGridColumns() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(33);
        column1.setHalignment(HPos.LEFT);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(33);
        column2.setHalignment(HPos.CENTER);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(33);
        column3.setHalignment(HPos.RIGHT);
        grid.getColumnConstraints().addAll(column1, column2, column3);
    }

    @Override
    public void prepareScene() {
        Scene scene = new Scene(grid, 800, 520);
        stage.setScene(scene);
    }
}
