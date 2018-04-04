package gyurix.activityplanner.gui.renderers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import gyurix.activityplanner.core.Callable;
import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.Observer;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import gyurix.activityplanner.gui.scenes.editor.TextEditor;
import gyurix.activityplanner.gui.scenes.editor.UrlEditor;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

import static gyurix.activityplanner.gui.assets.Icons.*;
import static gyurix.activityplanner.gui.scenes.SceneUtils.*;
import static java.lang.Double.MAX_VALUE;

public class ElementRenderer extends DataRenderer implements ElementVisitor {
    private static final double ADD_ICON_SIZE_MULTIPLIER = 0.04;
    private static final double ASPECT_RATIO = 0.75;
    private static final double ICON_SIZE_MULTIPLIER = 0.04;
    private static final double USER_WIDTH_MULTIPLIER = 0.35;
    private static final double VIDEO_BOX_HEIGHT_MULTIPLIER = 0.9;
    private static final double VIDEO_BOX_WIDTH_MULTIPLIER = 0.95;
    private static final double WIDTH_MULTIPLIER = 0.74;
    private final UserScene userScene;
    private GridPane box;
    private ArrayList<WebView> destroyableWebViews = new ArrayList<>();
    private boolean editable;
    private ElementHolder elementHolder;
    private ElementHolderScene<? extends ElementHolder> holder;
    private int row;
    @Getter
    private Observable<Double> screenWidth;

    public ElementRenderer(ElementHolderScene<? extends ElementHolder> holderScene) {
        this.userScene = holderScene.getUserScene();
        screenWidth = holderScene.getScreenWidth();
        this.holder = holderScene;
        this.box = holderScene.getElements();
        this.elementHolder = holderScene.getInfo();
        editable = userScene.getInfo().isContentEditable(this.elementHolder.getId().getData());
    }

    public ElementRenderer(UserScene userScene) {
        this.userScene = userScene;
        screenWidth = new Observable<>();
        attach(userScene.getScreenWidth(), () -> screenWidth.setData(userScene.getScreenWidth().getData() * USER_WIDTH_MULTIPLIER));
    }

    public void addToBox(TextElement e, Region r) {
        Pane edit = createClickableImage(EDIT, ICON_SIZE_MULTIPLIER, () -> openEditor(e));
        Pane remove = createClickableImage(REMOVE, ICON_SIZE_MULTIPLIER, () -> elementHolder.getElements().remove(new Observable<>(e)));
        box.add(makeContentGrid(r, edit, remove), 0, row++);
        box.add(makeSeparatorGrid(), 0, row++);
    }

    public void createAddButtons() {
        if (!editable)
            return;
        GridPane grid = new GridPane();
        RowConstraints seprow = new RowConstraints();
        RowConstraints mainrow = new RowConstraints();
        seprow.setPercentHeight(10);
        mainrow.setPercentHeight(80);
        grid.getRowConstraints().addAll(seprow, mainrow, seprow);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(14);
        col.setHalignment(HPos.CENTER);
        ColumnConstraints sep = new ColumnConstraints();
        sep.setPercentWidth(5);
        makeDynamicBackground(grid, elementHolder.getColor());
        grid.getColumnConstraints().addAll(sep, col, sep, col, sep, col, sep, col, sep, col, sep);
        grid.add(createAddElementButton(ELEMENT_TEXT,
                () -> new TextElement("Simple Text")),
                1, 1);
        grid.add(createAddElementButton(ELEMENT_URL,
                () -> new LinkElement("Click here", "Link to page")),
                3, 1);
        grid.add(createAddElementButton(ELEMENT_AUDIO,
                () -> new AudioElement("Audio", "Link to sound")),
                5, 1);
        grid.add(createAddElementButton(ELEMENT_PICTURE,
                () -> new PictureElement("Picture", "Link to picture")),
                7, 1);
        grid.add(createAddElementButton(ELEMENT_VIDEO,
                () -> new VideoElement("Video", "Link to video")),
                9, 1);
        box.add(grid, 0, row++);
    }

