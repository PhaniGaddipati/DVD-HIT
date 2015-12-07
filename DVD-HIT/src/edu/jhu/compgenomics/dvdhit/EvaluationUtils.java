package edu.jhu.compgenomics.dvdhit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phani on 12/7/2015.
 */
public class EvaluationUtils {

    public static double getGenusScore(List<Cluster> clusters) {
        double scoresSum = 0;
        for (Cluster cluster : clusters) {
            //Find most common genus
            Map<String, Integer> counts = new HashMap<String, Integer>();
            int maxCount = -1;
            String maxGenus = null;
            for (Sequence s : cluster) {
                int newVal = (counts.get(s.getGenus()) == null ? 0 : counts.get(s.getGenus())) + 1;
                counts.put(s.getGenus(), newVal);
                if (newVal > maxCount) {
                    maxGenus = s.getGenus();
                    maxCount = newVal;
                }
            }
            //Calc score
            scoresSum += ((double) maxCount) / cluster.size();
        }
        //Average for all clusters
        return scoresSum / clusters.size();
    }

}
