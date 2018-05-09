package gyurix.activityplanner.core.storage;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.user.Lector;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

import java.io.File;

import static java.lang.System.currentTimeMillis;

/**
 * Utilities for configuration management
 */
public class ConfigUtils {
    /**
     * Default File name used for configuration
     */
    private static final File configFile = new File("conf.json");

    /**
     * Instance of this singleton class
     */
    @Getter
    private static final ConfigUtils instance = new ConfigUtils();

    /**
     * ConfigUtils should not be instantiated more than ones.
     */
    private ConfigUtils() {
    }

    /**
     * Generates the default configuration, which contains 2 students, 2 lectures.
     * The first lecture has some created contents
     */
    public void generateDefaultConfig() {
        DataStorage ds = new DataStorage();
        Student s1 = new Student("s1", "pwd");
        Student s2 = new Student("s2", "pwd");
        Lector l1 = new Lector("l1", "pwd");
        Lector l2 = new Lector("l2", "pwd");
        l1.getAssignedStudents().add("s1");
        l1.getAssignedStudents().add("s2");
        l2.getAssignedStudents().add("s1");
        l2.getAssignedStudents().add("s2");
        s1.getLectores().add("l1");
        s1.getLectores().add("l2");
        s2.getLectores().add("l1");
        s2.getLectores().add("l2");
        ds.addUser(s1);
        ds.addUser(s2);
        ds.addUser(l1);
        ds.addUser(l2);

        Table t1 = new Table("Table title", "Table subtitle", "ffff00");
        Table t2 = new Table("Table2 title", "Table2 subtitle", "ffffff");
        Alert a1 = new Alert(currentTimeMillis(), "Alert title", "Alert subtitle", "ff8000");
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 50; ++i) {
            longText.append("This is the ").append(i).append(". line of this super long text\n");
        }
        a1.getElements().add(new Observable<>(new TextElement(longText.toString())));
        a1.getElements().add(new Observable<>(new LinkElement("Click here", "http://google.com")));
        a1.getElements().add(new Observable<>(new PictureElement("Let's see some cubes",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/220px-PNG_transparency_demonstration_1.png")));
        a1.getElements().add(new Observable<>(new AudioElement("With some song :D",
                "http://other.web.ri01.sycdn.kuwo.cn/resource/n1/95/69/1605089080.mp3")));
        a1.getElements().add(new Observable<>(new VideoElement("And even video",
                "https://downloadmp.org/@download/22-5af36875df3c5-mp4-19718405/videos/6DQfKoxJY8w/%25E9%2580%25B2%25E6%2592%2583%25E3%2581%25AE%25E5%25B7%25A8%25E4%25BA%25BA%2BShingeki%2Bno%2BKyojin%2BVoice%2BActors%2BSinging%2BOpening%2BTheme.mp4")));

        Alert a2 = new Alert(currentTimeMillis(), "Alert2 title", "Alert2 subtitle", "ff4000");
        l1.getCreatedContents().add(ds.addContent(t1));
        l1.getCreatedContents().add(ds.addContent(t2));
        l1.getCreatedContents().add(ds.addContent(a1));
        l1.getCreatedContents().add(ds.addContent(a2));
        saveConfig();
    }

    /**
     * Loads the configuration, generates default one, if tbe config file does not exist
     */
    public void loadConfig() {
        if (!configFile.exists())
            generateDefaultConfig();
        DataStorage.load(configFile);
    }

    /**
     * Saves the configuration
     */
    public void saveConfig() {
        DataStorage.getInstance().save(configFile);
    }
}
