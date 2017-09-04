# UML-Parser
CMPE202 Personal Project
Submitted by: Pranjali Shrivastava (011549603)
Tools & Libraries used for JavaParser:
1. eclipse 4.3 (Kepler) Eclipse 4.3 tool is used to create the project.
2. javaparser-1.0.8.jar library is used to parse the input java files and get the ASTs.
javaparser.jar uses libraries and functions that follow the Visitor pattern. Same pattern is followed in the
project code to parse the input java files and create the intermediate code. For eg: for fetching methods,
MethodVisitor class is created that inherits the VoidVisitorAdapter class. The intermediate language is
formed as is accepted by the plantUML.
3. plantuml.jar is used to generate the final Class Diagram. The intermediate language formed in the
above step is as per the input type accepted by the plantUml. The generateImage() method takes the
input intermediate code and converts it to the Class Diagram Image.
4. graphviz-2.38.msi is installed that is used to view the class diagram image generated in the above
step.


Instructions to run the jar on single system:

1. Download the project jar file(UMLParser.jar) from the link provided in the attached file
(GoogleFolderLinkToZip.pdf)
2. In the command prompt, change the path to the folder where you have saved the jar file.
3. Run the following command to generate the UML class diagram-
>java -jar UMLParser.jar <path to testcase folder> <path where you want to save the image generated
with the image name>
For eg:
>java -jar UMLParser.jar F:\user\SJSU\202\PersonalProject\Samples\test4
F:\user\SJSU\202\PersonalProject\Samples\test4\classDiagram4.svg
