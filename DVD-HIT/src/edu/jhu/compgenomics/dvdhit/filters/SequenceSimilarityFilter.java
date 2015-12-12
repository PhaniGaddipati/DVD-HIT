package edu.jhu.compgenomics.dvdhit.filters;

import edu.jhu.compgenomics.dvdhit.model.Cluster;
import edu.jhu.compgenomics.dvdhit.model.Sequence;

/**
 * A filter that is responsible for determining if a sequence
 * belongs in a given cluster.
 */
public interface SequenceSimilarityFilter {

    /**
     * Returns if sequence belongs in the given cluster.
     *
     * @param cluster  The cluster to compare against
     * @param sequence The candidate sequence to add to cluster
     * @return Whether sequence is similar to EVERY seq in cluster
     */
    boolean isSimilar(Cluster cluster, Sequence sequence);

    String getName();

    void setK(int k);

    void setThreshold(double thresh);

}
