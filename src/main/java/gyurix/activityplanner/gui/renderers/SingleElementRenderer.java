package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

/**
 * Single element renderer is used for rendering just a single element without
 * adding any kind of extra wrapping to it (like edit / remove buttons)
 * by making the given consumer accept the rendering result of the ElementRenderer
 */
public class SingleElementRenderer implements ElementVisitor {
    /**
     * The consumer, which consumes the rendering result
     */
    private final Consumer<Region> consumer;
    /**
     * The element renderer used for rendering
     */
    private final ElementRenderer renderer;

    /**
     * Constructs a new SingleElementRenderer
     *
     * @param renderer - The renderer used for rendering
     * @param consumer - The consumer, which consumes the rendering result
     */
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