    public Pane createAddElementButton(Icons icon, Callable<TextElement> elementCreator) {
        return createClickableImage(icon, ADD_ICON_SIZE_MULTIPLIER, () -> {
            TextElement el = elementCreator.call();
            elementHolder.getElements().add(new Observable<>(el));
            openEditor(el);
        });
    }

    private WebView createWebView(boolean audio) {
        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);
        int width = (int) (getScreenWidth().getData() * WIDTH_MULTIPLIER);
        int height = audio ? 58 : (int) (width * ASPECT_RATIO);
        webView.setMaxSize(width, height);
        webView.setPrefSize(width, height);
        webView.setOnScroll((ev) -> holder.getElementScroller().handle(ev));
        Observer resize;
        if (audio) {
            resize = () -> {
                Document d = webView.getEngine().getDocument();
                if (d != null) {
                    Element el = d.getElementById("d");
                    if (el != null) {
                        int maxx = (int) (getScreenWidth().getData() * 0.75);
                        webView.setMaxWidth(maxx);
                        webView.setPrefWidth(maxx);
                        el.setAttribute("style", "width: " + String.valueOf(maxx * VIDEO_BOX_WIDTH_MULTIPLIER) + "px");
                    }
                }
            };
        } else {
            resize = () -> {
                Document d = webView.getEngine().getDocument();
                if (d != null) {
                    Element el = d.getElementById("d");
                    if (el != null) {
                        int maxx = (int) (getScreenWidth().getData() * 0.75);
                        int maxy = (int) (maxx * ASPECT_RATIO);
                        webView.setMaxSize(maxx, maxy);
                        webView.setPrefSize(maxx, maxy);
                        el.setAttribute("width", String.valueOf(maxx * VIDEO_BOX_WIDTH_MULTIPLIER));
                        el.setAttribute("height", String.valueOf(maxy * VIDEO_BOX_HEIGHT_MULTIPLIER));
                    }
                }
            };
        }
        attach(getScreenWidth(), resize);
        destroyableWebViews.add(webView);
        return webView;
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyableWebViews.forEach((wv) -> wv.getEngine().load(null));
    }

    public Color getBackgroundColor(int row) {
        boolean brighter = row % 2 == 0;
        Color c = Color.web("#" + elementHolder.getColor().getData());
        return brighter ? avgColor(c, Color.WHITE) : avgColor(c, avgColor(c, Color.WHITE));
    }

    public GridPane makeContentGrid(Region content, Pane edit, Pane remove) {
        GridPane grid = new GridPane();
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(90);
        grid.getColumnConstraints().addAll(side, center, side);

        RowConstraints row = new RowConstraints();
        row.setValignment(VPos.CENTER);
        grid.getRowConstraints().add(row);

        makeDynamicBackground(grid, elementHolder.getColor());
        if (editable) {
            grid.add(edit, 0, 0);
            grid.add(remove, 2, 0);
        }
        grid.add(content, 1, 0);
        return grid;
    }

    public void makeDynamicBackground(Region r, Observable<String> obs) {
        int row = this.row;
        attach(obs, () -> r.setBackground(bgColor(getBackgroundColor(row))));
    }

    public GridPane makeSeparatorGrid() {
        GridPane grid = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        grid.getColumnConstraints().add(main);
        makeDynamicBackground(grid, elementHolder.getColor());
        grid.add(new Label(), 0, 0);
        return grid;
    }

    public void openEditor(gyurix.activityplanner.core.data.element.TextElement e) {
        (e instanceof LinkElement ?
                new UrlEditor(holder, e.getText(), ((LinkElement) e).getUrl())
                : new TextEditor(holder, e.getText())).start();
    }

    public Region renderAudio(AudioElement e) {
        int row = this.row;
        GridPane pane = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        pane.getColumnConstraints().add(main);
        WebView webView = createWebView(true);
        Observer o = () -> {
            int width = (int) (getScreenWidth().getData() * WIDTH_MULTIPLIER);
            webView.getEngine().loadContent(
                    "<body style=\"background-color:" + colorToHex(getBackgroundColor(row)) + "\">" +
                            "<audio id=\"d\" style=\"width:" + width * VIDEO_BOX_WIDTH_MULTIPLIER + "px\"controls>" +
                            "<source src=\"" + e.getUrl().getData() + "\"></audio></body>");
        };
        attach(e.getUrl(), o);
        attach(elementHolder.getColor(), o);
        pane.add(webView, 0, 0);
        pane.add(renderLink(e), 0, 1);
        return pane;
    }

    public Region renderLink(LinkElement link) {
        Label label = renderText(link);
        Observable<String> url = link.getUrl();
        Tooltip tooltip = new Tooltip();
        attach(url, () -> tooltip.setText(url.getData()));
        label.setTooltip(tooltip);
        label.setOnMouseReleased((e) -> {
            if (e.getButton() != MouseButton.PRIMARY)
                return;
            HostServicesDelegate hostServices = HostServicesFactory.getInstance(ActivityPlannerLauncher.getInstance());
            hostServices.showDocument(url.getData());
        });
        return label;
    }

    public Region renderPicture(PictureElement e) {
        GridPane pane = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        pane.getColumnConstraints().add(main);
        ImageView imgView = new ImageView(Icons.LOADING.getImage());
        imgView.setPreserveRatio(true);
        //Image loading is an external I/O operation, so we do it asynchronously
        attach(e.getUrl(), () -> runAsync(() -> {
            Image img = new Image(e.getUrl().getData());
            Platform.runLater(() -> {
                imgView.setImage(img);
                imgView.setFitWidth(getScreenWidth().getData() * WIDTH_MULTIPLIER);
            });
        }));
        attach(getScreenWidth(), () -> imgView.setFitWidth(getScreenWidth().getData() * WIDTH_MULTIPLIER));
        pane.add(renderLink(e), 0, 0);
        pane.add(imgView, 0, 1);
        return pane;
    }

    public Label renderText(TextElement el) {
        Observable<String> obs = el.getText();
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }

    public Region renderVideo(VideoElement e) {
        int row = this.row;
        GridPane pane = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        pane.getColumnConstraints().add(main);
        WebView webView = createWebView(false);
        Observer contentChange = () -> {
            int width = (int) (getScreenWidth().getData() * WIDTH_MULTIPLIER);
            int height = (int) (width * ASPECT_RATIO);
            webView.getEngine().loadContent("<body style=\"background-color:" + colorToHex(getBackgroundColor(row)) + "\">" +
                    "<video id=\"d\" width=\"" + width * VIDEO_BOX_WIDTH_MULTIPLIER + "\" " +
                    "height=\"" + height * VIDEO_BOX_HEIGHT_MULTIPLIER + "\" controls>" +
                    "<source src=\"" + e.getUrl().getData() + "\">" +
                    "</video>" +
                    "</body>");
        };
        attach(e.getUrl(), contentChange);
        attach(elementHolder.getColor(), contentChange);
        pane.add(webView, 0, 1);
        pane.add(renderLink(e), 0, 0);
        return pane;
    }

    @Override
    public void visit(AudioElement e) {
        addToBox(e, renderAudio(e));
    }

    @Override
    public void visit(TextElement e) {
        addToBox(e, renderText(e));
    }

    @Override
    public void visit(PictureElement e) {
        addToBox(e, renderPicture(e));
    }

    @Override
    public void visit(VideoElement e) {
        addToBox(e, renderVideo(e));
    }

    @Override
    public void visit(LinkElement e) {
        addToBox(e, renderLink(e));
    }
}
