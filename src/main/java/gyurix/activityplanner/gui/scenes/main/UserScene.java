package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.renderers.ChatRenderer;
import gyurix.activityplanner.gui.renderers.ContentRenderer;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import gyurix.activityplanner.gui.scenes.core.InfoScene;
import gyurix.activityplanner.gui.scenes.viewer.ContentViewer;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;

import static gyurix.activityplanner.gui.scenes.SceneUtils.*;

@Getter
public class UserScene extends InfoScene<User> {
    private static final double ICON_SIZE = 0.025;
    private static final Color alertBackground = Color.web("#ff7070");
    private static final Color chatBackground = Color.web("#a0a0ff");
    private static final Color mainBackground = Color.SILVER;
    private static final Color tableBackground = Color.web("#a07000");

    private GridPane alerts = new GridPane(), alertsWrapper;
    private GridPane chat = new GridPane();
    private Button logoutButton = new Button("Logout");
    private ContentRenderer renderer;
    private ChatRenderer chatRenderer;
    private GridPane tables = new GridPane(), tableWrapper;
    private Label usernameLabel = new Label();

    public UserScene(User info, Stage stage) {
        super(info, stage);
    }


    @Override
    public void addNodesToGrid() {
        chatRenderer.render();
        addNodesToMainGrid();
    }

    @Override
    public void createNodes() {
        chatRenderer = new ChatRenderer(this);
        Observable<String> un = info.getUsername();
        usernameLabel = new Label();
        logoutButton.setOnAction((e) -> {
            disable();
            new LoginScene(stage).start();
        });
        chat.setBackground(bgColorGradient(chatBackground));
        attach(info.getCreatedContents(), () -> info.visitCreatedContents(renderer = new ContentRenderer(this)));
        alertsWrapper = createWrapper(alerts, bgColorGradient(alertBackground),
                createClickableImage(Icons.ADD, ICON_SIZE, () -> {
                    Alert a = new Alert(System.currentTimeMillis(),
                            "Alert Title",
                            "Alert Subtitle",
                            colorToHex(getRandomColor()).substring(1));
                    DataStorage ds = DataStorage.getInstance();
                    getInfo().getCreatedContents().add(ds.addContent(a));
                    new ContentViewer(this, a, new Stage()).start();
                }));
        tableWrapper = createWrapper(tables, bgColorGradient(tableBackground),
                createClickableImage(Icons.ADD, ICON_SIZE, () -> {
                    Table t = new Table("Table Title",
                            "Table Subtitle",
                            colorToHex(getRandomColor()).substring(1));
                    DataStorage ds = DataStorage.getInstance();
                    info.getCreatedContents().add(ds.addContent(t));
                    new ContentViewer(this, t, new Stage()).start();
                }));
        attach(un, () -> usernameLabel.setText(un.getData()));
    }

    @Override
    public void createScene() {
        createResizableScene(0.8, "User Dashboard");
    }

    @Override
    public void makeGrid() {
        prepareGrid(grid, 10);
        prepareGrid(chat, 10);
        prepareGrid(tables, 10);
        prepareGrid(alerts, 10);

        grid.setBackground(bgColorGradient(mainBackground));

        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        makeChatGridColumns();
        makeContentGridColumns();
        makeMainGridColumns();
    }

    @Override
    public void makeGridRows() {
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        row0.setPercentHeight(10);
        row1.setPercentHeight(90);
        grid.getRowConstraints().addAll(row0, row1);
    }

    private void addNodesToMainGrid() {
        grid.add(usernameLabel, 1, 0);
        grid.add(logoutButton, 2, 0);
        grid.add(doubleWrap(tableWrapper), 0, 1);
        grid.add(doubleWrap(alertsWrapper), 1, 1);
        grid.add(doubleWrap(chat), 2, 1);
    }

    private GridPane createWrapper(GridPane pane, Background bg, Pane addButton) {
        GridPane grid = new GridPane();
        ColumnConstraints mainCol = new ColumnConstraints();
        mainCol.setPercentWidth(90);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(10);
        grid.getColumnConstraints().addAll(mainCol, rightCol);

        RowConstraints topRow = new RowConstraints();
        topRow.setPrefHeight(48);
        RowConstraints bottomRow = new RowConstraints();
        bottomRow.setPrefHeight(24);
        RowConstraints mainRow = new RowConstraints();
        mainRow.setPercentHeight(85);

        grid.getRowConstraints().addAll(topRow, mainRow, bottomRow);
        grid.add(addButton, 1, 0);
        grid.add(pane, 0, 1, 2, 1);
        grid.setBackground(bg);
        return grid;
    }

    @Override
    public void destroy() {
        disable();
        SceneUtils.getIoThread().shutdown();
    }

    public void disable() {
        super.destroy();
        DataStorage.getInstance().save(new File("conf.json"));
        renderer.destroy();
    }

    private ScrollPane doubleWrap(GridPane content) {
        GridPane grid = new GridPane();

        RowConstraints fullRow = new RowConstraints();
        fullRow.setPercentHeight(100);
        grid.getRowConstraints().addAll(fullRow);

        ColumnConstraints fullCol = new ColumnConstraints();
        fullCol.setPercentWidth(100);
        grid.getColumnConstraints().add(fullCol);

        grid.add(content, 0, 0);
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        return scroll;
    }

    public ColumnConstraints makeAlignedColumn(HPos alignment) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33);
        col.setHalignment(alignment);
        return col;
    }

    private void makeChatGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(6);
        ColumnConstraints body = new ColumnConstraints();
        body.setPercentWidth(22);
        chat.getColumnConstraints().addAll(side, body, body, body, body, side);
    }

    private void makeContentGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(90);
        tables.getColumnConstraints().addAll(side, main, side);
        alerts.getColumnConstraints().addAll(side, main, side);
    }

    public void makeMainGridColumns() {
        grid.getColumnConstraints().addAll(makeAlignedColumn(HPos.LEFT), makeAlignedColumn(HPos.CENTER), makeAlignedColumn(HPos.RIGHT));
    }

    public void prepareGrid(GridPane grid, double gap) {
        grid.setHgap(gap);
        grid.setVgap(gap);
    }
}
