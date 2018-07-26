import java.util.HashSet;
import java.util.Set;

public class Road {
	private Set<Segment> segments = new HashSet<Segment>();
	private int ID, speedLimit; //if speedLimit is -1, there is no speed limit
	private String name, roadClass;
	private boolean oneWay, car, pedestrian, bike;//car, pedestrian, and bike are true when these modes of transport CAN use this road
	
	Road(int ID, String name, int oneWay, int speed, int roadClass, int car, int pedestrian, int bike){
		this.ID = ID;
		this.name = name;
		this.oneWay = oneWay == 1 ? true : false;
		this.car = car == 0 ? true : false;
		this.pedestrian = pedestrian == 0 ? true : false;
		this.bike = bike == 0 ? true : false;
		
		switch(speed) {
			case 0: this.speedLimit = 5;
					break;
			case 1: this.speedLimit = 20;
					break;
			case 2: this.speedLimit = 40;
					break;
			case 3: this.speedLimit = 60;
					break;
			case 4: this.speedLimit = 80;
					break;
			case 5: this.speedLimit = 100;
					break;
			case 6: this.speedLimit = 110;
					break;
			case 7: this.speedLimit = -1;
					break;
		}
		switch(roadClass) {
			case 0: this.roadClass = "Residential";
					break;
			case 1: this.roadClass = "Collector";
					break;
			case 2: this.roadClass = "Arterial";
					break;
			case 3: this.roadClass = "Principal HW";
					break;
			case 4: this.roadClass = "Major HW";
					break;
			case -1: this.roadClass = "Invalid";
		}
		
	
	}
	public static int speedID(int speed) {
		switch(speed) {
		case 5:
				return 0;
		case 20:
				return 1;
		case 40:
				return 2;
		case 60:
				return 3;
		case 80:
				return 4;
		case 100:
				return 5;
		case 110:
				return 6;
				
		}
		return 7;
	}
	
	public int ID() {
		return ID;
	}
	
	public int speedLimit() {
		return speedLimit;
	}
	public String name() {
		return name;
	}
	public String roadClass() {
		return roadClass;
	}
	
	public boolean oneWay() {
		return oneWay;
	}
	public boolean car() {
		return car;
	}
	public boolean pedestrian() {
		return pedestrian;
	}
	public boolean bike() {
		return bike;
	}
	
	public Set<Segment> segments(){
		HashSet<Segment> set = new HashSet<Segment>();
		for(Segment segment : this.segments) {
			set.add(segment);
		}
		return set;
	}
	
	public void addSegment(Segment segment) {
		this.segments.add(segment);
	}
	
	public static int classID(String roadClass) {
		switch(roadClass) {
		case "Residential":
				return 0;
		case "Collector":
				return 1;
		case "Arterial":
				return 2;
		case "Principal HW":
				return 3;
		case "Major HW":
				return 4;
				
		}
		return -1;
	}
}
