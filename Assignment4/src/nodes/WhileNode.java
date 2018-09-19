package nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

public class WhileNode implements RobotProgramNode {
	private ConditionNode condition;
	private BlockNode block;
	
	public WhileNode(ConditionNode condition, BlockNode block) {
		this.condition = condition;
		this.block = block;
	}

	@Override
	public void execute(Robot robot) {
		while(condition.evaluate(robot)) {
			block.execute(robot);
		}
		
	}
	
	@Override
	public String toString() {
		return "while("+condition.toString()+")"+block.toString();
	}
}
