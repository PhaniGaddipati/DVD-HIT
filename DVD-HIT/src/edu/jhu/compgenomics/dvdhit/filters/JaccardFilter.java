package edu.jhu.compgenomics.dvdhit.filters;

import edu.jhu.compgenomics.dvdhit.model.Cluster;
import edu.jhu.compgenomics.dvdhit.model.Sequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Uses the original Jaccard index to implement a similarity filter.
 */
public class JaccardFilter implements SequenceSimilarityFilter {

    private int MIN_K = 8;
    private int MAX_K = 8;
    private double threshold = .7;
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
            if (jaccard < threshold) return false;
        }
        masterIndex.remove(sequence.getSequence());
        return true;
    }

    /**
     * Adds all k-mers of the String into the master index.
     *
     * @param s
     */
    private void addToMasterIndex(String s) {
        Map<Integer, Map<String, Integer>> kmerIndex = new HashMap<>();
        String kmer;
        for (int k = MIN_K; k <= MAX_K; k++) {
            Map<String, Integer> kmers = new HashMap<>(3000);
            for (int i = 0; i < s.length() - k; i++) {
                kmer = s.substring(i, i + k);
                int oldVal = kmers.get(kmer) == null ? 0 : kmers.get(kmer);
                kmers.put(kmer, oldVal + 1);
            }
            kmerIndex.put(k, kmers);
        }
        masterIndex.put(s, kmerIndex);
    }

    @Override
    public String getName() {
        return "Jaccard";
    }

    @Override
    public void setK(int k) {
        this.MIN_K = k;
        this.MAX_K = k;
    }

    @Override
    public void setThreshold(double thresh) {
        this.threshold = thresh;
    }
}
