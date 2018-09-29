package nodes;
import code.src.Robot;
import code.src.RobotProgramNode;

public class AssignmentNode implements RobotProgramNode{
	VariableNode variable;
	ExpressionNode expression;
	public AssignmentNode(VariableNode variable, ExpressionNode expression){
		this.variable = variable;
		this.expression = expression;
	}

	@Override
	public void execute(Robot robot) {
		this.variable.setValue(expression.evaluate(robot));
	}
	
	@Override
	public String toString() {
		return this.variable+" = "+this.expression;
	}
}
