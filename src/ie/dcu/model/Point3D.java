package ie.dcu.model;

import java.io.Serializable;

/*
 * Point3D.java
 * Declares a 3D point in space and sets the x, y and z coordinates
 * 
 */
public class Point3D implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point3D other = (Point3D) obj;
		if ((Float.floatToIntBits(x) == Float.floatToIntBits(other.x))
				&& (Float.floatToIntBits(y) == Float.floatToIntBits(other.y))
				&& (Float.floatToIntBits(z) == Float.floatToIntBits(other.z))) {
			return true;
		}
		return false;

	}
}
