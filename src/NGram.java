import java.util.Arrays;

/**
 * Evan O'Connor (eco2116)
 * Network Security
 * Programming Assignment 3
 *
 * NGram.java
 *
 * A wrapper around the n-gram (array of bytes) that can be sorted as stated in the assignment and pretty
 * printed in hex format.
 */
public class NGram implements Comparable<NGram> {

    private byte[] data;

    public NGram(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        // String to print out hex value of byte
        StringBuilder sb = new StringBuilder();
        for(byte b: this.data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        // Use Arrays class to generate custom hash of bytes in the data array
        return Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(Object obj) {
        // Use Arrays class to generate custom equals function using bytes in data array
        return obj instanceof NGram && ((obj == this) || Arrays.equals(data, ((NGram) obj).getData()));
    }

    /*
        Custom comparator so bytes with lower values will be returned in top n-grams (e.g.: 0x00 before 0x01)
        Resource used: http://stackoverflow.com/questions/5108091/java-comparator-for-byte-array-lexicographic
     */
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
