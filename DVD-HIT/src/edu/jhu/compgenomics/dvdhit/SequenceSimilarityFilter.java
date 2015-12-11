package edu.jhu.compgenomics.dvdhit;

/**
 * Created by Phani on 11/17/2015.
 */
public interface SequenceSimilarityFilter {

    /**
     * Returns if sequence belongs in the given cluster.
     *
     * @param cluster  The cluster to compare against
     * @param sequence The candidate sequence to add to cluster
     * @return Whether sequence is similar to EVERY seq in cluster
     */
    public boolean isSimilar(Cluster cluster, Sequence sequence);

    public String getName();

    public void setK(int k);

    public void setThreshold(double thresh);

}
