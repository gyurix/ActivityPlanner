package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.renderers.ContentRenderer;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import gyurix.activityplanner.gui.scenes.core.InfoScene;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradient;

@Getter
public class UserScene extends InfoScene<User> {
    private static final Color alertBackground = Color.web("#ff7070");
    private static final Color chatBackground = Color.web("#a0a0ff");
    private static final Color mainBackground = Color.SILVER;
    private static final Color tableBackground = Color.web("#a07000");

    private GridPane alerts = new GridPane();
    private GridPane chat = new GridPane();
    private Button logoutButton = new Button("Logout");
    private ContentRenderer renderer;
    private GridPane tables = new GridPane();
    private Label usernameLabel = new Label();

    public UserScene(User info, Stage stage) {
        super(info, stage);
    }


    @Override
    public void addNodesToGrid() {
        addNodesToMainGrid();
        attach(info.getCreatedContents(), () -> info.visitCreatedContents(renderer = new ContentRenderer(this)));
        addNodesToGridChat();
    }

    @Override
    public void createNodes() {
        Observable<String> un = info.getUsername();
        usernameLabel = new Label();
        logoutButton.setOnAction((e) -> {
            disable();
            new LoginScene(stage).start();
        });
        attach(un, () -> usernameLabel.setText(un.getData()));
    }

    @Override
    public void createScene() {
        createResizableScene(0.8, "User Dashboard");
    }

    @Override
    public void makeGrid() {
        prepareGrid(grid, bgColorGradient(mainBackground), 10);
        prepareGrid(chat, bgColorGradient(chatBackground), 10);
        prepareGrid(tables, bgColorGradient(tableBackground), 10);
        prepareGrid(alerts, bgColorGradient(alertBackground), 10);

        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        makeMainGridColumns();
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(90);
        tables.getColumnConstraints().addAll(side, main, side);
        alerts.getColumnConstraints().addAll(side, main, side);
    }

    @Override
    public void makeGridRows() {
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        row0.setPercentHeight(10);
        row1.setPercentHeight(90);
        grid.getRowConstraints().addAll(row0, row1);
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
    public void destroy() {
        disable();
        SceneUtils.getIoThread().shutdown();
    }

    public void disable() {
        super.destroy();
        renderer.destroy();
    }

    public ColumnConstraints makeAlignedColumn(HPos alignment) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33);
        col.setHalignment(alignment);
        return col;
    }

    public ColumnConstraints makeColumn(double width, HPos alignment) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(width);
        col.setHalignment(alignment);
        return col;
    }

    public void makeMainGridColumns() {
        grid.getColumnConstraints().addAll(makeAlignedColumn(HPos.LEFT), makeAlignedColumn(HPos.CENTER), makeAlignedColumn(HPos.RIGHT));
    }

    public void prepareGrid(GridPane grid, Background bg, double gap) {
        grid.setBackground(bg);
        grid.setHgap(gap);
        grid.setVgap(gap);
    }
}
