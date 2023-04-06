# Descriptive Modeling and Clustering of Textual Data

Instructions to run code (please use Eclipse IDE) :

1. Clone this repository to your local machine :

`git clone git@github.com:ssnap03/bds.git`

2. Open Eclipse -> File -> Import project from file system or archive -> browse to the directory named `bds` where you cloned the repository -> open and click finish

3. Eclipse will setup the repository as a maven project and download dependencies

4. Once the dependencies are resolved, from the file explorer on the left pane, navigate to com.bds.textmining.driver.Driver.java

5. If you want to test on the same set of 10 unknown files, right click on this class and click run as -> java application

6. The output will display on the console, the program might take a while to execute as it first vectorizes the training corpus, then the test files, runs KNN for different values of k using both cosine similarity and euclidean distance and then prints the confusion matrix and other evaluation metrics for the best k value and then runs fuzzy KNN

7. If you wish to test on another test set, please put the files in a folder and set the testdir variable with the correct path, for example, if you put the files under src/data/test, set testdir to "/src/data/test/"
   you will also need to update the csv file "src/data/labels.csv" with your test file names and actual labels.
 

