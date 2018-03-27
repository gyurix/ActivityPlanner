package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import lombok.Getter;

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

    public abstract void visitCreatedContents(ContentVisitor visitor);
}
