# UML-Parser
The project is designed to convert a Java Source Code into a UML Class Diagram

Working on the structure and finding methods of JavaParser.
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
	}
	private static void parseFiles(String inputfilepath2) {
		// TODO Auto-generated method stub
		File file = new File(inputfilepath);
		File[] files = file.listFiles();
		for(File f: files){
			if(f.getName().contains(".java")) {
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
			}
		}
	}
	private static class InterfaceVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(ClassOrInterfaceDeclaration c, Void arg){
			if(c.getImplements()!=null){
				String s =c.getImplements().toString();
				StringTokenizer st = new StringTokenizer(s, " [,]");
				while(st.hasMoreTokens()){
					interfaceNames.add(st.nextToken());
				}
			
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
			System.out.println("hello");
		}
	}

}
