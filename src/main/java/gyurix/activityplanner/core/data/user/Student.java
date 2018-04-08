package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;


@Getter
public class Student extends User {
    private ObservableList<String> lectures = new ObservableList<>();

    public Student(String username, String password) {
        super(username, password);
    }

    @Override
    public void accept(UserVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void visitCreatedContents(ContentVisitor visitor) {
        DataStorage ds = DataStorage.getInstance();
        HashSet<Integer> visited = new HashSet<>();
        lectures.forEach((s) -> ds.getUser(s, (u) -> u.visitCreatedContents(visitor, visited)));
    }
}
