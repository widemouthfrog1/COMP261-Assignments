import java.util.HashSet;
import java.util.Set;

public class Node {
	private int ID;
	private Location pos;
	private double latitude;
	private double longitude;
	private Set<Segment> outgoingSegments;
	private Set<Segment> incomingSegments;
	Node(int ID, double latitude, double longitude){
		this.ID = ID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pos = Location.newFromLatLon(latitude, longitude);
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
	public Location pos() {
		return new Location(pos.x, pos.y);
	}
	public Node copy() {
		return new Node(this.ID, this.latitude, this.longitude);
	}
}
