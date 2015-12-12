package edu.jhu.compgenomics.dvdhit.utils;

import edu.jhu.compgenomics.dvdhit.model.Cluster;
import edu.jhu.compgenomics.dvdhit.model.Sequence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities to evaluate clustering results.
 */
public class EvaluationUtils {

    /**
     * Computes the 'genus score' of a list of clusters. For each
     * cluster, the most frequent genome is determined. The cluster
     * score is computed as (# of the most common genus)/sizeof(cluster).
     * The overall clustering score is the average of the cluster scores.
     *
     * @param clusters
     * @return
     */
    public static double getGenusScore(List<Cluster> clusters) {
        double scoresSum = 0;
        for (Cluster cluster : clusters) {
            //Find most common genus
            Map<String, Integer> counts = new HashMap<>();
            int maxCount = -1;
            for (Sequence s : cluster) {
                int newVal = (counts.get(s.getGenus()) == null ? 0 : counts.get(s.getGenus())) + 1;
                counts.put(s.getGenus(), newVal);
                if (newVal > maxCount) {
                    maxCount = newVal;
                }
            }
            scoresSum += ((double) maxCount) / cluster.size();
        }
        //Average for all clusters
        return scoresSum / clusters.size();
    }

}
