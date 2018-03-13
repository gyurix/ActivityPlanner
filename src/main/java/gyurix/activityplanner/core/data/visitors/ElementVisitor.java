package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.element.*;

public interface ElementVisitor {
    void visit(AudioElement e);

    void visit(TextElement e);

    void visit(PictureElement e);

    void visit(VideoElement e);

    void visit(LinkElement e);
}
