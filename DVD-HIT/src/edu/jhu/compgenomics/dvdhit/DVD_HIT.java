package edu.jhu.compgenomics.dvdhit;

import edu.jhu.compgenomics.dvdhit.filters.SequenceSimilarityFilter;
import edu.jhu.compgenomics.dvdhit.model.Cluster;
import edu.jhu.compgenomics.dvdhit.model.Sequence;
import edu.jhu.compgenomics.dvdhit.utils.FASTAUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Phani on 11/17/2015.
 */
public class DVD_HIT {

    /**
     * Clusters the given sequences into the given list of clusters, adding more
     * clusters as needed. Greedly clustering with the given filter
     *
     * @param clusters
     * @param sequences
     * @param filter
     */
    private void cluster(List<Cluster> clusters, List<Sequence> sequences, SequenceSimilarityFilter filter) {
        //Step 1, Sort by decreasing length
        Collections.sort(sequences, (o1, o2) -> -Integer.compare(o1.getSequence().length(), o2.getSequence().length()));

        //At this points, sequences is a list of to-be-clustered seqs
        Sequence sequence;
        for (int i = 0; i < sequences.size(); i++) {
            sequence = sequences.get(i);
            boolean added = false;
            for (Cluster cluster : clusters) {
                if (filter.isSimilar(cluster, sequence)) {
                    cluster.add(sequence);
                    added = true;
                    break;
                }
            }
            if (!added) {
                Cluster cluster = new Cluster();
                cluster.add(sequence);
                clusters.add(cluster);
            }
        }

        // Sort by descending cluster size for easier clstr file comparisons
        // Not truly neccessary, more for convenience
        Collections.sort(clusters, (o1, o2) -> -Integer.compare(o1.size(), o2.size()));
    }

    /**
     * Clusters the file using a multi-leveled approach.
     * The first of the filters is used for the initial clustering. Each subsequent
     * filter is used to subdivide each cluster into more.
     *
     * @param filters
     * @return
     */
    public List<Cluster> cluster(File file, List<SequenceSimilarityFilter> filters) {
        List<Cluster> clusters = new ArrayList<>();
        List<Sequence> sequences = FASTAUtils.readSequences(file);

        if (filters == null || filters.size() == 0) {
            Cluster cluster = new Cluster();
            cluster.addAll(sequences);
            clusters.add(cluster);
            return clusters;
        }

        //Initial clustering
        cluster(clusters, sequences, filters.get(0));

        if (filters.size() > 1) {
            // For each next filter, take each cluster, re-cluster it independently
            // with the new filter into splitClusters, and add the new clusters
            // to the newClusters list
            List<Cluster> newClusters = new ArrayList<>();
            for (int i = 1; i < filters.size(); i++) {
                for (Cluster c : clusters) {
                    List<Cluster> splitClusters = new ArrayList<>();
                    cluster(splitClusters, c, filters.get(i));
                    newClusters.addAll(splitClusters);
                }
            }
            clusters = newClusters;
        }

        return clusters;
    }
}
