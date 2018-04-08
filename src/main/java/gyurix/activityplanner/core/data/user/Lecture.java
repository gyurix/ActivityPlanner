package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;


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

    @Override
    public boolean isContentEditable(int contentId) {
        return true;
    }

    @Override
    public boolean removeContent(int contentId) {
        if (super.removeContent(contentId))
            return true;
        DataStorage ds = DataStorage.getInstance();
        assignedStudents.forEach((s) -> ds.getUser(s, (u) -> {
            u.removeContent(contentId);
        }));
        return true;
    }

    @Override
    protected void visitCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        DataStorage ds = DataStorage.getInstance();
        visitOwnCreatedContents(visitor, visited);
        assignedStudents.forEach((s) -> ds.getUser(s, (u) -> u.visitCreatedContents(visitor, visited)));
    }
}
