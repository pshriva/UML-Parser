import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class MyUMLParser {

	static String inputfilepath;
	static String[] javaClassName;
	static File parseFile;
	static ArrayList<String> classNames = new ArrayList<String>();
	static ArrayList<String> implementedClasses = new ArrayList<String>();
	static ArrayList<String> totalImplementedClasses = new ArrayList<String>();
	static ArrayList<String> methodNames = new ArrayList<String>();
	static ArrayList<String> fieldNames = new ArrayList<String>();
	static ArrayList<String> subClassNames = new ArrayList<String>();
	static ArrayList<String> totalSubClassNames = new ArrayList<String>();
	static ArrayList<String> associations = new ArrayList<String>();
	static ArrayList<String> totalAssociations = new ArrayList<String>();
	static ArrayList<String> dependency = new ArrayList<String>();
	static ArrayList<String> totalDependency = new ArrayList<String>();
	static ArrayList<String> constructorDependency = new ArrayList<String>();
	static ArrayList<String> totalConstructorDependency = new ArrayList<String>();
	static ArrayList<String> interfaces = new ArrayList<String>();
	static ArrayList<String> totalInterfaces = new ArrayList<String>();
	static ArrayList<String> constructors = new ArrayList<String>();	
	static StringBuffer umlFile = new StringBuffer();
	static CompilationUnit cu;
	static ConstructorVisitor constructorVisitor;
	static MethodVisitor methodVisitor;
	static InterfaceAndClassVisitor interfaceAndClassVisitor;
	static FieldVisitor fieldVisitor;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		inputfilepath = "F:/user/SJSU/202/PersonalProject/Samples/Lab5Part2TestCases";
		File file = new File(inputfilepath);
		UMLGenerator umlgenerator = new UMLGenerator();
		classNames = getJavaClasses(file);
		parseFiles(file);
		/*for(String s : associations){
			System.out.println(s);
		}*/
		//System.out.println(umlFile);
		umlgenerator.createClassDiagram(umlFile.toString());
	}
	private static ArrayList<String> getJavaClasses(File file) {
		ArrayList<String> classes = new ArrayList<String>();
		// TODO Auto-generated method stub
		File[] files = file.listFiles();
		for(File f: files){
			if(f.getName().contains(".java")){
				javaClassName = f.getName().split("\\.");
				classes.add(javaClassName[0]);
			}
		}
		
		return classes;
	}
	private static void parseFiles(File file) {
		File[] files = file.listFiles();
		umlFile.append("@startuml\n");
		for(File f: files){
			if(f.getName().contains(".java")){
				javaClassName = f.getName().split("\\.");
				//classNames.add(javaClassName[0]);
				try {
					cu = JavaParser.parse(f);
					JavaParser.setCacheParser(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fieldVisitor = new FieldVisitor(classNames,javaClassName[0]);
				fieldVisitor.visit(cu, null);
				fieldNames = fieldVisitor.getFieldNames();
			    associations = fieldVisitor.getAssociations();
				methodVisitor = new MethodVisitor(classNames,javaClassName[0],fieldNames);
				methodVisitor.visit(cu,null);
				methodNames = methodVisitor.getMethodNames();
				dependency = methodVisitor.getDependency();
				for(String s: dependency){
					if(totalDependency.contains(s) == false)
						totalDependency.add(s);
				}
				constructorVisitor = new ConstructorVisitor(classNames,javaClassName[0]);
				constructorVisitor.visit(cu,null);
				constructors = constructorVisitor.getConstructors();
				constructorDependency = constructorVisitor.getConstructorDependency();
				for(String s: constructorDependency){
					if(totalConstructorDependency.contains(s) == false)
					totalConstructorDependency.add(s);
				}
				interfaceAndClassVisitor = new InterfaceAndClassVisitor(javaClassName[0]);
				interfaceAndClassVisitor.visit(cu, null);
				interfaces = interfaceAndClassVisitor.getInterfaces();
				for(String s: interfaces){
					if(totalInterfaces.contains(s) == false)
					totalInterfaces.add(s);
				}
				subClassNames = interfaceAndClassVisitor.getSubClassNames();
				for(String s: subClassNames){
					if(totalSubClassNames.contains(s) == false)
					totalSubClassNames.add(s);
				}
				implementedClasses = interfaceAndClassVisitor.getImplementedClasses();
				for(String s: implementedClasses){
					if(totalImplementedClasses.contains(s) == false)
					totalImplementedClasses.add(s);
				}
				//new InterfaceVisitor().visit(cu,null);
				
				createFile();
			}
			
		}
		getConnections();
	}
	
	public static void createFile(){
		if(cu.getTypes().toString().contains("interface")){
			umlFile.append("Interface " + javaClassName[0] + "{\n");
		}else{
			umlFile.append("Class " + javaClassName[0] + "{\n");	
		}
		for (String fName : fieldNames){
			umlFile.append(fName + "\n");
		}
		umlFile.append("\n");
		for (String fName : constructors){
			umlFile.append(fName + "\n");
		}
		umlFile.append("\n");
		for (String mname : methodNames){
			umlFile.append(mname + "\n");
		}
		umlFile.append("}\n");
		methodNames.clear();
		fieldNames.clear();
		constructors.clear();
	}
	public static void getConnections(){
		for (String iName : totalImplementedClasses){
			umlFile.append(iName + "\n");
		}
		for (String cName : totalSubClassNames){
			umlFile.append(cName + "\n");
		}
		for (String asName: associations){
			umlFile.append(asName + "\n");
		}
		for (String asName: totalDependency){
			String[] ifaces = asName.split(" ");
			if(totalInterfaces.contains(ifaces[0]) == false && totalInterfaces.contains(ifaces[2])){
				//if(classNames.contains(ifaces[0]) == false && classNames.contains(ifaces[2]) == false){
					umlFile.append(asName + "\n");
				//}
			}
		}
		for (String asName1: totalConstructorDependency){
			String[] ifaces = asName1.split(" ");
			if(totalInterfaces.contains(ifaces[0]) == false && totalInterfaces.contains(ifaces[2])){
				//if(classNames.contains(ifaces[0]) == false && classNames.contains(ifaces[2]) == false){
					umlFile.append(asName1 + "\n");
				//}
			}
		}
		umlFile.append("@enduml");
		//System.out.println(umlFile);
	}
	/*private static class InterfaceVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(ClassOrInterfaceDeclaration c, Void arg){
			String interfaceString = null;
			String subClass = null;
			if(c.getImplements()!=null){
				String s =c.getImplements().toString();
				StringTokenizer st = new StringTokenizer(s, " [,]");
				while(st.hasMoreTokens()){
					String token = st.nextToken();
					String interfacetag = token + "<<interface>>";
					implementedClasses.add(interfacetag);
					interfaceString = token + " <|.. " + javaClassName[0];
					implementedClasses.add(interfaceString);
					interfaces.add(token);
				}
			
			}
			if(c.getExtends()!= null){
				subClass = c.getExtends().get(0) + " <|-- " + javaClassName[0];
				subClassNames.add(subClass);
			}
		}
	}*/
	
}
