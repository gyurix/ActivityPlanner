package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.ObservableList;
import lombok.Getter;


@Getter
public class Lecture extends User {
    private ObservableList<String> assignedStudents = new ObservableList<>();

    public Lecture(String username, String password) {
        super(username, password);
    }

    @Override
    public void accept(UserVisitor visitor) {
        visitor.visit(this);
    }
}
