package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * DateEditor used for changing the time and date of an Alert
 */
@Getter
public class DateEditor extends Editor {
    /**
     * The changeable date
     */
    private Observable<Long> date;

    /**
     * The date label
     */
    private Label dateLabel = new Label("Date");

    /**
     * The date time picker box
     */
    private DateTimePicker datePicker = new DateTimePicker();

    /**
     * Edit lock, used for locking date changes, if it's getting edited by this editor
     */
    private boolean editLock;

    /**
     * Constructs a new DateEditor
     *
     * @param holder - The ElementHolderScene
     * @param date   - The editable date
     */
    public DateEditor(ElementHolderScene<? extends ElementHolder> holder, Observable<Long> date) {
        super(holder, new Stage());
        this.date = date;
    }

    @Override
    public void addNodesToGrid() {
        grid.add(dateLabel, 1, 1);
        grid.add(datePicker, 1, 2);
    }

    @Override
    public void createNodes() {
        datePicker.setPrefWidth(Double.MAX_VALUE);
        dateLabel.setAlignment(Pos.CENTER);
        dateLabel.setPrefWidth(Double.MAX_VALUE);
        attach(date, () -> {
            if (!editLock) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(date.getData()));
                LocalDate ld = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                LocalTime lt = LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                datePicker.setDateTimeValue(LocalDateTime.of(ld, lt));
            }
        });
        Runnable r = () -> {
            editLock = true;
            LocalDateTime ld = datePicker.getDateTimeValue();
            Calendar cal = Calendar.getInstance();
            cal.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth(), ld.getHour(), ld.getMinute(), ld.getSecond());
            date.setData(cal.getTime().getTime());
            editLock = false;
        };
        datePicker.dateTimeValueProperty().addListener((observable, oldValue, newValue) -> r.run());
    }

    @Override
    public void createScene() {
        createResizableScene(0.17, "Date Editor");
    }
}
