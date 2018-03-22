package gyurix.activityplanner.gui.assets;

import javafx.scene.image.Image;

public enum Icons {
    BROWSE("browse.png"),
    EDIT("edit.png"),
    LOADING("loading.gif"),
    LOGO("logo.png"),
    REMOVE("remove.png"),
    ELEMENT_AUDIO("element/audio.png"),
    ELEMENT_PICTURE("element/picture.png"),
    ELEMENT_TEXT("element/text.png"),
    ELEMENT_URL("element/url.png"),
    ELEMENT_VIDEO("element/video.png");

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
