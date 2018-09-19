package nodes;

import java.util.ArrayList;

import code.src.Robot;
import code.src.RobotProgramNode;

public class BlockNode implements RobotProgramNode {
	ArrayList<StatementNode> statements;
	public BlockNode(ArrayList<StatementNode> statements){
		this.statements = statements;
		if(this.statements.isEmpty()) {
			throw new java.lang.RuntimeException("A block requires at least 1 statement");
		}
	}
	@Override
	public void execute(Robot robot) {
		for(StatementNode statement : statements) {
			statement.execute(robot);
		}
		
	}
	
	@Override
	public String toString() {
		String statements = "{\n";
		for(int i = 0; i < this.statements.size(); i++) {
			statements += "\n\t";
			statements += this.statements.get(i).toString();
			if(i != this.statements.size()-1) {
				statements += " ";
			}
		}
		statements += "\n}";
		return statements;
	}

}
