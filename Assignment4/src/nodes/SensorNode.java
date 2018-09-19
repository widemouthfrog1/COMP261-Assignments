package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotSensorNode;

public class SensorNode implements RobotSensorNode {
	private String name;
	public SensorNode(String name) {
		if(name.equals("fuelLeft")||name.equals("oppLR")||name.equals("oppFB")||name.equals("numBarrels")||name.equals("barrelLR")||name.equals("barrelFB")||name.equals("wallDist")) {
			this.name = name;
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
			return robot.getClosestBarrelLR();
		}else if(name.equals("barrelFB")) {
			return robot.getClosestBarrelFB();
		}else if(name.equals("wallDist")) {
			return robot.getDistanceToWall();
		}else {
			throw new ParserFailureException("Sensor name invalid");
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

}
