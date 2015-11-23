import java.util.HashSet;

/**
 * Created by Phani on 11/17/2015.
 */
public class Cluster extends HashSet<Sequence> {

    private Sequence longestSeq = null;


    @Override
    public boolean add(Sequence s) {
        boolean ret = super.add(s);
        if (ret && (longestSeq == null
                || s.getSequence().length() > longestSeq.getSequence().length())) {
            longestSeq = s;
        }
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        Object removed = o;
        boolean ret = super.remove(o);
        if (ret && longestSeq.equals(o)) {
            int maxLen = -1;
            Sequence longest = null;
            for (Sequence s : this) {
                if (s.getSequence().length() > maxLen) {
                    maxLen = s.getSequence().length();
                    longest = s;
                }
            }
            this.longestSeq = longest;
        }
        return ret;
    }

    public Sequence getLongestSequence() {
        return longestSeq;
    }

}
