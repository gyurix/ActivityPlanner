package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;

public abstract class Element extends StorableData {
    public abstract void accept(ElementVisitor visitor);
}
