package nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

public class ActionNode implements RobotProgramNode {
	private enum Action {MOVE, TURNLEFT, TURNRIGHT, TAKEFUEL, WAIT}
	private Action action = null;
	
	public ActionNode(String action) {
		if(action.equals("move")) {
			this.action = Action.MOVE;
		}else if(action.equals("turnL")) {
			this.action = Action.TURNLEFT;
		}else if(action.equals("turnR")) {
			this.action = Action.TURNRIGHT;
		}else if(action.equals("takeFuel")) {
			this.action = Action.TAKEFUEL;
		}else if(action.equals("wait")) {
			this.action = Action.WAIT;
		}else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	@Override
	public void execute(Robot robot) {
		switch(action) {
		case MOVE:
			robot.move();
			break;
		case TAKEFUEL:
			robot.takeFuel();
			break;
		case TURNLEFT:
			robot.turnLeft();
			break;
		case TURNRIGHT:
			robot.turnRight();
			break;
		case WAIT:
			robot.idleWait();
			break;
		default:
			break;
		
		}
	}
	
	@Override
	public String toString() {
		switch(action) {
		case MOVE:
			return "move";
		case TAKEFUEL:
			return "takeFuel";
		case TURNLEFT:
			return "turnL";
		case TURNRIGHT:
			return "turnR";
		case WAIT:
			return "wait";
		default:
			return "Error";
		}
	}

}
