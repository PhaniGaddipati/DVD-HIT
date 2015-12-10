package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phani on 12/10/2015.
 */
public class SpacedShortWordFilter implements SequenceSimilarityFilter {
    private int MIN_K = 8;
    private int MAX_K = 8;
    private double threshold = .95;
    // A mapping of a sequence to a map, which maps k to a map of the counts of the kmer
    private Map<String, Map<Integer, Map<String, Integer>>> masterIndex = new HashMap<>();

    //tmp vars
    private int L;
    private int needed;
    private int count;
    private Map<String, Integer> repIndex;
    private Map<String, Integer> seqIndex;

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
            // Each index contains a mapping of how many of each kmer it is
            repIndex = masterIndex.get(rep.getSequence()).get(k);
            seqIndex = masterIndex.get(sequence.getSequence()).get(k);
            L = sequence.getSequence().length();
            needed = (int) Math.round(L - k + 1 - (1 - threshold) * k * L);
            count = 0;
            for (String kmer : seqIndex.keySet()) {
                if (repIndex.containsKey(kmer)) {
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
        //Adding this sequence so we don't need the index anymore
        masterIndex.remove(sequence.getSequence());
        return true;
    }

    public void addToMasterIndex(String rep) {
        Map<Integer, Map<String, Integer>> kmerIndex = new HashMap<>();
        char kmerArr[];
        String kmer;
        for (int k = MIN_K; k <= MAX_K; k++) {
            Map<String, Integer> kmers = new HashMap<>(3000);
            for (int i = 0; i < rep.length() - k * 2; i++) {
                kmerArr = new char[k];
                for (int d = 0; d < k; d++) {
                    kmerArr[d] = rep.charAt(d * 2);
                }
                kmer = new String(kmerArr);
                int oldVal = kmers.get(kmer) == null ? 0 : kmers.get(kmer);
                kmers.put(kmer, oldVal + 1);
            }
            kmerIndex.put(k, kmers);
        }
        masterIndex.put(rep, kmerIndex);
    }

    @Override
    public String getName() {
        return "Spaced Subsequence Short Word";
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
