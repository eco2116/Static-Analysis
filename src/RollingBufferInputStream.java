
import java.io.InputStream;
import java.io.IOException;

/**
 * http://tutorials.jenkov.com/java-howto/iterating-streams-using-buffers.html
 */
public class RollingBufferInputStream {

    private InputStream source = null;
    private byte[] buffer = null;
    private int start = 0;
    private int end = 0;
    private int bytesRead = 0;

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

    public void moveStart(int noOfBytesToMove) throws RollingBufferInputStreamException{
        if(this.start + noOfBytesToMove > this.end) {
            throw new RollingBufferInputStreamException("Attempt to move buffer 'start' beyond 'end'. start= "
                    + this.start + ", end: " + this.end + ", bytesToMove: " + noOfBytesToMove);
        }
        this.start += noOfBytesToMove;
    }

    public boolean hasAvailableBytes(int numberOfBytes) throws RollingBufferInputStreamException {
        if(!hasAvailableBytesInBuffer(numberOfBytes)){
            if(streamHasMoreData()){
                if(!bufferHasSpaceFor(numberOfBytes)) {
                    compact();
                }
                fillDataFromStreamIntoBuffer();
            }
        }
        return hasAvailableBytesInBuffer(numberOfBytes);
    }

    private void fillDataFromStreamIntoBuffer() throws RollingBufferInputStreamException {
        try {
            this.bytesRead  = this.source.read(this.buffer, this.end, this.buffer.length - this.end);
        } catch(IOException e) {
            throw new RollingBufferInputStreamException("Unexpected failure reading from rolling buffer input stream");
        }
        this.end += this.bytesRead;
    }

    private void compact() {
        int bytesToCopy = end - start;
        // TODO: array copy?
        for(int i=0; i<bytesToCopy; i++){
            this.buffer[i] = this.buffer[start + i];
        }
        this.start = 0;
        this.end = bytesToCopy;
    }

    private boolean bufferHasSpaceFor(int numberOfBytes) {
        return (this.buffer.length - this.start) >= numberOfBytes;
    }

    public boolean streamHasMoreData() {
        return this.bytesRead > -1;
    }

    private boolean hasAvailableBytesInBuffer(int numberOfBytes) {
        return (this.end - this.start) >= numberOfBytes;
    }

    public static class RollingBufferInputStreamException extends Exception {
        public RollingBufferInputStreamException(String msg) {
            super(msg);
        }
    }
}