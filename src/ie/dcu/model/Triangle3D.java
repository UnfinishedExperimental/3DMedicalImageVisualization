 package ie.dcu.model;

/*
 * Triangle3D.java
 * Declares a Triangle with Three 3D point in space
 * 
 */
public class Triangle3D {
	public Point3D points[] = new Point3D[3];

	public Triangle3D() {
		for (int i = 0; i < 3; i++) {
			points[i] = new Point3D();
		}
	}

	public Triangle3D(Triangle3D trian) {
		for (int i = 0; i < 3; i++) {
			points[i] = new Point3D(trian.points[i]);
		}
	}
}
