package nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

public class StatementNode implements RobotProgramNode {
	private RobotProgramNode node;
	
	public StatementNode(RobotProgramNode node) {
		this.node = node;
	}
	
	@Override
	public void execute(Robot robot) {
		node.execute(robot);
	}
	
	@Override
	public String toString() {
		if(node instanceof ActionNode) {
			return node.toString()+";";
		}else if(node instanceof LoopNode || node instanceof IfNode || node instanceof WhileNode){
			return node.toString();
		}else {
			return "Node Error";
		}
	}

}
