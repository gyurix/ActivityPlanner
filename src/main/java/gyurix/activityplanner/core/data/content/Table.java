package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import lombok.Getter;


@Getter
public class Table extends Content {
    private Observable<String> color;

    public Table(String title, String subtitle, String color) {
        super(title, subtitle);
        this.color = new Observable<>(color);
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }
}
