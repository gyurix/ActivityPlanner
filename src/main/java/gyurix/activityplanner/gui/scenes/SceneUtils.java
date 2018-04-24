package gyurix.activityplanner.gui.scenes;

import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SceneUtils contains static utility methods for various operations with scenes
 */
public class SceneUtils {

    /**
     * ioThread is a thread pool used for making async I/O operations
     */
    @Getter
    private static final ExecutorService ioThread = Executors.newCachedThreadPool();

    /**
     * Instance of random used for generating random colors
     */
    private static final Random random = new Random();

    /**
     * Simple date format used for formatting time of Alerts
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy.MM.dd] HH:mm:ss");

    /**
     * Calculates the average of two numbers
     *
     * @param num1 - First number
     * @param num2 - Second number
     * @return The average of the entered 2 numbers
     */
    public static double avg(double num1, double num2) {
        return (num1 + num2) / 2.0;
    }

    /**
     * Calculates the average of two colors
     *
     * @param color1 - First color
     * @param color2 - Second color
     * @return The average of the given 2 colors
     */
    public static Color avgColor(Color color1, Color color2) {
        double r = avg(color1.getRed(), color2.getRed());
        double g = avg(color1.getGreen(), color2.getGreen());
        double b = avg(color1.getBlue(), color2.getBlue());
        double a = avg(color1.getOpacity(), color2.getOpacity());
        return new Color(r, g, b, a);
    }

    /**
     * Makes a background from a solid color
     *
     * @param color - The color of the background
     * @return The created background
     */
    public static Background bgColor(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    /**
     * Makes a gradient background having the given color with a little bit brighter / darker colors
     * and the given corner radius
     *
     * @param color - The color of the background
     * @param radii - The corner radius of the background
     * @return The created background
     */
    public static Background bgColorGradient(Color color, CornerRadii radii) {
        Color brighter = avgColor(Color.WHITE, color);
        Color darker = avgColor(Color.BLACK, color);
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop(0, darker), new Stop(0.2, color), new Stop(1, brighter));
        return new Background(new BackgroundFill(lg, radii, null));
    }

    /**
     * Makes a gradient background having the given color with a little bit brighter / darker colors
     * without corner radius
     *
     * @param color - The color of the background
     * @return The created background
     */
    public static Background bgColorGradient(Color color) {
        return bgColorGradient(color, null);
    }

    /**
     * Makes an inversed gradient background having the given color with a little bit brighter / darker colors
     * without corner radius
     *
     * @param color - The color of the background
     * @return The created background
     */
    public static Background bgColorGradientInv(Color color) {
        Color brighter = avgColor(Color.WHITE, color);
        Color darker = avgColor(Color.BLACK, color);
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop(0, brighter), new Stop(0.8, color), new Stop(1, darker));
        return new Background(new BackgroundFill(lg, null, null));
    }

    /**
     * Makes a gradient background having the given color with a little bit brighter / darker colors
     * with 0.15% top corner radius
     *
     * @param color - The color of the background
     * @return The created background
     */
    public static Background bgColorGradientTop(Color color) {
        return bgColorGradient(color, new CornerRadii(0.15, 0.15, 0, 0, true));
    }

    /**
     * Converts a Color to hex code
     *
     * @param color - The convertable color
     * @return The hex code of the given Color
     */
    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    /**
     * Formats the given time using sdf
     *
     * @param time - The formatable time
     * @return The formatting result
     */
    public static String formatTime(long time) {
        return sdf.format(time);
    }

    /**
     * Generates a relatively bright random color
     *
     * @return A random color
     */
    public static Color getRandomColor() {
        return Color.color(0.3 + random.nextDouble() * 0.7, 0.3 + random.nextDouble() * 0.7, random.nextDouble());
    }

    /**
     * Creates a popup alert
     *
     * @param title - The title of the alert
     * @param msg   - The message of the alert
     * @return The created popup alert
     */
    public static Alert makeAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        return a;
    }

    /**
     * Runs a task on async thread
     *
     * @param runnable - The runnable, which should run asynchronously
     */
    public static void runAsync(Runnable runnable) {
        ioThread.submit(runnable);
    }
}
