package assignment1;

public class DicomByteOrderable {
	
	public static final int BYTE_ORDERING_LITTLE_ENDIAN = 1;
	public static final int BYTE_ORDERING_BIG_ENDIAN = 2;
	protected int byteOrdering;
	
	public DicomByteOrderable() {
		this(BYTE_ORDERING_LITTLE_ENDIAN);
	}
	
	public DicomByteOrderable(int byteOrdering) {
		this.byteOrdering = byteOrdering;
	}
	
	public void setByteOrdering(int byteOrdering) {
		this.byteOrdering = byteOrdering;
	}
	
	public int getByteOrdering() {
		return this.byteOrdering;
	}
}
