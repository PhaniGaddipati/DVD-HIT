package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phani on 11/17/2015.
 */
public class JaccardFilter implements SequenceSimilarityFilter {

    private static final int MIN_K = 8;
    private static final int MAX_K = 8;
    private static final double THRESHOLD = .7;
    // A mapping of a sequence to a map, which maps k to a set of the kmers
    private Map<String, Map<Integer, Map<String, Integer>>> masterIndex = new HashMap<>();

    @Override

    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (!masterIndex.containsKey(rep.getSequence())) {
            addToMasterIndex(rep.getSequence());
        }
        if (!masterIndex.containsKey(sequence.getSequence())) {
            addToMasterIndex(sequence.getSequence());
        }
        for (int k = MIN_K; k <= MAX_K; k++) {
            Map<String, Integer> repIndex = masterIndex.get(rep.getSequence()).get(k);
            Map<String, Integer> seqIndex = masterIndex.get(sequence.getSequence()).get(k);

            Set<String> union = new HashSet<String>(repIndex.keySet());
            union.addAll(seqIndex.keySet());

            Set<String> intersection = new HashSet<String>(repIndex.keySet());
            intersection.retainAll(seqIndex.keySet());

            float jaccard = (float) intersection.size() / (float) union.size();
//            System.out.println(jaccard);
            if (jaccard < THRESHOLD) return false;
        }
        masterIndex.remove(sequence.getSequence());
        return true;
    }

    private void addToMasterIndex(String rep) {
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
        return "Jaccard";
    }
}
