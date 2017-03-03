
public class LeftShiftValue {

	public static void main(String[] args) {
		int sample = 248;
		sample = 0xff000000 | (sample << 16) | (sample << 8) | sample;
		System.out.println(Integer.toHexString(sample));
	}

}
