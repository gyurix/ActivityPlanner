package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.user.Lector;
import gyurix.activityplanner.core.data.user.Student;

/**
 * Visitor for Users
 */
public interface UserVisitor {
    /**
     * Visits the given Student
     *
     * @param s - The visitable Student
     */
    void visit(Student s);

    /**
     * Visits the given Lector
     *
     * @param l - The visitable Lector
     */
    void visit(Lector l);
}
