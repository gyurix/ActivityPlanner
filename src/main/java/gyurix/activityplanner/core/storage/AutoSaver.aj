package gyurix.activityplanner.core.storage;

public aspect AutoSaver {
    pointcut dataChange(): call(void gyurix.activityplanner.core.observation.Observable.setData(Object))||
            call(void gyurix.activityplanner.core.observation.ObservableList.add(Object))||
            call(void gyurix.activityplanner.core.observation.ObservableList.remove(Object));
    after() returning: dataChange(){
        System.out.println("Auto save by AutoSaver aspect");
        ConfigUtils.getInstance().saveConfig();
    }
}