import java.util.ArrayList;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Void>{
	ArrayList<String> classNames = new ArrayList<String>();
	static ArrayList<String> associations = new ArrayList<String>();
	ArrayList<String> fieldNames = new ArrayList<String>();
	String javaclassName = null;
	
	public ArrayList<String> getAssociations() {
		/*for(String s : associations){
			System.out.println(s);
		}*/
		return associations;
	}
	
	public ArrayList<String> getFieldNames() {
		return fieldNames;
	}
	public FieldVisitor(ArrayList<String> classNames, String javaclassName) {
		// TODO Auto-generated constructor stub
		//System.out.println("hieeeeeeee "  + javaclassName);
		this.classNames = classNames;
		this.javaclassName = javaclassName;
		
	}

	@Override
	public void visit(FieldDeclaration fd, Void arg){
		/*System.out.println("Class is " + javaclassName);
		System.out.println(fd.getVariables());*/
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
				associate = javaclassName + " -- " + classname;
				reverse = classname + " -- " + javaclassName;
				reverse12 = associate;
				flag = 1;
			}
			else if(fd.getType().toString().contains(arrayTypeObject)){
				associate = javaclassName + " -- \"*\" " +classname;
				reverse = classname + " -- \"*\" " + javaclassName;
				flag = 2;
			}
			reverse1 = classname + " -- " + javaclassName;
		    reverse2 = classname + " -- \"*\" " + javaclassName;
			if(associate != null && associations.contains(associate) == false && associations.contains(reverse1) == false && associations.contains(reverse2) == false){
				associations.add(associate);
			}else if(associate != null && associations.contains(associate) == false && (associations.contains(reverse1) || associations.contains(reverse2))){
				if(associations.contains(reverse1) && flag == 2){
					associations.remove(reverse1);
					allocate = javaclassName + " \"1\" -- \"*\" " +classname;
					associations.add(allocate);
				}else if(associations.contains(reverse2) && flag == 1){
					associations.remove(reverse2);
					allocate = javaclassName + " \"*\" -- \"1\" " +classname;
					associations.add(allocate);
				}else if(associations.contains(reverse2) && flag == 2){
					associations.remove(reverse2);
					allocate = javaclassName + " \"*\" -- \"*\" " +classname;
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
