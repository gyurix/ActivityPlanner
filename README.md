# ActivityPlanner

## Introduction

#### What is ActityPlanner?

ActivityPlanner is an universal software for helping in teaching students in a modern way.

#### Example usage purposes
- making projects together
- sharing study materials
- organizing various events
- setting up deadlines
- consulting with lectures
- chatting with schoolmates

## Setup & Usage

#### Step 1: Download or compile the software:
You can download the already compiled releases of the software from the
[artifacts](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/artifacts) directory
or from the
[releases](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/releases) page.

The project should be compiled using **Maven** by using the `mvn install` command.

#### Step 2: First run
After completing the first step, you should run the software, so it could generate it's default
configuration.

You can double click to the application for running it in GUI only mode.
If you would like to see the console too, then you can run it using the
`java -jar ActivityPlanner-1.1.jar` command.

#### Step 3: Configuration
After running the program first time, make sure to close it. 

You can see that the programs configuration (`conf.json`) has been generated.
It's important, that the configuration should only be edited, when the program is **not** running,
otherwise your changes in config, won't have any effect, since the program saves the config, when
you close it. 

Open the `conf.json` file with a text editor, and edit it according to your requirements.
The students usernames, passwords and their lectors can only be set in the configuration,
while the created contents and their elements can be created, edited and removed in the **GUI** too.

#### Step 4: Finished
You are done with the setup and can use the application whenever you want.

## Logical parts
ActivityPlanner consists of two logical parts: the core and the GUI part.

#### Core part
The core part includes all the required logic for making the application work.

The exact class hierarchy of the core part can be found in the **doc/uml.uxf** file.

#### GUI part
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
6. **New table button:** Creates a new table
7. **Remove table button:** You can see this icon on the right top corner of every table
to which you have edit permission. It removes the table.
8. **New alert button:** Creates a new alert.
By default alerts date is their creation time.
9. **Remove alert button:** Removes the alert. If you remove a table or an alert it's
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
11. **Own chat messages:** You can see your own chat messages in a yellow box, while
others chat messages in a silver one. The message senders name is shown on the left top
part of the box, while the sending date and time is shown on the right top part of the
box.
12. **Others chat messages:** As you can see the chat messages can be any kind of
supported elements, not just simple text messages
13. **Message box:** You can write your sendable message to this box. For sending it
just press ENTER. For messages containing an URL the message will show up as a link
element. If the URL ends to a known file extension, then it will be shown up as an
extension to a **LinkElement**, so it will be **AudioElement**,
**PictureElement** or **VideoElement**. You can check this table for the exact extension
element pairs: 
---
|**Extension**|**Element type**|
| m4a | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)   |
| m4b | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)   |
| mp3 | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)   |
| wav | [AudioElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/AudioElement.java)   |
---
| bmp | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| gif | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| jpeg| [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| jpg | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
| png | [PictureElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/PictureElement.java) |
---
| avi | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| mkv | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| mp4 | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
| wmv | [VideoElement](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/blob/master/src/main/java/gyurix/activityplanner/core/data/element/VideoElement.java) |
--- 

* **Content Viewer**

![Content Viewer](https://gyurix.pro/oop/screenshots//contentviewer.jpg)

* **Text Editor**

![Text Editor](https://gyurix.pro/oop/screenshots/texteditor.jpg)

* **Date Editor**

![Date Editor](https://gyurix.pro/oop/screenshots/dateeditor.jpg)

* **Url Editor**

![Url Editor](https://gyurix.pro/oop/screenshots/urleditor.jpg)