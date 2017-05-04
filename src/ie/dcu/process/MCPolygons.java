package ie.dcu.process;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import ie.dcu.model.GridCell;
import ie.dcu.model.MCLookUpTables;
import ie.dcu.model.Point3D;
import ie.dcu.model.Triangle3D;
import ie.dcu.ui.ImageConstants;

public class MCPolygons {
	// obj data
	
	public List<Triangle3D> triangles;
	public List<Triangle3D> trilist;
	public List<Point3D> triNormallist;
	public int normalTriangle;
	public boolean closesides = true;
	File newFile;
	FileWriter fileWriter;
	File folderData = null;
	int totalTriangle = 0;
	ImageProcessUtil imageProcess = new ImageProcessUtil();
/*	public void generateFloodFilledData(File[] fileSelections, String currentDir) {
		folderData = new File(currentDir + "\\" + ImageConstants.FF_DATA_FOLDER);
		if (folderData.exists()) {
			// Process the images and create an obj file
			initiateMCProcess(fileSelections, currentDir);
		} else {
			// Create a folder and create images
			folderData.mkdir();
			createImagesUsingFloodFill(fileSelections, currentDir);
			// Process the images and create an obj file
			initiateMCProcess(fileSelections, currentDir);
		}

	}

	private void createImagesUsingFloodFill(File[] fileSelections, String currentDir) {
		Arrays.sort(fileSelections, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Integer.parseInt(f1.getName()) - Integer.parseInt(f2.getName());
			}
		});
		ImageProcessUtil imageProcess = new ImageProcessUtil();
		for (int i = 0; i < fileSelections.length; i++) {
			imageProcess.imageFilesFetchDataFF(i, fileSelections[i], currentDir);
		}

	}*/

