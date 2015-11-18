import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
                return -Integer.compare(o1.length(), o2.length());
            }
        });

        //At this points, sequences is a list of to-be-clustered seqs
        int lastSize = sequences.size();
        while (sequences.size() > 0) {
            if (sequences.size() < (lastSize - 100)) {
                System.out.println(sequences.size() + " left to cluster");
                lastSize = sequences.size();
            }
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

    private List<String> readSequences(File fileName) {
        List<String> sequences = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder currentSeq = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (currentSeq != null) {
                        sequences.add(currentSeq.toString());
                    }
                    currentSeq = new StringBuilder();
                } else {
                    currentSeq.append(line.trim());
                }
            }
            if (currentSeq != null) {
                sequences.add(br.toString());
            }
            br.close();
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return sequences;
    }

}
