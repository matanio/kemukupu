# SOFTENG 206 Assignment 3 Team 39

### Loading Kēmu Kupu

To run this application, type the following from the command line:

First, give the script file StartGame.sh executable permission:

```
> chmod +x StartGame.sh
```

Now run it:

```
> ./StartGame.sh
```

This will run the application (jar file) with the required arguments.

-------------------------------------------------------------------

Alternatively, you can run the jar file KemuKupu.jar directly from command line by typing:

```
> java -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar KemuKupu.jar
```
-------------------------------------------------------------------

### Important Notes

- In **both** cases, make sure the ``words`` and ``data`` directories exist in the same directory as the jar and script files.


## Attribution Credits

Sound effects obtained from https://www.zapsplat.com

Some code snippets from:
- https://stackoverflow.com/questions/36727777/how-to-animate-dashed-line-javafx (Main menu)
- https://stackoverflow.com/questions/12744542/requestfocus-in-textfield-doesnt-work/38900429 (Practice)
- https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/ (FileIO)
