package renderer;

import java.awt.Color;
import java.util.List;

/**
 * The Scene class is where we store data about a 3D model and light source
 * inside our renderer. It also contains a static inner class that represents one
 * single polygon.
 * 
 * Method stubs have been provided, but you'll need to fill them in.
 * 
 * If you were to implement more fancy rendering, e.g. Phong shading, you'd want
 * to store more information in this class.
 */
public class Scene {
	private Vector3D lightPos;
	List<Polygon> polygons;
	Float yMax = null;
	Float yMin = null;
	Float xMax = null;
	Float xMin = null;
	Float zMax = null;
	Float zMin = null;
	public Scene(List<Polygon> polygons, Vector3D lightPos) {
          this.lightPos = lightPos;
          this.polygons = polygons;
          for(Polygon p : polygons) {
        	  if(yMax == null || yMax < p.yMax) {
        		  yMax = p.yMax;
        	  }
        	  if(yMin == null || yMin > p.yMin) {
        		  yMin = p.yMin;
        	  }
        	  if(xMax == null || xMax < p.xMax) {
        		  xMax = p.xMax;
        	  }
        	  if(xMin == null || xMin > p.xMin) {
        		  xMin = p.xMin;
        	  }
          }
	}
	
	public void updateBoundingBox() {
		for(Polygon p : polygons) {
      	  if(yMax == null || yMax < p.yMax) {
      		  yMax = p.yMax;
      	  }
      	  if(yMin == null || yMin > p.yMin) {
      		  yMin = p.yMin;
      	  }
      	  if(xMax == null || xMax < p.xMax) {
      		  xMax = p.xMax;
      	  }
      	  if(xMin == null || xMin > p.xMin) {
      		  xMin = p.xMin;
      	  }
      	  
      	  if(zMax == null || zMax < p.zMax) {
    		  zMax = p.zMax;
    	  }
    	  if(zMin == null || zMin > p.zMin) {
    		  zMin = p.zMin;
    	  }
        }
	}
	
	public void resetBoundingBox() {
		this.xMax = null;
		this.xMin = null;
		this.yMax = null;
		this.yMin = null;
		this.zMax = null;
		this.zMin = null;
		
	}

	public Vector3D getLight() {
          return lightPos;
	}

	public List<Polygon> getPolygons() {
          return polygons;
	}

	/**
	 * Polygon stores data about a single polygon in a scene, keeping track of
	 * (at least!) its three vertices and its reflectance.
         *
         * This class has been done for you.
	 */
	public static class Polygon {
		Vector3D[] vertices;
		Color reflectance;
		Float yMax = null;
		Float yMin = null;
		Float xMax = null;
		Float xMin = null;
		Float zMax = null;
		Float zMin = null;

		/**
		 * @param points
		 *            An array of floats with 9 elements, corresponding to the
		 *            (x,y,z) coordinates of the three vertices that make up
		 *            this polygon. If the three vertices are A, B, C then the
		 *            array should be [A_x, A_y, A_z, B_x, B_y, B_z, C_x, C_y,
		 *            C_z].
		 * @param color
		 *            An array of three ints corresponding to the RGB values of
		 *            the polygon, i.e. [r, g, b] where all values are between 0
		 *            and 255.
		 */
		public Polygon(float[] points, int[] color) {
			this.vertices = new Vector3D[3];
			
			float x, y, z;
			for (int i = 0; i < 3; i++) {
				x = points[i * 3];
				y = points[i * 3 + 1];
				z = points[i * 3 + 2];
				this.vertices[i] = new Vector3D(x, y, z);
			}

			int r = color[0];
			int g = color[1];
			int b = color[2];
			this.reflectance = new Color(r, g, b);
			updateBoundingBox();
		}

		/**
		 * An alternative constructor that directly takes three Vector3D objects
		 * and a Color object.
		 */
		public Polygon(Vector3D a, Vector3D b, Vector3D c, Color color) {
			this.vertices = new Vector3D[] { a, b, c };
			this.reflectance = color;
		}
		public void resetBoundingBox() {
			this.xMax = null;
			this.xMin = null;
			this.yMax = null;
			this.yMin = null;
			this.zMax = null;
			this.zMin = null;
		}
		public void updateBoundingBox() {
			for(Vector3D point: vertices) {
				if(yMax == null) {
					yMax = point.y;
				}
				if(yMin == null) {
					yMin = point.y;
				}
				if(yMax < point.y) {
					yMax = point.y;
				}
				if(yMin > point.y) {
					yMin = point.y;
				}

				if(xMax == null) {
					xMax = point.x;
				}
				if(xMin == null) {
					xMin = point.x;
				}
				if(xMax < point.x) {
					xMax = point.x;
				}
				if(xMin > point.x) {
					xMin = point.x;
				}
				
				if(zMax == null) {
					zMax = point.z;
				}
				if(zMin == null) {
					zMin = point.z;
				}
				if(zMax < point.z) {
					zMax = point.z;
				}
				if(zMin > point.z) {
					zMin = point.z;
				}
			}
		}

		public Vector3D[] getVertices() {
			return vertices;
		}

		public Color getReflectance() {
			return reflectance;
		}
		
		
		public Vector3D getNormal() {
			Vector3D edge1 = vertices[1].minus(vertices[0]);
			Vector3D edge2 = vertices[2].minus(vertices[1]);
			return edge1.crossProduct(edge2);
		}
		
		public float getYMax() {
			return yMax;
		}
		public float getYMin() {
			return yMin;
		}

		@Override
		public String toString() {
			String str = "polygon:";

			for (Vector3D p : vertices)
				str += "\n  " + p.toString();

			str += "\n  " + reflectance.toString();

			return str;
		}
	}
}

// code for COMP261 assignments
