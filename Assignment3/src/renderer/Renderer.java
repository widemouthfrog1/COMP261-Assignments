package renderer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

public class Renderer extends GUI {
	
	private Scene scene;
	private Color[][] image = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
	private Float maxZ = null;
	private Color ambientLight = new Color(10,10,10);
	@Override
	protected void onLoad(File file) {

		/*
		 * This method should parse the given file into a Scene object, which
		 * you store and use to render an image.
		 */
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			List<Polygon> polygons = new ArrayList<Polygon>();
			String line = reader.readLine();
			String[] words = line.split(" ");
			Vector3D lightPos = new Vector3D(Float.parseFloat(words[0]), Float.parseFloat(words[1]), Float.parseFloat(words[2]));
			int size = 0;
			while(reader.readLine() != null) {
				size++;
			}
			float[][] triangles = new float[size][9];
			int[][] triangleColors = new int[size][3];
			reader.close();
			reader = new BufferedReader(new FileReader(file));
			reader.readLine();
			line = reader.readLine();
			for(int i = 0; line != null; i++) {
				words = line.split(" ");
				for(int j = 0; j < 9; j++) {
					triangles[i][j] = Float.parseFloat(words[j]);
					if((j+1)%3 == 0) {
						if(this.maxZ == null || triangles[i][j] > this.maxZ) {
							 this.maxZ = triangles[i][j]+1000;
						}
					}
				}
				for(int j = 0; j < 3; j++) {
					triangleColors[i][j] = Integer.parseInt(words[9+j]);
				}
				polygons.add(new Polygon(triangles[i], triangleColors[i]));
				line = reader.readLine();
				
			}
			scene = new Scene(polygons, lightPos);
			
			reader.close();
			Pipeline.translateScene(scene);
			Pipeline.scaleScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {

		/*
		 * This method should be used to rotate the user's viewpoint.
		 */
		if(ev.getKeyChar() == 'a') {
			Pipeline.rotateScene(scene, 0, 0.05f);
		}
		if(ev.getKeyChar() == 'd') {
			Pipeline.rotateScene(scene, 0, -0.05f);
		}
		if(ev.getKeyChar() == 'w') {
			Pipeline.rotateScene(scene, 0.05f, 0);
		}
		if(ev.getKeyChar() == 's') {
			Pipeline.rotateScene(scene, -0.05f, 0);
		}
		
		if(ev.getKeyChar() == 'j') {
			Pipeline.translateScene(scene, 5f, 0);
		}
		if(ev.getKeyChar() == 'l') {
			Pipeline.translateScene(scene, -5f, 0);
		}
		if(ev.getKeyChar() == 'i') {
			Pipeline.translateScene(scene, 0, 5f);
		}
		if(ev.getKeyChar() == 'k') {
			Pipeline.translateScene(scene, 0, -5f);
		}
		
	}

	@Override
	protected BufferedImage render() {

		/*
		 * This method should put together the pieces of your renderer, as
		 * described in the lecture. This will involve calling each of the
		 * static method stubs in the Pipeline class, which you also need to
		 * fill in.
		 */
		if(this.maxZ == null) {
			return null;
		}
		float[][] zdepth = new float[CANVAS_WIDTH][CANVAS_HEIGHT];
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				zdepth[x][y] = this.maxZ;
				image[x][y] = new Color(100,100,100);
			}
		}
		this.ambientLight = new Color(this.getAmbientLight()[0], this.getAmbientLight()[1], this.getAmbientLight()[2]);
		for(Polygon polygon : scene.getPolygons()) {
			if(!Pipeline.isHidden(polygon))
				Pipeline.computeZBuffer(image, zdepth, Pipeline.computeEdgeList(polygon), Pipeline.getShading(polygon, scene.getLight(), new Color(100,100,100), ambientLight ));
		}
		
		return convertBitmapToImage(image);
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}
}

// code for comp261 assignments
