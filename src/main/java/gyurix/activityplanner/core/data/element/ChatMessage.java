package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

@Getter
public class ChatMessage {
    private Observable<Long> date;
    private Observable<Element> message;
    private Observable<String> sender;

    public ChatMessage(Long date, Element message, String sender) {
        this.date = new Observable<>(date);
        this.message = new Observable<>(message);
        this.sender = new Observable<>(sender);
    }
}
