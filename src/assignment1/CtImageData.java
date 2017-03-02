package assignment1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CtImageData {

	private final String filename = "CTData.raw";
	private final int width = 512;
	private final int height = 512;
	private int[][] sampleData = null;
	
	
	public CtImageData() {
		try {
			sampleData = new int[512][512];
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			for(int y=0; y<height; y++)
				for(int x=0; x<width; x++) {
					sampleData[x][y] = raf.readShort();
				}
		}
		catch(FileNotFoundException fnfe) {
			System.out.println(filename+" not found. Make sure it is in the root directory of the project");
System.exit(0);
		}
		catch(IOException ioe) {
			System.out.println("error reading file");
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSample(int x, int y) {
		return sampleData[x][y];
	}
}
