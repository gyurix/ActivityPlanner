package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

@Getter
public class ChatMessage {
    private long date;
    private Observable<Element> message;
    private Observable<String> sender;
}
