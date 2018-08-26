import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Node {
	private final int NODE_SIZE = 4;
	public static final double MAXSPEED = 110;
	private int ID;
	private Point pos;
	private Location location;
	private double latitude;
	private double longitude;
	private Set<Segment> outgoingSegments;
	private Set<Segment> incomingSegments;
	private boolean highlightGoal = false;
	private boolean highlightStart = false;
	private boolean visited = false;
	private Node previous = null;
	private Integer count = null;
	
	
	Node(int ID, double latitude, double longitude){
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = Location.newFromLatLon(latitude, longitude);
		this.outgoingSegments = new HashSet<Segment>();
		this.incomingSegments = new HashSet<Segment>();
	}
	/**
	 * Add segments connected to node
	 * @param segment
	 * @param mode
	 * 			Either "Outgoing", "Incoming", or "Both"
	 */
	public void addSegment(Segment segment, String mode) {
		switch(mode) {
		case "Outgoing":
			outgoingSegments.add(segment);
			break;
		case "Incoming":
			incomingSegments.add(segment);
			break;
		case "Both":
			outgoingSegments.add(segment);
			incomingSegments.add(segment);
		}
	}
	
	public void draw(Graphics g, Location origin, double scale){
		if(highlightStart) {
			g.setColor(Color.red);
		}
		if(highlightGoal) {
			g.setColor(Color.magenta);
		}
		this.pos = location.asPoint(origin,scale);
		g.fillOval(this.pos.x, this.pos.y, this.NODE_SIZE, this.NODE_SIZE);
		g.setColor(Color.black);
	}
	
	public void drawArticulationPoint(Graphics g, Location origin, double scale) {
		g.setColor(Color.red);
		this.pos = location.asPoint(origin,scale);
		g.drawOval(this.pos.x, this.pos.y, this.NODE_SIZE+1, this.NODE_SIZE+1);
		g.setColor(Color.black);
	}
	
	public Set<Segment> outgoingSegments() {
		HashSet<Segment> set = new HashSet<Segment>();
		for(Segment segment : this.outgoingSegments) {
			set.add(segment);
		}
		return set;
	}
	
	public void highlightStart() {
		highlightStart = true;
		dehighlightGoal();
	}
	public void highlightGoal() {
		highlightGoal = true;
		dehighlightStart();
	}
	
	public void dehighlightStart() {
		highlightStart = false;
	}
	public void dehighlightGoal() {
		highlightGoal = false;
	}
	
	public Set<Segment> incomingSegments() {
		HashSet<Segment> set = new HashSet<Segment>();
		for(Segment segment : this.incomingSegments) {
			set.add(segment);
		}
		return set;
	}
	
	public int ID() {
		return ID;
	}
	public Point pos() {
		return new Point(pos.x, pos.y);
	}
	
	public double getEstimatedCost(Node goal, boolean heuristicIsDistance) {
		if(heuristicIsDistance) {
			return this.location.distance(goal.location());
		}
		//time = distance/velocity
		return this.location.distance(goal.location())/MAXSPEED ;
	}
	
	public boolean visited() {
		return visited;
	}
	public void visited(boolean visited) {
		this.visited = visited;
	}
	
	public Node previous() {
		return this.previous;
	}
	
	public void previous(Node previous) {
		this.previous = previous;
	}
	
	public Integer count() {
		return count;
	}
	
	public void count(Integer count) {
		this.count = count;
	}
	
	public ArrayList<Node> getNeighbours() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		for(Segment segment : this.incomingSegments()) {
			if(segment.from().ID() == this.ID) {
				nodes.add(segment.to());
			}else {
				nodes.add(segment.from());
			}
		}
		for(Segment segment : this.outgoingSegments()) {
			if(segment.from().ID() == this.ID) {
				nodes.add(segment.to());
			}else {
				nodes.add(segment.from());
			}
		}
		return nodes;
	}
	
	public Location location() {
		return location;
	}
	public Node copy() {
		return new Node(this.ID, this.latitude, this.longitude);
	}
	public String toString() {
		return this.incomingSegments.toString();
	}
}
