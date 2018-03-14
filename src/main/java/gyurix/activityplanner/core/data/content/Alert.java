package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;


@Getter
public class Alert extends Table {
    private Observable<Long> dueDate;

    public Alert(long dueDate, String title, String subtitle, String color) {
        super(title, subtitle, color);
        this.dueDate = new Observable<>(dueDate);
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }
}
