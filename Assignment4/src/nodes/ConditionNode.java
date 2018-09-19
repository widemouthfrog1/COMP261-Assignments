package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotConditionNode;

public class ConditionNode implements RobotConditionNode {
	private String name;
	private SensorNode sensor;
	private int number;
	public ConditionNode(String name, SensorNode sensor, int number) {
		this.name = name;
		this.sensor = sensor;
		this.number = number;
	}
	@Override
	public boolean evaluate(Robot robot) {
		if(this.name.equals("eq")) {
			return sensor.evaluate(robot) == number;
		}else if(this.name.equals("lt")) {
			return sensor.evaluate(robot) < number;
		}else if(this.name.equals("gt")) {
			return sensor.evaluate(robot) > number;
		}
		throw new ParserFailureException("Condition expects eq, lt, or gt");
	}
	
	@Override
	public String toString() {
		return this.name+"("+this.sensor+", "+this.number+")";
	}

}
