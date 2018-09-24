package nodes;

import code.src.ParserFailureException;
import code.src.Robot;
import code.src.RobotProgramNode;

public class ActionNode implements RobotProgramNode {
	private enum Action {MOVE, TURNLEFT, TURNRIGHT, TURNAROUND, SHIELDON, SHIELDOFF, TAKEFUEL, WAIT}
	private Action action = null;
	private ExpressionNode steps = null;
	
	public ActionNode(String action) {
		if(action.equals("turnL")) {
			this.action = Action.TURNLEFT;
		}else if(action.equals("turnR")) {
			this.action = Action.TURNRIGHT;
		}else if(action.equals("turnAround")) {
			this.action = Action.TURNAROUND;
		}else if(action.equals("shieldOn")) {
			this.action = Action.SHIELDON;
		}else if(action.equals("shieldOff")) {
			this.action = Action.SHIELDOFF;
		}else if(action.equals("takeFuel")) {
			this.action = Action.TAKEFUEL;
		}else if(action.equals("move")){
			this.action = Action.MOVE;
		}else if(action.equals("wait")){
			this.action = Action.WAIT;
		}else {
			throw new ParserFailureException("Action expected found: "+action);
		}
	}
	
	public ActionNode(String action, ExpressionNode steps) {
		if(action.equals("move")) {
			this.action = Action.MOVE;
		}else if(action.equals("wait")) {
			this.action = Action.WAIT;
		}else {
			throw new ParserFailureException("move or wait expected");
		}
		this.steps = steps;
	}

	@Override
	public void execute(Robot robot) {
		switch(action) {
		case MOVE:
			if(steps == null) {
				robot.move();
			}else {
				for(int i = 0; i < steps.evaluate(robot); i++) {
					robot.move();
				}
			}
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
		case TURNAROUND:
			robot.turnAround();
			break;
		case WAIT:
			if(steps == null) {
				robot.idleWait();
			}else {
				for(int i = 0; i < steps.evaluate(robot); i++) {
					robot.idleWait();
				}
			}
			break;
		case SHIELDOFF:
			robot.setShield(true);
			break;
		case SHIELDON:
			robot.setShield(false);
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
		case SHIELDOFF:
			return "shieldOff";
		case SHIELDON:
			return "shieldOn";
		case TURNAROUND:
			return "turnAround";
		default:
			return "Error";
			
		}
	}

}
