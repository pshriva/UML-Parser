import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class ConstructorVisitor extends VoidVisitorAdapter<Void>{
	ArrayList<String> constructors = new ArrayList<String>();
	ArrayList<String> constructorDependency = new ArrayList<String>();
	ArrayList<String> classNames = new ArrayList<String>();
	String javaclassName = null;
	
	public void setClassNames(ArrayList<String> classNames) {
		this.classNames = classNames;
	}
	public void setJavaclassName(String javaclassName) {
		this.javaclassName = javaclassName;
	}
	public ArrayList<String> getConstructorDependency() {
		return constructorDependency;
	}
	public ArrayList<String> getConstructors(){
		//System.out.println(constructors);
		return constructors;
	}
	@Override
	public void visit(ConstructorDeclaration n, Void arg) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String method = null;
		String dependent = null;
		if(n.getParameters() != null && (n.getModifiers()==1 || n.getModifiers() == 1025)){
			for(int i =0; i<n.getParameters().size(); i++){
				if(classNames.contains(n.getParameters().get(i).getType().toString())){
					dependent = javaclassName + " ..> " + n.getParameters().get(i).getType().toString();
					//System.out.println(dependent);
					if(constructorDependency.contains(dependent) == false)
						constructorDependency.add(dependent);
				}
				sb.append(n.getParameters().get(i).getId() + " : " + n.getParameters().get(i).getType());
				if(n.getParameters().size()>1 && i != n.getParameters().size()-1)
					sb.append(" ");
			}
			method = "+ " + n.getName() + "(" +sb.toString() + ")";
		}else if(n.getModifiers()==1 || n.getModifiers() == 1025){
			method = "+ " + n.getName() + "()";
		}
		if(method != null){
			constructors.add(method);
		}
	}
	
	

	/*public ArrayList<String> getDependency(ArrayList<String> classNames2, String javaClassName) {
		// TODO Auto-generated method stub
		this.classNames = classNames2;
		this.javaclassName = javaClassName;
		return constructorDependency;
	}*/
	
}
