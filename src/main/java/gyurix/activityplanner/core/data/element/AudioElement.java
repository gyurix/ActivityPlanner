package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.visitors.ElementVisitor;
import lombok.Getter;

/**
 * AudioElement indicates, that the LinkElements URL points to an audio material
 */
@Getter
public class AudioElement extends LinkElement {
    /**
     * Array of supported file extensions
     */
    @Getter
    private static final String[] extensions = new String[]{"mp3", "m4a", "m4b", "wav"};

    /**
     * Constructor containing every required data of a AudioElement
     *
     * @param text - The text of the constructable AudioElement
     * @param url  - The URL of the constructable AudioElement
     */
    public AudioElement(String text, String url) {
        super(text, url);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
