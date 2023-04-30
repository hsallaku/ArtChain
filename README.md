# ArtChain

**Members**: Haki Sallaku, Gary LaPicola, Thomas Scardino, Matthew Creese

## What is ArtChain?

ArtChain is a blockchain-based platform aiming to provide secure methods of purchasing and trading art to both artists and art collectors. The program uses a Proof-of-Authority (PoA) validation system so that all blocks are checked and validated by a team of real people.

## Dependencies

ArtChain requires: JavaFX 18 or later (https://jdk.java.net/javafx20/) and Gson (https://github.com/google/gson)

## How to Build

The program should be compiled through NetBeans, version 14 or later. 

Create a Java with Ant project in NetBeans (we recommend naming it "ArtChain"). If possible, you can instead make a JavaFX project and skip the "Setting Up JavaFX" step below.

### Setting up Gson

Download the Gson jar file, right click the project in the NetBeans project pane, under Categories click on Libraries. 

Within the Compile-time Libraries pane, click the + symbol next to Classpath and then click Add Library. In the next window, click Create and then name the Library Gson. Click OK, then click Add JAR/Folder in the next window. Navigate to your jar file and select it, click Ok, then click Add Library. Gson should now be set up.

### Setting Up JavaFX

Later versions of NetBeans may not have JavaFX or will bring up a "Failed to automatically set-up a JavaFX Platform" error when attempting to create a JavaFX project. 

In this case, you will have to manually set up JavaFX for your project. 
Under the "Tools" tab, click Libraries, and then within that window, choose Add Library. Name it JavaFX, and include all .jar files from the "lib" folder of your downloaded JavaFX version.

Right click on the project in Netbeans, and then clock on Properties.

Navigate to "Libraries", and in the Compile tab add the JavaFX library to the Classpath tab. 
Next, in the Run tab, add this same library to the Modulepath.

Now, navitage to the "Run" area of the project's properties and under VM Options, type the following:
``--add-modules javafx.controls,javafx.fxml``

JavaFX should now be set up for your project.

### Compiling

In NetBeans, choose the "Clean and Build Project" option in the Run tab. Make sure both Gson and JavaFX are set up and integrated into your project.
