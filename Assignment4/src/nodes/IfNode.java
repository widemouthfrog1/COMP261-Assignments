package nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

public class IfNode implements RobotProgramNode {
	private ConditionNode condition;
	private BlockNode block, elseBlock = null;
	
	public IfNode(ConditionNode condition, BlockNode block, BlockNode elseBlock) {
		this.condition = condition;
		this.block = block;
		this.elseBlock = elseBlock;
	}
	public IfNode(ConditionNode condition, BlockNode block) {
		this.condition = condition;
		this.block = block;
	}

	@Override
	public void execute(Robot robot) {
		if(condition.evaluate(robot)) {
			block.execute(robot);
		}else {
			if(elseBlock != null) {
				elseBlock.execute(robot);
			}
		}
		
	}
	
	@Override
	public String toString() {
		return "if("+condition.toString()+")"+block.toString();
	}

}
