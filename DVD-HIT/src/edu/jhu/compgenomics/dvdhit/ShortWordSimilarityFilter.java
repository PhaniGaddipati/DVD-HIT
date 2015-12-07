package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {

    private static final boolean USE_INDEX = false;
    private static final int MIN_K = 2;
    private static final int MAX_K = 5;
    private static final double THRESHOLD = .90;
    // A mapping of a sequence to a map, which maps k to a set of the kmers
    private Map<String, Map<Integer, Set<String>>> masterIndex = new HashMap<String, Map<Integer, Set<String>>>();

    @Override
    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (rep == null) {
            return false;
        }
        if (USE_INDEX && !masterIndex.containsKey(rep.getSequence())) {
            index(rep.getSequence());
        }
        for (int k = MIN_K; k <= MAX_K; k++) {
            Set<String> index = null;
            if (USE_INDEX) {
                index = masterIndex.get(rep.getSequence()).get(k);
            }
            int L = sequence.getSequence().length();
            int needed = (int) Math.round(L - k + 1 - (1 - THRESHOLD) * k * L);
            int count = 0;
            for (int i = 0; i < L - k; i++) {
                if (USE_INDEX) {
                    if (index.contains(sequence.getSequence().substring(i, i + k))) {
                        count++;
                    }
                } else {
                    if (sequence.getSequence().substring(i, i + k).equals(rep.getSequence().substring(i, i + k))) {
                        count++;
                    }
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
        Map<Integer, Set<String>> kmerIndex = new HashMap<Integer, Set<String>>();
        for (int k = MIN_K; k <= MAX_K; k++) {
            Set<String> kmers = new HashSet<String>();
            for (int i = 0; i < rep.length() - k; i++) {
                kmers.add(rep.substring(i, i + k));
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
