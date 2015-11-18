import java.util.HashSet;

/**
 * Created by Phani on 11/17/2015.
 */
public class Cluster extends HashSet<String> {

    private String longestSeq = null;


    @Override
    public boolean add(String s) {
        boolean ret = super.add(s);
        if (ret && (longestSeq == null || s.length() > longestSeq.length())) {
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
            String longest = null;
            for (String s : this) {
                if (s.length() > maxLen) {
                    maxLen = s.length();
                    longest = s;
                }
            }
            this.longestSeq = longest;
        }
        return ret;
    }

    public String getLongestSequence() {
        return longestSeq;
    }

}
