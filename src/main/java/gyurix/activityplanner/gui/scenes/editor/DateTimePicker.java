package gyurix.activityplanner.gui.scenes.editor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimePicker is a special DatePicker used for showing the systems date picker
 * and for adding the possibility to edit the time and date manually.
 */
public class DateTimePicker extends DatePicker {

    /**
     * The format of the editable date and time
     */
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * String to date and vice versa converter
     */
    private final InternalConverter converter = new InternalConverter();

    /**
     * The editable dateTimeValue
     */
    private final ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());

    /**
     * Formatter of the date time
     */
    private DateTimeFormatter formatter;

    /**
     * The date time format
     */
    private final ObjectProperty<String> format = new SimpleObjectProperty<String>() {
        public void set(String newValue) {
            super.set(newValue);
            formatter = DateTimeFormatter.ofPattern(newValue);
        }
    };

    /**
     * Constructs a new date time picker
     */
    public DateTimePicker() {
        getStyleClass().add("datetime-picker");
        format.set(dateTimeFormat);
        setConverter(converter);

        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                dateTimeValue.set(null);
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
                } else {
                    LocalTime time = dateTimeValue.get().toLocalTime();
                    dateTimeValue.set(LocalDateTime.of(newValue, time));
                }
            }
        });

        dateTimeValue.addListener((observable, oldValue, newValue) ->
                setValue(newValue == null ? null : newValue.toLocalDate()));

        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                simulateEnterPressed();
        });

    }

    /**
     * Gets the date time value property
     *
     * @return The date time value property
     */
    public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return dateTimeValue;
    }

    /**
     * Gets the current date time value
     *
     * @return The current date time value
     */
    public LocalDateTime getDateTimeValue() {
        return dateTimeValue.get();
    }

    /**
     * Sets the current date tiem value
     *
     * @param dateTimeValue - The new date time value
     */
    public void setDateTimeValue(LocalDateTime dateTimeValue) {
        if (dateTimeValue.isAfter(LocalDateTime.of(1971, 6, 30, 12, 00)))
            this.dateTimeValue.set(dateTimeValue);
        else {
            this.dateTimeValue.set(null);
        }
        getEditor().setText(converter.toString(null));
    }

    /**
     * Simulates pressing enter for saving the edit
     */
    private void simulateEnterPressed() {
        getEditor().fireEvent(new KeyEvent(getEditor(), getEditor(), KeyEvent.KEY_PRESSED, null, null, KeyCode.ENTER, false, false, false, false));
    }

    /**
     * Internal StringConverter used for supporting date time format
     */
    class InternalConverter extends StringConverter<LocalDate> {
        @Override
        public String toString(LocalDate object) {
            LocalDateTime value = getDateTimeValue();
            return (value != null) ? value.format(formatter) : "";
        }

        @Override
        public LocalDate fromString(String value) {
            if (value == null) {
                dateTimeValue.set(null);
                return null;
            }
            dateTimeValue.set(LocalDateTime.parse(value, formatter));
            return dateTimeValue.get().toLocalDate();
        }
    }
}