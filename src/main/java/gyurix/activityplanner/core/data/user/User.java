package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;

/**
 * The base User class having username, password and createdContents properties
 */
@Getter
public abstract class User extends StorableData {
    /**
     * Own created contents of the User
     */
    private ObservableList<Integer> createdContents = new ObservableList<>();
    /**
     * Password of the User
     */
    private Observable<String> password;
    /**
     * Username of the User
     */
    private Observable<String> username;

    /**
     * Constructs a new User from it's username and password parameters
     *
     * @param username - The username of the constructable User
     * @param password - The password of the constructable User
     */
    public User(String username, String password) {
        this.username = new Observable<>(username);
        this.password = new Observable<>(password);
    }

    /**
     * Users can be visited by UserVisitors
     *
     * @param visitor - The UserVisitor, which wants to visit this User
     */
    public abstract void accept(UserVisitor visitor);

    /**
     * Checks if the User have permission for editing the given Content
     *
     * @param contentId - Id of the checkable Content
     * @return True if the User is allowed to edit the content, false otherwise
     */
    public boolean isContentEditable(int contentId) {
        return createdContents.getWrappedData().contains(contentId);
    }

    /**
     * Removes the given content of the User
     *
     * @param contentId - Id of the removable Content
     * @return True if the removing process was successful, false otherwise
     */
    public boolean removeContent(int contentId) {
        return createdContents.remove(contentId);
    }

    /**
     * Visits every created Content associated to this User
     *
     * @param visitor - The ContentVisitor, which wants to visit the Contents
     */
    public void visitCreatedContents(ContentVisitor visitor) {
        visitCreatedContents(visitor, new HashSet<>());
    }

    /**
     * Visits every created Content associated to this User except the already visited ones
     *
     * @param visitor - The ContentVisitor, which wants to visit the Contents
     * @param visited - Already visited contents, which should not be visited again
     */
    protected void visitCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        visitOwnCreatedContents(visitor, visited);
    }

    /**
     * Visits only the Contents created by this User. Skips the already visited ones
     *
     * @param visitor - The ContentVisitor, which wants to visit the Contents
     * @param visited - Already visited contents, which should not be visited again
     */
    protected void visitOwnCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        DataStorage ds = DataStorage.getInstance();
        createdContents.forEach((id) -> {
            if (visited.add(id))
                ds.getContent(id, (c) -> c.accept(visitor));
        });
    }
}
