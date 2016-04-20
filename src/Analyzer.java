
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class Analyzer {

    public static void main(String[] args) {

        if(args.length != 4) {
            failWithMessage("Incorrect number of arguments: required 4");
        }
        int n = validateNGram(args[0]);
        int s = validateSlide(args[1], n);
        File inFile = validateFile(args[2]);
        System.out.println("Processing file [name = " + args[2] + ", size = " + inFile.length() +
                " bytes, n = " + n + ", s = " + s + "]");

        byte[] data = new byte[n * 1024];
        byte[] window;

        HashMap<NGram, Long> nGrams = new HashMap<NGram, Long>();

        try {
            // Create writer to output file
            PrintWriter printWriter = new PrintWriter(args[3]);

            // Create a rolling buffer input stream to read and process n-grams in a stream using sliding window
            FileInputStream fis = new FileInputStream(inFile);
            RollingBufferInputStream bufferInput = new RollingBufferInputStream(fis, data);


            while(bufferInput.hasAvailableBytes(n)) {

                window = Arrays.copyOfRange(bufferInput.getBuffer(), bufferInput.getStart(), bufferInput.getStart() + n);

                bufferInput.moveStart(s);
                NGram key = new NGram(window);
                if(nGrams.containsKey(key)) {
                    nGrams.put(key, nGrams.get(key) + 1);
                } else {
                    nGrams.put(key, 1L);
                }
            }
            fis.close();

            long total = 0;
            for(NGram ng : nGrams.keySet()) {
                long count = nGrams.get(ng);
                total += count;
            }

            Map<NGram, Long> sortedByCount = MapUtil.sortByValue(nGrams);
            int rank = 1;
            for(NGram ng : sortedByCount.keySet()) {
                if (rank > 20) { break; }
                long count = nGrams.get(ng);
                DecimalFormat df = new DecimalFormat("0.00##");
                String percent = df.format(((double) count / total) * 100);
                printWriter.write("#" + rank++ + " : " + ng.toString() + " has count " + nGrams.get(ng) +
                        " which is " + percent + " %\n");

            }
            printWriter.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static int validateNGram(String input) {
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
        return file;
    }

    private static void failWithMessage(String msg) {
        System.out.println(msg);
        System.out.println("Usage: java Analyzer <n> <s> <inputFile> <outputFile>");
        System.exit(1);
    }

    /*
    http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     */
    public static class MapUtil {
        public static <K, V extends Comparable<? super V>> Map<K, V>
        sortByValue(Map<K, V> map) {
            List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                // TODO: fix o1 o2 orders
                public int compare(Map.Entry<K, V> o2, Map.Entry<K, V> o1) {
                    if (o1.getValue().equals(o2.getValue())) {
                        return ((NGram) o1.getKey()).compareTo((NGram) o2.getKey());
                    }
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });

            Map<K, V> result = new LinkedHashMap<K, V>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }
    }

}
