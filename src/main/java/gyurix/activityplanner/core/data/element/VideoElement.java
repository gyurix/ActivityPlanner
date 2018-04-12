package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

/**
 * VideoElement indicates, that the LinkElements URL points to a video
 */
public class VideoElement extends LinkElement {
    @Getter
    private static final String[] extensions = new String[]{"avi", "mp4", "mkv", "wmv"};

    /**
     * Constructor containing every required data of a VideoElement
     *
     * @param text - The text of the constructable VideoElement
     * @param url  - The URL of the constructable VideoElement
     */
    public VideoElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
