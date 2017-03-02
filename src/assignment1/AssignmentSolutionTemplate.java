package assignment1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AssignmentSolutionTemplate {

	public static void main(String args[]) {
		new AssignmentSolutionTemplate();
	}
	
	public AssignmentSolutionTemplate() {
		
		// Print out the CT image data to the console
		CtImageData ctImageData = new CtImageData();
		for(int y=0; y<512; y++)
			for(int x=0; x<512; x++)
				System.out.println(ctImageData.getSample(x, y));
		
		try {
	
			DicomValueRepresentationOutputStream outputStream = 
					new DicomValueRepresentationOutputStream(new FileOutputStream("assignment.dcm"), 
					DicomValueRepresentationOutputStream.BYTE_ORDERING_LITTLE_ENDIAN);
		
			
			outputStream.close();
		}
		catch(FileNotFoundException fnfe) {
			
		}
		catch(IOException ioe) {
			
		}
	}
}
