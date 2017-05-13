Tools & Libraries for JavaParser:

1. javaparser.jar library is used to parse the input java files and get the ASTs. JavaParser.jar uses libraries and functions that follow the Visitor pattern, the same classes have been used in the visitor pattern to parse the input java files and create the intermediate code.
2. plantuml.jar is used to generate the final Class Diagram. The intermediate language formed in the above step is as per the input type accepted by the plantUml.
3. graphviz.msi is installed that is used to generate and view the class diagram image generated in the above step.




Please follow the instructions to run the jar in the command prompt.

1. Download the project jar file(UMLParser.jar) from the link provided in the attaced file (LinkToParserZip.txt)
2. In the command prompt, change the path to the folder where you have saved the jar file.
3. Run the folowing command to generate the UML class diagram- 


>java -jar UMLParser.jar <path till the testcase folder> <path where you want to save the image generated with the image name>


For eg:

>java -jar UMLParser.jar F:\user\SJSU\202\PersonalProject\Samples\test4 F:\user\SJSU\202\PersonalProject\Samples\test4\classDiagram4.svg

Note: You may need the graphviz.msi software insatlled to get the image. There are 5 testcases folders, each folder has the image file for the class diagram, please compare the image generated with the corresponding image in the testcase images.
// I am generating .svg images, reference images are .png
