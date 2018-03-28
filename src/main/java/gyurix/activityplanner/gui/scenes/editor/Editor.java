package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.stage.Stage;

public abstract class Editor extends AbstractScene {
    public Editor(ElementHolderScene<? extends ElementHolder> holder, Stage stage) {
        super(stage);
        attachLater(holder.getInfo().getId(), () -> {
            destroy();
            stage.hide();
        });
    }
}
