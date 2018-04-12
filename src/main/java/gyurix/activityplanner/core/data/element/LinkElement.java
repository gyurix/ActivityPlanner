package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

/**
 * LinkElement extends the TextElement with a URL parameter.
 * It can be used for making clickable links
 */
@Getter
public class LinkElement extends TextElement {
    /**
     * The address of the stored link
     */
    private Observable<String> url;

    /**
     * Constructor containing every required data of a LinkElement
     *
     * @param text - The text of the constructable LinkElement
     * @param url  - The URL of the constructable LinkElement
     */
    public LinkElement(String text, String url) {
        super(text);
        this.url = new Observable<>(url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }

}
