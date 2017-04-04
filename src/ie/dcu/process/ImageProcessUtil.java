package ie.dcu.process;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

public class ImageProcessUtil {
	private int[] pixelData;
	private InputStream inputStream;
	private int sampleIndex = 0;
	int min = 0;
	int max = 0;
	public ImageIcon imageFileProcess(String imageFile) {
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
		return extractGreyScaleImage();
		
	}
	
	private ImageIcon extractGreyScaleImage() {
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
				if (sample < ImageConstants.THRESHOLD) {
					sample = ImageConstants.BLACK;
				} else {
					sample = ImageConstants.WHITE;
				}
				// System.out.println("Sample x: " + x + "Sample y: " +y +
				// "value: " + sample);
				image.setRGB(x, y, 0xff000000 | (sample << 16) | (sample << 8) | sample);
			}
		}
		sampleIndex = 0;
		pixelData = null;
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
