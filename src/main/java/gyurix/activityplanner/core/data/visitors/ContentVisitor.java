package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;

/**
 * Visitor for Contents
 */
public interface ContentVisitor {
    /**
     * Visits the given Alert
     *
     * @param a - The visitable Alert
     */
    void visit(Alert a);

    /**
     * Visits the given Table
     *
     * @param t - The visitable Table
     */
    void visit(Table t);
}
