package gyurix.activityplanner.gui.scenes;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.text.SimpleDateFormat;

public class SceneUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("[yyyy.MM.dd] HH:mm:ss");

    public static String formatTime(long time) {
        return sdf.format(time);
    }

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

    public static Background bgColorGradientTop(Color color) {
        return bgColorGradient(color, new CornerRadii(0.15, 0.15, 0, 0, true));
    }
}
