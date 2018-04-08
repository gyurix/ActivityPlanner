package gyurix.activityplanner.core.storage;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.element.*;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

import java.io.File;

import static java.lang.System.currentTimeMillis;

public class ConfigUtils {
    private static final File configFile = new File("conf.json");
    @Getter
    private static final ConfigUtils instance = new ConfigUtils();

    private ConfigUtils() {
    }

    public void generateDefaultConfig() {
        DataStorage ds = new DataStorage();
        Student s1 = new Student("s1", "pwd");
        Student s2 = new Student("s2", "pwd");
        Lecture l1 = new Lecture("l1", "pwd");
        Lecture l2 = new Lecture("l2", "pwd");
        l1.getAssignedStudents().add("s1");
        l1.getAssignedStudents().add("s2");
        l2.getAssignedStudents().add("s1");
        l2.getAssignedStudents().add("s2");
        s1.getLectures().add("l1");
        s1.getLectures().add("l2");
        s2.getLectures().add("l1");
        s2.getLectures().add("l2");
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
        a1.getElements().add(new Observable<>(new PictureElement("Some nice cat picture",
                "https://gyurix.pro/oop/cat.jpg")));
        a1.getElements().add(new Observable<>(new AudioElement("With some song :D",
                "https://gyurix.pro/oop/alone.mp3")));
        a1.getElements().add(new Observable<>(new VideoElement("And even video",
                "https://gyurix.pro/oop/attackontitan.mp4")));

        Alert a2 = new Alert(currentTimeMillis(), "Alert2 title", "Alert2 subtitle", "ff4000");
        l1.getCreatedContents().add(ds.addContent(t1));
        l1.getCreatedContents().add(ds.addContent(t2));
        l1.getCreatedContents().add(ds.addContent(a1));
        l1.getCreatedContents().add(ds.addContent(a2));
        saveConfig();
    }

    public void loadConfig() {
        if (!configFile.exists())
            generateDefaultConfig();
        DataStorage.load(configFile);
    }

    public void saveConfig() {
        DataStorage.getInstance().save(configFile);
    }
}
