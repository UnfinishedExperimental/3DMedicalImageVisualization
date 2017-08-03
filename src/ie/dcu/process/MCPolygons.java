package ie.dcu.process;

import com.google.common.io.Files;
import darwin.jopenctm.compression.MG2Encoder;
import darwin.jopenctm.data.AttributeData;
import darwin.jopenctm.data.Mesh;
import darwin.jopenctm.errorhandling.InvalidDataException;
import darwin.jopenctm.io.CtmFileWriter;
import ie.dcu.model.Interpolated;
import ie.dcu.model.MCLookUpTables;
import ie.dcu.model.Point3D;
import ie.dcu.model.Triangle3D;
import ie.dcu.ui.ImageConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class MCPolygons {
	public static final AttributeData[] EMPTY_ATTRIBUTES = new AttributeData[0];
	// obj data

	public Set<Triangle3D> triangleSet;
	public List<Triangle3D> triTotalList;
	public List<Point3D> triNormalList;
 	//CTM Data 
 	public float[] verticesCTM;
 	public int[] indicesCTM;
 	public float[] normalsCTM;
	
	public int normalTriangle;
	public boolean closesides = true;
	File folderData = null;
	int totalTriangle = 0;
	ImageProcessUtil imageProcess = new ImageProcessUtil();
	/*
	 * public void generateFloodFilledData(File[] fileSelections, String
	 * currentDir) { folderData = new File(currentDir + "\\" +
	 * ImageConstants.FF_DATA_FOLDER); if (folderData.exists()) { // Process the
	 * images and create an obj file initiateMCProcess(fileSelections,
	 * currentDir); } else { // Create a folder and create images
	 * folderData.mkdir(); createImagesUsingFloodFill(fileSelections,
	 * currentDir); // Process the images and create an obj file
	 * initiateMCProcess(fileSelections, currentDir); }
	 * 
	 * }
	 * 
	 * private void createImagesUsingFloodFill(File[] fileSelections, String
	 * currentDir) { Arrays.sort(fileSelections, new Comparator<File>() { public
	 * int compare(File f1, File f2) { return Integer.parseInt(f1.getName()) -
	 * Integer.parseInt(f2.getName()); } }); ImageProcessUtil imageProcess = new
	 * ImageProcessUtil(); for (int i = 0; i < fileSelections.length; i++) {
	 * imageProcess.imageFilesFetchDataFF(i, fileSelections[i], currentDir); }
	 * 
	 * }
	 */

	public void initiateMCProcess(File[] fileSelections, String currentDir, String dataFolder, boolean rawImage) {
		int totalSlices = fileSelections.length;
		if(rawImage) {
			Arrays.sort(fileSelections, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Integer.parseInt(f2.getName()) - Integer.parseInt(f1.getName());
				}
			});
		} else {
			Arrays.sort(fileSelections, new Comparator<File>() {
				public int compare(File f1, File f2) {
					String n1 = Files.getNameWithoutExtension(f1.getName()).trim() ;
					String n2 = Files.getNameWithoutExtension(f2.getName()).trim() ;
					return Integer.parseInt(n1) - Integer.parseInt(n2);
				}
			});
		}
		initResolution();
		long start = System.currentTimeMillis();
		for (int i = 0; i < totalSlices - 1; i++) {
			System.out.println("proccess slice "+i);
			if(rawImage){
				processRawImage(fileSelections[i].getName(), fileSelections[i + 1].getName(), i, currentDir, dataFolder);
			}else{
				processImage(fileSelections[i].getName(), fileSelections[i + 1].getName(), i, currentDir);
			}
		}

		saveVerticesCTM();
		saveNormalsCTM();
		saveIndicesCTM();
		createCTMFile(dataFolder);

		long end = System.currentTimeMillis();
		System.out.println(triTotalList.size());
		JOptionPane.showMessageDialog(null,
				"Total time to process the data set  : " + (float) ((end - start) / 1000f) + " seconds");
	}
	
	private void createCTMFile(String dataFolder) {
		try(OutputStream fos = new FileOutputStream(dataFolder + ".ctm")) {
 	 		CtmFileWriter writer = new CtmFileWriter(fos, new MG2Encoder());
			Mesh newM = new Mesh(verticesCTM, normalsCTM, indicesCTM, EMPTY_ATTRIBUTES, EMPTY_ATTRIBUTES);
			writer.encode(newM, "Reconstructed the file");
		} catch (IOException | InvalidDataException e) {
			e.printStackTrace();
		}
	}

	private void saveIndicesCTM() {
 		int indiceCount = triTotalList.size()*3;
 		indicesCTM = new int[indiceCount];
 		for (int i = 0; i < indiceCount; i+=3) {
 			indicesCTM[i] = i;
 			indicesCTM[i+1] = i+1;
 			indicesCTM[i+2] = i+2;
 		}
		
	}

	private void saveNormalsCTM() {
 		normalsCTM = new float[verticesCTM.length];
 		Iterator<Point3D> itr = triNormalList.iterator();
 		int i = 0;
	    while (itr.hasNext()){
	    	Point3D point = itr.next();
			for (int j = 0; j < 3; j++) {
				normalsCTM[i] = point.x;
				normalsCTM[i+1] = point.y;
				normalsCTM[i+2] = point.z;
				i +=3;
			}
	    }
		
	}

	private void saveVerticesCTM() {
 		int triCount = triTotalList.size();
 		verticesCTM = new float[triCount*9];
 		System.out.println("triCount" + triCount);
 		System.out.println("verticesCTM" + verticesCTM.length);
 		Iterator<Triangle3D> itr = triTotalList.iterator();
 		int vertIndx = 0;
	    while (itr.hasNext()){
	    	Triangle3D trian = itr.next();
 			verticesCTM[vertIndx++] = trian.points[0].x;
 			verticesCTM[vertIndx++] = trian.points[0].y;
 			verticesCTM[vertIndx++] = trian.points[0].z;
 			verticesCTM[vertIndx++] = trian.points[1].x;
 			verticesCTM[vertIndx++] = trian.points[1].y;
 			verticesCTM[vertIndx++] = trian.points[1].z;
 			verticesCTM[vertIndx++] = trian.points[2].x;
 			verticesCTM[vertIndx++] = trian.points[2].y;
 			verticesCTM[vertIndx++] = trian.points[2].z;
	    }
		
	}

	private void generateMarchingCubePolygons(int i, int j, int index, Interpolated interpolated) {
		int numberTriangles = Polygonise(i, j, index, interpolated);
		// calc tri norms
		Iterator<Triangle3D> itr = triangleSet.iterator();
	    while (itr.hasNext()){
	    	Triangle3D trian = itr.next();
	    	triNormalList.add(trian.calcnormal());
	    	triTotalList.add(trian);
	    }
		totalTriangle += numberTriangles;
		triangleSet.clear();
	}

	private int Polygonise(final int i, final int j, final int k, Interpolated interpolated) {
		Point3D vertlist[] = new Point3D[12];
		float isolevel = ImageConstants.ISO_VALUE;

		/*
		 * Determine the index into the edge table which tells us which
		 * verticesPosition are inside of the surface
		 */
/*
000
010
110
100
001
011
111
101
 */
float x0 = i, y0 = j, z0 = k;
float x1 = i, y1 = j+1, z1 = k;
float x2 = i+1, y2 = j+1, z2 = k;
float x3 = i+1, y3 = j, z3 = k;
float x4 = i, y4 = j, z4 = k+1;
float x5 = i, y5 = j+1, z5 = k+1;
float x6 = i+1, y6 = j+1, z6 = k+1;
float x7 = i+1, y7 = j, z7 = k+1;

float i0 = interpolated.get(i,j,0);
float i1 = interpolated.get(i,j+1,0);
float i2 = interpolated.get(i+1,j+1,0);
float i3 = interpolated.get(i+1,j,0);
float i4 = interpolated.get(i,j,1);
float i5 = interpolated.get(i,j+1,1);
float i6 = interpolated.get(i+1,j+1,1);
float i7 = interpolated.get(i+1,j,1);


		int cubeindex = 0;
		if (i0 < isolevel) {
			cubeindex |= 1;
		}
		if (i1 < isolevel) {
			cubeindex |= 2;
		}
		if (i2 < isolevel) {
			cubeindex |= 4;
		}
		if (i3 < isolevel) {
			cubeindex |= 8;
		}
		if (i4 < isolevel) {
			cubeindex |= 16;
		}
		if (i5 < isolevel) {
			cubeindex |= 32;
		}
		if (i6 < isolevel) {
			cubeindex |= 64;
		}
		if (i7 < isolevel) {
			cubeindex |= 128;
		}

		/* Cube is entirely in/out of the surface */
		if (MCLookUpTables.edgeTable[cubeindex] == 0) {
			return 0;
		}
		/* Find the vertices where the surface intersects the cube */
		// int temp = edgeTable[cubeindex] & 1;
		if ((MCLookUpTables.edgeTable[cubeindex] & 1) != 0) {
			vertlist[0] = VertexInterp(isolevel, x0,y0,z0, x1,y1,z1,
					i0, i1);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2) != 0) {
			vertlist[1] = VertexInterp(isolevel, x1,y1,z1, x2,y2,z2,
					i1,i2);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 4) != 0) {
			vertlist[2] = VertexInterp(isolevel, x2,y2,z2, x3,y3,z3,
					i2,i3);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 8) != 0) {
			vertlist[3] = VertexInterp(isolevel, x3,y3,z3, x0,y0,z0,
					i3,i0);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 16) != 0) {
			vertlist[4] = VertexInterp(isolevel, x4,y4,z4, x5,y5,z5,
					i4,i5);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 32) != 0) {
			vertlist[5] = VertexInterp(isolevel, x5,y5,z5, x6,y6,z6,
					i5,i6);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 64) != 0) {
			vertlist[6] = VertexInterp(isolevel, x6,y6,z6, x7,y7,z7,
					i6,i7);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 128) != 0) {
			vertlist[7] = VertexInterp(isolevel, x7,y7,z7, x4,y4,z4,
					i7,i4);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 256) != 0) {
			vertlist[8] = VertexInterp(isolevel, x0,y0,z0, x4,y4,z4,
					i0,i4);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 512) != 0) {
			vertlist[9] = VertexInterp(isolevel, x1,y1,z1, x5,y5,z5,
					i1,i5);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 1024) != 0) {
			vertlist[10] = VertexInterp(isolevel, x2,y2,z2, x6,y6,z6,
					i2,i6);
		}
		if ((MCLookUpTables.edgeTable[cubeindex] & 2048) != 0) {
			vertlist[11] = VertexInterp(isolevel, x3,y3,z3, x7,y7,z7,
					i3,i7);
		}

		// Create the triangle
		int ntriang = 0;
		for (int a = 0; MCLookUpTables.triTable[cubeindex][a] != -1; a += 3) {
			Triangle3D trian = new Triangle3D(vertlist[MCLookUpTables.triTable[cubeindex][a]],
					vertlist[MCLookUpTables.triTable[cubeindex][a + 1]],
					vertlist[MCLookUpTables.triTable[cubeindex][a + 2]]);
			triangleSet.add(trian);
			ntriang++;
		}
		return ntriang;
	}

	// * Linearly interpolate the position where an isosurface cuts an edge
	// * between two verticesPosition, each with their own scalar value

	private Point3D VertexInterp(float isolevel, float x1, float y1, float z1, float x2, float y2, float z2, float valp1, float valp2) {
		float mu = (isolevel - valp1) / (valp2 - valp1);
		float px = x1 + mu * (x2 - x1);
		float py = y1 + mu * (y2 - y1);
		float pz = z1 + mu * (z2 - z1);
		return new Point3D(px, py, pz);
	}

	private void processRawImage(String firstFile, String secondFile, int index, String currentDir, String dataFolder) {
		Interpolated interpolationData = imageProcess.saveInterpolationPoints(firstFile, secondFile, currentDir, dataFolder);
		for (int i = 0; i < ImageConstants.ROWS - 1; i++) {
            for (int j = 0; j < ImageConstants.COLUMNS - 1; j++) {
                generateMarchingCubePolygons(i, j, index, interpolationData);
            }
        }
	}

	private void processImage(String firstFile, String secondFile, int index, String currentDir) {
		try {
            folderData = new File(currentDir + "\\" + ImageConstants.BUNNY_DATA_CT);
            int sample1[] = new int[4];
            int sample2[] = new int[4];
            BufferedImage image1 = ImageIO.read(new File(folderData + "\\" + firstFile));
            BufferedImage image2 = ImageIO.read(new File(folderData + "\\" + secondFile));
            for (int i = 0; i < ImageConstants.ROWS-1; i++) {
                for (int j = 0; j < ImageConstants.COLUMNS-1; j++) {
                    // 4 samples from slice 1
                    sample1[0] = image1.getRGB(i, j) & 0x0FFF;
                    sample1[1] = image1.getRGB(i, j+1) & 0x0FFF;
                    sample1[2] = image1.getRGB(i+1, j+1) & 0x0FFF;
                    sample1[3] = image1.getRGB(i+1, j) & 0x0FFF;
                    // 4 samples from slice 2
                    sample2[0] = image2.getRGB(i, j) & 0x0FFF;
                    sample2[1] = image2.getRGB(i, j+1) & 0x0FFF;
                    sample2[2] = image2.getRGB(i+1, j+1) & 0x0FFF;
                    sample2[3] = image2.getRGB(i+1, j) & 0x0FFF;

                    // Point 0
//                    gridCell.verticesPosition[0].x = i;
//                    gridCell.verticesPosition[0].y = j;
//                    gridCell.verticesPosition[0].z = index;
//                    gridCell.verticesPointValue[0] = sample1[0];
//
//                    // Point 1
//                    gridCell.verticesPosition[1].x = i;
//                    gridCell.verticesPosition[1].y = (j+1);
//                    gridCell.verticesPosition[1].z = index;
//                    gridCell.verticesPointValue[1] = sample1[1];
//
//                    // Point 2
//                    gridCell.verticesPosition[2].x = i+1;
//                    gridCell.verticesPosition[2].y = (j+1);
//                    gridCell.verticesPosition[2].z = index;
//                    gridCell.verticesPointValue[2] = sample1[2];
//
//                    // Point 3
//                    gridCell.verticesPosition[3].x = i+1;
//                    gridCell.verticesPosition[3].y = j;
//                    gridCell.verticesPosition[3].z = index;
//                    gridCell.verticesPointValue[3] = sample1[3] ;
//
//                    // Point 4
//                    gridCell.verticesPosition[4].x = i;
//                    gridCell.verticesPosition[4].y = j;
//                    gridCell.verticesPosition[4].z = index+1;
//                    gridCell.verticesPointValue[4] = sample2[0];
//
//                    // Point 5
//                    gridCell.verticesPosition[5].x = i;
//                    gridCell.verticesPosition[5].y = (j+1);
//                    gridCell.verticesPosition[5].z = index+1;
//                    gridCell.verticesPointValue[5] = sample2[1];
//
//                    // Point 6
//                    gridCell.verticesPosition[6].x = i+1;
//                    gridCell.verticesPosition[6].y = (j+1);
//                    gridCell.verticesPosition[6].z = index+1;
//                    gridCell.verticesPointValue[6] = sample2[2];
//
//                    // Point 7
//                    gridCell.verticesPosition[7].x = i+1;
//                    gridCell.verticesPosition[7].y = j;
//                    gridCell.verticesPosition[7].z = index+1;
//                    gridCell.verticesPointValue[7] = sample2[3] ;
//                    generateMarchingCubePolygons(gridCell);
throw new UnsupportedOperationException();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void initResolution() {
		triangleSet = new LinkedHashSet<>();
		triTotalList = new ArrayList<>();
		triNormalList = new ArrayList<>();
	}
}
