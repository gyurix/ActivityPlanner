package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;


/**
 * Lecture extends the base User by adding a list of assigned students to it
 */
@Getter
public class Lecture extends User {
    /**
     * List of students assigned to this Lecture
     */
    private ObservableList<String> assignedStudents = new ObservableList<>();

    /**
     * Constructs a new Lecture from it's username and password parameters
     *
     * @param username - The username of the constructable Lecture
     * @param password - The password of the constructable Lecture
     */
    public Lecture(String username, String password) {
        super(username, password);
    }

    @Override
    public void accept(UserVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Lectures can edit every content
     *
     * @param contentId - Id of the checkable Content
     * @return Always True
     */
    @Override
    public boolean isContentEditable(int contentId) {
        return true;
    }

    /**
     * Removes the content from the Lectures own contents or
     * from the Lectures assigned students contents
     *
     * @param contentId - Id of the removable Content
     * @return True if the removing process was successful, false otherwise
     */
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

    /**
     * Visits the lectures own created contents first, then their assigned students contents
     *
     * @param visitor - The ContentVisitor, which wants to visit the Contents
     * @param visited - Already visited contents, which should not be visited again
     */
    @Override
    protected void visitCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        DataStorage ds = DataStorage.getInstance();
        visitOwnCreatedContents(visitor, visited);
        assignedStudents.forEach((s) -> ds.getUser(s, (u) -> u.visitCreatedContents(visitor, visited)));
    }
}
