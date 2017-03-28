# de.fr.o.st

Discuss forums offline statistics (de.fr.o.st), a tool for analysis of comments in web site's posts' comments

## About

This tool works with particullar web portal, forum, discuss or something like that. Each such "forum" is represented by class implementing interface `cz.martlin.defrost.forums.base.BaseForumDescriptor`.

In the first phase (static, hardcoded in descriptor class) list of all avaible categories is infered and displayed in main frame. For selected categories are loaded posts of theese categories and displayed. For selected posts can be loaded comments.

Both loaded posts and comments can be exported into or imported from CSV file. Comments can be in addition outputted groupped by author, groupped by post or in table user x post -> count of comments.

The de.fr.o.st naturally supports loading from paged sites, iterruption of loading processes and also avoidance of duplicities (twice loaded same category/post).

## Custom forum implementation

If you want to implement custom "forum", just create class implementing interface `BaseForumDescriptor`, or extend one from existing implementations (`CommonForumDescriptor` or `SelectorsUsingForumDescriptor` in the same package). Than modify the `cz.martlin.defrost.gui.DefrostApplication#DEFAULT_DESCRIPTOR` to yours or specify fully-qualified class name of your descriptor in command line.

## Screens

![screen of app selection](https://raw.githubusercontent.com/martlin2cz/de.fr.o.st/master/screens/selection.png)
![screen of app loading](https://raw.githubusercontent.com/martlin2cz/de.fr.o.st/master/screens/loading.png)
![screen output](https://raw.githubusercontent.com/martlin2cz/de.fr.o.st/master/screens/output.png)

## Build

de.fr.o.st is common maven project. Built using `mvn clean package` also generates executable jar file `target/jfx/app/de.fr.o.st-`version`-jfx.jar`.

