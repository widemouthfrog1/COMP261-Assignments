import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextArea;

public class Node {
	private final int NODE_SIZE = 3;
	private int ID;
	private Point pos;
	private Location location;
	private double latitude;
	private double longitude;
	private Set<Segment> outgoingSegments;
	private Set<Segment> incomingSegments;
	private boolean highlight = false;
	
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
	
	public void draw(Graphics g, Location origin, double scale, JTextArea jTextArea){
		if(highlight) {
			g.setColor(Color.red);
			for(Segment s : this.incomingSegments) {
				//jTextArea.setText(s.road().name());
			}
		}
		this.pos = location.asPoint(origin,scale);
		//jTextArea.setText("pos.x: " + pos.x + " pos.y: " + pos.y);
		g.drawOval(this.pos.x, this.pos.y, this.NODE_SIZE, this.NODE_SIZE);
		g.setColor(Color.black);
	}
	
	public Set<Segment> outgoingSegments() {
		HashSet<Segment> set = new HashSet<Segment>();
		for(Segment segment : this.outgoingSegments) {
			set.add(segment);
		}
		return set;
	}
	
	public void highlight() {
		highlight = true;
	}
	
	public void dehighlight() {
		highlight = false;
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
