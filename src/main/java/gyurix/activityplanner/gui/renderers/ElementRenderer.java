package gyurix.activityplanner.gui.renderers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import static java.lang.Double.MAX_VALUE;

public class ElementRenderer implements ElementVisitor {
    private final GridPane grid;
    private int row;

    public ElementRenderer(GridPane grid) {
        this.grid = grid;
    }

    private Node renderAudio(AudioElement e) {
        return renderLink(e);
    }

    private Node renderLink(LinkElement link) {
        Label label = renderText(link);
        Observable<String> url = link.getUrl();
        Tooltip tooltip = new Tooltip(url.getData());
        url.attach(() -> tooltip.setText(url.getData()));
        label.setTooltip(tooltip);
        label.setOnMouseReleased((e) -> {
            if (e.getButton() != MouseButton.PRIMARY)
                return;
            HostServicesDelegate hostServices = HostServicesFactory.getInstance(ActivityPlannerLauncher.getInstance());
            hostServices.showDocument(url.getData());
        });
        return label;
    }

    private Node renderPicture(PictureElement e) {
        return renderLink(e);
    }

    private Label renderText(TextElement el) {
        Observable<String> obs = el.getText();
        Label label = new Label(obs.getData());
        label.setPrefWidth(MAX_VALUE);
        obs.attach(() -> label.setText(obs.getData()));
        return label;
    }

    private Node renderVideo(VideoElement e) {
        return renderLink(e);
    }

    @Override
    public void visit(TextElement e) {
        grid.add(renderText(e), 0, row++);
    }

    @Override
    public void visit(VideoElement e) {
        grid.add(renderVideo(e), 0, row++);
    }

    @Override
    public void visit(PictureElement e) {
        grid.add(renderPicture(e), 0, row++);
    }

    @Override
    public void visit(LinkElement e) {
        grid.add(renderLink(e), 0, row++);
    }

    @Override
    public void visit(AudioElement e) {
        grid.add(renderAudio(e), 0, row++);
    }
}
