package nodes;

import code.src.ParserFailureException;
import code.src.Robot;

public class ExpressionNode {
	Integer num = null;
	SensorNode sensor = null;
	String operation = null;
	ExpressionNode expression1 = null, expression2 = null;
	
	public ExpressionNode(int num) {
		this.num = num;
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
		if(this.num != -1) {
			return this.num;
		}else if(this.sensor != null) {
			return this.sensor.evaluate(robot);
		}else if(this.operation != null) {
			if(this.operation.equals("add")) {
				return expression1.evaluate(robot)+expression2.evaluate(robot);
			}else if(this.operation.equals("sub")) {
				return expression1.evaluate(robot)-expression2.evaluate(robot);
			}else if(this.operation.equals("mul")) {
				return expression1.evaluate(robot)*expression2.evaluate(robot);
			}else if(this.operation.equals("div")) {
				return expression1.evaluate(robot)/expression2.evaluate(robot);
			}else {
				throw new ParserFailureException("Operation expected");
			}
		}else {
			throw new ParserFailureException("Can't evaluate expression");
		}
	}
	@Override
	public String toString() {
		if(this.num != null) {
			return this.num.toString();
		}else if(this.sensor != null) {
			return this.sensor.toString();
		}else if(this.operation != null) {
			return this.operation+"("+this.expression1.toString()+","+this.expression2.toString()+")";
		}else {
			throw new ParserFailureException("toString error");
		}
	}

}
