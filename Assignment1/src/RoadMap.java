import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoadMap extends GUI {
	ArrayList<Node> nodes = new ArrayList<Node>();
	private final int nodesize = 10;
	@Override
	protected void redraw(Graphics g) {
		// TODO Auto-generated method stub
		for(Node node : nodes) {
			g.drawOval((int)node.pos().x, (int)node.pos().y, nodesize, nodesize);
		}
	}

	@Override
	protected void onClick(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMove(Move m) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		// TODO Auto-generated method stub
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nodes));
			List<String> lines = reader.lines().collect(Collectors.toList());
			for(String line : lines) {
				String[] lineValues = line.split("\t");
				if(lineValues.length == 3) {
					this.nodes.add(new Node(Integer.parseInt(lineValues[0]), Double.parseDouble(lineValues[1]), Double.parseDouble(lineValues[2])));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			this.getTextOutputArea().setText("Failed to load nodes: "+ e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.getTextOutputArea().setText("Failed to close reader reading nodes: "+ e.toString());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RoadMap();
		
	}

}
