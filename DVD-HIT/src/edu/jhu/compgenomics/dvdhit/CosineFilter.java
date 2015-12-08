package edu.jhu.compgenomics.dvdhit;

import java.util.*;

/**
 * Created by Phani on 11/17/2015.
 */
public class CosineFilter implements SequenceSimilarityFilter {

    private static final int K = 8;
    private static final double THRESHOLD = .9;
    // A mapping of a sequence to a map, which maps k to a set of the kmers
    private static Map<String, Map<String, Integer>> masterIndex = new HashMap<String, Map<String, Integer>>();

    public static float innerproduct(float[] v1, float[] v2) {
        float sum = (float) 0.0;
        if (v1.length != v2.length) {
            throw new RuntimeException("Dimensions don't agree");
        }

        for (int i = 0; i < v1.length; i++) sum += (v1[i] * v2[i]);
        return sum;
    }

    public static float magnitude(float[] v) {
        float sum = (float) 0.0;
        for (int i = 0; i < v.length; i++) sum += Math.pow(v[i], 2);
        return (float) Math.sqrt(sum);
    }

    public static Collection<String> union(Collection<String> gram1, Collection<String> gram2) {
        Collection<String> union = new HashSet<>();
        union.addAll(gram1);
        union.addAll(gram2);
        return union;
    }//end Union

    @Override

    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        Sequence rep = cluster.getLongestSequence();
        if (!masterIndex.containsKey(rep.getSequence())) {
            index(rep.getSequence());
        }
        if (!masterIndex.containsKey(sequence.getSequence())) {
            index(sequence.getSequence());
        }

        String seq = sequence.getSequence();
        Map<String, Integer> seqSet = masterIndex.get(seq);
        Collection<String> union = union(masterIndex.get(rep.getSequence()).keySet(), seqSet.keySet());

        float[] vector1 = new float[union.size()];
        float[] vector2 = new float[union.size()];
        Iterator<String> itr = union.iterator();
        int i = 0;
        while (itr.hasNext()) {
            String key = itr.next();
            vector1[i] = masterIndex.get(rep.getSequence()).getOrDefault(key, 0);
            vector2[i] = seqSet.getOrDefault(key, 0);
            i++;
        }//end while

        float dot = innerproduct(vector1, vector2);
        float mag1 = magnitude(vector1);
        float mag2 = magnitude(vector2);

        if (dot / (mag1 * mag2) < THRESHOLD) return false;
        masterIndex.remove(seq);
        return true;
    }

    private void index(String s) {
        Map<String, Integer> gramMap = new HashMap<>();
        for (int i = 0; i <= s.length() - K; i++) {
            String sub = new String(s.substring(i, i + K));
            if (gramMap.containsKey(sub)) gramMap.put(sub, gramMap.get(sub) + 1);
            else gramMap.put(sub, 1);
        }//end for
        this.masterIndex.put(s, gramMap);
    }

    @Override
    public String getName() {
        return "Jaccard";
    }
}
