package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

/**
 * TextElement is the basic extension of the Element, which extends it by a text parameter
 */
@Getter
public class TextElement extends Element {
    /**
     * The text parameter of this TextElement
     */
    private Observable<String> text;

    /**
     * Constructs a new TextElement from the given text
     *
     * @param text - The text of the constructable TextElement
     */
    public TextElement(String text) {
        this.text = new Observable<>(text);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
