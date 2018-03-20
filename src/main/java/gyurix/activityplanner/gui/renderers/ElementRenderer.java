package gyurix.activityplanner.gui.renderers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObserverContainer;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import static gyurix.activityplanner.gui.scenes.SceneUtils.avgColor;
import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColor;
import static java.lang.Double.MAX_VALUE;

public class ElementRenderer extends ObserverContainer implements ElementVisitor {
    private final Alert alert;
    private final GridPane box;
    private int row;

    public ElementRenderer(Alert alert, GridPane box) {
        this.alert = alert;
        this.box = box;
    }

    public void addToBox(Region r) {
        box.add(makeContentGrid(r), 0, row++);
        box.add(makeSeparatorGrid(r), 0, row++);
    }

    public GridPane makeContentGrid(Region r) {
        GridPane grid = new GridPane();
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(90);
        grid.getColumnConstraints().addAll(side, center, side);
        makeDynamicBackground(grid, alert.getColor());
        grid.add(r, 1, 0);
        return grid;
    }

    public void makeDynamicBackground(Region r, Observable<String> obs) {
        boolean brighter = row % 2 == 0;
        attach(obs, () -> {
            Color c = Color.web("#" + obs.getData());
            if (brighter)
                c = avgColor(c, Color.WHITE);
            else
                c = avgColor(c, avgColor(c, Color.WHITE));
            r.setBackground(bgColor(c));
        });
    }

    public GridPane makeSeparatorGrid(Region r) {
        GridPane grid = new GridPane();
        ColumnConstraints main = new ColumnConstraints();
        main.setPercentWidth(100);
        grid.getColumnConstraints().add(main);
        makeDynamicBackground(grid, alert.getColor());
        grid.add(new Label(""), 0, 0);
        return grid;
    }

    private Region renderAudio(AudioElement e) {
        return renderLink(e);
    }

    private Region renderLink(LinkElement link) {
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

    private Region renderPicture(PictureElement e) {
        return renderLink(e);
    }

    private Label renderText(TextElement el) {
        Observable<String> obs = el.getText();
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }

    private Region renderVideo(VideoElement e) {
        return renderLink(e);
    }

    @Override
    public void visit(AudioElement e) {
        addToBox(renderAudio(e));
    }

    @Override
    public void visit(TextElement e) {
        addToBox(renderText(e));
    }

    @Override
    public void visit(PictureElement e) {
        addToBox(renderPicture(e));
    }

    @Override
    public void visit(VideoElement e) {
        addToBox(renderVideo(e));
    }

    @Override
    public void visit(LinkElement e) {
        addToBox(renderLink(e));
    }
}
