import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoadMap extends GUI {
	private final int PIXEL_DISTANCE = 100;

	ArrayList<Node> nodes = new ArrayList<Node>();
	Node highlightedNode = null;
	QuadTree<Node> nodeQuadTree = new QuadTree<Node>();
	Map<Integer, Node> nodeIDMap = new HashMap<Integer, Node>();
	ArrayList<Segment> segments = new ArrayList<Segment>();
	ArrayList<Road> roads = new ArrayList<Road>();
	Trie<Road> roadTrie = new Trie<Road>();
	Map<Integer, Road> roadIDMap = new HashMap<Integer, Road>();
	ArrayList<RoadMapPolygon> polygons = new ArrayList<RoadMapPolygon>();
	Location origin = new Location(0,0);
	private double scale = 100;
	private final double zoom = 2;
	@Override
	protected void redraw(Graphics g) {
		for(RoadMapPolygon polygon : polygons) {
			polygon.draw(g, origin, scale);
		}
		
		for(Segment segment : segments) {
			segment.draw(g, origin, scale);
		}
		for(Node node : nodes) {
			node.draw(g, origin, scale, this.getTextOutputArea());
		}
		
		
		
		//if(!this.nodeQuadTree.isEmpty()) {
			//this.nodeQuadTree.draw(g, origin, scale);
		//}
	}

	@Override
	protected void onClick(MouseEvent e) {
		if(this.highlightedNode != null) {
			this.highlightedNode.dehighlight();
		}
		Node node = nodeQuadTree.getNearest(Location.newFromPoint(e.getPoint(), origin, scale));
		if(node == null) {
			return;
		}
		this.highlightedNode = node;
		this.highlightedNode.highlight();
		String text = "ID: "+ node.ID() + "\n";
		Set<String> roadSet = new HashSet<String>();
		for(Segment segment : node.incomingSegments()) {
			if(!roadSet.contains(segment.road().name())) {
				text += segment.road().name() + "\n";
				roadSet.add(segment.road().name());
			}
		}
		for(Segment segment : node.outgoingSegments()) {
			if(!roadSet.contains(segment.road().name())) {
				text += segment.road().name() + "\n";
				roadSet.add(segment.road().name());
			}
		}
		this.getTextOutputArea().setText(text);
		
	}

	@Override
	protected void onSearch() {
		for(Segment segment : this.segments) {
			segment.dehighlight();
		}
		ArrayList<Road> output = this.roadTrie.get(this.getSearchBox().getText());
		if(output == null) {
			return;
		}
		for(Road road : output) {
			for(Segment segment : road.segments()) {
				segment.highlight();
			}
		}
	}

	@Override
	protected void onMove(Move m) {
		switch(m) {
		case ZOOM_IN:
			Dimension dimensions = this.getDrawingAreaDimension();
			origin = Location.newFromPoint(
					new Point(
							origin.asPoint(origin, scale).x + ((int)(dimensions.getWidth()) - (int)(dimensions.getWidth()/zoom))/2, 
							origin.asPoint(origin, scale).y + ((int)(dimensions.getHeight()) - (int)(dimensions.getHeight()/zoom))/2
							), 
					origin, 
					scale);
			scale *= zoom;
			
			
			break;
		case ZOOM_OUT:
			dimensions = this.getDrawingAreaDimension();
			origin = Location.newFromPoint(
					new Point(
							origin.asPoint(origin, scale).x + ((int)(dimensions.getWidth()) - (int)(dimensions.getWidth()*zoom))/2, 
							origin.asPoint(origin, scale).y + ((int)(dimensions.getHeight()) - (int)(dimensions.getHeight()*zoom))/2
							), 
					origin, 
					scale);
			scale /= zoom;
			break;
		case NORTH:
			this.origin = Location.newFromPoint(new Point(origin.asPoint(origin, scale).x, origin.asPoint(origin, scale).y - PIXEL_DISTANCE), origin, scale);
			break;
		case SOUTH:
			this.origin = Location.newFromPoint(new Point(origin.asPoint(origin, scale).x, origin.asPoint(origin, scale).y + PIXEL_DISTANCE), origin, scale);
			break;
		case EAST:
			this.origin = Location.newFromPoint(new Point(origin.asPoint(origin, scale).x + PIXEL_DISTANCE, origin.asPoint(origin, scale).y), origin, scale);
			break;
		case WEST:
			this.origin = Location.newFromPoint(new Point(origin.asPoint(origin, scale).x - PIXEL_DISTANCE, origin.asPoint(origin, scale).y), origin, scale);
			break;
		}
	}
	
	private void clearState() {
		this.nodes.clear();
		this.nodeIDMap.clear();
		this.roadIDMap.clear();
		this.roads.clear();
		this.segments.clear();
		this.roadTrie.clear();
		this.origin = new Location(0,0);
		this.scale = 100;
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		clearState();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodes));
			List<String> lines = reader.lines().collect(Collectors.toList());
			Location BL = null, TR = null;
			boolean first = true;
			for(String line : lines) {
				String[] lineValues = line.split("\t");
				if(lineValues.length == 3) {
					Node node = new Node(Integer.parseInt(lineValues[0]), Double.parseDouble(lineValues[1]), Double.parseDouble(lineValues[2]));
					this.nodes.add(node);
					if(first) {
						first = false;
						BL = Location.newFromLatLon(Double.parseDouble(lineValues[1]), Double.parseDouble(lineValues[2]));
						TR = Location.newFromLatLon(Double.parseDouble(lineValues[1]), Double.parseDouble(lineValues[2]));
					}else {
						Location location = Location.newFromLatLon(Double.parseDouble(lineValues[1]), Double.parseDouble(lineValues[2]));
						if(location.x < BL.x) {
							BL = new Location(location.x, BL.y);
						}
						if(location.y < BL.y) {
							BL = new Location(BL.x, location.y);
						}
						if(location.x > TR.x) {
							TR = new Location(location.x, TR.y);
						}
						if(location.y > TR.y) {
							TR = new Location(TR.x, location.y);
						}
					}
					BL = new Location(BL.x-0.005, BL.y-0.005);
					TR = new Location(TR.x+0.01, TR.y+0.005);
					this.nodeIDMap.put(Integer.parseInt(lineValues[0]), node);
				}
			}
			this.nodeQuadTree.setSize(BL, TR);
			for(Node node : this.nodes) {
				this.nodeQuadTree.add(node, node.location());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			this.getTextOutputArea().setText("Failed to load nodes: "+ e.toString());
		} catch (IOException e) {
			this.getTextOutputArea().setText("Failed to close reader reading nodes: "+ e.toString());
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(roads));
			List<String> lines = reader.lines().collect(Collectors.toList());
			for(String line : lines) {
				if(!line.contains("roadid")) {
					String[] lineValues = line.split("\t");
				
					this.roads.add(
						new Road(
							Integer.parseInt(lineValues[0]),
							lineValues[2], 
							Integer.parseInt(lineValues[4]), 
							Integer.parseInt(lineValues[5]), 
							Integer.parseInt(lineValues[6]), 
							Integer.parseInt(lineValues[7]), 
							Integer.parseInt(lineValues[8]),
							Integer.parseInt(lineValues[9])
						));
					this.roadTrie.add(lineValues[2], this.roads.get(this.roads.size()-1));
					this.roadIDMap.put(Integer.parseInt(lineValues[0]), this.roads.get(this.roads.size()-1) );//useful for adding segments to roads based off ID instead of name
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			this.getTextOutputArea().setText("Failed to load roads: "+ e.toString());
		} catch (IOException e) {
			this.getTextOutputArea().setText("Failed to close reader reading roads: "+ e.toString());
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(segments));
			List<String> lines = reader.lines().collect(Collectors.toList());
			for(String line : lines) {
				if(!line.contains("roadID")) {
					String[] lineValues = line.split("\t");
					ArrayList<Location> coords = new ArrayList<Location>();
					for(int i = 4; i < lineValues.length; i += 2) {
						coords.add(Location.newFromLatLon(Double.parseDouble(lineValues[i]), Double.parseDouble(lineValues[i+1])));
					}
					Road road = this.roadIDMap.get(Integer.parseInt(lineValues[0]));
					Segment segment = new Segment(
							road,
							Double.parseDouble(lineValues[1]), 
							this.nodeIDMap.get(Integer.parseInt(lineValues[2])),
							this.nodeIDMap.get(Integer.parseInt(lineValues[3])),
							coords
							);
					this.segments.add(segment);
					
					road.addSegment(segment);
					String mode1 = "Both";
					String mode2 = "Both";
					if(road.oneWay()) {
						mode1 = "Outgoing";
						mode2 = "Incoming";
					}
					this.nodeIDMap.get(Integer.parseInt(lineValues[2])).addSegment(segment, mode1);
					this.nodeIDMap.get(Integer.parseInt(lineValues[3])).addSegment(segment, mode2);
							
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			this.getTextOutputArea().setText("Failed to load road segments: "+ e.toString());
		} catch (IOException e) {
			this.getTextOutputArea().setText("Failed to close reader reading road segments: "+ e.toString());
		}
		
		if(polygons == null) {
			return;
		}
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(polygons));
			List<String> lines = reader.lines().collect(Collectors.toList());
			String color = null, label = null, endLevel = null;
			ArrayList<Location> coords = new ArrayList<Location>();
			Set<String> types = new HashSet<String>();
			for(String s : lines) {
				
				
				if(s.contains("Type")) {
					color = s.substring(5);
					types.add(color);
					color = color.trim();
				}
				if(s.contains("Label")) {
					label = s.substring(6);
					label = label.trim();
				}
				if(s.contains("EndLevel")) {
					endLevel = s.substring(9);
					endLevel = endLevel.trim();
				}
				if(s.contains("Data")) {
					s = s.substring(6);
					s = s.replaceAll("\\(", "");
					s = s.replaceAll("\\)", "");
					String[] strings;
					strings = s.split(",");
					for(int i = 0; i < strings.length; i += 2) {
						coords.add(Location.newFromLatLon(Double.parseDouble(strings[i]), Double.parseDouble(strings[i+1])));
					}
				}
				if(s.contains("[END]")) {
					
					this.polygons.add(new RoadMapPolygon(color,endLevel, label, coords));
					color = null;
					label = null;
					coords = new ArrayList<Location>();
				}
			}
			
			
			
			reader.close();
		} catch (FileNotFoundException e) {
			this.getTextOutputArea().setText("Failed to load polygons: "+ e.toString());
		} catch (IOException e) {
			this.getTextOutputArea().setText("Failed to close reader reading polygons: "+ e.toString());
		}
		
	}
	
	public static void main(String[] args) {
		new RoadMap();
		
	}

}
