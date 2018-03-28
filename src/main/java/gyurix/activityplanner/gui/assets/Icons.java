package gyurix.activityplanner.gui.assets;

import javafx.scene.image.Image;

public enum Icons {
    ADD("add.png"),
    BROWSE("browse.png"),
    CHAT_BOTH("chat/both.png"),
    CHAT_LECTOR("chat/lector.png"),
    CHAT_STUDENT("chat/student.png"),
    CHAT_STUDENTS("chat/students.png"),
    EDIT("edit.png"),
    ELEMENT_AUDIO("element/audio.png"),
    ELEMENT_PICTURE("element/picture.png"),
    ELEMENT_TEXT("element/text.png"),
    ELEMENT_URL("element/url.png"),
    ELEMENT_VIDEO("element/video.png"),
    LOADING("loading.gif"),
    LOGO("logo.png"),
    REMOVE("remove.png");

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
