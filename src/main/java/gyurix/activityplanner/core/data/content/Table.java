package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;


@Getter
public class Table extends Content {
    private Observable<String> title, subtitle;

    public Table(String title, String subtitle, String color) {
        super(color);
        this.title = new Observable<>(title);
        this.subtitle = new Observable<>(subtitle);
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }
}
