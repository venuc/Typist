    This is a very simple program which will measure your typing speed, display
incorrectly typed words in red, automatically know that you have finished typing
(when you hit spacebar after typing the entire text, there is no stop button) etc.

    This program is a work in progress (please find to-do list in TO-DO.txt file)
and largely untested. You will find the basic code here which you are free to use.
I haven't codded the frills yet like the menus at the top or a timer. I am sure
the program can be improved a lot and would love to see it being tuned.

=====================
KNOWN BUGS
=====================

1. Line breaks / carriage returns are not supported and may have unexpected
    results.
2. Say the first word is incorrect, then you type a correct word and then you go
    back to the first word with the arrow keys and correct it, it still shows in
    red.

=====================
DIRECTORY STRUCTURE
=====================

Java source files in "src" have all the code written so far. Find the "main()"
method in src/org/typist/driver/Driver.java.

"build.xml" file is the ant build file.

"source.repos" file is a clear text file which is the repository for all the paragraphs
that are displayed. This repository file has a few rules that one needs to take care of
when editing.

"TO-DO.txt" contains the features that may be added to improve the program.

=========================
ABOUT source.repos
=========================

 This file is a repository for the text displayed on UI. This file MUST obey the
 following rules:
1. The very first line must be an integer (no spaces on this line). This
    represents the total number of paragraphs present in the file.
2. Every line MUST have atleast 1 character. A blank line should have atleast 1
	space.
3. Paragraphs should be numbered as "$<paragraph no>" Eg: $1 or $13 (wihtout
	quotes. NO spaces allowed on this line.
4. Para number starts from 0, so the maximum para number is always 1 less than the
    number in the very first line.
5. The path of this file is hardcoded in "Constants.java".

==========================
RUNNING THE PROGRAM
==========================

To build and run the program enter the following in your terminal:
ant compile jar run
(Please note that Apache Ant should be installed on your machine.)
You should see a new directory with the name "build" which will have all the
executables.

==========================
MY VIEW ABOUT DESIGN
==========================

This is how the program works:

The Driver class is the one which "drives" the show, interacts with the Engine 
components. Engine interacts with the Utils. Utils are for basic low level tasks.

Bean classes are beans :)

Exception is for exceptional cases and situations :)

And Constants are for constants :)

The program selects a para randomly from the repos file and displays in the upper 
text box which I call the source text throughout the program and the textbox into
which one types is called the destination or dest text.

That's all I can think about for the Readme.txt now...

Please read the comments in the source files as well.
