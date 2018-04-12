package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;


/**
 * Alert extends the Table with a dueDate property
 */
@Getter
public class Alert extends Table {
    /**
     * The dueDate of this Alert
     */
    private Observable<Long> dueDate;

    /**
     * Constructor containing every required arguments of the alert
     *
     * @param dueDate  - The dueDate of the constructable Alert
     * @param title    - The title of the constructable Alert
     * @param subtitle - The subtitle of the constructable Alert
     * @param color    - The color of the constructable Alert
     */
    public Alert(long dueDate, String title, String subtitle, String color) {
        super(title, subtitle, color);
        this.dueDate = new Observable<>(dueDate);
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }
}
