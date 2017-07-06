package ie.dcu.process;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import ie.dcu.model.Point3D;
import ie.dcu.ui.ImageConstants;

public class ImageProcessUtil {
	private int[] pixelData;
	private int sampleIndex = 0;
	int min = 0;
	int max = 0;

	// This method is used for displaying the images in ImageViewer
	public ImageIcon imageFileProcess(boolean changeEvent, int sliceNumber, String imageFile) {
		File rawImageFile = new File(imageFile);
		try {
			InputStream inputStream = new FileInputStream(rawImageFile);
			// System.out.println("File Size of Image:: " +
			// rawImageFile.length());
			pixelData = readBitsPerWord((int) rawImageFile.length(), inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extractGreyScaleImage(changeEvent, sliceNumber);
	}
	
	private ImageIcon extractGreyScaleImage(boolean changeEvent, int sliceNumber) {
		BufferedImage image = new BufferedImage(ImageConstants.COLUMNS, ImageConstants.ROWS,
				BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < ImageConstants.ROWS; y++) {
			for (int x = 0; x < ImageConstants.COLUMNS; x++) {
				int sample = pixelData[sampleIndex++] & 0x0FFF;
				if (sample < min) {
					min = sample;
				}
				if (sample > max) {
					max = sample;
				}
			}
		}
		sampleIndex = 0;
		for (int y = 0; y < ImageConstants.ROWS; y++) {
			for (int x = 0; x < ImageConstants.COLUMNS; x++) {
				int sample = pixelData[sampleIndex++] & 0x0FFF;
				sample = (((sample - min) * 255) / (max - min)) + 0;
				if(sample<ImageConstants.THRESHOLD) {
					sample = ImageConstants.BLACK;
				} else {
					sample = ImageConstants.WHITE;
				}
				image.setRGB(x, y, 0xff000000 | (sample << 16) | (sample << 8) | sample);
			}
		}
		sampleIndex = 0;
		pixelData = null;
		new FloodFill().floodFill(image, new Point(0, 0), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(0, 500), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(500, 0), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(500, 500), Color.WHITE, Color.BLACK);
		ImageIcon imageIcon = new ImageIcon(image); // load the image to a
													// imageIcon
		return imageIcon;
	}
/*
	// This method is used for fetching data from raw images.
	public void imageFilesFetchDataFF(int sliceNumber, File rawImageFile, String currentDir) {
		int[] pixelData = new int[512*512];
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(rawImageFile);
			pixelData = readBitsPerWord((int) rawImageFile.length(), inStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageFilesCreateImageFF(inStream, pixelData, sliceNumber, currentDir);
	}

	// This method is used for creating the FloodFill images from raw images.
	private void imageFilesCreateImageFF(InputStream inStream, int[] pixelData, int sliceNumber, String currentDir) {
		BufferedImage image = new BufferedImage(ImageConstants.COLUMNS, ImageConstants.ROWS,
				BufferedImage.TYPE_INT_ARGB);
		int sampleIndexLocal = 0;
		int minLocal = 0;
		int maxLocal = 0;
		for (int y = 0; y < ImageConstants.ROWS; y++) {
			for (int x = 0; x < ImageConstants.COLUMNS; x++) {
				int sample = pixelData[sampleIndexLocal++] & 0x0FFF;
				if (sample < minLocal) {
					minLocal = sample;
				}
				if (sample > maxLocal) {
					maxLocal = sample;
				}
			}
		}
		sampleIndexLocal = 0;
		for (int y = 0; y < ImageConstants.ROWS; y++) {
			for (int x = 0; x < ImageConstants.COLUMNS; x++) {
				int sample = pixelData[sampleIndexLocal++] & 0x0FFF;
				sample = (((sample - minLocal) * 255) / (maxLocal - minLocal)) + 0;
				if(sample<ImageConstants.THRESHOLD) {
					sample = ImageConstants.BLACK;
				} else {
					sample = ImageConstants.WHITE;
				}
				image.setRGB(x, y, 0xff000000 | (sample << 16) | (sample << 8) | sample);
			}
		}
		sampleIndexLocal = 0;
		pixelData = null;
		new FloodFill().floodFill(image, new Point(0, 0), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(0, 500), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(500, 0), Color.WHITE, Color.BLACK);
		new FloodFill().floodFill(image, new Point(500, 500), Color.WHITE, Color.BLACK);
		try {
			ImageIO.write(image, "png", new File(currentDir + "\\" + ImageConstants.FF_DATA_FOLDER + "\\" +  (sliceNumber+1) + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
*/
	public Map<Point3D, Float> saveInterpolationPoints(String firstFile, String secondFile, int sliceNumber, String currentDir) {
		Map<Point3D, Float> interpolationData = new HashMap<Point3D, Float>();
		saveOneSliceperIteration(firstFile, sliceNumber, interpolationData, currentDir);
		saveOneSliceperIteration(secondFile, sliceNumber+1, interpolationData, currentDir);
		return interpolationData;
	}

	private void saveOneSliceperIteration(String fileName, int sliceNumber, Map<Point3D, Float> interpolationData, String currentDir) {
		int[] pixelDataLocal = null;
		int sampleIndexLocal = 0;
		int minLocal = 0;
		int maxLocal = 0;
		byte[] byteArray = null;
		File rawImageFile = new File(currentDir  + "\\" + ImageConstants.RAW_DATA_FOLDER+ "\\" +fileName);
		try {
			byteArray = com.google.common.io.Files.toByteArray(rawImageFile);
			pixelDataLocal = new int[byteArray.length/2];
			for (int i = 0,j =0; i < byteArray.length; i+=2, j++) {
				int sample =  (byteArray[i+1] &0x000000ff) | ((byteArray[i] &0x000000ff) << 8) & 0x0FFF;
				pixelDataLocal[j] = sample;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int y = 0; y < ImageConstants.ROWS*ImageConstants.COLUMNS; y++) {
			int sample = pixelDataLocal[sampleIndexLocal++];
			if (sample < minLocal) {
				minLocal = sample;
			}
			if (sample > maxLocal) {
				maxLocal = sample;
			}
		}
		sampleIndexLocal = 0;
		for (int y = 0; y < ImageConstants.ROWS; y++) {
			for (int x = 0; x < ImageConstants.COLUMNS; x++) {
				int sample = pixelDataLocal[sampleIndexLocal++] & 0x0FFF;
				sample = (((sample - minLocal) * 255) / (maxLocal - minLocal)) + 0;
				double distance = Math.sqrt(Math.pow(x-ImageConstants.CENTER, 2) + Math.pow(y-ImageConstants.CENTER, 2) );
				if(distance < ImageConstants.CENTER*0.80) {
					interpolationData.put(new Point3D(x, y, sliceNumber), (float) sample);
				} else {
					interpolationData.put(new Point3D(x, y, sliceNumber), (float) ImageConstants.BLACK);
				}
			}
		}
		sampleIndexLocal = 0;
		pixelDataLocal = null;
	}
	
	public int[] readBitsPerWord(int fileLength, InputStream inputStream) throws IOException {
		int numberOfWords = fileLength / 2;
		int[] wordString = new int[numberOfWords];
		for (int i = 0; i < numberOfWords; i++) {
			wordString[i] = readUInt16Bit(inputStream);
		}
		return wordString;
	}

	public int readUInt16Bit(InputStream inputStream) throws IOException {
		int b1 = inputStream.read();
		int b0 = inputStream.read();
		return (b1 << 8) | b0;
	}

}
