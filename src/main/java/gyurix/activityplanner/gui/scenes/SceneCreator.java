package gyurix.activityplanner.gui.scenes;

public interface SceneCreator {
    void addNodesToGrid();

    void prepareScene();

    void createNodes();

    void makeGrid();

    void makeGridColumns();

    void makeGridRows();

    void start();
}
