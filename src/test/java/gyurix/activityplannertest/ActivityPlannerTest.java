package gyurix.activityplannertest;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.element.LinkElement;
import gyurix.activityplanner.core.data.element.TextElement;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.ActivityPlannerLauncher;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.System.currentTimeMillis;

public class ActivityPlannerTest extends Application {
    public static void generateTestingData() {
        DataStorage ds = new DataStorage();
        Student s1 = new Student("s1", "pwd");
        Student s2 = new Student("s2", "pwd");
        Lecture l1 = new Lecture("l1", "pwd");
        Lecture l2 = new Lecture("l2", "pwd");
        ds.addUser(s1);
        ds.addUser(s2);
        ds.addUser(l1);
        ds.addUser(l2);

        Table t1 = new Table("Table title", "Table subtitle", "ffff00");
        Table t2 = new Table("Table2 title", "Table2 subtitle", "ffffff");
        Alert a1 = new Alert(currentTimeMillis(), "Alert title", "Alert subtitle", "ff8000");
        a1.getElements().add(new Observable<>(new TextElement("Some text goes here\nWhich can have\nMultiline support")));
        a1.getElements().add(new Observable<>(new LinkElement("Click here", "google.com")));
        Alert a2 = new Alert(currentTimeMillis(), "Alert2 title", "Alert2 subtitle", "ff4000");
        s1.getLectures().add("l1");
        l1.getCreatedContents().add(ds.addContent(t1));
        l1.getCreatedContents().add(ds.addContent(t2));
        l1.getCreatedContents().add(ds.addContent(a1));
        l1.getCreatedContents().add(ds.addContent(a2));
        ds.save(new File("conf.json"));
    }

    public static void loadTestingData() {
        File f = new File("conf.json");
        if (!f.exists())
            generateTestingData();
        DataStorage.load(f);
    }

    public static void main(String[] args) {
        loadTestingData();
        ActivityPlannerLauncher.main(args);
    }

    @Override
    public void start(Stage primaryStage) {
    }
}
