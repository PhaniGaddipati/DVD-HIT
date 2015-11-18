import java.util.HashSet;
import java.util.Set;

/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {

    private static final int MIN_K = 2;
    private static final int MAX_K = 5;
    private static final double THRESHOLD = .8;

    @Override
    public boolean isSimilar(Cluster cluster, String sequence) {
        String rep = cluster.getLongestSequence();
        if (rep == null) {
            return false;
        }
        for (int k = MIN_K; k <= MAX_K; k++) {
            //TODO Cache indexes
            Set<String> clusterIndex = getIndex(rep, k);
            int L = sequence.length();
            int needed = (int) Math.round(L - k + 1 - (1 - THRESHOLD) * k * L);
            int count = 0;
            for (int i = 0; i < L - k; i++) {
                if (clusterIndex.contains(sequence.substring(i, i + k))) {
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

    /**
     * Returns a Set of all kmers in str
     *
     * @param str The string to index
     * @param k   the length of kmers
     * @return A Set of all kmers
     */
    private Set<String> getIndex(String str, int k) {
        Set<String> index = new HashSet<String>();
        for (int i = 0; i < str.length() - k; i++) {
            index.add(str.substring(i, i + k));
        }
        return index;
    }

    @Override
    public String getName() {
        return "Short Word";
    }
}
