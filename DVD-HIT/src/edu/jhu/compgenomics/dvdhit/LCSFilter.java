package edu.jhu.compgenomics.dvdhit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phani on 11/17/2015.
 */
public class LCSFilter implements SequenceSimilarityFilter {
	
    private int MIN_K = 8;
    private int MAX_K = 8;
    private double threshold = .7;
    @Override

    public boolean isSimilar(Cluster cluster, Sequence sequence) {
        String rep = cluster.getLongestSequence().getSequence();
        String seq = sequence.getSequence();
        if (LLCS(rep, seq) < threshold) return false;
        return true;
    }

    @Override
    public String getName() {
        return "Longest Common Subsequence";
    }

    @Override
    public void setThreshold(double thresh) {
        this.threshold = thresh;
    }

	@Override
	public void setK(int k) {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Integer> lcs_lens(String xs, String ys) {
    	ArrayList<Integer> curr = new ArrayList<Integer>(Collections.nCopies(ys.length() +  1, 0));
    	for (char x : xs.toCharArray()) {
    		ArrayList<Integer> prev = curr;
    		for (int i = 0; i < ys.length(); i++) {
    			char y = ys.charAt(i);
    			if (x == y) curr.set(i+1, prev.get(i) + 1);
    			else curr.set(i+1, Math.max(curr.get(i), prev.get(i+1)));
    		}//end for
    	}//end for
    	return curr;
    }//end lcs_lens
    
    public String LCS(String xs, String ys) {
    	int nx = xs.length(); int ny = ys.length();
    	
    	if (nx == 0) return "";
    	else if (nx == 1) { //returns letters of the longest common subsequence in order
    		String i = xs.substring(0, 1);
    		if (ys.indexOf(i) >= 0) return i;
    		else return "";
    	} else {
    		int i = nx/2;
    		String xb = xs.substring(0,i); String xe = xs.substring(i);
    		ArrayList<Integer> ll_b = lcs_lens(xb,ys);
    		ArrayList<Integer> ll_e = lcs_lens(new StringBuilder(xe).reverse().toString(), new StringBuilder(ys).reverse().toString());
    		
    		ArrayList<Integer> temp = new ArrayList<>();
    		for (int j = 0; j < ny+1; j++) temp.add(ll_b.get(j) + ll_e.get(ny - j));
    		int k = temp.lastIndexOf(Collections.max(temp));
    		
    		String yb = ys.substring(0, k);
    		String ye = ys.substring(k);
    		return LCS(xb, yb) + LCS(xe, ye);
    	}//end if-else
    }//end Longest Common Subsequence
    
    public float LLCS(String a, String b) {
    	return (float) LCS(a, b).length()/Math.min(a.length(), b.length());
    }//end LLCS
}
