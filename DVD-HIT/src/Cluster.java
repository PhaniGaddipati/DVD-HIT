import java.util.HashSet;

/**
 * Created by Phani on 11/17/2015.
 */
public class Cluster extends HashSet<String> {

    public String getLongestSequence() {
        String longest = null;
        int longestLen = Integer.MIN_VALUE;
        for (String s : this) {
            if (s.length() > longestLen) {
                longestLen = s.length();
                longest = s;
            }
        }
        return longest;
    }

}
