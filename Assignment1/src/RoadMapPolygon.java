import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class RoadMapPolygon {
	private Color color;
	private ArrayList<Location> coords;
	private int endLevel;
	
	RoadMapPolygon(String color, String endLevel, String label, ArrayList<Location> coords){
		this.endLevel = Integer.parseInt(endLevel);
		this.color = new Color(Integer.decode(color));
		switch(color) {
		case "0x19":
			this.color = Color.green;
			break;
		case "0x2":
			this.color = Color.green;
			break;
		case "0x1e":
			this.color = Color.blue;
			break;
		case "0x3e":
			this.color = Color.blue;
			break;
		case "0x5":
			this.color = Color.blue;
			break;
		case "0x7":
			this.color = Color.green;
			break;
		case "0x3c":
			this.color = Color.blue;
			break;
		case "0x1a":
			this.color = Color.blue;
			break;
		case "0x8":
			this.color = Color.blue;
			break;
		case "0x41":
			this.color = Color.blue;
			break;
		case "0x40":
			this.color = Color.blue;
			break;
		case "0x50":
			this.color = Color.blue;
			break;
		case "0x18":
			this.color = Color.green;
			break;
		case "0x28":
			this.color = Color.blue;
			break;
		case "0x17":
			this.color = Color.blue;
			break;
		case "0xa":
			this.color = Color.orange;
			break;
		case "0xb":
			this.color = Color.blue;
			break;
		case "0x16":
			this.color = Color.blue;
			break;
		case "0x48":
			this.color = Color.blue;
			break;
		case "0x47":
			this.color = Color.blue;
			break;
		case "0x13":
			this.color = Color.orange;
			break;
		case "0xe":
			this.color = Color.darkGray;
			break;
		case "0x45":
			this.color = Color.blue;
			break;
		}
		this.coords = coords;
		
	}
	
	public void draw(Graphics g, Location origin, double scale) {
		if(scale < 50) {
			if(this.endLevel == 1) {
				return;
			}
		}
		if(scale < 25) {
			if(this.endLevel == 2) {
				return;
			}
		}
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
