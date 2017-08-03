package ie.dcu.model;

public class Interpolated {
    private final int xd, yd;
    private final float[] data;

    public Interpolated(int x, int y, int z) {
        this.xd = x;
        this.yd = y;
        data = new float[x * y * z];
    }

    public void set(int x, int y, int z, float d) {
        data[x + xd * y + xd * yd * z] = d;
    }

    public float get(int x, int y, int z) {
        return data[x + xd * y + xd * yd * z];
    }
}
