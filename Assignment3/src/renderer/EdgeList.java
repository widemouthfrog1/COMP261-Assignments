package renderer;

import java.util.HashMap;
import java.util.Map;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	private int startY, endY;
	private Map<Integer, float[]> xz = new HashMap<Integer, float[]>(); 
	public EdgeList(int startY, int endY) {
		// TODO fill this in.
		this.startY = startY;
		this.endY = endY;
	}
	
	public void addRow(int y, float xLeft, float xRight, float zLeft, float zRight) {
		xz.put(y, new float[] {xLeft, xRight, zLeft, zRight});
	}

	public int getStartY() {
		return startY;
	}

	public int getEndY() {
		return endY;
	}

	public float getLeftX(int y) {
		return xz.get(y-this.startY)[0];
	}

	public float getRightX(int y) {
		return xz.get(y-this.startY)[1];
	}

	public float getLeftZ(int y) {
		return xz.get(y-this.startY)[2];
	}

	public float getRightZ(int y) {
		return xz.get(y-this.startY)[3];
	}
}

// code for comp261 assignments
