package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Getter
public class DateEditor extends AbstractScene {
    private Label dateLabel = new Label("Date");
    private DateTimePicker datePicker = new DateTimePicker();
    private boolean editLock;
    private Observable<Long> text;

    public DateEditor(Observable<Long> text) {
        super(new Stage());
        this.text = text;
    }

    public void addNodesToGrid() {
        grid.add(dateLabel, 1, 1);
        grid.add(datePicker, 1, 2);
    }

    public void createNodes() {
        datePicker.setPrefWidth(Double.MAX_VALUE);
        dateLabel.setAlignment(Pos.CENTER);
        dateLabel.setPrefWidth(Double.MAX_VALUE);
        attach(text, () -> {
            if (!editLock) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(text.getData()));
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
            text.setData(cal.getTime().getTime());
            editLock = false;
        };
        datePicker.dateTimeValueProperty().addListener((observable, oldValue, newValue) -> r.run());
    }

    @Override
    public void createScene() {
        createResizableScene(0.17, "Date Editor");
    }

    public void makeGrid() {
        grid.setVgap(5);
        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        side.setPercentWidth(5);
        ColumnConstraints center = new ColumnConstraints();
        center.setPercentWidth(90);
        grid.getColumnConstraints().addAll(side, center, side);
    }

    public void makeGridRows() {
        RowConstraints side = new RowConstraints();
        side.setPercentHeight(5);
        RowConstraints label = new RowConstraints();
        label.setPercentHeight(5);
        RowConstraints editor = new RowConstraints();
        editor.setPercentHeight(85);
        grid.getRowConstraints().addAll(side, label, editor, side);
    }
}
