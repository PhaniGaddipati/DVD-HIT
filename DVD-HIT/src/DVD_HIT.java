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

    public List<Cluster> cluster(SequenceSimilarityFilter filter, File outFile) throws IOException {
        List<Cluster> clusters = new ArrayList<Cluster>();
        List<Sequence> sequences = readSequences(this.file);

        //Step 1, Sort by decreasing length
        Collections.sort(sequences, new Comparator<Sequence>() {
            @Override
            public int compare(Sequence o1, Sequence o2) {
                return -Integer.compare(o1.getSequence().length(), o2.getSequence().length());
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
            for (Iterator<Sequence> it = sequences.iterator(); it.hasNext(); ) {
                Sequence seq = it.next();
                if (filter.isSimilar(cluster, seq)) {
                    cluster.add(seq);
                    it.remove();
                }
            }

            clusters.add(cluster);
        }

        // Sort by descending cluster size
        Collections.sort(clusters, new Comparator<Cluster>() {
            @Override
            public int compare(Cluster o1, Cluster o2) {
                return -Integer.compare(o1.size(), o2.size());
            }
        });

        if (outFile != null) {
            ClstrFileUtils.writeClstrFile(clusters, outFile);
        }

        return clusters;
    }

    private List<Sequence> readSequences(File fileName) {
        List<Sequence> sequences = new ArrayList<Sequence>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder currentSeq = null;
            String seqName = null;
            String seqDescription = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    seqName = line.substring(1, line.indexOf(" "));
                    seqDescription = line.substring(line.indexOf(" ") + 1);
                    if (currentSeq != null) {
                        sequences.add(new Sequence(seqName, seqDescription, currentSeq.toString()));
                    }
                    currentSeq = new StringBuilder();
                } else {
                    currentSeq.append(line.trim());
                }
            }
            if (currentSeq != null) {
                sequences.add(new Sequence(seqName, seqDescription, currentSeq.toString()));
            }
            br.close();
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return sequences;
    }

}
