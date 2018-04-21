#ActivityPlanner

##Introduction

####What is ActityPlanner?

ActivityPlanner is an universal software for helping in teaching students in a modern way.

####Example usage purposes
- making projects together
- sharing study materials
- organizing various events
- setting up deadlines
- consulting with lectures
- chatting with schoolmates

##Setup & Usage

####Step 1: Download or compile the software:
You can download the already compiled releases of the software from the
[artifacts](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/artifacts) directory
or from the
[releases](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/releases) page.

The project should be compiled using **Maven** by using the `mvn install` command.

####Step 2: First run
After completing the first step, you should run the software, so it could generate it's default
configuration.

You can double click to the application for running it in GUI only mode.
If you would like to see the console too, then you can run it using the
`java -jar ActivityPlanner-1.1.jar` command.

####Step 3: Configuration
After running the program first time, make sure to close it. 

You can see that the programs configuration (`conf.json`) has been generated.
It's important, that the configuration should only be edited, when the program is **not** running,
otherwise your changes in config, won't have any effect, since the program saves the config, when
you close it. 

Open the `conf.json` file with a text editor, and edit it according to your requirements.
The students usernames, passwords and their lectors can only be set in the configuration,
while the created contents and their elements can be created, edited and removed in the **GUI** too.

####Step 4: Finished
You are done with the setup and can use the application whenever you want.

##Logical parts
ActivityPlanner consists of two logical parts: the core and the GUI part.

####Core part
The core part includes all the required logic for making the application work.

The exact class hierarchy of the core part can be found in the **doc/uml.uxf** file.

####GUI part
The graphical user interface part consists of the following screens:
- **Login**

![Login](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/login.jpg)

- **User Dashboard**

![User Dashboard](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/user.jpg)

- **Content Viewer**

![Content Viewer](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/contentviewer.jpg)

- **Text Editor**

![Text Editor](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/texteditor.jpg)

- **Date Editor**

![Date Editor](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/dateeditor.jpg)

- **Url Editor**

![Url Editor](https://github.com/OOP-FIIT/oop-projekt-stv-09-a-povazanova-gyurix/tree/master/screenshots/urleditor.jpg)