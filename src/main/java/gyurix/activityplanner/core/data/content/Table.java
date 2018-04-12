package gyurix.activityplanner.core.data.content;

import gyurix.activityplanner.core.data.visitors.ContentVisitor;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;


/**
 * Table extends the Content with title and subtitle properties
 */
@Getter
public class Table extends Content {
    /**
     * The subtitle of this Table
     */
    private Observable<String> subtitle;
    /**
     * The title of this Table
     */
    private Observable<String> title;

    /**
     * Constructor containing every required arguments of the Table
     *
     * @param title    - The title of the constructable Table
     * @param subtitle - The subtitle of the constructable Table
     * @param color    - The color of the constructable Table
     */
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
