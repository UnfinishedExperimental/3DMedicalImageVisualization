package ie.dcu.model;

/*
 * Point3D.java
 * Declares a 3D point in space and sets the x, y and z coordinates
 * 
 */
public class Point3D {
	public float x;
	public float y;
	public float z;
	
	public Point3D() {
		
	}
	public Point3D(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D(Point3D point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}
	
}
