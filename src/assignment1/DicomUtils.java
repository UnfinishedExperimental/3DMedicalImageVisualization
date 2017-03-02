package assignment1;

public class DicomUtils {
	public static String getHexTag(int groupNumber, int elementNumber) {
		String groupNumberString = Integer.toHexString(groupNumber).toUpperCase();
		groupNumberString = addLeadingZerosToHexString(groupNumberString);
		String elementNumberString = Integer.toHexString(elementNumber).toUpperCase();
		elementNumberString = addLeadingZerosToHexString(elementNumberString);
		return "("+groupNumberString+","+elementNumberString+")";
	}
	
	private static String addLeadingZerosToHexString(String hexString) {
		if(hexString.length() == 1)
			return "000" + hexString;
		else if(hexString.length() == 2)
			return "00" + hexString;
		else if(hexString.length() == 3)
			return "0" +hexString;
		else 
			return hexString;
	}
}
