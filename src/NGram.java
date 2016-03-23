import java.util.Arrays;

public class NGram implements Comparable<NGram> {

    private byte[] data;
    private int windowSize;

    public NGram(byte[] data) {
        this.data = data;
        this.windowSize = data.length;
    }

    public byte[] getData() {
        return data;
    }

    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(byte b: this.data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NGram && ((obj == this) || Arrays.equals(data, ((NGram) obj).getData()));
    }

    //http://stackoverflow.com/questions/5108091/java-comparator-for-byte-array-lexicographic
    @Override
    public int compareTo(NGram ng) {
        byte[] left = this.getData();
        byte[] right = ng.getData();

        for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
            int a = (left[i] & 0xff);
            int b = (right[j] & 0xff);
            if (a != b) {
                return a - b;
            }
        }
        return left.length - right.length;
    }
}
