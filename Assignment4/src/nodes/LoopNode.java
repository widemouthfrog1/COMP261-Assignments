package nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

public class LoopNode implements RobotProgramNode {
	private BlockNode block;
	public LoopNode(BlockNode block) {
		this.block = block;
	}
	@Override
	public void execute(Robot robot) {
		while(true) {
			block.execute(robot);
		}
	}
	@Override
	public String toString() {
		return "loop"+block.toString();
	}

}
