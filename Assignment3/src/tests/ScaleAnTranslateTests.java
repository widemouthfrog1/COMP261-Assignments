package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import renderer.Pipeline;
import renderer.Scene;
import renderer.Vector3D;
import renderer.Scene.Polygon;

class ScaleAnTranslateTests {
	
	private static boolean eq(Polygon p1, Polygon p2) {
		Vector3D[] v1 = p1.getVertices();
		Vector3D[] v2 = p2.getVertices();

		return v1[0].equals(v2[0]) & v1[1].equals(v2[1]) & v1[2].equals(v2[2]);
	}
	
	@Test
	public void testScale() {
		float[] verts = new float[] { 0,0,0, 0,10,0, 10,10,0 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);
		List<Polygon> ar = new ArrayList<>();
		ar.add(p1);
		Scene scene = new Scene(ar, light) ;
				

		Scene res = Pipeline.scaleScene(scene);
		Polygon p2 = res.getPolygons().get(0);
		verts = new float[] { 0,0,0, 0,600,0, 600,600,0 };
		final Polygon p3 = new Polygon(verts, col);
		assertTrue(eq(p3, p2));
	}
	
	@Test
	public void testScale2Triangles() {
		float[] verts = new float[] { 0,0,0, 0,10,0, 5,10,0 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);
		verts = new float[] { 5,0,0, 10,0,0, 10,10,0 };
		final Polygon p2 = new Polygon(verts, col);
		List<Polygon> ar = new ArrayList<>();
		ar.add(p1);
		ar.add(p2);
		Scene scene = new Scene(ar, light) ;
				

		Scene res = Pipeline.scaleScene(scene);
		Polygon p3 = res.getPolygons().get(0);
		Polygon p4 = res.getPolygons().get(1);
		verts = new float[] { 0,0,0, 0,600,0, 300,600,0 };
		final Polygon p5 = new Polygon(verts, col);
		verts = new float[] { 300,0,0, 600,0,0, 600,600,0 };
		final Polygon p6 = new Polygon(verts, col);
		assertTrue(eq(p5, p3));
		assertTrue(eq(p6, p4));
	}
	
	@Test
	public void testTranslate() {
		float[] verts = new float[] { 5,0,0, 5,10,0, 10,10,0 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);
		List<Polygon> ar = new ArrayList<>();
		ar.add(p1);
		Scene scene = new Scene(ar, light) ;
		Scene res = Pipeline.translateScene(scene);
		Polygon p2 = res.getPolygons().get(0);
		verts = new float[] { 0,0,0, 0,10,0, 5,10,0 };
		final Polygon p3 = new Polygon(verts, col);
		assertTrue(eq(p3, p2));
	}
	
	@Test
	public void testTranslate2() {
		float[] verts = new float[] { -1,0,0, 0,10,0, 5,10,0 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);
		verts = new float[] { 5,0,0, 10,0,0, 10,10,0 };
		final Polygon p2 = new Polygon(verts, col);
		List<Polygon> ar = new ArrayList<>();
		ar.add(p1);
		ar.add(p2);
		Scene scene = new Scene(ar, light) ;
				

		Scene res = Pipeline.translateScene(scene);
		Polygon p3 = res.getPolygons().get(0);
		Polygon p4 = res.getPolygons().get(1);
		verts = new float[] { 0,0,0, 1,10,0, 6,10,0};
		final Polygon p5 = new Polygon(verts, col);
		verts = new float[] {6,0,0, 11,0,0, 11,10,0};
		final Polygon p6 = new Polygon(verts, col);
		assertTrue(eq(p5, p3));
		assertTrue(eq(p6, p4));
	}
	
	@Test
	public void testTranslateAndScale2() {
		float[] verts = new float[] { -1,0,0, -1,10,0, 4,10,0 };
		int[] col = new int[] { 0, 0, 0 };
		final Vector3D light = new Vector3D(0, 0, 0);

		final Polygon p1 = new Polygon(verts, col);
		verts = new float[] { 4,0,0, 9,0,0, 9,10,0 };
		final Polygon p2 = new Polygon(verts, col);
		List<Polygon> ar = new ArrayList<>();
		ar.add(p1);
		ar.add(p2);
		Scene scene = new Scene(ar, light) ;
				

		Scene res = Pipeline.translateScene(scene);
		res = Pipeline.scaleScene(scene);
		Polygon p3 = res.getPolygons().get(0);
		Polygon p4 = res.getPolygons().get(1);
		verts = new float[] {0,0,0, 0,600,0, 300,600,0};
		final Polygon p5 = new Polygon(verts, col);
		verts = new float[] {300,0,0, 600,0,0, 600,600,0};
		final Polygon p6 = new Polygon(verts, col);
		assertTrue(eq(p5, p3));
		assertTrue(eq(p6, p4));
	}


}
