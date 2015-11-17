package filters;

/**
 * Created by Phani on 11/17/2015.
 */
public interface SequenceSimilarityFilter {

    /**
     * Returns if sequence is similar to ALL sequences in cluster
     *
     * @param cluster  The cluster to compare against
     * @param sequence The candidate sequence to add to cluster
     * @return Whether sequence is similar to EVERY seq in cluster
     */
    public boolean isSimilar(Cluster cluster, String sequence);

}
