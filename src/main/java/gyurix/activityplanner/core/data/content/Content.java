package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import lombok.Getter;

@Getter
public abstract class Content extends StorableData implements ElementHolder {
    private Observable<String> color;
    private ObservableList<Observable<Element>> elements = new ObservableList<>();
    private Observable<Integer> id = new Observable<Integer>(0) {
        @Override
        public void setData(Integer data) {
            if (getData() != 0)
                throw new RuntimeException("Content " + Content.this + " already have an id.");
            super.setData(data);
        }
    };

    public Content(String color) {
        this.color = new Observable<>(color);
    }

    public abstract void accept(ContentVisitor visitor);
}
