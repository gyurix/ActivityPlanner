package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

public class PictureElement extends LinkElement {
    @Getter
    private static final String[] extensions = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};

    public PictureElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
