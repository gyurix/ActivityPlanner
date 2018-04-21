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

/**
 * Element renderer used for rendering text, link, audio, video and picture elements.
 */
public class ElementRenderer extends DataRenderer implements ElementVisitor {
    /**
     * Width multiplier of the add new element buttons
     */
    private static final double ADD_ICON_SIZE_MULTIPLIER = 0.04;

    /**
     * Width multiplier of the edit and remove buttons
     */
    private static final double ICON_SIZE_MULTIPLIER = 0.04;

    /**
     * Width multiplier of the UserScene used for the element rendering
     */
    private static final double USER_WIDTH_MULTIPLIER = 0.35;

    /**
     * Prefered aspect ratio of the video elements
     */
    private static final double VIDEO_ASPECT_RATIO = 0.75;

    /**
     * Height multiplier of the video boxes relative to the available space for them
     */
    private static final double VIDEO_BOX_HEIGHT_MULTIPLIER = 0.9;

    /**
     * Width multiplier of the video boxes relative to the available space for them
     */
    private static final double VIDEO_BOX_WIDTH_MULTIPLIER = 0.95;

    /**
     * Width multiplier for audio and video boxes
     */
    private static final double WIDTH_MULTIPLIER = 0.74;

    /**
     * The parent user scene
     */
    private final UserScene userScene;
    /**
     * List of web views used for rendering audio and video elements, which should be
     * destroyed when his ElementRenderer is destroyed
     */
    private ArrayList<WebView> destroyableWebViews = new ArrayList<>();
    /**
     * Should edit and remove buttons be rendered?
     */
    private boolean editable;
    /**
     * The holder Content of the renderable elements
     */
    private ElementHolder elementHolder;
    /**
     * The holder box of the elements, to which the new elements should be added
     */
    private GridPane elementHolderBox;
    /**
     * The holder scene of the renderable elements
     */
    private ElementHolderScene<? extends ElementHolder> holder;

    /**
     * Row of the next renderable element
     */
    private int row;

    /**
     * The screen width used for calculating the renderable elements
     */
    @Getter
    private Observable<Double> screenWidth;

    /**
     * Constructs a new ElementRenderer for rendering the elements of the given ElementHolderScene
     *
     * @param holderScene - The ElementHolderScene, containing the renderable elements
     */
    public ElementRenderer(ElementHolderScene<? extends ElementHolder> holderScene) {
        this.userScene = holderScene.getUserScene();
        screenWidth = holderScene.getScreenWidth();
        this.holder = holderScene;
        this.elementHolderBox = holderScene.getElements();
        this.elementHolder = holderScene.getInfo();
        editable = userScene.getInfo().isContentEditable(this.elementHolder.getId().getData());
    }

    /**
     * Constructs a new ElementRenderer for rendering the elements of the chat
     *
     * @param userScene - The parent UserScene
     */
    public ElementRenderer(UserScene userScene) {
        this.userScene = userScene;
        screenWidth = new Observable<>();
        attach(userScene.getScreenWidth(), () -> screenWidth.setData(userScene.getScreenWidth().getData() * USER_WIDTH_MULTIPLIER));
    }

    /**
     * Adds the given element to the elementHolderBox
     *
     * @param element  - The addable element
     * @param rendered - The addable elements rendered version
     */
    public void addToBox(TextElement element, Region rendered) {
        Pane edit = createClickablePicture(EDIT, ICON_SIZE_MULTIPLIER, () -> openEditor(element));
        Pane remove = createClickablePicture(REMOVE, ICON_SIZE_MULTIPLIER, () -> elementHolder.getElements().remove(new Observable<>(element)));
        elementHolderBox.add(makeContentGrid(rendered, edit, remove), 0, row++);
        elementHolderBox.add(makeSeparatorGrid(), 0, row++);
    }

