package gyurix.activityplanner.core.storage;

import javafx.application.Platform;

public aspect AutoSaver {
    private boolean saving;
    pointcut dataChange(): call(void gyurix.activityplanner.core.observation.Observable.setData(Object))||
            call(void gyurix.activityplanner.core.observation.ObservableList.add(Object))||
            call(void gyurix.activityplanner.core.observation.ObservableList.remove(Object));
    after() returning: dataChange(){
        if (!saving) {
            saving = true;
            Platform.runLater(() -> {
                saving = false;
                ConfigUtils.getInstance().saveConfig();
            });
        }
    }
}