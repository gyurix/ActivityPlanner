package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

@Getter
public class TextElement extends Element {
    private Observable<String> text;

    public TextElement(String text) {
        this.text = new Observable<>(text);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
