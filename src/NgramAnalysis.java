
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class NgramAnalysis {

    private int n;
    private int s;
    private File file;
    HashMap<byte[], Long> byteHash;

    public NgramAnalysis(int n, int s, File inFile) {
        this.n = n;
        this.s = s;
        this.file = inFile;
        this.byteHash = new HashMap<byte[], Long>();
    }
    /*
    an integer n that is the length of the ngrams
    • an integer s that is the length of the slide
    • the name of the file to analyze
    • the name of the output file
     */
    public static void main(String[] args) {

        if(args.length != 4) {
            failWithMessage("Incorrect number of arguments: required 4");
        }
        int n = validateNgram(args[0]);
        int s = validateSlide(args[1], n);
        File inFile = validateFile(args[2]);

        byte[] data = new byte[n * 1024];
        byte[] window = new byte[n];

        NgramAnalysis ngramAnalysis = new NgramAnalysis(n, s, inFile);
        try {
            //ngramAnalysis.readFile();

            RollingBufferInputStream bufferInput = new RollingBufferInputStream(new FileInputStream(inFile), data);
            while(bufferInput.hasAvailableBytes(n)) {
                StringBuilder sb = new StringBuilder();

                window = Arrays.copyOfRange(bufferInput.getBuffer(), bufferInput.start, bufferInput.start + n);
                for(byte b: window) {
                    sb.append(String.format("%02X ", b));
                }
                System.out.println("Added to count for byte " + sb.toString());
                bufferInput.moveStart(s);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }


    }



//    int               blockSize   = 1024;
//    byte[]            buffer      = new byte[blockSize * 4];
//
//    RollingBufferInputStream bufferInput =
//            new RollingBufferInputStream(sourceInputStream, buffer);
//
//
//    while(bufferInput.hasAvailableBytes(blockSize)){
//
//        boolean matchFound = lookForMatch(
//                bufferInput.getBuffer(),
//                bufferInput.getStart(),
//                bufferInput.getEnd());
//
//        if(matchFound){
//            localFileSource.moveStart(this.blockSize);
//        } else {
//            localFileSource.moveStart(1);
//        }
//    }

    private void readFile() throws Exception {

        FileInputStream fileInputStream = new FileInputStream(file);
        byte data[] = new byte[n];

        int bytesRead;

        int offset;
        boolean start = true;
        LinkedList<Byte> list = new LinkedList<Byte>();
        // TODO: send bytes read
        while((bytesRead = fileInputStream.read(data)) != -1) {

            if(start) {
                for(int i=0; i<n; i++) {
                    list.add(data[i]);
                }
                start = false;
            } else {
                for(int i=0; i<s; i++) {
                    list.add(data[i]);
                }
            }

            System.out.println("Read " + bytesRead + " bytes");




            //byte[] filledBytes = Arrays.copyOf()
            // Increment count for bytes read
            //slideWindow(data);

        }
        fileInputStream.close();

    }

    private void slideWindow(byte[] buff) {
        // TODO: incomplete byte array
        System.out.println(buff.length / n);

        for(int i = 0; i < buff.length; i++) {
            int begin = i * n - (n - s);
            begin = begin < 0 ? 0 : begin;
            int end = ((i + 1) * n) - (n - s) + 1;

            System.out.println("reading from pos " + begin + " to " + end);
            byte[] window = Arrays.copyOfRange(buff, begin, end);

            StringBuilder sb = new StringBuilder();
            for(byte b: window) {
                if(b == 0) return;
                sb.append(String.format("%02X ", b));
            }
            System.out.println("Added to count for byte " + sb.toString());

            if (byteHash.containsKey(window)) {
                byteHash.put(window, byteHash.get(window) + 1);
            } else {
                byteHash.put(window, 0L);
            }


        }
    }

    private static int validateNgram(String input) {
        // For efficiency, only valid n are: 1, 2, 3
        int n = -1;
        try {
            n = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            failWithMessage("Invalid n: Only accept integer values");
        }

        if(n > 3 || n < 1) {
            failWithMessage("Invalid n: Only accept integers in range (1, 3)");
        }
        return n;
    }

    private static int validateSlide(String input, int n) {
        int s = -1;
        try {
            s = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Invalid s: Only accept integer values");
            System.exit(1);
        }
        if(s > n || s < 1) {
            failWithMessage("Invalid s: Only accept integers in range (1, 3)");
        }
        return s;
    }

    private static File validateFile(String name) {
        File file = new File(name);
        if(!file.exists() || !file.canRead()) {
            failWithMessage("Invalid file: either does not exist or is not readable");
        }
        System.out.println("file size " + file.length());
        return file;
    }

    private static void failWithMessage(String msg) {
        System.out.println(msg);
        System.exit(1);
    }

}
