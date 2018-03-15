package gyurix.activityplanner.gui.renderers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObserverContainer;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import static java.lang.Double.MAX_VALUE;

public class ElementRenderer extends ObserverContainer implements ElementVisitor {
    private final VBox box;

    public ElementRenderer(VBox box) {
        this.box = box;
    }

    private Node renderAudio(AudioElement e) {
        return renderLink(e);
    }

    private Node renderLink(LinkElement link) {
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

    private Node renderPicture(PictureElement e) {
        return renderLink(e);
    }

    private Label renderText(TextElement el) {
        Observable<String> obs = el.getText();
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }

    private Node renderVideo(VideoElement e) {
        return renderLink(e);
    }

    @Override
    public void visit(AudioElement e) {
        box.getChildren().add(renderAudio(e));
    }

    @Override
    public void visit(TextElement e) {
        box.getChildren().add(renderText(e));
    }

    @Override
    public void visit(PictureElement e) {
        box.getChildren().add(renderPicture(e));
    }

    @Override
    public void visit(VideoElement e) {
        box.getChildren().add(renderVideo(e));
    }

    @Override
    public void visit(LinkElement e) {
        box.getChildren().add(renderLink(e));
    }
}
