import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Phani on 11/17/2015.
 */
public class DVD_HIT {

    private File file;

    public DVD_HIT(File inFile) {
        this.file = inFile;
    }

    /**
     * Usage: DVD_HIT input_file
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("USAGE: DVD_HIT <input_file>");
            System.exit(0);
        }
        File inFile = new File(args[0]);
        if (!inFile.exists() || inFile.isDirectory()) {
            System.out.println("Invalid input file!");
            System.exit(0);
        }

        try {
            List<Cluster> clusters = new DVD_HIT(inFile).cluster();
        } catch (IOException e) {
            System.out.println("Error using the specified file!");
        }
    }

    public List<Cluster> cluster() throws IOException {
        List<Cluster> clusters = new ArrayList<Cluster>();

        return clusters;
    }

    private List<String> readSequences(File file) {
        return Collections.emptyList();
    }

}
