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

    public List<Cluster> cluster() throws IOException {
        List<Cluster> clusters = new ArrayList<Cluster>();

        return clusters;
    }

    private List<String> readSequences(File file) {
        return Collections.emptyList();
    }

}
