import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Node {
	private final int PIXEL_DISTANCE = 100;
	private final int NODE_SIZE = 3;
	private int ID;
	private Point pos;
	private double latitude;
	private double longitude;
	private Set<Segment> outgoingSegments;
	private Set<Segment> incomingSegments;
	private double scale = 100;
	private Location origin = new Location(0,0);
	
	Node(int ID, double latitude, double longitude){
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pos = Location.newFromLatLon(latitude, longitude).asPoint(origin, scale);
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
	
	public void draw(Graphics g){
		g.drawOval(this.pos.x, this.pos.y, this.NODE_SIZE, this.NODE_SIZE);
	}
	
	public Set<Segment> outgoingSegments() {
		HashSet<Segment> set = new HashSet<Segment>();
		for(Segment segment : this.outgoingSegments) {
			set.add(segment);
		}
		return set;
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
	
	public void moveUp() {
		this.pos.y += this.PIXEL_DISTANCE;
	}
	public void moveDown() {
		this.pos.y -= PIXEL_DISTANCE;
	}
	public void moveLeft() {
		this.pos.x -= PIXEL_DISTANCE;
	}
	public void moveRight() {
		this.pos.x += PIXEL_DISTANCE;
	}
	public Node copy() {
		return new Node(this.ID, this.latitude, this.longitude);
	}
	public void update(double scale, Location origin) {
		this.pos = Location.newFromLatLon(latitude, longitude).asPoint(origin,scale);
	}
}
