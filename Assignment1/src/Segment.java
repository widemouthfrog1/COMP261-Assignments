import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Segment {
	private static int id = 0;
	private int ID;
	private double length;
	private Node from;
	private Node to;
	private Road road;
	ArrayList<Location> coords;
	Segment(Road road, double length, Node from, Node to, ArrayList<Location> coords){
		this.length = length;
		this.from = from;
		this.to = to;
		this.coords = coords;
		ID = id++;
		this.road = road;
	}
	
	public void draw(Graphics g, Location origin, double scale) {
		for(int i = 0; i < coords.size()-1; i++) {
			Point p1 = coords.get(i).asPoint(origin, scale);
			Point p2 = coords.get(i+1).asPoint(origin, scale);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	public int ID() {
		return ID;
	}
	
	public Road road() {
		return road;
	}
	
	
	public double length() {
		return length;
	}
	
	public Node from() {
		return from.copy();
	}
	
	public Node to() {
		return to.copy();
	}
	
	
	public static ArrayList<Location> doublesToListOfCoords(ArrayList<Double> xys) {
		ArrayList<Location> coords = new ArrayList<Location>();
		while(!xys.isEmpty()) {
			coords.add(Location.newFromLatLon(xys.remove(1), xys.remove(0)));
		}
		return coords;
	}
}