    /**
     * Creates the add new element buttons
     */
    public void createAddButtons() {
        //If the elements are not editable, then we should not have add element buttons
        if (!editable)
            return;

        //Make the holder grid of the element adding buttons
        GridPane grid = new GridPane();
        grid.getRowConstraints().addAll(pctRow(10), pctRow(80), pctRow(10));
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(14);
        col.setHalignment(HPos.CENTER);
        ColumnConstraints sep = pctCol(5);
        makeDynamicBackground(grid, elementHolder.getColor());
        grid.getColumnConstraints().addAll(sep, col, sep, col, sep, col, sep, col, sep, col, sep);

        //Create the add element buttons and add them to the holder grid
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

        //Add the holding grid to the element holder box
        elementHolderBox.add(grid, 0, row++);
    }

    /**
     * Creates a clickable picture for creating a new element
     *
     * @param icon           - The icon showed as picture
     * @param elementCreator - The creator of the new element
     * @return The clickable picture for creating a new element
     */
    public Pane createAddElementButton(Icons icon, Callable<TextElement> elementCreator) {
        return createClickablePicture(icon, ADD_ICON_SIZE_MULTIPLIER, () -> {
            TextElement el = elementCreator.call();
            elementHolder.getElements().add(new Observable<>(el));
            openEditor(el);
        });
    }

    /**
     * Creates a WebView container for rendering an audio or a video element
     *
     * @param audio - true for audio element, false for video element
     * @return The created WebView container
     */
    private WebView createWebView(boolean audio) {
        //Create the webView with the proper size and disabled context menu
        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);
        int width = (int) (getScreenWidth().getData() * WIDTH_MULTIPLIER);
        int height = audio ? 58 : (int) (width * VIDEO_ASPECT_RATIO);
        webView.setMaxSize(width, height);
        webView.setPrefSize(width, height);

        //Fix web views parent are not scrolled when scrolling above web view bug
        webView.setOnScroll((ev) -> holder.getElementScroller().handle(ev));

