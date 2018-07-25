import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Node {
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
	public void zoomIn(Dimension dimentions) {
		double zoom = 2;
		scale *= zoom;
		origin = Location.newFromPoint(new Point(origin.asPoint(origin, scale/zoom).x + ((int)(dimentions.getWidth()) - (int)(dimentions.getWidth()/zoom))/2, origin.asPoint(origin, scale/zoom).y+ ((int)(dimentions.getHeight()) - (int)(dimentions.getHeight()/zoom))/2), origin, scale/zoom);
		this.pos = Location.newFromLatLon(latitude, longitude).asPoint(origin,scale);
	}
	public void zoomOut(Dimension dimentions) {
		double zoom = 2;
		scale /= zoom;	
		origin = Location.newFromPoint(
				new Point(origin.asPoint(origin, scale*zoom).x + ((int)(dimentions.getWidth()) - (int)(dimentions.getWidth()*zoom))/2, origin.asPoint(origin, scale*zoom).y + ((int)(dimentions.getHeight()) - (int)(dimentions.getHeight()*zoom))/2), 
				origin, 
				scale*zoom);
		this.pos = Location.newFromLatLon(latitude, longitude).asPoint(origin,scale);
	}
	public Node copy() {
		return new Node(this.ID, this.latitude, this.longitude);
	}
}
