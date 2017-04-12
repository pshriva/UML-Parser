import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;



public class UMLGenerator {
	public void createClassDiagram(String umlFile) throws IOException {
		// TODO Auto-generated method stub
		SourceStringReader reader = new SourceStringReader(umlFile);
		FileOutputStream output = new FileOutputStream(new File("F:/user/SJSU/202/PersonalProject/Samples/testcase3/test.svg"));
		 reader.generateImage(output, new FileFormatOption(FileFormat.SVG, false));
	}
}
