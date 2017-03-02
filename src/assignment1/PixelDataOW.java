package assignment1;

public class PixelDataOW {
	
	int bitsAllocated;
	int bitsStored;
	int highBit;
	int[] pixelData;
	
	private int sampleIndex;
	private int samplesPerWord;
	
	public PixelDataOW(int bitsAllocated, int bitsStored, int highBit, int[] pixelData) {
		this.bitsAllocated = bitsAllocated;
		this.bitsStored = bitsStored;
		this.highBit = highBit;
		this.pixelData = pixelData;
		
		this.sampleIndex = 0;
		this.samplesPerWord = 16 / bitsAllocated;
	}
	
	public int getNextSample() {
		if(bitsAllocated == 8 && bitsStored == 8 && highBit == 7)
			return getNextSample8_8_7();
		else if(bitsAllocated == 16 && bitsStored == 12 && highBit == 11)
			return getNextSample16_12_11();
		else 
			return -1; // unsupported
	}
	
	public int getNextSample8_8_7() {
		int wordIndex = sampleIndex / samplesPerWord;
		int word = pixelData[wordIndex];
		int byteIndex = sampleIndex % samplesPerWord;
		sampleIndex++;
		
		int sample = 0;
		if(byteIndex == 0)
			sample = word & 0xff;
		else
			sample = (word >> 8) & 0xff;
	
		return sample;
	}
	
	public int getNextSample16_12_11() {
		return pixelData[sampleIndex++] & 0x0FFF;
	}
	
	
}
