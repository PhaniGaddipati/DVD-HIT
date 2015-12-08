package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {

    private static final int MIN_K = 8;
    private static final int MAX_K = 8;
    private static final double THRESHOLD = .95;
    // A mapping of a sequence to a map, which maps k to a map of the counts of the kmer
    private Map<String, Map<Integer, Map<String, Integer>>> masterIndex = new HashMap<>();

    //tmp vars
    private int L;
    private int needed;
    private int count;
    private String kmer;

    @Override
    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (!masterIndex.containsKey(rep.getSequence())) {
            index(rep.getSequence());
        }
        for (int k = MIN_K; k <= MAX_K; k++) {
            Map<String, Integer> index = null;
            index = masterIndex.get(rep.getSequence()).get(k);
            L = sequence.getSequence().length();
            needed = (int) Math.round(L - k + 1 - (1 - THRESHOLD) * k * L);
            count = 0;
            for (int i = 0; i < L - k; i++) {
                kmer = sequence.getSequence().substring(i, i + k);
                if (index.containsKey(kmer)) {
                    count += index.get(kmer);
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

    private void index(String rep) {
        Map<Integer, Map<String, Integer>> kmerIndex = new HashMap<>();
        String kmer;
        for (int k = MIN_K; k <= MAX_K; k++) {
            Map<String, Integer> kmers = new HashMap<>(3000);
            for (int i = 0; i < rep.length() - k; i++) {
                kmer = rep.substring(i, i + k);
                int oldVal = kmers.get(kmer) == null ? 0 : kmers.get(kmer);
                kmers.put(kmer, oldVal + 1);
            }
            kmerIndex.put(k, kmers);
        }
        masterIndex.put(rep, kmerIndex);
    }

    @Override
    public String getName() {
        return "Short Word";
    }
}
