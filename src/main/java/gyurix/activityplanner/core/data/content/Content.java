package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import lombok.Getter;

@Getter
public abstract class Content extends StorableData implements ElementHolder {
    private Observable<String> color;
    private ObservableList<Observable<Element>> elements = new ObservableList<>();
    private Integer id = 0;

    public Content(String color) {
        this.color = new Observable<>(color);
    }

    public abstract void accept(ContentVisitor visitor);

    @Override
    public void setId(int id) {
        if (this.id != 0 && id != 0)
            throw new RuntimeException("Content " + this + " already have an id (" + this.id + ").");
        this.id = id;
    }
}
