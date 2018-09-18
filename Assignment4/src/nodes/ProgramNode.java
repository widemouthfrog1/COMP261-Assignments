package nodes;

import java.util.ArrayList;

import code.src.Robot;
import code.src.RobotProgramNode;

public class ProgramNode implements RobotProgramNode {
	private ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
	@SuppressWarnings("unchecked")
	public ProgramNode(ArrayList<StatementNode> statements) {
		this.statements = (ArrayList<StatementNode>)statements.clone();
	}

	@Override
	public void execute(Robot robot) {
		for(StatementNode statement : statements) {
			statement.execute(robot);
		}
	}
	
	@Override
	public String toString() {
		String statements = "";
		for(int i = 0; i < this.statements.size(); i++) {
			statements += this.statements.get(i).toString();
			if(i != this.statements.size()-1) {
				statements += " ";
			}
		}
		return statements;
	}

}
