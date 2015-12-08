package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {

    private static final int MIN_K = 8;
    private static final int MAX_K = 8;
    private static final double THRESHOLD = .90;
    // A mapping of a sequence to a map, which maps k to a map of the counts of the kmer
    private Map<String, Map<Integer, Map<String, Integer>>> masterIndex = new HashMap<>();

    //tmp vars
    private int L;
    private int needed;
    private int count;

    @Override
    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (!masterIndex.containsKey(rep.getSequence())) {
            addToMasterIndex(rep.getSequence());
        }
        Map<Integer, Map<String, Integer>> seqKmerIndexes = getStringKmerCounts(sequence.getSequence());
        for (int k = MIN_K; k <= MAX_K; k++) {
            // Each index contains a mapping of how many of each kmer it is
            Map<String, Integer> repIndex = masterIndex.get(rep.getSequence()).get(k);
            Map<String, Integer> seqIndex = seqKmerIndexes.get(k);
            L = sequence.getSequence().length();
            needed = (int) Math.round(L - k + 1 - (1 - THRESHOLD) * k * L);
            count = 0;
            for (String kmer : seqIndex.keySet()) {
                if (repIndex.containsKey(kmer) && seqIndex.containsKey(kmer)) {
                    //They share the minimum count of this kmer
                    count += Math.min(repIndex.get(kmer), seqIndex.get(kmer));
                }
                if (count >= needed) {
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

    private void addToMasterIndex(String rep) {
        Map<Integer, Map<String, Integer>> kmerIndex = getStringKmerCounts(rep);
        masterIndex.put(rep, kmerIndex);
    }

    private Map<Integer, Map<String, Integer>> getStringKmerCounts(String rep) {
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
        return kmerIndex;
    }

    @Override
    public String getName() {
        return "Short Word";
    }
}
