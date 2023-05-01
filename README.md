# ArtChain

**Members**: Haki Sallaku, Gary LaPicola, Thomas Scardino, Matthew Creese

## What is ArtChain?

ArtChain is a blockchain-based platform aiming to provide secure methods of purchasing and trading art to both artists and art collectors. The program uses a Proof-of-Authority (PoA) validation system so that all blocks are checked and validated by a team of real people.

## Dependencies

ArtChain requires: JavaFX 18 or later (https://jdk.java.net/javafx20/) and Gson (https://github.com/google/gson)

## How to Build

The program should be compiled through NetBeans, version 14 or later. 

Create a Java with Ant project in NetBeans (we recommend naming it "ArtChain"). 

### Setting up Gson

Download the Gson jar file, right click the project in the NetBeans project pane, under Categories click on Libraries. 

Within the Compile-time Libraries pane, click the + symbol next to Classpath and then click Add Library. In the next window, click Create and then name the Library Gson. Click OK, then click Add JAR/Folder in the next window. Navigate to your jar file and select it, click Ok, then click Add Library. Gson should now be set up.

### Setting Up JavaFX

Follow the instructions in [this video](https://youtu.be/6E4IkTuvUCI) to set up JavaFX for NetBeans.

### Compiling

In NetBeans, choose the "Clean and Build Project" option in the Run tab. Afterwards, right click on the main.java file and click "Run". Make sure both Gson and JavaFX are set up and integrated into your project.
