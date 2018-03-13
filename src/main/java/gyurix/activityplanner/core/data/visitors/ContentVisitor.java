package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;

public interface ContentVisitor {
    void visit(Alert a);

    void visit(Table t);
}
