 package ie.dcu.model;

import java.util.Arrays;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(points);
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
		Triangle3D other = (Triangle3D) obj;
		if (points[0].equals(other.points[0])
				&& points[1].equals(other.points[1])
				&& points[2].equals(other.points[2])) {
			return true;
		}
		return false;
	}

	public Triangle3D(Triangle3D trian) {
		for (int i = 0; i < 3; i++) {
			points[i] = new Point3D(trian.points[i]);
		}
	}
	
	public Triangle3D(Point3D x, Point3D y, Point3D z) {
		points[0] = x;
		points[1] = y;
		points[2] = z;
	}
	public Point3D calcnormal() {
		Point3D nv = new Point3D();
		Point3D v1 = new Point3D((points[1].x - points[0].x), (points[1].y - points[0].y),
				(points[1].z - points[0].z));
		Point3D v2 = new Point3D((points[2].x - points[0].x), (points[2].y - points[0].y),
				(points[2].z - points[0].z));

		nv.x = (v1.y * v2.z) - (v1.z * v2.y);
		nv.y = (v1.z * v2.x) - (v1.x * v2.z);
		nv.z = (v1.x * v2.y) - (v1.y * v2.x);
		return nv;
	}
	
/*	public Point3D[] calcnormal() {
		Point3D[] nv = new Point3D[] {new Point3D(), new Point3D(), new Point3D()};
		Point3D p0v1 = new Point3D((points[1].x - points[0].x), (points[1].y - points[0].y),
				(points[1].z - points[0].z));
		Point3D p0v2 = new Point3D((points[2].x - points[0].x), (points[2].y - points[0].y),
				(points[2].z - points[0].z));
		Point3D p1v1 = new Point3D((points[0].x - points[1].x), (points[0].y - points[1].y),
				(points[0].z - points[1].z));
		Point3D p1v2 = new Point3D((points[2].x - points[1].x), (points[2].y - points[1].y),
				(points[2].z - points[1].z));
		Point3D p2v1 = new Point3D((points[0].x - points[2].x), (points[0].y - points[2].y),
				(points[0].z - points[2].z));
		Point3D p2v2 = new Point3D((points[1].x - points[2].x), (points[1].y - points[2].y),
				(points[1].z - points[2].z));
		nv[0].x = (p0v1.y * p0v2.z) - (p0v1.z * p0v2.y);
		nv[0].y = (p0v1.z * p0v2.x) - (p0v1.x * p0v2.z);
		nv[0].z = (p0v1.x * p0v2.y) - (p0v1.y * p0v2.x);
		nv[1].x = (p1v1.y * p1v2.z) - (p1v1.z * p1v2.y);
		nv[1].y = (p1v1.z * p1v2.x) - (p1v1.x * p1v2.z);
		nv[1].z = (p1v1.x * p1v2.y) - (p1v1.y * p1v2.x);
		nv[2].x = (p2v1.y * p2v2.z) - (p2v1.z * p2v2.y);
		nv[2].y = (p2v1.z * p2v2.x) - (p2v1.x * p2v2.z);
		nv[2].z = (p2v1.x * p2v2.y) - (p2v1.y * p2v2.x);
		return nv;
	}*/
}
