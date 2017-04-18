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
	/*private static class MethodVisitor extends VoidVisitorAdapter<Void> {  
		@Override
		public void visit(MethodDeclaration m, Void arg) {
			String method = null;
			StringBuffer sb = new StringBuffer();
			String dependent = null;
			// to check if the method is a getter setter type
			//boolean isGetterSetter = false;
			String field = null;
			field = getGetterSetterStatus(m);
			if(field != null){
				String[] fieldTokens = field.split(" ");
				fieldNames.remove(field);
				fieldNames.add("+ " + fieldTokens[1] + " " + fieldTokens[2] + " " + fieldTokens[3]);
			}
			if(m.getParameters()!= null && (m.getModifiers()==1 || m.getModifiers() == 1025 || m.getModifiers() == 9)){
				for(int i = 0; i<m.getParameters().size();i++){
					if(classNames.contains(m.getParameters().get(i).getType().toString())){
						dependent = javaClassName[0] + " ..> " + m.getParameters().get(i).getType().toString();
						if(dependency.contains(dependent) == false)
						dependency.add(dependent);
					}
					sb.append(m.getParameters().get(i).getId() + " : " + m.getParameters().get(i).getType());
					if(m.getParameters().size()>1 && i != m.getParameters().size()-1)
						sb.append(" ");
				}
				if(m.getModifiers() == 9){
					method = "+ " + m.getName() + "(" +sb.toString() + ") : " + m.getType() + "{static}";
				}else{
				    method = "+ " + m.getName() + "(" +sb.toString() + ") : " + m.getType();
				}
			} else if(m.getModifiers()==1 || m.getModifiers() == 1025){
				method = "+ " + m.getName() + "() : " + m.getType();
			} else if(m.getModifiers() == 9){
				method = "+ " + m.getName() + "(" +sb.toString() + ") : " + m.getType() + "{static}";
			}
			if(method != null && field == null){
				methodNames.add(method);
			}
			if(m.getBody() != null){
				if(m.getBody().getStmts() != null){
					for(int i = 0; i < m.getBody().getStmts().size(); i++){
						String statement = m.getBody().getStmts().get(i).toString();
						if(statement.contains(" = new") || statement.contains(" =new")){
							String[] reference = statement.split("=");
							for(String classname : classNames){
								if(reference[0].toString().contains(classname) && dependency.contains(javaClassName[0] + " ..> " + classname) == false){
									dependency.add(javaClassName[0] + " ..> " + classname);
								}
							}
						}
					}
				}
			}
	}

		private String getGetterSetterStatus(MethodDeclaration m) {
			// TODO Auto-generated method stub
			String field = null;
			for(String fieldname : fieldNames){
				String[] attribute = fieldname.split(" ");
				if(m.getName().equalsIgnoreCase("get" + attribute[1]) || m.getName().equalsIgnoreCase("set" + attribute[1])){
					field = fieldname; 
				}
			} 
			return field;
			
		}


}*/
	/*private static class FieldVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public void visit(FieldDeclaration fd, Void arg){
			System.out.println("This is class " + javaClassName[0]);
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
					flag = 1;
				}
				else if(fd.getType().toString().contains(arrayTypeObject)){
					associate = javaClassName[0] + " -- \"*\" " +classname;
					reverse = classname + " -- \"*\" " + javaClassName[0];
					flag = 2;
				}
				reverse1 = classname + " -- " + javaClassName[0];
			    reverse2 = classname + " -- \"*\" " + javaClassName[0];
				if(associate != null && associations.contains(associate) == false && associations.contains(reverse1) == false && associations.contains(reverse2) == false){
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
			for(String s : associations){
				System.out.println(s);
			}
		}
	}*/
}
