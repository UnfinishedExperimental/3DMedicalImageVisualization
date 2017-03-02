package assignment1;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DicomImageReader {

	public DicomImageReader() {
		try {
		System.out.println("Starting CT Image Reader");
		FileInputStream fileInputStream = new FileInputStream(
				"CT-MONO2-8-abdo");
		
		// Group 0002 always written with little endian explicit
		DicomValueRepresentationInputStream inputStream = 
				new DicomValueRepresentationInputStream(fileInputStream, 
				DicomValueRepresentationInputStream.BYTE_ORDERING_LITTLE_ENDIAN);
		
		// Skip the 128 bytes of preamble
		inputStream.skip(128);
		
		// Read the prefix and make sure that it is equal to "DICM"
		String prefix = inputStream.readString(4);
		if(prefix.equals("DICM")) 
			System.out.println("Valid prefix ("+prefix+")");
		else {
			System.out.println("Invalid prefix ("+prefix+")");
			System.exit(0);
		}
		
		String transferSyntaxUID = null;
		
		// Read the file meta elements
		int groupNumber = inputStream.readUInt16();
		int elementNumber = inputStream.readUInt16();
		while(groupNumber == 0x0002) {
			System.out.print(DicomUtils.getHexTag(groupNumber, elementNumber));
			System.out.print(DicomDictionary.getFieldName(groupNumber, elementNumber));
			String valueRepresentation = inputStream.readString(2);
			System.out.print(" - "+valueRepresentation+" - ");
			
			// Cater for the two explicit VR scenarios
			int length = 0;
			if(valueRepresentation.equals("OB")|
					valueRepresentation.equals("OW") |
					valueRepresentation.equals("OF") |
					valueRepresentation.equals("SQ") |
					valueRepresentation.equals("UT") |
					valueRepresentation.equals("UN")) {
				inputStream.skip(2);
				length = inputStream.readUInt32();
			}
			else 
				length = inputStream.readUInt16();			
			System.out.println(length+" bytes - ");
			
			
			if(elementNumber == 0x0010)
				transferSyntaxUID = inputStream.readUI(length);
			else
				inputStream.skip(length);
		
			groupNumber = inputStream.readUInt16();
			elementNumber = inputStream.readUInt16();
		}
		
		int rows = 0, columns = 0;
		int bitsAllocated = 0, bitsStored = 0, highBit = 0;
		int[] pixelData = null;
		
		System.out.println("Negotiated Transfer Syntax UID: "+transferSyntaxUID);
		if(transferSyntaxUID.equals(
				DicomConstants.TRANSFER_SYNTAX_IMPLICIT_VR_LITTLE_ENDIAN)) {
			// No need to set byte ordering as it is already little endian
			while(true) {
				System.out.print(DicomUtils.getHexTag(groupNumber, elementNumber));
				
				int length = inputStream.readUInt32();
						
				System.out.println(length+" bytes - ");
				
				if(groupNumber == 0x0028) {
					if(elementNumber == 0x0010)
						rows = inputStream.readUS();
					else if(elementNumber == 0x0011)
						columns = inputStream.readUS();
					else if(elementNumber == 0x0100)
						bitsAllocated = inputStream.readUS();
					else if(elementNumber == 0x0101)
						bitsStored = inputStream.readUS();
					else if(elementNumber == 0x0102)
						highBit = inputStream.readUS();	
					else
						inputStream.skip(length);	
				}
				else if(groupNumber == 0x7FE0) {
					if(elementNumber == 0x0010)
						pixelData = inputStream.readOW(length);
					else 
						inputStream.skip(length);	
				}
				else
					inputStream.skip(length);	
				
				if(!inputStream.hasMoreData())
					break;
				
				groupNumber = inputStream.readUInt16();
				elementNumber = inputStream.readUInt16();
			}
		}
		decodeAndDisplayImageData(rows, columns, bitsAllocated, bitsStored, highBit, pixelData);
		
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	public void decodeAndDisplayImageData(int rows, int columns, 
			int bitsAllocated, int bitsStored, int highBit, int[] pixelData) {
		System.out.println("Rows: "+rows);
		System.out.println("Columns: "+columns);
		System.out.println("Bits Allocated: "+bitsAllocated);
		System.out.println("Bits Stored: "+bitsStored);
		System.out.println("High Bit: "+highBit);
		
		BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_ARGB);
		
		PixelDataOW pixelDataOW = new PixelDataOW(bitsAllocated, bitsStored, highBit, pixelData);
		for(int y=0; y<rows; y++)
			for(int x=0; x<columns; x++) {
				int sample = pixelDataOW.getNextSample();
				image.setRGB(x,y,0xff000000 | (sample << 16) | (sample << 8) | sample);
			}
			
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		new DicomImageReader();
	}
}
