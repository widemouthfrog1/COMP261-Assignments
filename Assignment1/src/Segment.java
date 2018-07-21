import java.util.ArrayList;

public class Segment {
	private static int id = 0;
	private int ID;
	private Road road;
	private double length;
	private Node from;
	private Node to;
	ArrayList<Location> coords;
	Segment(Road road, double length, Node from, Node to, ArrayList<Location> coords){
		this.road = road;
		this.length = length;
		this.from = from;
		this.to = to;
		this.coords = coords;
		ID = id++;
	}
	
	public int ID() {
		return ID;
	}
	
	public Road road() {
		return new Road(road.ID(), road.name(), road.oneWay() ? 1 : 0, Road.speedID(road.speedLimit()), Road.classID(road.roadClass()), road.car() ? 0:1, road.pedestrian() ? 0:1, road.bike() ? 0:1);
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
