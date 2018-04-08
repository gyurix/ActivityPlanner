package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import lombok.Getter;

import java.util.HashSet;

@Getter
public abstract class User extends StorableData {
    private ObservableList<Integer> createdContents = new ObservableList<>();
    private Observable<String> password;
    private Observable<String> username;

    public User(String username, String password) {
        this.username = new Observable<>(username);
        this.password = new Observable<>(password);
    }

    public abstract void accept(UserVisitor visitor);

    public boolean isContentEditable(int contentId) {
        return createdContents.getWrappedData().contains(contentId);
    }

    public boolean removeContent(int contentId) {
        return createdContents.remove(contentId);
    }

    public void visitCreatedContents(ContentVisitor visitor) {
        visitCreatedContents(visitor, new HashSet<>());
    }

    protected void visitCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        visitOwnCreatedContents(visitor, visited);
    }

    protected void visitOwnCreatedContents(ContentVisitor visitor, HashSet<Integer> visited) {
        DataStorage ds = DataStorage.getInstance();
        createdContents.forEach((id) -> {
            if (visited.add(id))
                ds.getContent(id, (c) -> c.accept(visitor));
        });
    }
}
