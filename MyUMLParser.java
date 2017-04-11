import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
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
	static ArrayList<String> interfaceNames = new ArrayList<String>();
	static ArrayList<String> methodNames = new ArrayList<String>();
	static ArrayList<String> fieldNames = new ArrayList<String>();
	static ArrayList<String> subClassNames = new ArrayList<String>();
	static ArrayList<String> associations = new ArrayList<String>();
	static StringBuffer umlFile = new StringBuffer();
	static CompilationUnit cu;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		inputfilepath = "F:/user/SJSU/202/PersonalProject/Samples/testcase2";
		File file = new File(inputfilepath);
		UMLGenerator umlgenerator = new UMLGenerator();
		classNames = getJavaClasses(file);
		parseFiles(file);
		/*for(String s:interfaceNames){
			System.out.println(s);
		} */
		/*for(String s:classTypeObjects){
			System.out.println(s);
		} */
		
		for(String s:associations){
			System.out.println(s);
		}
		System.out.println(umlFile);
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

				new MethodVisitor().visit(cu,null);
				new InterfaceVisitor().visit(cu,null);
				new FieldVisitor().visit(cu,null);
				createFile();
			}
		}
		getConnections();
	}
	
	public static void createFile(){
		umlFile.append("Class " + javaClassName[0] + "{\n");
		for (String fName : fieldNames){
			umlFile.append(fName + "\n");
		}
		umlFile.append("\n");
		for (String mname : methodNames){
			umlFile.append(mname + "\n");
		}
		umlFile.append("}\n");
		methodNames.clear();
		fieldNames.clear();
	}
	public static void getConnections(){
		for (String iName : interfaceNames){
			umlFile.append(iName + "\n");
		}
		for (String cName : subClassNames){
			umlFile.append(cName + "\n");
		}
		for (String asName: associations){
			umlFile.append(asName + "\n");
		}
		umlFile.append("@enduml");
	}
	private static class InterfaceVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(ClassOrInterfaceDeclaration c, Void arg){
			String interfaces = null;
			String subClass = null;
			if(c.getImplements()!=null){
				String s =c.getImplements().toString();
				StringTokenizer st = new StringTokenizer(s, " [,]");
				while(st.hasMoreTokens()){
					interfaces = st.nextToken() + " <|.. " + javaClassName[0];
					interfaceNames.add(interfaces);
				}
			
			}
			if(c.getExtends()!= null){
				subClass = c.getExtends().get(0) + " <|-- " + javaClassName[0];
				subClassNames.add(subClass);
			}
		}
	}
	private static class MethodVisitor extends VoidVisitorAdapter<Void> {  
		@Override
		public void visit(MethodDeclaration m, Void arg) {
			String method = null;
			StringBuffer sb = new StringBuffer();
			if(m.getParameters()!= null && m.getModifiers()==1){
				for(int i = 0; i<m.getParameters().size();i++){
					sb.append(m.getParameters().get(i).getId() + " : " + m.getParameters().get(i).getType());
					if(m.getParameters().size()>1 && i != m.getParameters().size()-1)
						sb.append(" ");
				}
			    method = "+ " + m.getName() + "(" +sb.toString() + ") : " + m.getType();
			    methodNames.add(method);
			} else if(m.getModifiers()==1){
				method = "+ " + m.getName() + "() : " + m.getType();
				methodNames.add(method);
			}
		
	}


}
	private static class FieldVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(FieldDeclaration fd, Void arg){
			String field = null;
			String arrayTypeObject = null;
			// looks for if a class contains objects of some other class
			for(String classname : classNames){
				String associate = null;
				String reverse1 = null;
				String reverse2 = null;
				String reverse12 = null;
				String reverse = null;
				String allocate = null;
				int flag = 0;
				arrayTypeObject = "<" + classname + ">";
				if(fd.getType().toString().equals(classname)){
					associate = javaClassName[0] + " -- " + classname;
					reverse = classname + " -- " + javaClassName[0];
					reverse12 = associate;
					System.out.println("reverse is : " + reverse);
					flag = 1;
				}
				else if(fd.getType().toString().contains(arrayTypeObject)){
					associate = javaClassName[0] + " -- \"*\" " +classname;
					reverse = classname + " -- \"*\" " + javaClassName[0];
					System.out.println("reverse is : " +reverse);
					flag = 2;
				}
				reverse1 = classname + " -- " + javaClassName[0];
			    reverse2 = classname + " -- \"*\" " + javaClassName[0];
				if(associate != null && associations.contains(associate) == false && associations.contains(reverse1) == false && associations.contains(reverse2) == false){
					System.out.println("I am here");
					associations.add(associate);
				}else if(associate != null && associations.contains(associate) == false && (associations.contains(reverse1) || associations.contains(reverse2))){
					if(associations.contains(reverse1) && flag == 2){
						associations.remove(reverse1);
						allocate = javaClassName[0] + " \"1\" -- \"*\" " +classname;
						associations.add(allocate);
					}else if(associations.contains(reverse2) && flag == 1){
						associations.remove(reverse2);
						allocate = javaClassName[0] + " \"*\" -- \"1\" " +classname;
						associations.add(allocate);
					}else if(associations.contains(reverse2) && flag == 2){
						associations.remove(reverse2);
						allocate = javaClassName[0] + " \"*\" -- \"*\" " +classname;
						associations.add(allocate);
					}
				}
			}
			if(fd.getModifiers()==1 && classNames.contains(fd.getType().toString()) == false && fd.getType().toString().contains("<") == false){
				field = "+ " + fd.getVariables().get(0) + " : " + fd.getType();
				fieldNames.add(field);
			}else if(fd.getModifiers()==2 && classNames.contains(fd.getType().toString()) == false && fd.getType().toString().contains("<")==false){
				field = "- " + fd.getVariables().get(0) + " : " + fd.getType();
				fieldNames.add(field);
			}
		}
	}
}
