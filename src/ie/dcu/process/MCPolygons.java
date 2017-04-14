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

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import ie.dcu.model.GridCell;
import ie.dcu.model.MCLookUpTables;
import ie.dcu.model.Point3D;
import ie.dcu.model.Triangle3D;
import ie.dcu.ui.ImageConstants;

public class MCPolygons {
	// obj data
	public GridCell grid;
	public List<Triangle3D> triangles;
	public List<Triangle3D> trilist;
	public int normalTriangle;
	public boolean invertnormals = true;
	public boolean closesides = true;
	File newFile;
	FileWriter fileWriter;
	File folderData = null;
	int totalTriangle = 0;
	// 3D data for images.
	public static int[][][] gridSlicesData;

	public void generateFloodFilledData(File[] fileSelections, String currentDir) {
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

	}

	private void initiateMCProcess(File[] fileSelections, String currentDir) {
		int totalSlices = fileSelections.length;
		Arrays.sort(fileSelections, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Integer.parseInt(f1.getName()) - Integer.parseInt(f2.getName());
			}
		});
		gridSlicesData = new int[ImageConstants.ROWS][ImageConstants.COLUMNS][totalSlices];
		initResolution();
		for (int i = 0; i < totalSlices; i++) {
			fill3DArrayWithImageData(fileSelections[i].getName(), i, totalSlices);
		}
		for (int sliceNumber = 0; sliceNumber < totalSlices-1; sliceNumber++) {
			generateMarchingCubePolygons(sliceNumber);
		}
		System.out.println("Number of triangles:: " + trilist.size());	
		System.out.println("Number of triangles:: " + totalTriangle);	
		try {
			writeVertices(totalSlices);
			//writeFaces(totalSlices);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeVertices(int totalSlices) throws IOException {
/*		for (Iterator<Triangle3D> iterator = trilist.iterator(); iterator.hasNext();) {
			Triangle3D triangle3d = (Triangle3D) iterator.next();
			fileWriter.write("v " + (float)(triangle3d.points[0].x/ImageConstants.ROWS) + " " + (float)(triangle3d.points[0].y/ImageConstants.COLUMNS) + " "
					+ (float)(triangle3d.points[0].z/totalSlices));
			fileWriter.write("\n");
			fileWriter.write("v " + (float)(triangle3d.points[1].x/ImageConstants.ROWS) + " " + (float)(triangle3d.points[1].y/ImageConstants.COLUMNS) + " "
					+ (float)(triangle3d.points[1].z/totalSlices));
			fileWriter.write("\n");
			fileWriter.write("v " + (float)(triangle3d.points[2].x/ImageConstants.ROWS) + " " + (float)(triangle3d.points[2].y/ImageConstants.COLUMNS) + " "
					+ (float)(triangle3d.points[2].z/totalSlices));
			fileWriter.write("\n");
		}*/
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

	private void writeFaces(int totalSlices) throws IOException {
		int i = 1;
		for (int j = 0; j < trilist.size(); j++) {
			fileWriter.write("f " + i + " " + (i+1) + " "
					+ (i+2));
			fileWriter.write("\n");
			i++;
		}
		
	}
	
	private void generateMarchingCubePolygons(int z) {
		for (int x = 0; x < ImageConstants.ROWS-1; x++) { 
			// Point 0
			grid.verticesPosition[0].x = x; 
			grid.verticesPosition[0].y = x; 
			grid.verticesPosition[0].z = z; 
			grid.verticesPointValue[0] = gridSlicesData[x][x][z];
			
			// Point 1
			grid.verticesPosition[1].x = x; 
			grid.verticesPosition[1].y = (x+1); 
			grid.verticesPosition[1].z = z; 
			grid.verticesPointValue[1] = gridSlicesData[x][x+1][z];
			
			// Point 2
			grid.verticesPosition[2].x = x+1; 
			grid.verticesPosition[2].y = (x+1); 
			grid.verticesPosition[2].z = z; 
			grid.verticesPointValue[2] = gridSlicesData[x+1][x+1][z];
			
			// Point 3
			grid.verticesPosition[3].x = x+1; 
			grid.verticesPosition[3].y = x; 
			grid.verticesPosition[3].z = z; 
			grid.verticesPointValue[3] = gridSlicesData[x+1][x][z];
			
			// Point 4
			grid.verticesPosition[4].x = x; 
			grid.verticesPosition[4].y = x; 
			grid.verticesPosition[4].z = (z+1); 
			grid.verticesPointValue[4] = gridSlicesData[x][x][z+1];
			
			// Point 5
			grid.verticesPosition[5].x = x; 
			grid.verticesPosition[5].y = (x+1); 
			grid.verticesPosition[5].z = (z+1); 
			grid.verticesPointValue[5] = gridSlicesData[x][x+1][z+1];
			
			// Point 6
			grid.verticesPosition[6].x = (x+1); 
			grid.verticesPosition[6].y = (x+1); 
			grid.verticesPosition[6].z = (z+1); 
			grid.verticesPointValue[6] = gridSlicesData[x+1][x+1][z+1];
			
			// Point 7
			grid.verticesPosition[7].x = (x+1); 
			grid.verticesPosition[7].y = x; 
			grid.verticesPosition[7].z = (z+1); 
			grid.verticesPointValue[7] = gridSlicesData[x+1][x][z+1];
			int numberTriangles = Polygonise(grid);
			totalTriangle += numberTriangles;
			for(Triangle3D  triangle: triangles) {
				trilist.add(triangle);
			}
			triangles.clear();
}
	}

	private int Polygonise(GridCell cubeGrid) {
		Point3D vertlist[] = new Point3D[12];
		float isolevel = ImageConstants.ISO_VALUE;
		
		 /* Determine the index into the edge table which tells us which
		 * verticesPosition are inside of the surface*/
		 
		int cubeindex = 0;
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
		
		/* Cube is entirely in/out of the surface */
		if (MCLookUpTables.edgeTable[cubeindex] == 0) {
			return 0;
		}
		/* Find the vertices where the surface intersects the cube */
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
		float mu;
		final Point3D p = new Point3D();
		p.x = p.y = p.z = 0.0f;
		mu = ((isolevel - valp1) / (valp2 - valp1));
		p.x = p1.x + mu * (p2.x - p1.x);
		p.y = p1.y + mu * (p2.y - p1.y);
		p.z = p1.z + mu * (p2.z - p1.z);

		return (p);
	}

	public void fill3DArrayWithImageData(String currentFileName, int indexFile, int totalFiles) {
		int min = 0;
		int max = 0;
		try {
			BufferedImage image = ImageIO.read(new File(folderData + "\\" + currentFileName + ".png"));
			//System.out.println(folderData + "\\" + currentFileName + ".png");
			for (int i = 0; i < ImageConstants.ROWS; i++) {
				for (int j = 0; j < ImageConstants.COLUMNS; j++) {
					int sample = image.getRGB(i, j) & 0x0FFF;
					if (sample < min) {
						min = sample;
					}
					if (sample > max) {
						max = sample;
					}
				}
			}
			for (int i = 0; i < ImageConstants.ROWS; i++) {
				for (int j = 0; j < ImageConstants.COLUMNS; j++) {
					int sample = image.getRGB(i, j) & 0x0FFF;
					sample = (((sample - min) * 255) / (max - min)) + 0;
					gridSlicesData[i][j][indexFile] = sample;
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, currentFileName);
			e.printStackTrace();
		}
	}

	private void initResolution() {
		triangles = new ArrayList<Triangle3D>();
		trilist = new ArrayList<Triangle3D>();
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
