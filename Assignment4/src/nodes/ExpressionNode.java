package nodes;

import code.src.ParserFailureException;
import code.src.Robot;

public class ExpressionNode {
	Integer num = null;
	VariableNode variable = null;
	SensorNode sensor = null;
	String operation = null;
	ExpressionNode expression1 = null, expression2 = null;
	boolean parenthesise = false;//new word? Definition: To surround in parentheses
	
	public ExpressionNode(int num) {
		this.num = num;
	}
	
	public ExpressionNode(VariableNode variable) {
		this.variable = variable;
	}
	
	public ExpressionNode(ExpressionNode expression) {
		this.expression1 = expression;
		this.parenthesise = true;
	}
	
	public ExpressionNode(SensorNode sensor) {
		this.sensor = sensor;
	}
	public ExpressionNode(String operation, ExpressionNode expression1, ExpressionNode expression2) {
		this.operation = operation;
		this.expression1 = expression1;
		this.expression2 = expression2;
	}
	
	public int evaluate(Robot robot) {
		if(this.num != null) {
			return this.num;
		}else if(this.variable != null) {
			return this.variable.evaluate(robot);
		}else if(this.sensor != null) {
			return this.sensor.evaluate(robot);
		}else if(this.operation != null) {
			if(this.operation.equals("add")||this.operation.equals("+")) {
				return expression1.evaluate(robot)+expression2.evaluate(robot);
			}else if(this.operation.equals("sub")||this.operation.equals("-")) {
				return expression1.evaluate(robot)-expression2.evaluate(robot);
			}else if(this.operation.equals("mul")||this.operation.equals("*")) {
				return expression1.evaluate(robot)*expression2.evaluate(robot);
			}else if(this.operation.equals("div")||this.operation.equals("/")) {
				return expression1.evaluate(robot)/expression2.evaluate(robot);
			}else {
				throw new ParserFailureException("Operation expected");
			}
		}else if(this.parenthesise) {
			return this.expression1.evaluate(robot);
		}else {
			throw new ParserFailureException("Can't evaluate expression");
		}
	}
	@Override
	public String toString() {
		if(this.num != null) {
			return this.num.toString();
		}else if(this.variable != null){
			return this.variable.toString();
		}else if(this.sensor != null) {
			return this.sensor.toString();
		}else if(this.operation != null) {
			if(this.operation.length() == 1) {
				return this.expression1+this.operation+this.expression2;
			}
			return this.operation+"("+this.expression1+","+this.expression2+")";
		}else if(this.parenthesise) {
			return "("+this.expression1+")";
		}else {
			throw new ParserFailureException("toString error");
		}
	}

}
