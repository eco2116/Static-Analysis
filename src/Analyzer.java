
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;

public class Analyzer {

    public static void main(String[] args) {

        if(args.length != 4) {
            failWithMessage("Incorrect number of arguments: required 4");
        }
        int n = validateNgram(args[0]);
        int s = validateSlide(args[1], n);
        File inFile = validateFile(args[2]);

        byte[] data = new byte[n * 1024];
        byte[] window;

        HashMap<NGram, Long> ngrams = new HashMap<NGram, Long>();

        try {
            FileInputStream fis = new FileInputStream(inFile);
            RollingBufferInputStream bufferInput = new RollingBufferInputStream(fis, data);
            while(bufferInput.hasAvailableBytes(n)) {
                StringBuilder sb = new StringBuilder();

                window = Arrays.copyOfRange(bufferInput.getBuffer(), bufferInput.getStart(), bufferInput.getStart() + n);

                bufferInput.moveStart(s);
                NGram key = new NGram(window);
                if(ngrams.containsKey(key)) {
                    ngrams.put(key, ngrams.get(key) + 1);
                } else {
                    ngrams.put(key, 1L);
                }
            }
            fis.close();

            int total = 0;
            for(NGram ng : ngrams.keySet()) {
                long count = ngrams.get(ng);
                System.out.println("Count for bytes " + ng.toString() + " is: " + count);
                total += count;
            }
            System.out.println("Total bytes is: " + total);

        } catch(Exception e) {
            e.printStackTrace();
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
