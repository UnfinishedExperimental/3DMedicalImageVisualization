package assignment1;

public class DicomFieldDescription {
	
	private String fieldName;
	private String valueRepresentation;
	private int valueMultiplicity;
	
	public DicomFieldDescription(String fieldName, String valueRepresentation, int valueMultiplicity) {
		this.fieldName = fieldName;
		this.valueRepresentation = valueRepresentation;
		this.valueMultiplicity = valueMultiplicity;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public String getValueRepresentation() {
		return valueRepresentation;
	}
	
	public int getValueMultiplicity() {
		return valueMultiplicity;
	}
}
