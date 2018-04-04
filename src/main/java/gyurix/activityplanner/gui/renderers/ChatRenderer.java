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
    private Observable<ObservableList<ChatMessage>> currentChannel = new Observable<>();
    private UserScene parent;
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
        chat.add(doubleWrap(chatMessages), 0, 1, 6, 1);
    }

    private void createBottomRow() {
        sendMessage = new TextField();
        sendMessage.setOnAction((e) -> sendMessage());
        chat.add(sendMessage, 1, 2, 4, 1);
    }

    private MenuItem createInvidualMenuItem(User user) {
        MenuItem menuItem = new MenuItem(user.getUsername().getData());
        menuItem.setOnAction((e) -> showChat(user));
        return menuItem;
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

    private void createNodes() {
        chat.setPrefHeight(Double.MAX_VALUE);
        chat.getRowConstraints().addAll(pctRow(15), pctRow(75), pctRow(10));
        createTopRow();
        createBody();
        createBottomRow();
    }

    private void createTopRow() {
        chat.add(createImageMenu(CHAT_STUDENT, CHAT_ICON_SIZE, makeOneStudentMenu()), 1, 0);
        chat.add(createImageMenu(CHAT_STUDENTS, CHAT_ICON_SIZE, makeStudentsMenu()), 2, 0);
        chat.add(createImageMenu(CHAT_LECTOR, CHAT_ICON_SIZE, makeOneLectorMenu()), 3, 0);
        chat.add(createImageMenu(CHAT_BOTH, CHAT_ICON_SIZE, makeBothMenu()), 4, 0);
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

    @Override
    public Observable<Double> getScreenWidth() {
        return parent.getScreenWidth();
    }

    private Consumer<Consumer<ContextMenu>> makeBothMenu() {
        return (consumer) -> DataStorage.getInstance().getLectures((lectures -> {
            ContextMenu menu = new ContextMenu();
            MenuItem loading = new MenuItem("Loading both menu...");
            menu.getItems().add(loading);
            consumer.accept(menu);
        }));
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

    private Consumer<Consumer<ContextMenu>> makeOneLectorMenu() {
        return (consumer) -> {
            DataStorage ds = DataStorage.getInstance();
            ContextMenu menu = new ContextMenu();
            if (parent.getInfo() instanceof Lecture) {
                ds.getLectures((lectures -> {
                    javafx.collections.ObservableList<MenuItem> items = menu.getItems();
                    lectures.forEach((l) -> items.add(createInvidualMenuItem(l)));
                }));
                consumer.accept(menu);
                return;
            }
            javafx.collections.ObservableList<MenuItem> items = menu.getItems();
            ((Student) parent.getInfo()).getLectures().forEach((name) -> {
                ds.getUser(name, (u) -> items.add(createInvidualMenuItem(u)));
            });
            consumer.accept(menu);
        };
    }

    private Consumer<Consumer<ContextMenu>> makeOneStudentMenu() {
        return (consumer) -> {
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
    }

    private Consumer<Consumer<ContextMenu>> makeStudentsMenu() {
        return (consumer) -> {
            DataStorage.getInstance().getLectures((lectures -> {
                ContextMenu menu = new ContextMenu();
                MenuItem loading = new MenuItem("Loading students menu...");
                menu.getItems().add(loading);
                consumer.accept(menu);
            }));
        };
    }

    public void render() {
        createNodes();
        showChat(parent.getInfo());
    }

    public GridPane makeEmptyLine() {
        GridPane emptyLine = new GridPane();
        emptyLine.setMinHeight(12);
        return emptyLine;
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
        StringBuilder channel = new StringBuilder("uu:");
        switch (u1.compareTo(u2)) {
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
        DataStorage.getInstance().getChatMessages(channel.toString(), (cm) -> currentChannel.setData(cm));
    }

    public void showGroupChat(Lecture l, boolean includeLecture) {
        String lectureName = l.getUsername().getData();
        String channel = (includeLecture ? "l:" : "nl:") + lectureName;
        DataStorage.getInstance().getChatMessages(channel, (cm) -> currentChannel.setData(cm));
    }
}
