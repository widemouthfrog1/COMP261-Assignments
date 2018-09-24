package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotConditionNode;

public class ConditionNode implements RobotConditionNode {
	private ExpressionNode expression1, expression2;
	private ConditionNode condition1, condition2;
	private String name;
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
	@Override
	public boolean evaluate(Robot robot) {
		if(this.name.equals("gt")) {
			return expression1.evaluate(robot) > expression2.evaluate(robot);
		}else if(this.name.equals("lt")) {
			return expression1.evaluate(robot) < expression2.evaluate(robot);
		}else if(this.name.equals("eq")) {
			return expression1.evaluate(robot) == expression2.evaluate(robot);
		}else if(this.name.equals("and")) {
			return condition1.evaluate(robot) && condition2.evaluate(robot);
		}else if(this.name.equals("or")) {
			return condition1.evaluate(robot) || condition2.evaluate(robot);
		}else if(this.name.equals("not")) {
			return !condition1.evaluate(robot);
		}else {
			throw new ParserFailureException("Relational operator expected (ConditionNode.evaluate)");
		}
	}
	
	@Override
	public String toString() {
		if(expression1 != null) {
			return this.name+"("+this.expression1.toString()+", "+this.expression2.toString()+")";
		}else if(this.condition2 != null) {
			return this.name+"("+this.condition1.toString()+", "+this.condition2.toString()+")";
		}else {
			return this.name+"("+this.condition1.toString()+")";
		}
	}

}
