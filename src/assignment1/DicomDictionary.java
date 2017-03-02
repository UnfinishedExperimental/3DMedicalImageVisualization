package assignment1;

public class DicomDictionary {
	
	public static String getFieldName(int groupNumber, int elementNumber) {
		return get(groupNumber, elementNumber).getFieldName();
	}
	
	public static String getValueRepresentation(int groupNumber, int elementNumber) {
		return get(groupNumber, elementNumber).getValueRepresentation();
	}
	
	public static int getValueMultiplicity(int groupNumber, int elementNumber) {
		return get(groupNumber, elementNumber).getValueMultiplicity();
	}
	
	
	public static DicomFieldDescription get(int groupNumber, int elementNumber) {
		switch(groupNumber) {
			case 0x0000:
				switch(elementNumber) {
					case 0x0000: return new DicomFieldDescription("Command Group Length", "UL", 1);
					case 0x0002: return new DicomFieldDescription("Affected SOP Class UID", "UI", 1);
					case 0x0100: return new DicomFieldDescription("Command Field", "US", 1);
					case 0x0110: return new DicomFieldDescription("Message ID", "US", 1);
					case 0x0120: return new DicomFieldDescription("Message ID BeingResponded To", "US", 1);
					case 0x0800: return new DicomFieldDescription("Command Data Set Type", "US", 1);
					case 0x0900: return new DicomFieldDescription("Status", "US", 1);
					default: return null;
				}
			case 0x0002:
				switch(elementNumber) {
					case 0x0000: return new DicomFieldDescription("File Meta Information Group Length", "UL", 1);
					case 0x0001: return new DicomFieldDescription("File Meta Information Version", "OB", 1);
					case 0x0002: return new DicomFieldDescription("Media Storage SOP Class UID", "UI", 1);
					case 0x0003: return new DicomFieldDescription("Media Storage SOP Instance UID", "UI", 1);
					case 0x0010: return new DicomFieldDescription("Transfer Syntax UID", "UI", 1);
					case 0x0012: return new DicomFieldDescription("Implementation Class UID", "UI", 1);
					case 0x0013: return new DicomFieldDescription("Implementation Version Name", "SH", 1);
					case 0x0016: return new DicomFieldDescription("Source Application Entity Title", "AE", 1);
					case 0x0017: return new DicomFieldDescription("Sending Application Entity Title", "AE", 1);
					case 0x0018: return new DicomFieldDescription("Receiving Application Entity Title", "AE", 1);
					case 0x0100: return new DicomFieldDescription("Private Information Creator UID", "UI", 1);
					case 0x0102: return new DicomFieldDescription("Private Information", "OB", 1);
					default: return null;
				}
			default: return null;
		}
	}
}
