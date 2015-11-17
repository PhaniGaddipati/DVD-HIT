import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Phani on 11/17/2015.
 */
public class MainApp {

    private static SequenceSimilarityFilter[] filters = new SequenceSimilarityFilter[]
            {new ShortWordSimilarityFilter()};

    /**
     * Usage: DVD_HIT input_file
     *
     * @param args
     */
    public static void main(String[] args) {
        if (validateArgs(args)) {
            try {
                System.out.println("Clustering...");
                long startTime = System.currentTimeMillis();
                List<Cluster> clusters = new DVD_HIT(new File(args[0]))
                        .cluster(filters[Integer.parseInt(args[1])]);
                long stopTime = System.currentTimeMillis();
                System.out.println("Finished in " + (stopTime - startTime) / 1000 + " seconds.");

            } catch (IOException e) {
                System.out.println("Error using the specified file!");
            }
        } else {
            printUsage();
        }
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            return false;
        }
        try {
            int filter = Integer.parseInt(args[1]);
            if (filter < 0 || filter >= filters.length) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        File inFile = new File(args[0]);
        if (!inFile.exists() || inFile.isDirectory()) {
            return false;
        }
        return true;
    }

    private static void printUsage() {
        System.out.println("USAGE: DVD_HIT <input_file> <filter>");
        System.out.println("FILTERS: ");
        for (int i = 0; i < filters.length; i++) {
            System.out.println("\t" + i + ":\t" + filters[i].getName());
        }
        System.out.println();
    }

}
