# ActivityPlanner

# Introduction

### What is ActityPlanner?

ActivityPlanner is an universal software for helping in teaching students in a modern way.

### Example usage purposes
- making projects together
- sharing study materials
- organizing various events
- setting up deadlines
- consulting with lectors
- chatting with schoolmates

# Setup & Usage

### Step 1: Download or compile the software:
You can download the already compiled releases of the software from the
[artifacts](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/artifacts) directory
or from the
[releases](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/releases) page.

The project should be compiled using **Maven** by using the `mvn install` command.

### Step 2: First run
After completing the first step, you should run the software, so it could generate it's default
configuration.

You can double click to the application for running it in GUI only mode.
If you would like to see the console too, then you can run it using the
`java -jar ActivityPlanner-1.1.jar` command.

### Step 3: Configuration
After running the program first time, make sure to close it. 

You can see that the programs configuration (`conf.json`) has been generated.
It's important, that the configuration should only be edited, when the program is **not** running,
otherwise your changes in config, won't have any effect, since the program saves the config, when
you close it. 

Open the `conf.json` file with a text editor, and edit it according to your requirements.
The students usernames, passwords and their lectors can only be set in the configuration,
while the created contents and their elements can be created, edited and removed in the **GUI** too.

### Step 4: Finished
You are done with the setup and can use the application whenever you want.

# Changelog

### 1.0
- Initial release

### 1.1
- Setup a whole maven building process
- Added [AutoSaver](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/storage/AutoSaver.aj)
aspect for automatically saving the configuration on every data change
- Added UML class hierarchy visualization for the core part 
- Added documentation (README.MD with screenshots and JavaDoc)


# Logical parts
ActivityPlanner consists of two logical parts: the core and the GUI part.

### Core part
The core part includes all the required logic for making the application work.

The exact class hierarchy of the core part can be found in the **doc/uml.uxf** file.

### GUI part
The graphical user interface part consists of the following screens:
* **Login**

![Login](https://gyurix.pro/oop/screenshots/login.jpg)

When you start ActivityPlanner you will see this screen.
After entering the **username** and the **password** of a valid user
you can press the **ENTER** or click to the **Login** button for logging in

* **User Dashboard**

  ![User Dashboard](https://gyurix.pro/oop/screenshots/user.jpg)
  
You can see this screen after logging in. It consists of the following elements:

1. **Your username:** Just an informative text, for checking who is logged in
2. **Logout button:** Click to it for logging out
3. **Tables column:** Here you can find all the tables accessible by the logged in user.
Tables are information blocks. They consists of a title, subtitle, and a list of elements.
They also have a color, which is randomly choice on when you create them.
You can click to a table for opening its content viewer.
Lectors can see and edit their own and their students tables.
Students can see their lectors, and their lectors students tables.
Students can see and edit their own tables.
4. **Alerts column:** Here you can find all the alerts accessible by the logged in user.
Alerts not only have all the properties, what Tables have, but they also have a date
parameter which can be used for setting up due dates for various events or tasks.
Alerts have the same visibility and edit permission system, as Tables.
5. **Chat column:** ActivityPlanner has a built in chat system, for giving the possibility
for students and lectors to directly communicate between each other very easily. There are
individual and group chat channels too. And users can also communicate with themselves for
making private notes easily.
6. **New [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java) button:** Creates a new [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java)
7. **Remove [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java) button:** You can see this icon on the right top corner of every [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java)
to which you have edit permission. It removes the [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java).
8. **New [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java) button:** Creates a new [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java).
By default [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java)s date is their creation time.
9. **Remove [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java) button:** Removes the [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java). If you remove a [Table](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Table.java) or an [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java) it's
every opened content viewer window will be closed automatically
10. **Chat channel selector icons:**
    1. Student selector for **individual chatting** with a student.
Lectors can only see their students, while students see a list of their lectors
and if they hover their mouse over a lectors name the list of students of that
lector shows up for them, from which they can select their recipient 
    2. Lector selector for **group chatting** with a lectors group **without** the lector
When a lector clicks to this icon, an error message will appear for them, since
lectors can not communicate in this group.
    3. Lector selector for **individual chatting** with a lector. Lectors can see all the
other lectors, while students can only see their own lectors.
    4. Lector selector for **group chatting** with a lectors group **with** the lector.
For students a menu pops up for selecting the lector, while for lectors this
button shows the chat with their group.
11. **Own [ChatMessages](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/ChatMessage.java):** You can see your own chat messages in a yellow box, while
others chat messages in a silver one. The message senders name is shown on the left top
part of the box, while the sending date and time is shown on the right top part of the
box.
12. **Others [ChatMessages](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/ChatMessage.java):** As you can see the chat messages can be any kind of
supported elements, not just simple text messages
13. **Message box:** You can write your sendable message to this box. For sending it
just press ENTER. For messages containing an URL the message will show up as a link
element. If the URL ends to a known file extension, then it will be shown up as an
extension to a
**[LinkElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/LinkElement.java)**,
so it will be
**[AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)**,
**[PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java)**
or
**[VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java)**.
You can check this table for the exact extension [Element](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/Element.java) pairs:

|**Extension**|**[Element](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/Element.java) type**|
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| _m4a_ | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java) |
| _m4b_ | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java) |
| _mp3_ | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java) |
| _wav_ | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java) |
| | |
| _bmp_ | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| _gif_ | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| _jpeg_| [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| _jpg_ | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| _png_ | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| | |
| _avi_ | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| _mkv_ | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| _mp4_ | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| _wmv_ | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
 

* **[Content](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Content.java) Viewer**

![Content Viewer](https://gyurix.pro/oop/screenshots/contentviewer.jpg)

You can see this new window popping up when you open an **Alert** or a **Table** by clicking to them.
It consists of the following elements:
1. **The title of the [Content](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Content.java):**
You can double click to edit it if you have edit permissions
2. **The subtitle of the [Content](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Content.java):**
You can double click to edit it if you have edit permissions
3. **The date of the [Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java):**
For tables you can't see this.
You can double click to edit it if you have edit permissions
4. **Edit [Element](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/Element.java) button:**
You can only see it if you have edit permissions for the opened content.
You can click to it for opening the elements editor.
5. **Remove [Element](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/Element.java) button:**
You can only see it if you have edit permissions for the opened content.
You can click to it for removing the element.
If an element is removed, it's editors are closed automatically.
6. **Add new Element buttons:**
You can only see it if you have edit permissions for the opened content.
Creates a new element and opens its editor.
The types of the supported elements are as follows: 
    1. [TextElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/TextElement.java)
    2. [LinkElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/LinkElement.java)
    3. [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)
    4. [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java)
    5. [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java)
    

* **Text Editor**

![Text Editor](https://gyurix.pro/oop/screenshots/texteditor.jpg)

When you are editing [TextElements](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/TextElement.java)
or the title/subtitle of a
[Content](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Content.java)
then you are doing it with this editor. Every change you make will be instantly visible by anything else showing the same text. 

* **Date Editor**

![Date Editor](https://gyurix.pro/oop/screenshots/dateeditor.jpg)

You are using this editor for changing the date of an
[Alert](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/content/Alert.java)
You can click to the calendar icon for showing up that popup calendar for easier date selection.

* **Url Editor**

![Url Editor](https://gyurix.pro/oop/screenshots/urleditor.jpg)

You are using this editor for changing a
[LinkElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/LinkElement.java)
or one of it's extensions. You can click to the browse icon next to the link for opening your local file explorer and
choosing a file with it.