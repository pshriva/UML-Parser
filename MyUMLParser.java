import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class ConstructorVisitor extends VoidVisitorAdapter<Void>{
	static ArrayList<String> constructors = new ArrayList<String>();
	static ArrayList<String> constructorDependency = new ArrayList<String>();
	@Override
	public void visit(ConstructorDeclaration n, Void arg) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String method = null;
		if(n.getParameters() != null && (n.getModifiers()==1 || n.getModifiers() == 1025)){
			for(int i =0; i<n.getParameters().size(); i++){
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
	
	public ArrayList<String> getConstructors(){
		//System.out.println(constructors);
		return constructors;
	}

	public ArrayList<String> getDependency() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
