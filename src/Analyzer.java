
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Analyzer {

    public static void main(String[] args) {

        if(args.length != 4) {
            failWithMessage("Incorrect number of arguments: required 4");
        }
        int n = validateNGram(args[0]);
        int s = validateSlide(args[1], n);
        File inFile = validateFile(args[2]);

        byte[] data = new byte[n * 1024];
        byte[] window;

        HashMap<NGram, Long> nGrams = new HashMap<NGram, Long>();

        try {
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
            for(NGram ng : nGrams.keySet()) {
                long count = nGrams.get(ng);
                //System.out.println("Count for bytes " + ng.toString() + " is: " + count);
                //System.out.println("% of total: " + 100L * (count / total));
                total += count;
            }
            Map<NGram, Long> sortedByCount = MapUtil.sortByValue(nGrams);
            for(NGram ng : sortedByCount.keySet()) {
                System.out.println(ng.toString() + " has count " + nGrams.get(ng));
            }

            System.out.println("Total bytes is: " + total);

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
        System.out.println("file size " + file.length());
        return file;
    }

    private static void failWithMessage(String msg) {
        System.out.println(msg);
        System.exit(1);
    }

    /*
    http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     */
    public static class MapUtil
    {
        public static <K, V extends Comparable<? super V>> Map<K, V>
        sortByValue( Map<K, V> map )
        {
            List<Map.Entry<K, V>> list =
                    new LinkedList<Map.Entry<K, V>>( map.entrySet() );
            Collections.sort( list, new Comparator<Map.Entry<K, V>>()
            {
                //http://stackoverflow.com/questions/5108091/java-comparator-for-byte-array-lexicographic
                public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
                {
                    byte[] left = ((NGram) o1.getKey()).getData();
                    byte[] right = ((NGram) o2.getKey()).getData();
                    for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                        int a = (left[i] & 0xff);
                        int b = (right[j] & 0xff);
                        if (a != b) {
                            return a - b;
                        }
                    }
                    return left.length - right.length;
                }
            } );


            Map<K, V> result = new LinkedHashMap<K, V>();
            for (Map.Entry<K, V> entry : list)
            {
                result.put( entry.getKey(), entry.getValue() );
            }
            return result;
        }
    }

}
