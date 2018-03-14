package gyurix.activityplannertest;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.element.TextElement;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.DataStorage;
import javafx.application.Application;
import javafx.stage.Stage;

import static java.lang.System.currentTimeMillis;

public class ActivityPlannerTest extends Application {
    public static void main(String[] args) {
        prepareTestingData();
        //ActivityPlannerLauncher.main(args);
    }

    public static void prepareTestingData() {
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
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        a1.getElements().add(new Observable<>(new TextElement("Text element")));
        Alert a2 = new Alert(currentTimeMillis(), "Alert2 title", "Alert2 subtitle", "ff4000");
        s1.getLectures().add("l1");
        l1.getCreatedContents().add(ds.addContent(t1));
        l1.getCreatedContents().add(ds.addContent(t2));
        l1.getCreatedContents().add(ds.addContent(a1));
        l1.getCreatedContents().add(ds.addContent(a2));
        String ds1 = ds.toString();
        System.out.println("Before:\n" + ds1);
        String ds2 = DataStorage.getGson().fromJson(ds1, DataStorage.class).toString();
        System.out.println("\n\n\nAfter:\n" + ds2);
        System.out.println("\n\n\nEquals:\n" + ds1.equals(ds2));
    }

    @Override
    public void start(Stage primaryStage) {
    }
}
