package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;


@Getter
public class AudioElement extends LinkElement {
    @Getter
    private static final String[] extensions = new String[]{"mp3", "m4a", "m4b", "wav"};

    public AudioElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