	public void initiateMCProcess(File[] fileSelections, String currentDir, boolean rawImage) {
		int totalSlices = fileSelections.length;
		Arrays.sort(fileSelections, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Integer.parseInt(f2.getName()) - Integer.parseInt(f1.getName());
			}
		});
		initResolution();
		long start = System.currentTimeMillis();
		for (int i = 0; i < totalSlices-1; i++) {
			initializeCubeGridCreation(fileSelections[i].getName(), fileSelections[i+1].getName(), i, currentDir, rawImage);
		}
		try {
			writeVertices();
			writeNormals();
			writeFaces();
			//freeDataStructures();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null, "Total time to process the data set  : " + (float)((end-start)/1000f) + " seconds");
	}

	private void writeVertices() throws IOException {
		for (Iterator<Triangle3D> iterator = trilist.iterator(); iterator.hasNext();) {
			Triangle3D triangle3d = (Triangle3D) iterator.next();
			fileWriter.write("v " + triangle3d.points[0].x + " " + triangle3d.points[0].y + " "
					+ triangle3d.points[0].z);
			fileWriter.write("\n");
			fileWriter.write("v " + triangle3d.points[1].x + " " + triangle3d.points[1].y + " "
					+ triangle3d.points[1].z);
			fileWriter.write("\n");
			fileWriter.write("v " + triangle3d.points[2].x + " " + triangle3d.points[2].y + " "
					+ triangle3d.points[2].z);
			fileWriter.write("\n");
		}
	}
	
	private void writeNormals() throws IOException {
		for (Iterator<Point3D> iterator = triNormallist.iterator(); iterator.hasNext();) {
			Point3D pointNormal = (Point3D) iterator.next();
			fileWriter.write("vn " + pointNormal.x + " " + pointNormal.y + " "
					+ pointNormal.z);
			fileWriter.write("\n");
		}
	}

	private void writeFaces() throws IOException {
		System.out.println(trilist.size());
		int i = 1;
		for (int j = 0; j < trilist.size(); j++) {
			fileWriter.write("f " + i + " " + (i+1) + " " + (i+2));
			fileWriter.write("\n");
			i+=3;
		}
		fileWriter.flush();
		fileWriter.close();
	}
	
	private void generateMarchingCubePolygons(GridCell gridCell) {
			int numberTriangles = Polygonise(gridCell);
			// calc tri norms
			for (int indx = 0; indx < numberTriangles; indx++) {
				triNormallist.add(triangles.get(indx).calcnormal());
			}
			totalTriangle += numberTriangles;
			for(Triangle3D  triangle: triangles) {
				trilist.add(triangle);
			}
			triangles.clear();
	}

	private int Polygonise(GridCell gridCell) {
		Point3D vertlist[] = new Point3D[12];
		float isolevel = ImageConstants.ISO_VALUE;
		
		 /* Determine the index into the edge table which tells us which
		 * verticesPosition are inside of the surface*/
		 
		int cubeindex = 0;
		if (gridCell.verticesPointValue[0] < isolevel) {
			cubeindex |= 1;
		}
		if (gridCell.verticesPointValue[1] < isolevel) {
			cubeindex |= 2;
		}
		if (gridCell.verticesPointValue[2] < isolevel) {
			cubeindex |= 4;
		}
		if (gridCell.verticesPointValue[3] < isolevel) {
			cubeindex |= 8;
		}
		if (gridCell.verticesPointValue[4] < isolevel) {
			cubeindex |= 16;
		}
		if (gridCell.verticesPointValue[5] < isolevel) {
			cubeindex |= 32;
		}
		if (gridCell.verticesPointValue[6] < isolevel) {
			cubeindex |= 64;
		}
		if (gridCell.verticesPointValue[7] < isolevel) {
			cubeindex |= 128;
		}
		
		/* Cube is entirely in/out of the surface */
		if (MCLookUpTables.edgeTable[cubeindex] == 0) {
			return 0;
		}
		/* Find the vertices where the surface intersects the cube */
		// int temp = edgeTable[cubeindex] & 1;
		if ((MCLookUpTables.edgeTable[cubeindex] & 1) != 0) {
			vertlist[0] = VertexInterp(isolevel, gridCell.verticesPosition[0], gridCell.verticesPosition[1],
					gridCell.verticesPointValue[0], gridCell.verticesPointValue[1]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2) != 0) {
			vertlist[1] = VertexInterp(isolevel, gridCell.verticesPosition[1], gridCell.verticesPosition[2],
					gridCell.verticesPointValue[1], gridCell.verticesPointValue[2]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 4) != 0) {
			vertlist[2] = VertexInterp(isolevel, gridCell.verticesPosition[2], gridCell.verticesPosition[3],
					gridCell.verticesPointValue[2], gridCell.verticesPointValue[3]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 8) != 0) {
			vertlist[3] = VertexInterp(isolevel, gridCell.verticesPosition[3], gridCell.verticesPosition[0],
					gridCell.verticesPointValue[3], gridCell.verticesPointValue[0]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 16) != 0) {
			vertlist[4] = VertexInterp(isolevel, gridCell.verticesPosition[4], gridCell.verticesPosition[5],
					gridCell.verticesPointValue[4], gridCell.verticesPointValue[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 32) != 0) {
			vertlist[5] = VertexInterp(isolevel, gridCell.verticesPosition[5], gridCell.verticesPosition[6],
					gridCell.verticesPointValue[5], gridCell.verticesPointValue[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 64) != 0) {
			vertlist[6] = VertexInterp(isolevel, gridCell.verticesPosition[6], gridCell.verticesPosition[7],
					gridCell.verticesPointValue[6], gridCell.verticesPointValue[7]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 128) != 0) {
			vertlist[7] = VertexInterp(isolevel, gridCell.verticesPosition[7], gridCell.verticesPosition[4],
					gridCell.verticesPointValue[7], gridCell.verticesPointValue[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 256) != 0) {
			vertlist[8] = VertexInterp(isolevel, gridCell.verticesPosition[0], gridCell.verticesPosition[4],
					gridCell.verticesPointValue[0], gridCell.verticesPointValue[4]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 512) != 0) {
			vertlist[9] = VertexInterp(isolevel, gridCell.verticesPosition[1], gridCell.verticesPosition[5],
					gridCell.verticesPointValue[1], gridCell.verticesPointValue[5]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 1024) != 0) {
			vertlist[10] = VertexInterp(isolevel, gridCell.verticesPosition[2], gridCell.verticesPosition[6],
					gridCell.verticesPointValue[2], gridCell.verticesPointValue[6]);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2048) != 0) {
			vertlist[11] = VertexInterp(isolevel, gridCell.verticesPosition[3], gridCell.verticesPosition[7],
					gridCell.verticesPointValue[3], gridCell.verticesPointValue[7]);
		}

		// Create the triangle 
		int ntriang = 0;
		for (int i = 0; MCLookUpTables.triTable[cubeindex][i] != -1; i += 3) {
			Triangle3D trian = new Triangle3D(vertlist[MCLookUpTables.triTable[cubeindex][i]], vertlist[MCLookUpTables.triTable[cubeindex][i+1]], vertlist[MCLookUpTables.triTable[cubeindex][i+2]]);
			triangles.add(trian);
			ntriang++;
		}
		return ntriang;
	}

	 //* Linearly interpolate the position where an isosurface cuts an edge
	 //* between two verticesPosition, each with their own scalar value
	 
	private Point3D VertexInterp(float isolevel, Point3D p1, Point3D p2, float valp1, float valp2) {
		float mu = (isolevel-valp1)/ (valp2 - valp1);
		float px = p1.x + mu * (p2.x - p1.x);
		float py = p1.y + mu * (p2.y - p1.y);
		float pz = p1.z + mu * (p2.z - p1.z);
		return new Point3D(px, py, pz);
	}

	public void initializeCubeGridCreation(String firstFile, String secondFile, int index, String currentDir, boolean rawImage) {
		GridCell gridCell = new GridCell();
		Map<Point3D, Float> interpolationData = imageProcess.saveInterpolationPoints(firstFile, secondFile, index, currentDir);
		for (int i = 0; i < ImageConstants.ROWS-1; i++) {
			for (int j = 0; j < ImageConstants.COLUMNS-1; j++) {
				// Point 0
				gridCell.verticesPosition[0].x = i; 
				gridCell.verticesPosition[0].y = j; 
				gridCell.verticesPosition[0].z = index; 
				gridCell.verticesPointValue[0] = interpolationData.get(new Point3D(i, j, index));
				
				// Point 1
				gridCell.verticesPosition[1].x = i; 
				gridCell.verticesPosition[1].y = (j+1); 
				gridCell.verticesPosition[1].z = index; 
				gridCell.verticesPointValue[1] = interpolationData.get(new Point3D(i, j+1, index));
				
				// Point 2
				gridCell.verticesPosition[2].x = i+1; 
				gridCell.verticesPosition[2].y = (j+1); 
				gridCell.verticesPosition[2].z = index; 
				gridCell.verticesPointValue[2] = interpolationData.get(new Point3D(i+1, j+1, index));
				
				// Point 3
				gridCell.verticesPosition[3].x = i+1; 
				gridCell.verticesPosition[3].y = j; 
				gridCell.verticesPosition[3].z = index; 
				gridCell.verticesPointValue[3] = interpolationData.get(new Point3D(i+1, j, index));
				
				// Point 4
				gridCell.verticesPosition[4].x = i; 
				gridCell.verticesPosition[4].y = j; 
				gridCell.verticesPosition[4].z = index+1; 
				gridCell.verticesPointValue[4] = interpolationData.get(new Point3D(i, j, index+1));
				
				// Point 5
				gridCell.verticesPosition[5].x = i; 
				gridCell.verticesPosition[5].y = (j+1); 
				gridCell.verticesPosition[5].z = index+1; 
				gridCell.verticesPointValue[5] = interpolationData.get(new Point3D(i, j+1, index+1));
				
				// Point 6
				gridCell.verticesPosition[6].x = i+1; 
				gridCell.verticesPosition[6].y = (j+1); 
				gridCell.verticesPosition[6].z = index+1; 
				gridCell.verticesPointValue[6] = interpolationData.get(new Point3D(i+1, j+1, index+1));
				
				// Point 7
				gridCell.verticesPosition[7].x = i+1; 
				gridCell.verticesPosition[7].y = j; 
				gridCell.verticesPosition[7].z = index+1; 
				gridCell.verticesPointValue[7] = interpolationData.get(new Point3D(i+1, j, index+1));
				generateMarchingCubePolygons(gridCell);
		
			}
		}
		// Clear the Map after every two slice read
		interpolationData.clear();
	}

	private void initResolution() {
		triangles = new ArrayList<Triangle3D>();
		trilist = new ArrayList<Triangle3D>();
		triNormallist = new ArrayList<Point3D>();
		trilist.clear();
		try {
			newFile = new File("bunny.obj");
			fileWriter = new FileWriter(newFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
