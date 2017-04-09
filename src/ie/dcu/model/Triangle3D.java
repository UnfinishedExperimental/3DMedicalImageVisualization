 package ie.dcu.model;

/*
 * Triangle3D.java
 * Declares a Triangle with Three 3D point in space
 * 
 */
public class Triangle3D {
	public Point3D points[] = new Point3D[3];
	public Point3D normal = new Point3D(0, 1, 0);
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
	
	public Triangle3D(Point3D x, Point3D y, Point3D z) {
		points[0] = x;
		points[1] = y;
		points[2] = z;
	}
	public void calcnormal(boolean invertnormals) {
		Point3D nv = new Point3D();
		Point3D v1 = new Point3D((points[1].x - points[0].x), (points[1].y - points[0].y),
				(points[1].z - points[0].z));
		Point3D v2 = new Point3D((points[2].x - points[0].x), (points[2].y - points[0].y),
				(points[2].z - points[0].z));

		nv.x = (v1.y * v2.z) - (v1.z * v2.y);
		nv.y = (v1.z * v2.x) - (v1.x * v2.z);
		nv.z = (v1.x * v2.y) - (v1.y * v2.x);
		if (!invertnormals) {
			normal.x = nv.x;
			normal.y = nv.y;
			normal.z = nv.z;
		} else {
			normal.x = -nv.x;
			normal.y = -nv.y;
			normal.z = -nv.z;
		}
	}

}
