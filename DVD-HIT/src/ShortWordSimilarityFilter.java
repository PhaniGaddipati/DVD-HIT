/**
 * Created by Phani on 11/17/2015.
 */
public class ShortWordSimilarityFilter implements SequenceSimilarityFilter {
    @Override
    public boolean isSimilar(Cluster cluster, String sequence) {
        return false;
    }
}
