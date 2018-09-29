package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotSensorNode;

public class SensorNode implements RobotSensorNode {
	private String name;
	private ExpressionNode expression = null;
	public SensorNode(String name, ExpressionNode expression) {
		if(name.equals("fuelLeft")||name.equals("oppLR")||name.equals("oppFB")||name.equals("numBarrels")||name.equals("barrelLR")||name.equals("barrelFB")||name.equals("wallDist")) {
			this.name = name;
			if(this.name.equals("barrelLR")||this.name.equals("barrelFB")) {
				 this.expression = expression;
			}
		}else {
			throw new ParserFailureException("Expected sensor");
		}
	}

	@Override
	public int evaluate(Robot robot) {
		if(name.equals("fuelLeft")) {
			return robot.getFuel();
		}else if(name.equals("oppLR")) {
			return robot.getOpponentLR();
		}else if(name.equals("oppFB")) {
			return robot.getOpponentFB();
		}else if(name.equals("numBarrels")) {
			return robot.numBarrels();
		}else if(name.equals("barrelLR")) {
			if(expression == null) {
				return robot.getClosestBarrelLR();
			}
			return robot.getBarrelLR(expression.evaluate(robot));
		}else if(name.equals("barrelFB")) {
			if(expression == null) {
				return robot.getClosestBarrelFB();
			}
			return robot.getBarrelFB(expression.evaluate(robot));
		}else if(name.equals("wallDist")) {
			return robot.getDistanceToWall();
		}else {
			throw new ParserFailureException("Sensor name invalid");
		}
	}
	
	@Override
	public String toString() {
		if(expression == null) {
			return name;
		}
		return name+"("+expression+")";
	}

}
