
import java.io.InputStream;
import java.io.IOException;

/**
 * http://tutorials.jenkov.com/java-howto/iterating-streams-using-buffers.html
 */
public class RollingBufferInputStream {

    InputStream source    = null;

    protected byte[]      buffer    = null;
    protected int         start     = 0; //current location in buffer.
    protected int         end       = 0; //current limit of data read
    //into the buffer
    //= next element to read into.

    protected int         bytesRead = 0;

    public RollingBufferInputStream(InputStream source, byte[] buffer) {
        this.source = source;
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void moveStart(int noOfBytesToMove){
        if(this.start + noOfBytesToMove > this.end){
            throw new RuntimeException(
                    "Attempt to move buffer 'start' beyond 'end'. start= "
                            + this.start + ", end: " + this.end + ", bytesToMove: "
                            + noOfBytesToMove);
        }
        this.start += noOfBytesToMove;
    }

    public int availableBytes() {
        return this.end - this.start;
    }

    public boolean hasAvailableBytes(int numberOfBytes) throws IOException {
        if(! hasAvailableBytesInBuffer(numberOfBytes)){
            if(streamHasMoreData()){
                if(!bufferHasSpaceFor(numberOfBytes)){
                    compact();
                }
                fillDataFromStreamIntoBuffer();
            }
        }

        return hasAvailableBytesInBuffer(numberOfBytes);
    }

    private void fillDataFromStreamIntoBuffer() throws IOException {
        this.bytesRead  = this.source.read(this.buffer, this.end, this.buffer.length - this.end);
        this.end += this.bytesRead;
    }

    private void compact() {
        int bytesToCopy = end - start;

        for(int i=0; i<bytesToCopy; i++){
            this.buffer[i] = this.buffer[start + i];
        }

        this.start = 0;
        this.end   = bytesToCopy;
    }

    private boolean bufferHasSpaceFor(int numberOfBytes) {
        return (this.buffer.length - this.start) >= numberOfBytes;
    }

    public boolean streamHasMoreData() {
        return this.bytesRead > -1;
    }

    private boolean hasAvailableBytesInBuffer(int numberOfBytes) {
        return   (this.end - this.start) >= numberOfBytes;
    }

}