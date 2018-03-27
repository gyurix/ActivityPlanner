package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScene extends AbstractScene {
    private Background background = SceneUtils.bgColorGradient(Color.web("#b08045"));
    private Button loginButton = new Button("Login");
    private PasswordField passwordField = new PasswordField();
    private Label passwordLabel = new Label("Password");
    private TextField usernameField = new TextField("l1");
    private Label usernameLabel = new Label("Username");

    public LoginScene(Stage stage) {
        super(stage);
    }

    public void addNodesToGrid() {
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
    }

    public void createNodes() {
        loginButton.setOnAction(this::login);
        usernameField.setOnAction(this::login);
        passwordField.setOnAction(this::login);
    }

    @Override
    public void createScene() {
        createResizableScene(0.3, "Login");
    }

    public void makeGrid() {
        grid.setBackground(background);
        grid.setHgap(10);
        grid.setVgap(10);
        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        ColumnConstraints title = new ColumnConstraints();
        title.setPercentWidth(30);
        title.setHalignment(HPos.RIGHT);

        ColumnConstraints editor = new ColumnConstraints();
        editor.setHalignment(HPos.LEFT);

        grid.getColumnConstraints().addAll(title, editor);
    }

    public void makeGridRows() {
        RowConstraints sep = new RowConstraints();
        sep.setPercentHeight(11);

        RowConstraints main = new RowConstraints();

        grid.getRowConstraints().addAll(sep, main, sep, main);
    }

    @Override
    public void destroy() {
        disable();
        SceneUtils.getIoThread().shutdown();
    }

    public void disable() {
        super.destroy();
    }

    private void login(ActionEvent e) {
        DataStorage.getInstance().getUser(usernameField.getText(), (user) -> Platform.runLater(() -> {
            if (user == null) {
                makeAlert("Incorrect Username", "The entered username is incorrect").showAndWait();
                return;
            }
            if (!passwordField.getText().equals(user.getPassword().getData())) {
                makeAlert("Incorrect Password", "The entered password is incorrect").showAndWait();
                return;
            }
            new UserScene(user, stage).start();
        }));
    }

    public Alert makeAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        return a;
    }
}
