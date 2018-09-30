package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotConditionNode;

public class ConditionNode implements RobotConditionNode {
	private ExpressionNode expression1 = null, expression2 = null;
	private ConditionNode condition1 = null, condition2 = null;
	private String name = null;
	private Boolean bool = null;
	boolean parenthesise = false;//new word? Definition: To surround in parentheses
	public ConditionNode(String name, ExpressionNode expression1, ExpressionNode expression2) {
		this.name = name;
		this.expression1 = expression1;
		this.expression2 = expression2;
	}
	
	public ConditionNode(String name, ConditionNode condition1, ConditionNode condition2) {
		this.name = name;
		this.condition1 = condition1;
		this.condition2 = condition2;
	}
	public ConditionNode(String name, ConditionNode condition1) {
		this.name = name;
		this.condition1 = condition1;
	}
	public ConditionNode(boolean bool) {
		this.bool = bool;
	}
	public ConditionNode(ConditionNode condition) {
		this.condition1 = condition;
		this.parenthesise = true;
	}
	@Override
	public boolean evaluate(Robot robot) {
		if(this.bool != null) {
			return this.bool;
		}else if(this.parenthesise) {
			return this.condition1.evaluate(robot);
		}else if(this.name.equals("gt") || this.name.equals(">")) {
			return expression1.evaluate(robot) > expression2.evaluate(robot);
		}else if(this.name.equals("lt")|| this.name.equals("<")) {
			return expression1.evaluate(robot) < expression2.evaluate(robot);
		}else if(this.name.equals("eq")|| this.name.equals("==")) {
			return expression1.evaluate(robot) == expression2.evaluate(robot);
		}else if(this.name.equals("and")|| this.name.equals("&&")) {
			return condition1.evaluate(robot) && condition2.evaluate(robot);
		}else if(this.name.equals("or")|| this.name.equals("\\|\\|")) {
			return condition1.evaluate(robot) || condition2.evaluate(robot);
		}else if(this.name.equals("not")|| this.name.equals("!")) {
			return !condition1.evaluate(robot);
		}else if(this.name.equals(">=")) {
			return expression1.evaluate(robot) >= expression2.evaluate(robot);
		}else if(this.name.equals("<=")) {
			return expression1.evaluate(robot) <= expression2.evaluate(robot);
		}else if(this.name.equals("!=")) {
			return expression1.evaluate(robot) != expression2.evaluate(robot);
		}else{
			throw new ParserFailureException("Relational operator expected (ConditionNode.evaluate)");
		}
	}
	
	@Override
	public String toString() {
		if(this.name != null) {
			if(this.name.equals(">")||this.name.equals("<")||this.name.equals("<=")||this.name.equals(">=")||this.name.equals("!=")||this.name.equals("==")) {
				return this.expression1+this.name+this.expression2;
			}else if(this.name.equals("&&")||this.name.equals("||")) {
				return this.condition1+this.name+this.condition2;
			}else if(this.name.equals("!")) {
				return this.name+this.condition1;
			}
		}
		if(this.parenthesise) {
			return "("+this.condition1+")";
		}else if(expression1 != null) {
			return this.name+"("+this.expression1.toString()+", "+this.expression2.toString()+")";
		}else if(this.condition2 != null) {
			return this.name+"("+this.condition1.toString()+", "+this.condition2.toString()+")";
		}else if(this.bool != null) {
			return this.bool.toString();
		}else {
			return this.name+"("+this.condition1.toString()+")";
		}
	}

}
