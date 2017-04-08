package ie.dcu.process;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ImageProcessUtil {
	private int[] pixelData;
	public static int[][][] gridSlicesData;
	private InputStream inputStream;

	private int sampleIndex = 0;
	int min = 0;
	int max = 0;
	//int ones = 0;
	//int zeros = 0;
	public ImageIcon imageFileProcess(boolean changeEvent, int sliceNumber, String imageFile) {
		File rawImageFile = new File(imageFile);
		try {
			inputStream = new FileInputStream(rawImageFile);
			// System.out.println("File Size of Image:: " +
			// rawImageFile.length());
			pixelData = readBitsPerWord((int) rawImageFile.length());
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
				// Save value in the 3D array
				if(!changeEvent) {
					// System.out.println("Sample:" +  sample);
					gridSlicesData[x][y][sliceNumber] = sample;
				}
				if (sample < ImageConstants.THRESHOLD) {
					sample = ImageConstants.BLACK;
				} else {
					sample = ImageConstants.WHITE;
				}
				 image.setRGB(x, y, 0xff000000 | (sample << 16) | (sample << 8) | sample);
			}
		}
/*		System.out.println("-----11--------" + ones);
		System.out.println("-----00-------" + zeros);
		ones=0;
		zeros=0;*/
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
	
	public int[] readBitsPerWord(int fileLength) throws IOException {
		int numberOfWords = fileLength / 2;
		// System.out.println(" numberOfWords:: " + numberOfWords);
		int[] wordString = new int[numberOfWords];
		for (int i = 0; i < numberOfWords; i++) {
			wordString[i] = readUInt16Bit();
			// System.out.println(i + " :: " +wordString[i]);
		}
		return wordString;
	}

	public int readUInt16Bit() throws IOException {
		int b1 = inputStream.read();
		int b0 = inputStream.read();
		return (b1 << 8) | b0;
	}
	
}
