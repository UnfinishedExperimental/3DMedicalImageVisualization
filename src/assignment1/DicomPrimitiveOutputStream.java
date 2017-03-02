package assignment1;

import java.io.IOException;
import java.io.OutputStream;

public class DicomPrimitiveOutputStream extends DicomByteOrderable {
	
	protected OutputStream outputStream;
	
	public DicomPrimitiveOutputStream(OutputStream outputStream, int byteOrdering) {
		super(byteOrdering);
		this.outputStream = outputStream;
	}
	
	public void writeVR(String VR) throws IOException {
		writeString(VR);
	}
	
	public void writeString(String string) throws IOException {
		outputStream.write(string.getBytes());
	}
	
	public void writeUInt8(int value) throws IOException {
		outputStream.write(value);
	}

	public void writeUInt16(int value) throws IOException {
		int b0 = value & 0x00ff;
		int b1 = (value & 0xff00) >> 8;
		
		if(byteOrdering == BYTE_ORDERING_LITTLE_ENDIAN) {
			outputStream.write(b0);
			outputStream.write(b1);
		}
		else {
			outputStream.write(b1);
			outputStream.write(b0);
		}
	}

	public void writeUInt32(int value) throws IOException {
		int b0 = value & 0x000000ff;
		int b1 = (value & 0x0000ff00) >> 8;
		int b2 = (value & 0x00ff0000) >> 16;
		int b3 = (value & 0xff000000) >> 24;
		
		if(byteOrdering == BYTE_ORDERING_LITTLE_ENDIAN) {
			outputStream.write(b0);
			outputStream.write(b1);
			outputStream.write(b2);
			outputStream.write(b3);
		}
		else {
			outputStream.write(b3);
			outputStream.write(b2);
			outputStream.write(b1);
			outputStream.write(b0);
		}
	}
	
}
