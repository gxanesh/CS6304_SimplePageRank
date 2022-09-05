# SimplePageRank

### General Information:

* Operating System:         Mac -> Microsoft Remote Desktop, Windows -> Default Remote Desktop, Ubuntu -> Remmina
* Machine:                  cs6304-<mst_username>-01.class.mst.edu
* User:                     <mst_username>
* Default Password:         <mst_password>

### Importing Project:
* Open “eclipse”, right click on “Package Explorer” window, click import.
* Select “Git”-> “Projects from Git” and click “next”.
* Select “clone url” and click “next”.
* Paste “https://github.com/ArindamK92/CS6304_SimplePageRank” in the “url” textbox, and click “next”. 
* Choose “Import existing project” and click “finish”.

### Referencing libraries:
* Right click on project and select “build path”-> “configure build path” ->”libraries”->”add external jars”.
* Go to "home" -> "git" -> "SimplePageRank" -> "lib" and select all jars and click ok.

### Input file:
* Open folder "file", you will see the input file named "PageRank.txt"

### Output jar:
* Right click on project and select "Export".
* Choose type "Java" -> "Jar file" and click "next".
* Select the export destination as "home" -> "git" -> "SimplePageRank" -> "jar" and click "Finish".

### Hadoop Commands:
* hadoop fs -mkdir InputFolder                                      //to create a new input folder
* hadoop fs -copyFromLocal &lt;input file> InputFolder                  //to copy a file from local directory to hadoop environment
* hadoop fs -ls InputFolder                                          //to see the files inside "InputFolder"
* hadoop jar &lt;jar file name> &lt;class name> InputFolder OutputFolder   //running mapreduce operation
* hadoop fs -ls OutputFolder                                        //to see the files inside "OutputFolder"
* hadoop fs -cat OutputFolder/part-r-00000                          //to see the content inside "OutputFolder/part-r-00000" file
* hadoop fs -rm -r OutputFolder                                     //to remove "OutputFolder" directory and all its files
* hadoop fs -rm -r InputFolder					    //to remove "OutputFolder" directory and all its files

- remove OutputFolder before generating the next results.
- remove/clean InputFolder if you want to use a different file as input.
  
### Common error fix:

Error: "shell-init: error retrieving current directory: getcwd"
- the directory at which you are when you try to run hadoop command does not exist anymore.