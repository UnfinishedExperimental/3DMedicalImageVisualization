package ie.dcu.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	File newFile;
    FileWriter fileWriter;
    
	public void initiateMCProcess(int[][][] allSlicesData, int totalSlices) {
		initResolution();
		int numPointsInXDirection = ImageConstants.ROWS;
		int numPointsInSlice = numPointsInXDirection*(ImageConstants.COLUMNS);
		// System.out.println(Arrays.deepToString(allSlicesData));
		for (int x = 0; x < ImageConstants.ROWS -1; x++) {
			for (int y = 0; y < ImageConstants.ROWS -1; y++) {
				for (int z = 0; z < totalSlices-1; z++) {
					grid.verticesPosition[0].x = x;
					grid.verticesPosition[0].y = y*numPointsInXDirection;
					grid.verticesPosition[0].z = z*numPointsInSlice;
					grid.verticesPointValue[0] = allSlicesData[x][y][z];
					
					grid.verticesPosition[1].x = x;
					grid.verticesPosition[1].y = (y+1)*numPointsInXDirection;
					grid.verticesPosition[1].z = z*numPointsInSlice;
					grid.verticesPointValue[1] = allSlicesData[x][(y+1)][z];
					
					grid.verticesPosition[2].x = x+1;
					grid.verticesPosition[2].y = (y+1)*numPointsInXDirection;
					grid.verticesPosition[2].z = z*numPointsInSlice;
					grid.verticesPointValue[2] = allSlicesData[x+1][(y+1)][z];
					
					grid.verticesPosition[3].x = x+1;
					grid.verticesPosition[3].y = y*numPointsInXDirection;
					grid.verticesPosition[3].z = z*numPointsInSlice;
					grid.verticesPointValue[3] = allSlicesData[x+1][y][z];
					
					grid.verticesPosition[4].x = x;
					grid.verticesPosition[4].y = y*numPointsInXDirection;
					grid.verticesPosition[4].z = (z+1)*numPointsInSlice;
					grid.verticesPointValue[4] = allSlicesData[x][y][(z+1)];
					
					grid.verticesPosition[5].x = x;
					grid.verticesPosition[5].y = (y+1)*numPointsInXDirection;
					grid.verticesPosition[5].z = (z+1)*numPointsInSlice;
					grid.verticesPointValue[5] = allSlicesData[x][(y+1)][(z+1)];
					
					grid.verticesPosition[6].x = x+1;
					grid.verticesPosition[6].y = (y+1)*numPointsInXDirection;
					grid.verticesPosition[6].z = (z+1)*numPointsInSlice;
					grid.verticesPointValue[6] = allSlicesData[x+1][(y+1)][(z+1)];
					
					grid.verticesPosition[7].x = x+1;
					grid.verticesPosition[7].y = y*numPointsInXDirection;
					grid.verticesPosition[7].z = (z+1)*numPointsInSlice;
					grid.verticesPointValue[7] = allSlicesData[x+1][y][(z+1)];
					int numTriangleInPoly = Polygonise(grid);
/*					for (int l = 0; l < triangles.length; l++) {
						System.out.println("x:" + triangles[l].points[0].x + " y: " + triangles[l].points[0].y + " z: " + triangles[l].points[0].z);
						System.out.println("x:" + triangles[l].points[1].x + " y: " + triangles[l].points[1].y + " z: " + triangles[l].points[1].z);
						System.out.println("x:" + triangles[l].points[2].x + " y: " + triangles[l].points[2].y + " z: " + triangles[l].points[2].z);
					}*/
					// calc tri norms
					//if(numTriangleInPoly>0)
					//System.out.println("i: " + i + "j: " +j+ " k: " +k + " and ----TRIANGLES---" + numTriangleInPoly);
/*					for (int a0 = 0; a0 < n; a0++) {
						triangles[a0].calcnormal(invertnormals);
					}
					for (int l = 0; l < n; l++) {
						final Triangle3D t = new Triangle3D(triangles[l]);
						trilist.add(t);
					}
					normalTriangle += n;*/
				}
			}
		}
	}

	
	/*
	 * Given a grid cell and an isolevel, calculate the triangular facets
	 * required to represent the isosurface through the cell. Return the number
	 * of triangular facets, the array "triangles" will be loaded up with the
	 * verticesPosition at most 5 triangular facets. 0 will be returned if the grid cell
	 * is either totally above of totally below the isolevel.
	 */
	private int Polygonise(GridCell grid) {
		int i, ntriang;
		int cubeindex;
		final Point3D vertlist[] = new Point3D[12];
		float isolevel = ImageConstants.ISO_VALUE;
		/*
		 * Determine the index into the edge table which tells us which verticesPosition
		 * are inside of the surface
		 */
		cubeindex = 0;
		if (grid.verticesPointValue[0] < isolevel) {
			cubeindex |= 1;
		}
		if (grid.verticesPointValue[1] < isolevel) {
			cubeindex |= 2;
		}
		if (grid.verticesPointValue[2] < isolevel) {
			cubeindex |= 4;
		}
		if (grid.verticesPointValue[3] < isolevel) {
			cubeindex |= 8;
		}
		if (grid.verticesPointValue[4] < isolevel) {
			cubeindex |= 16;
		}
		if (grid.verticesPointValue[5] < isolevel) {
			cubeindex |= 32;
		}
		if (grid.verticesPointValue[6] < isolevel) {
			cubeindex |= 64;
		}
		if (grid.verticesPointValue[7] < isolevel) {
			cubeindex |= 128;
		}
		//System.out.println("cubeindex::::::" + cubeindex);
		try {
			fileWriter.write("v " + grid.verticesPointValue[0]
					+ "" + grid.verticesPointValue[1]
							+ "" + grid.verticesPointValue[2]
									+ "" + grid.verticesPointValue[3]
											+ "" + grid.verticesPointValue[4]
													+ "" + grid.verticesPointValue[5]
															+ "" + grid.verticesPointValue[6]
																	+ "" + grid.verticesPointValue[7]
					
					);
			fileWriter.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Cube is entirely in/out of the surface */
		if (MCLookUpTables.edgeTable[cubeindex] == 0) {
			return (0);
		}

		/* Find the verticesPosition where the surface intersects the cube */
		// int temp = edgeTable[cubeindex] & 1;
		if ((MCLookUpTables.edgeTable[cubeindex] & 1) != 0) {
			vertlist[0] = VertexInterp(isolevel, grid.verticesPosition[0], grid.verticesPosition[1],
					grid.verticesPointValue[0], grid.verticesPointValue[1]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2) != 0) {
			vertlist[1] = VertexInterp(isolevel, grid.verticesPosition[1], grid.verticesPosition[2],
					grid.verticesPointValue[1], grid.verticesPointValue[2]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 4) != 0) {
			vertlist[2] = VertexInterp(isolevel, grid.verticesPosition[2], grid.verticesPosition[3],
					grid.verticesPointValue[2], grid.verticesPointValue[3]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 8) != 0) {
			vertlist[3] = VertexInterp(isolevel, grid.verticesPosition[3], grid.verticesPosition[0],
					grid.verticesPointValue[3], grid.verticesPointValue[0]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 16) != 0) {
			vertlist[4] = VertexInterp(isolevel, grid.verticesPosition[4], grid.verticesPosition[5],
					grid.verticesPointValue[4], grid.verticesPointValue[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 32) != 0) {
			vertlist[5] = VertexInterp(isolevel, grid.verticesPosition[5], grid.verticesPosition[6],
					grid.verticesPointValue[5], grid.verticesPointValue[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 64) != 0) {
			vertlist[6] = VertexInterp(isolevel, grid.verticesPosition[6], grid.verticesPosition[7],
					grid.verticesPointValue[6], grid.verticesPointValue[7]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 128) != 0) {
			vertlist[7] = VertexInterp(isolevel, grid.verticesPosition[7], grid.verticesPosition[4],
					grid.verticesPointValue[7], grid.verticesPointValue[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 256) != 0) {
			vertlist[8] = VertexInterp(isolevel, grid.verticesPosition[0], grid.verticesPosition[4],
					grid.verticesPointValue[0], grid.verticesPointValue[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 512) != 0) {
			vertlist[9] = VertexInterp(isolevel, grid.verticesPosition[1], grid.verticesPosition[5],
					grid.verticesPointValue[1], grid.verticesPointValue[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 1024) != 0) {
			vertlist[10] = VertexInterp(isolevel, grid.verticesPosition[2], grid.verticesPosition[6],
					grid.verticesPointValue[2], grid.verticesPointValue[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2048) != 0) {
			vertlist[11] = VertexInterp(isolevel, grid.verticesPosition[3], grid.verticesPosition[7],
					grid.verticesPointValue[3], grid.verticesPointValue[7]);
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
	 * between two verticesPosition, each with their own scalar value
	 */
	private Point3D VertexInterp(float isolevel, Point3D p1, Point3D p2, float valp1,
			float valp2) {
		float mu;
		final Point3D p = new Point3D();
		p.x = p.y = p.z = 0.0f;
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
		try {
			newFile = new File("bunnyFile.obj");
		    fileWriter = new FileWriter(newFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
