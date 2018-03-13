package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.DataStorage;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import gyurix.activityplanner.gui.scenes.AbstractScreen;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScene extends AbstractScreen {
    private Background background = SceneUtils.bgColorGradient(Color.web("#b08045"));
    private GridPane grid = new GridPane();
    private Button loginButton = new Button("Login");
    private Label loginResult = new Label("");
    private PasswordField passwordField = new PasswordField();
    private Label passwordLabel = new Label("Password");
    private TextField usernameField = new TextField("l1");
    private Label usernameLabel = new Label("Username");

    public LoginScene(Stage stage) {
        super(stage);
    }

    public void addNodesToGrid() {
        grid.add(loginResult, 0, 0, 3, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
    }

    public void createNodes() {
        loginResult.setTextFill(Color.RED);
        loginResult.setAlignment(Pos.CENTER);
        loginResult.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(this::login);
        usernameField.setOnAction(this::login);
        passwordField.setOnAction(this::login);
    }

    private void login(ActionEvent e) {
        User user = DataStorage.getUser(usernameField.getText());
        if (user == null) {
            loginResult.setText("Incorrect username");
            return;
        }
        if (!passwordField.getText().equals(user.getPassword().getData())) {
            loginResult.setText("Incorrect password");
            return;
        }
        new UserScene(user, stage).start();
    }

    public void makeGrid() {
        grid = new GridPane();
        grid.setBackground(background);
        grid.setHgap(10);
        grid.setVgap(10);
        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        column1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints();
        //column2.setPercentWidth(70);
        column2.setHalignment(HPos.LEFT);
        grid.getColumnConstraints().addAll(column1, column2);
    }

    public void makeGridRows() {
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row0.setPercentHeight(11);
        row3.setPercentHeight(11);
        grid.getRowConstraints().addAll(row0, row1, row2, row3);
    }

    public void prepareScene() {
        Scene scene = new Scene(grid, 320, 240);
        stage.setResizable(false);
        stage.getIcons().add(new Image(ActivityPlannerLauncher.class.getResourceAsStream("/icons/icon.png")));
        stage.setTitle("• ActivityPlanner - Login •");
        stage.setScene(scene);
        stage.show();
    }
}
