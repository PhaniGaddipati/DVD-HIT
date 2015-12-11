package edu.jhu.compgenomics.dvdhit;

import java.util.ArrayList;

/**
 * A representation of a Cluster. Simply a list that keeps track of the longest
 * sequence contained, which acts as the representative sequence.
 */
public class Cluster extends ArrayList<Sequence> {

    /**
     * The longest sequence to act as the representative sequence.
     */
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
