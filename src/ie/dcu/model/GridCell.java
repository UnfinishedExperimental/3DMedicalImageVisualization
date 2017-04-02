package ie.dcu.model;

/*
 * MarchingCubeCell.java
 * Declares a Imaginary cube made form two successive slice so declaring 8 vertices at a time.
 * 
 */

public class GridCell {
	public Point3D vertices[] = new Point3D[8];
	public float verticeValues[] = new float[8];

	public GridCell() {
		for (int i = 0; i < 8; i++) {
			vertices[i] = new Point3D();
			verticeValues[i] = 0.0f;
		}
	}
}
