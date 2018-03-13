package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.core.data.observation.ObservableList;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import lombok.Getter;

@Getter
public abstract class Content extends StorableData {
    private ObservableList<Observable<Element>> elements = new ObservableList<>();
    private Observable<Integer> id = new Observable<Integer>(0) {
        @Override
        public void setData(Integer data) {
            if (getData() != 0)
                throw new RuntimeException("Content " + Content.this + " already have an id.");
            super.setData(data);
        }
    };
    private Observable<String> title, subtitle;

    private Content() {

    }

    public Content(String title, String subtitle) {
        this.title = new Observable<>(title);
        this.subtitle = new Observable<>(subtitle);
    }

    public abstract void accept(ContentVisitor visitor);
}
