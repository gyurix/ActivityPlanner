package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

public class VideoElement extends LinkElement {
    @Getter
    private static final String[] extensions = new String[]{"avi", "mp4", "mkv", "wmv"};

    public VideoElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
