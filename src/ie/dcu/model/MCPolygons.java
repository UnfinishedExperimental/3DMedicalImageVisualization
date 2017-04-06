package ie.dcu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ie.dcu.ui.ImageConstants;

public class MCPolygons {
	// obj data
	public GridCell grid;
	public Triangle3D[] triangles;
	public List<Triangle3D> trilist = new ArrayList<Triangle3D>();
	public int normalTriangle;
	public boolean invertnormals = true;
	public boolean closesides = true;
	public void initiateMCProcess(int[][][] allSlicesData, int totalSlices) {
		initResolution();
		// System.out.println(Arrays.deepToString(allSlicesData));
		for (int i = 0; i < ImageConstants.ROWS-1; i++) {
			for (int j = 0; j < ImageConstants.ROWS-1; j++) {
				for (int k = 0; k < totalSlices-1; k++) {
					grid.vertices[0].x = i;
					grid.vertices[0].y = j;
					grid.vertices[0].z = k;
					grid.verticeValues[0] = allSlicesData[i][j][k];
					grid.vertices[1].x = (i + 1);
					grid.vertices[1].y = j;
					grid.vertices[1].z = k;
					grid.verticeValues[1] = allSlicesData[i+1][j][k];
					grid.vertices[2].x = (i + 1);
					grid.vertices[2].y = (j + 1);
					grid.vertices[2].z = k;
					grid.verticeValues[2] = allSlicesData[i+1][j+1][k];
					grid.vertices[3].x = i;
					grid.vertices[3].y = (j + 1);
					grid.vertices[3].z = k;
					grid.verticeValues[3] = allSlicesData[i][j+1][k];
					grid.vertices[4].x = i;
					grid.vertices[4].y = j;
					grid.vertices[4].z = (k + 1);
					grid.verticeValues[4] = allSlicesData[i][j][k+1];
					grid.vertices[5].x = (i + 1);
					grid.vertices[5].y = j;
					grid.vertices[5].z = (k + 1);
					grid.verticeValues[5] = allSlicesData[i+1][j][k + 1];
					grid.vertices[6].x = (i + 1);
					grid.vertices[6].y = (j + 1);
					grid.vertices[6].z = (k + 1);
					grid.verticeValues[6] = allSlicesData[i+1][j + 1][k + 1];
					grid.vertices[7].x = i;
					grid.vertices[7].y = (j + 1);
					grid.vertices[7].z = (k + 1);
					grid.verticeValues[7] = allSlicesData[i][j + 1][k + 1];
					final int n = Polygonise(grid);
/*					for (int l = 0; l < triangles.length; l++) {
						System.out.println("x:" + triangles[l].points[0].x + " y: " + triangles[l].points[0].y + " z: " + triangles[l].points[0].z);
						System.out.println("x:" + triangles[l].points[1].x + " y: " + triangles[l].points[1].y + " z: " + triangles[l].points[1].z);
						System.out.println("x:" + triangles[l].points[2].x + " y: " + triangles[l].points[2].y + " z: " + triangles[l].points[2].z);
					}*/
					// calc tri norms
					for (int a0 = 0; a0 < n; a0++) {
						triangles[a0].calcnormal(invertnormals);
					}
					for (int l = 0; l < n; l++) {
						final Triangle3D t = new Triangle3D(triangles[l]);
						trilist.add(t);
					}
					normalTriangle += n;
				}
			}
		}
	}

	
	/*
	 * Given a grid cell and an isolevel, calculate the triangular facets
	 * required to represent the isosurface through the cell. Return the number
	 * of triangular facets, the array "triangles" will be loaded up with the
	 * vertices at most 5 triangular facets. 0 will be returned if the grid cell
	 * is either totally above of totally below the isolevel.
	 */
	private int Polygonise(GridCell grid) {
		int i, ntriang;
		int cubeindex;
		final Point3D vertlist[] = new Point3D[12];
		float isolevel = ImageConstants.ISO_VALUE;
		/*
		 * Determine the index into the edge table which tells us which vertices
		 * are inside of the surface
		 */
		cubeindex = 0;
		if (grid.verticeValues[0] < isolevel) {
			cubeindex |= 1;
		}
		if (grid.verticeValues[1] < isolevel) {
			cubeindex |= 2;
		}
		if (grid.verticeValues[2] < isolevel) {
			cubeindex |= 4;
		}
		if (grid.verticeValues[3] < isolevel) {
			cubeindex |= 8;
		}
		if (grid.verticeValues[4] < isolevel) {
			cubeindex |= 16;
		}
		if (grid.verticeValues[5] < isolevel) {
			cubeindex |= 32;
		}
		if (grid.verticeValues[6] < isolevel) {
			cubeindex |= 64;
		}
		if (grid.verticeValues[7] < isolevel) {
			cubeindex |= 128;
		}

		/* Cube is entirely in/out of the surface */
		if (MCLookUpTables.edgeTable[cubeindex] == 0) {
			return (0);
		}

		/* Find the vertices where the surface intersects the cube */
		// int temp = edgeTable[cubeindex] & 1;
		if ((MCLookUpTables.edgeTable[cubeindex] & 1) != 0) {
			vertlist[0] = VertexInterp(isolevel, grid.vertices[0], grid.vertices[1],
					grid.verticeValues[0], grid.verticeValues[1]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2) != 0) {
			vertlist[1] = VertexInterp(isolevel, grid.vertices[1], grid.vertices[2],
					grid.verticeValues[1], grid.verticeValues[2]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 4) != 0) {
			vertlist[2] = VertexInterp(isolevel, grid.vertices[2], grid.vertices[3],
					grid.verticeValues[2], grid.verticeValues[3]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 8) != 0) {
			vertlist[3] = VertexInterp(isolevel, grid.vertices[3], grid.vertices[0],
					grid.verticeValues[3], grid.verticeValues[0]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 16) != 0) {
			vertlist[4] = VertexInterp(isolevel, grid.vertices[4], grid.vertices[5],
					grid.verticeValues[4], grid.verticeValues[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 32) != 0) {
			vertlist[5] = VertexInterp(isolevel, grid.vertices[5], grid.vertices[6],
					grid.verticeValues[5], grid.verticeValues[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 64) != 0) {
			vertlist[6] = VertexInterp(isolevel, grid.vertices[6], grid.vertices[7],
					grid.verticeValues[6], grid.verticeValues[7]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 128) != 0) {
			vertlist[7] = VertexInterp(isolevel, grid.vertices[7], grid.vertices[4],
					grid.verticeValues[7], grid.verticeValues[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 256) != 0) {
			vertlist[8] = VertexInterp(isolevel, grid.vertices[0], grid.vertices[4],
					grid.verticeValues[0], grid.verticeValues[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 512) != 0) {
			vertlist[9] = VertexInterp(isolevel, grid.vertices[1], grid.vertices[5],
					grid.verticeValues[1], grid.verticeValues[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 1024) != 0) {
			vertlist[10] = VertexInterp(isolevel, grid.vertices[2], grid.vertices[6],
					grid.verticeValues[2], grid.verticeValues[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2048) != 0) {
			vertlist[11] = VertexInterp(isolevel, grid.vertices[3], grid.vertices[7],
					grid.verticeValues[3], grid.verticeValues[7]);
		}

		/* Create the triangle */
		ntriang = 0;
		for (i = 0; MCLookUpTables.triTable[cubeindex][i] != -1; i += 3) {
			triangles[ntriang].points[0] = vertlist[MCLookUpTables.triTable[cubeindex][i]];
			triangles[ntriang].points[1] = vertlist[MCLookUpTables.triTable[cubeindex][i + 1]];
			triangles[ntriang].points[2] = vertlist[MCLookUpTables.triTable[cubeindex][i + 2]];
			ntriang++;
		}

		return (ntriang);
	}
	
	/*
	 * Linearly interpolate the position where an isosurface cuts an edge
	 * between two vertices, each with their own scalar value
	 */
	private Point3D VertexInterp(float isolevel, Point3D p1, Point3D p2, float valp1,
			float valp2) {
		float mu;
		final Point3D p = new Point3D();
		p.x = p.y = p.z = 0.0f;

		if (Math.abs(isolevel - valp1) < 0.00001) {
			return (p1);
		}
		if (Math.abs(isolevel - valp2) < 0.00001) {
			return (p2);
		}
		if (Math.abs(valp1 - valp2) < 0.00001) {
			return (p1);
		}
		mu = ((isolevel - valp1) / (valp2 - valp1));
		p.x = p1.x + mu * (p2.x - p1.x);
		p.y = p1.y + mu * (p2.y - p1.y);
		p.z = p1.z + mu * (p2.z - p1.z);

		return (p);
	}
	
	private void initResolution() {
		triangles = new Triangle3D[5];
		normalTriangle = 0;
		for (int i = 0; i < triangles.length; i++) {
			triangles[i] = new Triangle3D();
		}
		grid = new GridCell();
		trilist.clear();
	}
}
