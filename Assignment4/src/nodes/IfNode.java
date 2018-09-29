package nodes;

import java.util.ArrayList;
import java.util.List;

import code.src.Robot;
import code.src.RobotProgramNode;

public class IfNode implements RobotProgramNode {
	private ConditionNode condition;
	private BlockNode block, elseBlock = null;
	private ArrayList<ConditionNode> elifConditions = new ArrayList<ConditionNode>();
	private ArrayList<BlockNode> elifBlocks = new ArrayList<BlockNode>();
	
	
	public IfNode(ConditionNode condition, BlockNode block, List<ConditionNode> elifConditions, List<BlockNode> elifBlocks, BlockNode elseBlock) {
		this.condition = condition;
		this.block = block;
		this.elifConditions.addAll(elifConditions);
		this.elifBlocks.addAll(elifBlocks);
		this.elseBlock = elseBlock;
	}
	public IfNode(ConditionNode condition, BlockNode block, List<ConditionNode> elifConditions, List<BlockNode> elifBlocks) {
		this.condition = condition;
		this.block = block;
		this.elifConditions.addAll(elifConditions);
		this.elifBlocks.addAll(elifBlocks);
	}

	@Override
	public void execute(Robot robot) {
		if(condition.evaluate(robot)) {
			block.execute(robot);
		}else {
			for(int i = 0; i < this.elifConditions.size();i++) {
				if(this.elifConditions.get(i).evaluate(robot)) {
					this.elifBlocks.get(i).execute(robot);
					return;
				}
			}
			if(elseBlock != null) {
				elseBlock.execute(robot);
			}
		}
		
	}
	
	@Override
	public String toString() {
		String output = "if("+condition+")"+block;
		for(int i = 0; i < this.elifConditions.size(); i++) {
			output += "elif("+this.elifConditions.get(i)+")"+this.elifBlocks.get(i);
		}
		if(this.elseBlock != null) {
			output += "else"+this.elseBlock;
		}
		return output;
	}

}
