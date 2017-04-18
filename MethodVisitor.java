import java.util.ArrayList;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class MethodVisitor extends VoidVisitorAdapter<Void>{
	ArrayList<String> classNames = new ArrayList<String>();
	ArrayList<String> fieldNames = new ArrayList<String>();
	ArrayList<String> dependency = new ArrayList<String>();
	ArrayList<String> methodNames = new ArrayList<String>();
	String javaclassName = null;
	public ArrayList<String> getDependency() {
		return dependency;
	}
	public ArrayList<String> getMethodNames() {
		return methodNames;
	}
	public MethodVisitor(ArrayList<String> classNames, String javaclassName, ArrayList<String> fieldNames) {
		// TODO Auto-generated constructor stub
		this.classNames = classNames;
		this.javaclassName = javaclassName;
		this.fieldNames = fieldNames;
	}
	@Override
	public void visit(MethodDeclaration m, Void arg) {
		// TODO Auto-generated method stub
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
					dependent = javaclassName + " ..> " + m.getParameters().get(i).getType().toString();
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
							if(reference[0].toString().contains(classname) && dependency.contains(javaclassName + " ..> " + classname) == false){
								dependency.add(javaclassName + " ..> " + classname);
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
	}

