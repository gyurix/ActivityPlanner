package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

@Getter
public class LinkElement extends TextElement {
    private Observable<String> url;

    public LinkElement(String text, String url) {
        super(text);
        this.url = new Observable<>(url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

}
