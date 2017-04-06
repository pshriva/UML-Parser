import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
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
	static StringBuffer umlFile = new StringBuffer();
	static CompilationUnit cu;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		inputfilepath = "F:/user/SJSU/202/PersonalProject/Samples";
		parseFiles(inputfilepath);
		for(String s:interfaceNames){
			System.out.println(s);
		}
		for(String s:methodNames){
			System.out.println(s);
		}
		for(String s:fieldNames){
			System.out.println(s);
		}
		for(String s:subClassNames){
			System.out.println(s);
		}
		System.out.println(umlFile);
	}
	private static void parseFiles(String inputfilepath2) {
		// TODO Auto-generated method stub
		File file = new File(inputfilepath);
		File[] files = file.listFiles();
		umlFile.append("@startuml\n");
		for(File f: files){
			if(f.getName().contains(".java")){
				javaClassName = f.getName().split("\\.");
				classNames.add(javaClassName[0]);
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
		umlFile.toString();
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
					interfaces = javaClassName[0] + " <|.. " + st.nextToken();
					interfaceNames.add(interfaces);
				}
			
			}
			if(c.getExtends()!= null){
				subClass = javaClassName[0] + " <|-- " + c.getExtends().get(0);
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
			if(fd.getModifiers()==1){
				field = "+ " + fd.getVariables().get(0) + " : " + fd.getType();
				fieldNames.add(field);
			}else if(fd.getModifiers()==2){
				field = "- " + fd.getVariables().get(0) + " : " + fd.getType();
				fieldNames.add(field);
			}
		}
	}

}
