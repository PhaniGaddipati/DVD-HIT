import filters.SequenceSimilarityFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Phani on 11/17/2015.
 */
public class DVD_HIT {

    private File file;

    public DVD_HIT(File inFile) {
        this.file = inFile;
    }

    public List<Cluster> cluster(SequenceSimilarityFilter filter) throws IOException {
        List<Cluster> clusters = new ArrayList<Cluster>();
        List<String> sequences = readSequences(file);

        //Step 1, Sort by decreasing length
        Collections.sort(sequences, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });

        //At this points, sequences is a list of to-be-clustered seqs
        while (sequences.size() > 0) {
            //Start cluster
            Cluster cluster = new Cluster();
            cluster.add(sequences.remove(0));

            // Add any sequences that are similar to the cluster
            for (Iterator<String> it = sequences.iterator(); it.hasNext(); ) {
                String seq = it.next();
                if (filter.isSimilar(cluster, seq)) {
                    cluster.add(seq);
                    it.remove();
                }
            }

            clusters.add(cluster);
        }
        //Now
        return clusters;
    }

    private List<String> readSequences(File file) {
        return Collections.emptyList();
    }

}
