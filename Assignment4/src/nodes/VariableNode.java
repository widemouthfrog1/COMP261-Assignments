package nodes;

import code.src.Robot;

public class VariableNode {
	private int value = 0;
	private String name;
	public VariableNode(String name){
		this.name = name;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int evaluate(Robot robot) {
		return this.value;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
