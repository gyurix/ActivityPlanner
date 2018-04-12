package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

/**
 * PictureElement indicates, that the LinkElements URL points to a picture
 */
public class PictureElement extends LinkElement {
    /**
     * Array of supported file extensions
     */
    @Getter
    private static final String[] extensions = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};

    /**
     * Constructor containing every required data of a PictureElement
     *
     * @param text - The text of the constructable PictureElement
     * @param url  - The URL of the constructable PictureElement
     */
    public PictureElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
