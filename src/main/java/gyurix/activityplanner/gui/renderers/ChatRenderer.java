package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.element.ChatMessage;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static gyurix.activityplanner.gui.assets.Icons.*;
import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColor;
import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradient;

public class ChatRenderer extends DataRenderer {
    private static final double CHAT_ICON_SIZE = 0.06;
    private static final Background MAIN_BG = bgColor(Color.web("#aaf"));
    private static final Background OTHERS_BG = bgColorGradient(Color.web("#aaa"), new CornerRadii(5));
    private static final Background OWN_BG = bgColorGradient(Color.web("#ffa"), new CornerRadii(5));
    private final ElementRenderer elementRenderer;
    private GridPane chat;
    private GridPane chatMessages = new GridPane();
    private Label chatTitle = new Label("Chat with yourself");
    private Observable<ObservableList<ChatMessage>> currentChannel = new Observable<>();
    private UserScene parent;
    private Consumer<Consumer<ContextMenu>> groupBoth = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        User user = parent.getInfo();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (user instanceof Student) {
            ((Student) user).getLectures().forEach(
                    (name) -> ds.getUser(name,
                            (l) -> items.add(createGroupMenuItem((Lecture) l, true))));
            consumer.accept(menu);
            return;
        }
        consumer.accept(menu);
    };
    private Consumer<Consumer<ContextMenu>> groupStudents = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        User user = parent.getInfo();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (user instanceof Student) {
            ((Student) user).getLectures().forEach(
                    (name) -> ds.getUser(name,
                            (l) -> items.add(createGroupMenuItem((Lecture) l, false))));
            consumer.accept(menu);
            return;
        }
        consumer.accept(menu);
    };
    private Consumer<Consumer<ContextMenu>> invLector = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (parent.getInfo() instanceof Lecture) {
            ds.getLectures((lectures -> lectures.forEach(
                    (l) -> items.add(createInvidualMenuItem(l)))));
            consumer.accept(menu);
            return;
        }
        ((Student) parent.getInfo()).getLectures().forEach((name) -> ds.getUser(name, (u) -> items.add(createInvidualMenuItem(u))));
        consumer.accept(menu);
    };
    private Consumer<Consumer<ContextMenu>> invStudent = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (parent.getInfo() instanceof Lecture) {
            ((Lecture) parent.getInfo()).getAssignedStudents().forEach(
                    (name) -> ds.getUser(name,
                            (u) -> items.add(createInvidualMenuItem(u))));
            consumer.accept(menu);
            return;
        }
        ((Student) parent.getInfo()).getLectures().forEach(
                (name) -> ds.getUser(name,
                        (u) -> items.add(createStudentListMenu((Lecture) u))));
        consumer.accept(menu);
    };
    private int row = 0;
    private TextField sendMessage;

    public ChatRenderer(UserScene parent) {
        this.parent = parent;
        elementRenderer = new ElementRenderer(parent);
        showChat(parent.getInfo());
        attach(currentChannel, this::renderCurrentChannel);
        chat = parent.getChat();
    }

    private void createBody() {
        chatMessages.setBackground(MAIN_BG);
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(3);
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(94);
        chatMessages.getColumnConstraints().addAll(side, main, side);
        chat.add(doubleWrap(chatMessages), 0, 2, 6, 1);
    }

    private void createBottomRow() {
        sendMessage = new TextField();
        sendMessage.setOnAction((e) -> sendMessage());
        chat.add(sendMessage, 1, 3, 4, 1);
    }

    private MenuItem createGroupMenuItem(Lecture l, boolean includeLector) {
        MenuItem menuItem = new MenuItem(l.getUsername().getData());
        menuItem.setOnAction((e) -> showGroupChat(l, includeLector));
        return menuItem;
    }

    private MenuItem createInvidualMenuItem(User user) {
        MenuItem menuItem = new MenuItem(user.getUsername().getData());
        menuItem.setOnAction((e) -> showChat(user));
        return menuItem;
    }

    private void createNodes() {
        chat.setPrefHeight(Double.MAX_VALUE);
        chat.getRowConstraints().addAll(pctRow(15), pctRow(5), pctRow(70), pctRow(10));
        createTopRow();
        createBody();
        createBottomRow();
    }

    private Menu createStudentListMenu(Lecture l) {
        Menu menu = new Menu(l.getUsername().getData());
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        DataStorage ds = DataStorage.getInstance();
        l.getAssignedStudents().forEach(
                (name) -> ds.getUser(name,
                        (s) -> items.add(createInvidualMenuItem(s))));

        return menu;
    }

    private void createTopRow() {
        chat.add(createImageMenu(CHAT_STUDENT, CHAT_ICON_SIZE, invStudent), 1, 0);
        chat.add(createImageMenu(CHAT_STUDENTS, CHAT_ICON_SIZE, groupStudents), 2, 0);
        chat.add(createImageMenu(CHAT_LECTOR, CHAT_ICON_SIZE, invLector), 3, 0);
        chat.add(createImageMenu(CHAT_BOTH, CHAT_ICON_SIZE, groupBoth), 4, 0);
        chatTitle.setAlignment(Pos.CENTER);
        chatTitle.setPrefWidth(Double.MAX_VALUE);
        chat.add(chatTitle, 0, 1, 6, 1);
    }

    private ScrollPane doubleWrap(GridPane pane) {
        GridPane wrap = new GridPane();
        wrap.getColumnConstraints().add(pctCol(100));
        wrap.getRowConstraints().add(pctRow(100));
        wrap.add(pane, 0, 0);
        ScrollPane scroll = new ScrollPane(wrap);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        return scroll;
    }

    public int fixCompare(int result) {
        return result == 0 ? 0 : result / Math.abs(result);
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return parent.getScreenWidth();
    }

    public GridPane makeChatLine(ChatMessage chatMessage) {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(pctCol(2), pctCol(3), pctCol(45), pctCol(45), pctCol(3), pctCol(2));
        grid.add(renderDate(chatMessage.getDate()), 3, 0, 2, 1);
        grid.add(renderText(12, chatMessage.getSender()), 1, 0, 2, 1);
        attach(chatMessage.getSender(), () ->
                grid.setBackground(chatMessage.getSender().equals(parent.getInfo().getUsername()) ? OWN_BG : OTHERS_BG));
        AtomicReference<Region> old = new AtomicReference<>();
        SingleElementRenderer renderer = new SingleElementRenderer(elementRenderer, (newEl) -> {
            Region oldEl = old.getAndSet(newEl);
            if (oldEl != null)
                grid.getChildren().remove(oldEl);
            grid.add(newEl, 2, 1, 2, 1);
        });
        chatMessage.getMessage().getData().accept(renderer);
        Observable<Element> message = chatMessage.getMessage();
        attach(message, () -> message.getData().accept(renderer));
        return grid;
    }

    public GridPane makeEmptyLine() {
        GridPane emptyLine = new GridPane();
        emptyLine.setMinHeight(12);
        return emptyLine;
    }

    private Consumer<Consumer<ContextMenu>> makeStudentsMenu() {
        return (consumer) -> DataStorage.getInstance().getLectures((lectures -> {
            ContextMenu menu = new ContextMenu();
            MenuItem loading = new MenuItem("Loading students menu...");
            menu.getItems().add(loading);
            consumer.accept(menu);
        }));
    }

    public void render() {
        createNodes();
        showChat(parent.getInfo());
    }

    public void renderCurrentChannel() {
        row = 0;
        chatMessages.getChildren().clear();
        chatMessages.add(makeEmptyLine(), 1, row++);
        currentChannel.getData().forEach((cm) -> {
            chatMessages.add(makeChatLine(cm), 1, row++);
            chatMessages.add(makeEmptyLine(), 1, row++);
        });
    }

    public void sendMessage() {
        currentChannel.getData().add(new ChatMessage(System.currentTimeMillis(),
                Element.of(sendMessage.getText()), parent.getInfo().getUsername().getData()));
        currentChannel.setData(currentChannel.getData());
    }

    public void showChat(User user) {
        String u1 = parent.getInfo().getUsername().getData();
        String u2 = user.getUsername().getData();
        chatTitle.setText(u1.equals(u2) ? "Chat with yourself" : "Chat with " + u2);
        StringBuilder channel = new StringBuilder("uu:");
        switch (fixCompare(u1.compareTo(u2))) {
            case -1:
                channel.append(u1).append(':').append(u2);
                break;
            case 0:
                channel.append(u1);
                break;
            case 1:
                channel.append(u2).append(':').append(u1);
                break;

        }
        System.out.println("Set channel to " + channel.toString());
        DataStorage.getInstance().getChatMessages(channel.toString(), (cm) -> currentChannel.setData(cm));
    }

    public void showGroupChat(Lecture l, boolean includeLecture) {
        String lectureName = l.getUsername().getData();
        chatTitle.setText("Chat with " + lectureName + "'s group " +
                (includeLecture ? "with" : "without") + " lecture");
        String channel = (includeLecture ? "l:" : "nl:") + lectureName;
        System.out.println("Set channel to " + channel);
        DataStorage.getInstance().getChatMessages(channel, (cm) -> currentChannel.setData(cm));
    }
}
