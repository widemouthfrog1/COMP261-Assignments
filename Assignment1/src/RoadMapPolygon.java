import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class RoadMapPolygon {
	private Color color;
	private String label; //may be used later
	private ArrayList<Location> coords;
	
	RoadMapPolygon(String color, String label, ArrayList<Location> coords){
		this.color = new Color(Integer.decode(color));
		this.label = label;
		this.coords = coords;
		
	}
	
	public void draw(Graphics g, Location origin, double scale) {
		int[] xPoints = new int[coords.size()];
		int[] yPoints = new int[coords.size()];
		
		for(Location loc : coords) {
			Point p = loc.asPoint(origin, scale);
			xPoints[coords.indexOf(loc)] = p.x;
			yPoints[coords.indexOf(loc)] = p.y;
		}
		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, xPoints.length);
	}
}
