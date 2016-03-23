import java.util.Arrays;

public class NGram {

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
}
