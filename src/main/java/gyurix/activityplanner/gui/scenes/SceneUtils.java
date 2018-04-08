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

public class SceneUtils {
    @Getter
    private static final ExecutorService ioThread = Executors.newCachedThreadPool();
    private static final Random random = new Random();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy.MM.dd] HH:mm:ss");

    public static double avg(double d1, double d2) {
        return (d1 + d2) / 2.0;
    }

    public static Color avgColor(Color c1, Color c2) {
        double r = avg(c1.getRed(), c2.getRed());
        double g = avg(c1.getGreen(), c2.getGreen());
        double b = avg(c1.getBlue(), c2.getBlue());
        double a = avg(c1.getOpacity(), c2.getOpacity());
        return new Color(r, g, b, a);
    }

    public static Background bgColor(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    public static Background bgColorGradient(Color color, CornerRadii radii) {
        Color brighter = avgColor(Color.WHITE, color);
        Color darker = avgColor(Color.BLACK, color);
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop(0, darker), new Stop(0.2, color), new Stop(1, brighter));
        return new Background(new BackgroundFill(lg, radii, null));
    }

    public static Background bgColorGradient(Color color) {
        return bgColorGradient(color, null);
    }

    public static Background bgColorGradientInv(Color color) {
        Color brighter = avgColor(Color.WHITE, color);
        Color darker = avgColor(Color.BLACK, color);
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop(0, brighter), new Stop(0.8, color), new Stop(1, darker));
        return new Background(new BackgroundFill(lg, null, null));
    }

    public static Background bgColorGradientTop(Color color) {
        return bgColorGradient(color, new CornerRadii(0.15, 0.15, 0, 0, true));
    }

    public static String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
    }

    public static String formatTime(long time) {
        return sdf.format(time);
    }

    public static Color getRandomColor() {
        return Color.color(0.3 + random.nextDouble() * 0.7, 0.3 + random.nextDouble() * 0.7, random.nextDouble());
    }

    public static Alert makeAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        return a;
    }

    public static void runAsync(Runnable r) {
        ioThread.submit(r);
    }
}
