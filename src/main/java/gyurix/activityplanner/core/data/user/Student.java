package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;


/**
 * Students extends the base User class with a lecture list parameter
 */
@Getter
public class Student extends User {
    /**
     * The lectures of this Student
     */
    private ObservableList<String> lectures = new ObservableList<>();

    /**
     * Constructs a new Student from username and password parameters
     *
     * @param username - The username of the constructable Student
     * @param password - The password of the constructable Student
     */
    public Student(String username, String password) {
        super(username, password);
    }

    @Override
    public void accept(UserVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Visiting contents associated to Students means visiting contents created by their lectures and by the lectures students
     *
     * @param visitor - The ContentVisitor, which wants to visit the Contents
     */
    @Override
    public void visitCreatedContents(ContentVisitor visitor) {
        DataStorage ds = DataStorage.getInstance();
        HashSet<Integer> visited = new HashSet<>();
        lectures.forEach((s) -> ds.getUser(s, (u) -> u.visitCreatedContents(visitor, visited)));
    }
}
