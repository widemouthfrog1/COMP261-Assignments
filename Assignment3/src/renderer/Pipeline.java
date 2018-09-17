package renderer;

import java.awt.Color;

import renderer.Scene.Polygon;

/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		return poly.getNormal().z >= 0; //normal of polygon is facing away from viewer
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 * 
	 * @param lightDirection
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
		
		float cosTheta = (poly.getNormal().dotProduct(lightDirection))/(poly.getNormal().mag*lightDirection.mag);
		int ar = ambientLight.getRed();
		int rr = poly.reflectance.getRed();
		int lr = lightColor.getRed();
		int ag = ambientLight.getGreen();
		int rg = poly.reflectance.getGreen();
		int lg = lightColor.getGreen();
		int ab = ambientLight.getBlue();
		int rb = poly.reflectance.getBlue();
		int lb = lightColor.getBlue();
		float incidentr = lr*rr*cosTheta/255;
		float incidentg = lg*rg*cosTheta/255;
		float incidentb = lb*rb*cosTheta/255;
		float ambientr = ar*rr/255;
		float ambientg = ag*rg/255;
		float ambientb = ab*rb/255;
		float r;
		float g;
		float b;
		if(incidentr < 0) {
			r = ambientr;
		}else {
			r = ambientr + incidentr;
		}
		if(incidentg < 0) {
			g = ambientg;
		}else {
			g = ambientg + incidentg;
		}
		if(incidentb < 0) {
			b = ambientb;
		}else {
			b = ambientb + incidentb;
		}
		if(r > 255) {
			r = 255;
		}
		if(g > 255) {
			g = 255;
		}
		if(b > 255) {
			b = 255;
		}
		return new Color((int)r,(int)g,(int)b);
	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 * 
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {
		//x:
		//[1,0,0]
		//[0,cosX,-sinX]
		//[0,sinX,cosX]
		//y:
		//[cosY,0,sinY]
		//[0,1,0]
		//[-sinY,0,cosY]
		//xXy:
		//[cosY,0,sinY]
		//[sinXsinY,cosX,-sinXcosY]
		//[-cosXsinY,sinX,cosXcosY]
		//(xXy)Xv
		//[cosY*v.x+sinY*v.z]
		//[sinXsinY*v.x + cosX*v.y + -sinXcosY*v.z]
		//[-cosXsinY*v.x + sinX*v.y + cosXcosY*v.z]
		float x = (scene.xMax-scene.xMin)/2;
		float y = (scene.yMax-scene.yMin)/2;
		float z = (scene.zMax-scene.zMin)/2;
		Transform translate = Transform.newTranslation(-x, -y, -z);
		for(Polygon p : scene.polygons) {
			for(int i = 0; i < p.vertices.length; i++) {
				p.vertices[i] = translate.multiply(p.vertices[i]);
			}
		}
		float cosX = (float)Math.cos(xRot);
		float sinX = (float)Math.sin(xRot);
		float cosY = (float)Math.cos(yRot);
		float sinY = (float)Math.sin(yRot);
		
		for(Polygon p : scene.polygons) {
			for(int i = 0; i < p.vertices.length; i++) {
				Vector3D v = p.vertices[i];
				p.vertices[i] = new Vector3D(cosY*v.x+sinY*v.z, sinX*sinY*v.x + cosX*v.y + -sinX*cosY*v.z, -cosX*sinY*v.x + sinX*v.y + cosX*cosY*v.z);
			
			}
			
		}
		translate = Transform.newTranslation(x, y, z);
		for(Polygon p : scene.polygons) {
			for(int i = 0; i < p.vertices.length; i++) {
				p.vertices[i] = translate.multiply(p.vertices[i]);
			}
			p.resetBoundingBox();
			p.updateBoundingBox();
		}
		scene.resetBoundingBox();
		scene.updateBoundingBox();
		
		return scene;
	}

	/**
	 * This should translate the scene by the appropriate amount.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene translateScene(Scene scene, float x, float y) {
		
		for(Polygon p : scene.polygons) {
			for(int i = 0; i < p.vertices.length; i++) {
				p.vertices[i] = new Vector3D(p.vertices[i].x-x, p.vertices[i].y-y, p.vertices[i].z);
			}
			p.resetBoundingBox();
			p.updateBoundingBox();
		}
		scene.resetBoundingBox();
		scene.updateBoundingBox();
		return scene;
	}
	
public static Scene translateScene(Scene scene) {
		
		if(scene.xMin != 0) {
			for(Polygon p : scene.polygons) {
				for(int i = 0; i < p.vertices.length; i++) {
					p.vertices[i] = new Vector3D(p.vertices[i].x-scene.xMin, p.vertices[i].y, p.vertices[i].z);
				}
				p.resetBoundingBox();
				p.updateBoundingBox();
			}
		}
		if(scene.yMin != 0) {
			for(Polygon p : scene.polygons) {
				for(int i = 0; i < p.vertices.length; i++) {
					p.vertices[i] = new Vector3D(p.vertices[i].x, p.vertices[i].y-scene.yMin, p.vertices[i].z);
				}
				p.resetBoundingBox();
				p.updateBoundingBox();
			}
		}
		scene.resetBoundingBox();
		scene.updateBoundingBox();
		return scene;
	}

	/**
	 * This should scale the scene.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene) {
		if(scene.xMax > scene.yMax) {
			if(scene.xMax > scene.zMax) {
				if(scene.xMax != GUI.CANVAS_WIDTH-1) {
					for(Polygon p : scene.polygons) {
						for(int i = 0; i < p.vertices.length; i++) {
							p.vertices[i] = new Vector3D(p.vertices[i].x*(GUI.CANVAS_WIDTH-1)/scene.xMax, p.vertices[i].y*(GUI.CANVAS_WIDTH-1)/scene.xMax, p.vertices[i].z*(GUI.CANVAS_WIDTH-1)/scene.xMax);//Scale x to fit screen
						}
						p.resetBoundingBox();
						p.updateBoundingBox();
					}
				}
			}else {
				if(scene.xMax != GUI.CANVAS_WIDTH-1) {
					for(Polygon p : scene.polygons) {
						for(int i = 0; i < p.vertices.length; i++) {
							p.vertices[i] = new Vector3D(p.vertices[i].x*(GUI.CANVAS_WIDTH-1)/scene.zMax, p.vertices[i].y*(GUI.CANVAS_WIDTH-1)/scene.zMax, p.vertices[i].z*(GUI.CANVAS_WIDTH-1)/scene.zMax);//Scale x to fit screen
						}
						p.resetBoundingBox();
						p.updateBoundingBox();
					}
				}
			}
		}else {
			if(scene.yMax > scene.zMax) {
				if(scene.yMax != GUI.CANVAS_HEIGHT-1) {
					for(Polygon p : scene.polygons) {
						for(int i = 0; i < p.vertices.length; i++) {
							p.vertices[i] = new Vector3D(p.vertices[i].x*(GUI.CANVAS_HEIGHT-1)/scene.yMax, p.vertices[i].y*(GUI.CANVAS_HEIGHT-1)/scene.yMax, p.vertices[i].z*(GUI.CANVAS_HEIGHT-1)/scene.yMax);//Scale y to fit screen
						}
						p.resetBoundingBox();
						p.updateBoundingBox();
					}
				}
			}else {
				if(scene.yMax != GUI.CANVAS_HEIGHT-1) {
					for(Polygon p : scene.polygons) {
						for(int i = 0; i < p.vertices.length; i++) {
							p.vertices[i] = new Vector3D(p.vertices[i].x*(GUI.CANVAS_HEIGHT-1)/scene.zMax, p.vertices[i].y*(GUI.CANVAS_HEIGHT-1)/scene.zMax, p.vertices[i].z*(GUI.CANVAS_HEIGHT-1)/scene.zMax);//Scale y to fit screen
						}
						p.resetBoundingBox();
						p.updateBoundingBox();
					}
				}
			}
		}
		scene.resetBoundingBox();
		scene.updateBoundingBox();
		return scene;
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
		EdgeList list = new EdgeList((int)poly.getYMin(), (int)poly.getYMax());
		Vector3D[] points = poly.getVertices();
		//initialise xLeft and xRight to height of polygon including both edges
		float[] xLeft = new float[list.getEndY()-list.getStartY()+1];
		float[] xRight = new float[list.getEndY()-list.getStartY()+1];
		float[] zLeft = new float[list.getEndY()-list.getStartY()+1];
		float[] zRight = new float[list.getEndY()-list.getStartY()+1];
		for(int i = 0; i < points.length; i++) {
			Vector3D point1 = points[i];
			Vector3D point2;
			if(i == points.length-1) {
				point2 = points[0];
			}else {
				point2 = points[i+1];
			}
			float slope = (point2.x-point1.x)/(point2.y-point1.y);
			float x = point1.x;
			int y = (int)point1.y;
			if(point1.y < point2.y) {
				while(y <= (int)point2.y) {
					xLeft[y-list.getStartY()] = x;
					x += slope;
					y++;
				}
			}else {
				while(y >= (int)point2.y) {
					xRight[y-list.getStartY()] = x;
					x -= slope;
					y--;
				}
			}
		}
		
		for(int i = 0; i < points.length; i++) {
			Vector3D point1 = points[i];
			Vector3D point2;
			if(i == points.length-1) {
				point2 = points[0];
			}else {
				point2 = points[i+1];
			}
			float slope = (point2.z-point1.z)/(point2.y-point1.y);
			float z = point1.z;
			int y = (int)point1.y;
			if(point1.y < point2.y) {
				while(y <= (int)point2.y) {
					zLeft[y-list.getStartY()] = z;
					z += slope;
					y++;
				}
			}else {
				while(y >= (int)point2.y) {
					zRight[y-list.getStartY()] = z;
					z -= slope;
					y--;
				}
			}
		}
		for(int y = 0; y <= list.getEndY()-list.getStartY(); y++) {
			list.addRow(y, xLeft[y], xRight[y], zLeft[y], zRight[y]);
		}
		return list;
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * 
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 * 
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param polyEdgeList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList polyEdgeList, Color polyColor) {

			for (int y = polyEdgeList.getStartY(); y <= polyEdgeList.getEndY(); y++) {
				float slope = (polyEdgeList.getRightZ(y)-polyEdgeList.getLeftZ(y))/(polyEdgeList.getRightX(y)-polyEdgeList.getLeftX(y));
				int x = (int)polyEdgeList.getLeftX(y);
				float z = polyEdgeList.getLeftZ(y) + slope*(x-polyEdgeList.getLeftX(y));
				while (x <= (int)polyEdgeList.getRightX(y)) {
					if(x < zdepth.length && x >= 0 && y < zdepth[x].length && y >= 0)
					if (z < zdepth[x][y]) {
						zdepth[x][y] = z;
						zbuffer[x][y] = polyColor;
						z += slope;
						
					}
					x++;
				}
			}
	}
}

// code for comp261 assignments
