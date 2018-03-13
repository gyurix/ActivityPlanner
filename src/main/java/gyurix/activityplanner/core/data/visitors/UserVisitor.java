package gyurix.activityplanner.core.data.visitors;

import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;

public interface UserVisitor {
    void visit(Student s);

    void visit(Lecture l);
}
