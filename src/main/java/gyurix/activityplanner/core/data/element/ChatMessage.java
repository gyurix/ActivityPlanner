package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

/**
 * ChatMessage wraps an Element and adds date and sender informations to it
 */
@Getter
public class ChatMessage {
    /**
     * Added date information
     */
    private Observable<Long> date;
    /**
     * Wrapped message element
     */
    private Observable<Element> message;
    /**
     * Added sender information
     */
    private Observable<String> sender;

    /**
     * Constructs a ChatMessage from it's every required parameters
     *
     * @param date    - Sending date and time of the ChatMessage
     * @param message - The sent message
     * @param sender  - The sender of the message
     */
    public ChatMessage(Long date, Element message, String sender) {
        this.date = new Observable<>(date);
        this.message = new Observable<>(message);
        this.sender = new Observable<>(sender);
    }
}
