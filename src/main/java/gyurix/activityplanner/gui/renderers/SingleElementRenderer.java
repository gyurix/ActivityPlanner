package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class SingleElementRenderer implements ElementVisitor {
    private final Consumer<Region> consumer;
    private final ElementRenderer renderer;

    public SingleElementRenderer(ElementRenderer renderer, Consumer<Region> consumer) {
        this.renderer = renderer;
        this.consumer = consumer;
    }

    @Override
    public void visit(AudioElement e) {
        consumer.accept(renderer.renderAudio(e));
    }

    @Override
    public void visit(TextElement e) {
        consumer.accept(renderer.renderText(e));
    }

    @Override
    public void visit(PictureElement e) {
        consumer.accept(renderer.renderPicture(e));
    }

    @Override
    public void visit(VideoElement e) {
        consumer.accept(renderer.renderVideo(e));
    }

    @Override
    public void visit(LinkElement e) {
        consumer.accept(renderer.renderLink(e));
    }
}
