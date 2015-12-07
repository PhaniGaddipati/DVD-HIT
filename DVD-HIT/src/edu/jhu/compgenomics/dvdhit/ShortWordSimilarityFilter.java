package edu.jhu.compgenomics.dvdhit;

/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {

    private static final int MIN_K = 2;
    private static final int MAX_K = 5;
    private static final double THRESHOLD = .9;

    @Override
    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (rep == null) {
            return false;
        }
        for (int k = MIN_K; k <= MAX_K; k++) {
            int L = sequence.getSequence().length();
            int needed = (int) Math.round(L - k + 1 - (1 - THRESHOLD) * k * L);
            int count = 0;
            for (int i = 0; i < L - k; i++) {
                if (sequence.getSequence().substring(i, i + k)
                        .equals(rep.getSequence().substring(i, i + k))) {
                    count++;
                }
                if (count >= needed) {
                    //Passed this kmer filter
                    break;
                }
            }
            if (count < needed) {
                //Failed
                return false;
            }
        }

        return true;
    }

    @Override
    public String getName() {
        return "Short Word";
    }
}
