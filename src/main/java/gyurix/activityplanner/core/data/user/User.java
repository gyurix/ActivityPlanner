package gyurix.activityplanner.core.data.user;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.data.visitors.UserVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
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

    public void visitCreatedContents(ContentVisitor visitor) {
        getCreatedContents().forEach((cid) -> DataStorage.getInstance().getContent(cid).accept(visitor));
    }
}
