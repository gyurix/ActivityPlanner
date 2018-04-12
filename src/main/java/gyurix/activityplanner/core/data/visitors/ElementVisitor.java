package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.element.*;

/**
 * Visitor for Elements
 */
public interface ElementVisitor {
    /**
     * Visits the given AudioElement
     *
     * @param e - The visitable AudioElement
     */
    void visit(AudioElement e);

    /**
     * Visits the given TextElement
     *
     * @param e - The visitable TextElement
     */
    void visit(TextElement e);

    /**
     * Visits the given PictureElement
     *
     * @param e - The visitable PictureElement
     */
    void visit(PictureElement e);

    /**
     * Visits the given VideoElement
     *
     * @param e - The visitable VideoElement
     */
    void visit(VideoElement e);

    /**
     * Visits the given LinkElement
     *
     * @param e - The visitable LinkElement
     */
    void visit(LinkElement e);
}
