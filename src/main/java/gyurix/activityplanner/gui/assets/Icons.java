package gyurix.activityplanner.gui.assets;

import javafx.scene.image.Image;

public enum Icons {
    EDIT("edit.png"), LOGO("logo.png"), LOADING("loading.gif");

    private String fileName;
    private Image image;

    Icons(String fileName) {
        this.fileName = fileName;
    }

    public Image getImage() {
        if (image == null)
            image = new Image(Icons.class.getResourceAsStream("/icons/" + fileName));
        return image;
    }
}
