package gyurix.activityplannertest;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import javafx.application.Application;
import javafx.stage.Stage;

import static gyurix.activityplanner.core.DataStorage.addContent;
import static gyurix.activityplanner.core.DataStorage.addUser;
import static java.lang.System.currentTimeMillis;

public class ActivityPlannerTest extends Application {
    public static void main(String[] args) {
        prepareTestingData();
        //ActivityPlannerLauncher.main(args);
    }

    public static void prepareTestingData() {
        Student s1 = new Student("s1", "pwd");
        Student s2 = new Student("s2", "pwd");
        Lecture l1 = new Lecture("l1", "pwd");
        Lecture l2 = new Lecture("l2", "pwd");
        addUser(s1);
        addUser(s2);
        addUser(l1);
        addUser(l2);

        Table t1 = new Table("Table title", "Table subtitle", "ffff00");
        Table t2 = new Table("Table2 title", "Table2 subtitle", "ffffff");
        Alert a1 = new Alert(currentTimeMillis(), "Alert title", "Alert subtitle", "ff8000");
        Alert a2 = new Alert(currentTimeMillis(), "Alert2 title", "Alert2 subtitle", "ff4000");
        s1.getLectures().add("l1");
        l1.getCreatedContents().add(addContent(t1));
        l1.getCreatedContents().add(addContent(t2));
        l1.getCreatedContents().add(addContent(a1));
        l1.getCreatedContents().add(addContent(a2));
        System.out.println(l1);
    }

    @Override
    public void start(Stage primaryStage) {
    }
}
