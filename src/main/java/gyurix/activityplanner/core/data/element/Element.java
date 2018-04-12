package gyurix.activityplanner.core.data.element;

import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.visitors.ElementVisitor;

/**
 * Base class for every Element
 */
public abstract class Element extends StorableData {
    /**
     * Utility method for checking if a String array contains a String element
     *
     * @param array   - Checkable array
     * @param element - Checkable element
     * @return True if the array contains the element, false otherwise
     */
    private static boolean contains(String[] array, String element) {
        for (String s : array)
            if (s.equals(element))
                return true;
        return false;
    }

    /**
     * Constructs an element from a String
     *
     * @param in - Input String
     * @return The appropriate element constructable from the given String
     */
    public static Element of(String in) {
        //Check if the given String represents a file / link
        if (in.startsWith("http://") || in.startsWith("https://") || in.startsWith("file:///")) {
            String fileName = in;

            //Trim the last slash from the end of the file name
            if (fileName.endsWith("/"))
                fileName = fileName.substring(0, fileName.length() - 1);

            //Find the last part of the file name, parts can be separated by normal and reversed slashes
            int revSlash = fileName.lastIndexOf("\\");
            fileName = fileName.substring((revSlash == -1 ? fileName.lastIndexOf("/") : revSlash) + 1);

            //Check the extension of the file, return LinkElement for files without extensions
            int extensionId = fileName.lastIndexOf(".");
            if (extensionId == -1)
                return new LinkElement(fileName, in);
            String extension = fileName.substring(extensionId + 1).toLowerCase();

            //Trim the extension name from the end of file name
            fileName = fileName.substring(0, extensionId);

            //Choose the returnable element type based on the files extension
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

    /**
     * Elements are visitable by ElementVisitors
     *
     * @param visitor - The ElementVisitor, which wants to visit this Element
     */
    public abstract void accept(ElementVisitor visitor);
}
