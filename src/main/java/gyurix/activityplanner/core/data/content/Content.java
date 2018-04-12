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
    /**
     * Color property of this Content
     */
    private Observable<String> color;
    /**
     * Elements of this Content
     */
    private ObservableList<Observable<Element>> elements = new ObservableList<>();
    /**
     * Identification number of this Content, registered by database.
     * Should be 0, when it's not yet registered
     */
    private Observable<Integer> id = new Observable<Integer>(0) {
        @Override
        public void setData(Integer data) {
            if (getData() != 0 && data != 0)
                throw new RuntimeException("Content " + Content.this + " already have an id (" + getData() + ").");
            super.setData(data);
        }
    };

    /**
     * Constructor with color property
     *
     * @param color - The color of the constructabe Content
     */
    public Content(String color) {
        this.color = new Observable<>(color);
    }

    /**
     * Contents can be visited by ContentVisitor
     *
     * @param visitor - The ContentVisitor, which wants to visit this Content
     */
    public abstract void accept(ContentVisitor visitor);
}
