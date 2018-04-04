package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;

public abstract class Element extends StorableData {
    public abstract void accept(ElementVisitor visitor);

    private static boolean contains(String[] array, String element) {
        for (String s : array)
            if (s.equals(element))
                return true;
        return false;
    }

    public static Element of(String in) {
        if (in.startsWith("http://") || in.startsWith("https://") || in.startsWith("file:///")) {
            String fileName = in;
            if (fileName.endsWith("/"))
                fileName = fileName.substring(0, fileName.length() - 1);
            int revSlash = fileName.lastIndexOf("\\");
            fileName = fileName.substring((revSlash == -1 ? fileName.lastIndexOf("/") : revSlash) + 1);
            int extensionId = fileName.lastIndexOf(".");
            if (extensionId == -1)
                return new LinkElement(fileName, in);
            String extension = fileName.substring(extensionId + 1).toLowerCase();
            fileName = fileName.substring(0, extensionId);
            if (contains(PictureElement.getExtensions(), extension))
                return new PictureElement(fileName, in);
            if (contains(AudioElement.getExtensions(), extension))
                return new AudioElement(fileName, in);
            if (contains(VideoElement.getExtensions(), extension))
                return new VideoElement(fileName, in);
            return new LinkElement(fileName, in);
        }
        return new TextElement(in);
    }
}
