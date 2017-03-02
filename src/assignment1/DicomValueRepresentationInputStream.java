package assignment1;

import java.io.IOException;
import java.io.InputStream;

public class DicomValueRepresentationInputStream extends DicomPrimitiveInputStream {

	public DicomValueRepresentationInputStream(InputStream inputStream, int byteOrdering) {
		super(inputStream, byteOrdering);
	}
	
	public int readUS() throws IOException {
		return readUInt16();
	}
	
	public String readUI(int length) throws IOException {
		return readString(length);
	}
	
	public int[] readOW(int length) throws IOException {
		int numberOfWords = length/2;
		int[] otherWordString = new int[numberOfWords];
		for(int i=0; i<numberOfWords; i++) {
			otherWordString[i] = readUInt16();
		}
		return otherWordString;
			
	}
}