        //Auto resize web view and it's contents if you resize the parent window
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
                        int maxy = (int) (maxx * VIDEO_ASPECT_RATIO);
                        webView.setMaxSize(maxx, maxy);
                        webView.setPrefSize(maxx, maxy);
                        el.setAttribute("width", String.valueOf(maxx * VIDEO_BOX_WIDTH_MULTIPLIER));
                        el.setAttribute("height", String.valueOf(maxy * VIDEO_BOX_HEIGHT_MULTIPLIER));
                    }
                }
            };
        }
        attach(getScreenWidth(), resize);

        //Make sure that the created webView will be properly destroyed on destroying this ElementRenderer
        destroyableWebViews.add(webView);
        return webView;
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyableWebViews.forEach((wv) -> wv.getEngine().load(null));
    }

    /**
     * Gets the background color of the given row.
     * Row background colors are calculated based on the contents background color.
     * Even rows are always brighter than odd ones.
     *
     * @param row - The row
     * @return The background color of the given row
     */
    public Color getBackgroundColor(int row) {
        boolean brighter = row % 2 == 0;
        Color c = Color.web("#" + elementHolder.getColor().getData());
        return brighter ? avgColor(c, Color.WHITE) : avgColor(c, avgColor(c, Color.WHITE));
    }

    /**
     * Make the final grid for the given rendered content
     *
     * @param content - The rendered content
     * @param edit    - The edit button
     * @param remove  - The remove button
     * @return The final grid for the given rendered content
     */
    public GridPane makeContentGrid(Region content, Pane edit, Pane remove) {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(pctCol(5), pctCol(90), pctCol(5));

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

    /**
     * Make a solid dynamic background color for the given region
     *
     * @param region - The region
     * @param color  - The dynamic color of the region
     */
    public void makeDynamicBackground(Region region, Observable<String> color) {
        int row = this.row;
        attach(color, () -> region.setBackground(bgColor(getBackgroundColor(row))));
    }

    /**
     * Make a separator grid for separating two elements between each other with a vertical space
     *
     * @return The separator grid
     */
    public GridPane makeSeparatorGrid() {
        GridPane grid = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        grid.getColumnConstraints().add(main);
        makeDynamicBackground(grid, elementHolder.getColor());
        grid.add(new Label(), 0, 0);
        return grid;
    }

    /**
     * Open the editor of the given element
     *
     * @param element - The editable element
     */
    public void openEditor(gyurix.activityplanner.core.data.element.TextElement element) {
        (element instanceof LinkElement ?
                new UrlEditor(holder, element.getText(), ((LinkElement) element).getUrl())
                : new TextEditor(holder, element.getText())).start();
    }

    /**
     * Renders the given audio element
     *
     * @param audioElement - The renderable audio element
     * @return The rendered audio element
     */
    public Region renderAudio(AudioElement audioElement) {
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
                            "<source src=\"" + audioElement.getUrl().getData() + "\"></audio></body>");
        };
        attach(audioElement.getUrl(), o);
        attach(elementHolder.getColor(), o);
        pane.add(webView, 0, 0);
        pane.add(renderLink(audioElement), 0, 1);
        return pane;
    }

    /**
     * Renders the given link element
     *
     * @param linkElement - The renderable link element
     * @return The rendered link element
     */
    public Region renderLink(LinkElement linkElement) {
        Label label = renderText(linkElement);
        Observable<String> url = linkElement.getUrl();
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

    /**
     * Renders the given picture element
     *
     * @param pictureElement - The renderable picture element
     * @return The rendered picture element
     */
    public Region renderPicture(PictureElement pictureElement) {
        GridPane pane = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        pane.getColumnConstraints().add(main);
        ImageView imgView = new ImageView(Icons.LOADING.getImage());
        imgView.setPreserveRatio(true);

        //Image loading is an external I/O operation, so we do it asynchronously
        attach(pictureElement.getUrl(), () -> runAsync(() -> {
            Image img = new Image(pictureElement.getUrl().getData());
            Platform.runLater(() -> {
                imgView.setImage(img);
                imgView.setFitWidth(getScreenWidth().getData() * WIDTH_MULTIPLIER);
            });
        }));
        attach(getScreenWidth(), () -> imgView.setFitWidth(getScreenWidth().getData() * WIDTH_MULTIPLIER));
        pane.add(renderLink(pictureElement), 0, 0);
        pane.add(imgView, 0, 1);
        return pane;
    }

    /**
     * Renders the given text element
     *
     * @param textElement - The renderable text element
     * @return The rendered text element
     */
    public Label renderText(TextElement textElement) {
        Observable<String> obs = textElement.getText();
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }

    /**
     * Renders the given video element
     *
     * @param videoElement - The renderable video element
     * @return The rendered video element
     */
    public Region renderVideo(VideoElement videoElement) {
        int row = this.row;
        GridPane pane = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        pane.getColumnConstraints().add(main);
        WebView webView = createWebView(false);
        Observer contentChange = () -> {
            int width = (int) (getScreenWidth().getData() * WIDTH_MULTIPLIER);
            int height = (int) (width * VIDEO_ASPECT_RATIO);
            webView.getEngine().loadContent("<body style=\"background-color:" + colorToHex(getBackgroundColor(row)) + "\">" +
                    "<video id=\"d\" width=\"" + width * VIDEO_BOX_WIDTH_MULTIPLIER + "\" " +
                    "height=\"" + height * VIDEO_BOX_HEIGHT_MULTIPLIER + "\" controls>" +
                    "<source src=\"" + videoElement.getUrl().getData() + "\">" +
                    "</video>" +
                    "</body>");
        };
        attach(videoElement.getUrl(), contentChange);
        attach(elementHolder.getColor(), contentChange);
        pane.add(webView, 0, 1);
        pane.add(renderLink(videoElement), 0, 0);
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
