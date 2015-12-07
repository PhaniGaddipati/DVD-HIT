import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeSet;

/*
 * Playground for different string similarity functions.
 * 1. LLCS using Hirschberg's algorithm: O(mn) time, O(n) space
 * 2. Jaccard similarity:
 */
public class StringSimilarityTests {
	public static void main(String[] args) {
		String c = "chimpanzee"; String h = "human";
		String seq1 = "AGCGAACGCTGGCGGCAGGCCTAACACATGCAAGTCGAGCGCCCAGCAATGGGAGCGGCAGACGGGTGAGTAACGCGTGGGGATGTGCCCAATGGTGCGGAATAACCCAGGGAAACTTGGATTAATACCGCATGTGCCCTTCGGGGGAAAGATTTATCGCCATTGGATCAACCCGCGTCTGATTAGCTAGTTGGTGAGGTAAAGGCTCACCAAGGCGACGATCAGTAGCTGGTCTGAGAGGATGATCAGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTGGGGAATATTGGACAATGGGCGCAAGCCTGATCCAGCCATGCCGCGTGAGTGATGAAGGCCTTAGGGTTGTAAAGCTCTTTCGCCGGTGAAGATAATGACGGTAACCGGAGAAGAAGCCCCGGCTAACTTCGTGCCAGCAGCCGCGGTAATACGAAGGGGGCAAGCGTTGCTCGGAATCACTGGGCGTAAAGCGCACGTAGGCGGATCGTTAAGTCAGGGGTGAAAGCCTGGAGCTCAACTCCAGAACTGCCCTTGATACTGGCGATCTTGAGTTCGAGAGAGGTTGGTGGAACTCCGAGTGTAGAGGTGAAATTCGTAGATATTCGGAAGAACACCAGTGGCGAAGGCGGCCAACTGGCTCGATACTGACGCTGAGGTGCGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCGTAAACGATGGATGCTAGCCGTTGGGGAGCTTGCTCTTCAGTGGCGCAGCTAACGCCTTAAGCATCCCGCCTGGGGAGTACGGTCGCAAGATTAAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGAGCATGTGGTTTAATTCGAAGCAACGCGCAGAACCTTACCAGCCTTTGACATGGCAGGACGACTTCCGGAGACGGATTTCTTCCAGCAATGGACCTGCACACAGGTGCTGCATGGCTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTCGCCTTTAGTTGCCATCATTCAGTTGGGCACTCTAAAGGGACTGCCGGTGATAAGCCGCGAGGAAGGTGGGGATGACGTCAAGTCCTCATGGCCCTTACGGGCTGGGCTACACACGTGCTACAATGGCGGTGACAATGGGATGCGAGCCTGCGAGGGTGAGCAAATCTCCAAAAGCCGTCTCAGTTCGGATTGCACTCTGCAACTCGAGTGCATGAAGTTGGAATCGCTAGTAATCGTGGATCAGCATGCCACGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCATGGGAGTTGGCTTTACCCGAAGGCGTTGCGCTAACCGCAAGGAGGCAGGCGACCACGGTAGGGTCAGTGACTGGGGTGAAG";
		String seq2 = "CGCTGGCGGAATGCTTTACACATGCAAGTCGAACGATGAACCTTAGCTTGCTAAGGGGATTAGTGGCGAACGGGTGAGTAATATATCGGAACGTGCCTTGTAATGGGGGATAACTAGTCGAAAGATTAGCTAATACCGCATACGCCCTGAGGGGGAAAGTAGGGGATCTTCGGACCTTACGTTATAAGAGCGGCCGATATCTGATTAGCTAGTTGGTGGGGTAATGGCCTACCAAGGCTTCGATCAGTAGCTGGTCTGAGAGGACGACCAGCCACACTGGAACTGAGACACGGTCCAGACTCCTACGGGAGGCAGCAGTGGGGAATTTTGGACAATGGGCGCAAGCCTGATCCAGCCATTCCGCGTGAGTGAAGAAGGCCTTCGGGTTGTAAAGCTCTTTCGCAAGGGAAGAAAACTTACATTCTAATAAAGTGTGAGGCTGACGGTACCTTGATAAGAAGCACCGGCTAACTACGTGCCAGCAGCCGCGGTAATACGTAGGGTGCGAGCGTTAATCGGAATTACTGGGCGTAAAGCGTGCGCAGGCGGTTTTGTAAGTCAGATGTGAAATCCCCGAGCTCAACTTGGGAACTGCGTTTGAAACTACAAGACTAGAATATGTCAGAGGGGGGTAGAATTCCACGTGTAGCAGTGAAATGCGTAGAGATGTGGAGGAATACCAATGGCGAAGGCAGCCCCCTGGGATAATATTGACGCTCATGCACGAAAGCGTGGGGAGCAAACAGGATTAGATACCCTGGTAGTCCACGCCCTAAACGATGTCTACTAGTTGTTGGTGGAGTAAAATCCATGAGTAACGCAGCTAACGCGTGAAGTAGACCGCCTGGGGAGTACGGTCGCAAGATTAAACTCAAAGGAATTGACGGGGGCCCGCACAAGCGGTGGATTATGTGGATTAATTCGATGCAACGCGAAAACCTTACCTGGCCTTGACATGCCACTAACGAAGCAGAGATGCATTAGGTGCCCGTAAGGGAAAGTGGACACAGGTGCTGCATGGCTGTCGTCAGCTCGTGTCGTGAGATGTTGGGTTAAGTCCCGCAACGAGCGCAACCCTTGCCATTAATTGCCATCATTTAGTTGGGCACTTTAATGGGACTGCCGGTGACAAACCGGAGGAAGGTGGGGATGACGTCAAGTCCTCATGGCCCTTATGGCCAGGGCTTCACACGTAATACAATGGTCGGTACAGAGAGTTGCCAACCCGCGAGGGGGAGCTAATCTCAGAAAGCCGATCGTAGTCCGGATTGTTCTCTGCAACTCGAGAGCATGAAGTCGGAATCGCTAGTAATCGCGGATCAGCATGTCGCGGTGAATACGTTCCCGGGCCTTGTACACACCGCCCGTCACACCATGGGAGTGGGTTTTACCAGAAGTAGTTAGTCTAACCGTAAGGGGGACGATTACCACGGTAGTATTCATGACTGGGGTGAAGTCGTAACAAG";
		System.out.println("Comparing " + h + " and " + c + "...");
		System.out.println("1. LLCS: " + LLCS(h, c));
		System.out.println("2.1 Jaccard: " + Jaccard(c, h, 1));
		System.out.println("2.2 Cosine: " + Cosine(c, h, 1));
		
		System.out.println();
		
		System.out.println("Comparing Seq1 and Seq2...");
		System.out.println("1. LLCS: " + LLCS(seq1, seq2));
		System.out.println("2.1 Jaccard: " + Jaccard(seq1, seq2, 8));
		System.out.println("2.2 Cosine: " + Cosine(seq1, seq2, 6));
	}//end main
	
	/***********************************************************************************
	************************************* 1. LCS ***************************************
	***********************************************************************************/
	public static ArrayList<Integer> lcs_lens(String xs, String ys) {
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
    }
    
    public static String LCS(String xs, String ys) {
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
    
    public static float LLCS(String a, String b) {
    	return (float) LCS(a, b).length()/Math.min(a.length(), b.length());
    }
    
    /***********************************************************************************
    ********************** 2. Q Gram Metrics********************************************
    ***********************************************************************************/
	public static Collection<String> stringToQGramSet(String s, int q){
		Collection<String> gramSet = new HashSet<String>();
		for (int i = 0; i <= s.length() - q; i++) gramSet.add(s.substring(i, i+q));
		return gramSet;
	}//end stringToQGramSet
	
	public static Map<String, Integer> stringToQGramMap(String s, int q){
		Map<String, Integer> gramMap = new HashMap<>();
		for (int i = 0; i <= s.length() - q; i++) {
			String sub = s.substring(i, i+q);
			if (gramMap.containsKey(sub)) gramMap.put(sub, gramMap.get(sub)+1);
			else gramMap.put(sub, 1);
		}//end for
		return gramMap;
	}//end stringToQGramSet
	
	public static Collection<String> union(Collection<String> gram1, Collection<String> gram2) {
		Collection<String> union = new HashSet<>();
		union.addAll(gram1);
		union.addAll(gram2);
		return union;
	}//end Union

    public static Collection<String> intersect(Collection<String> gram1, Collection<String> gram2) {
        Collection<String> intersection = new HashSet<>();
        for (String g1 : gram1) {
            if(gram2.contains(g1)) intersection.add(g1);
        } //end for
        return intersection;
    }
    
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
    
    /*
     * Jaccard Index
     */
    public static float Jaccard(String a, String b, int q) {
    	Collection<String> gram1 = stringToQGramSet(a, q);
    	Collection<String> gram2 = stringToQGramSet(b, q);
    	Collection<String> union = union(gram1, gram2);
    	Collection<String> intersection = intersect(gram1, gram2);
    	System.out.println("Gram 1: " + gram1);
    	System.out.println("Gram 2: " + gram2);
    	System.out.println("Intersection: " + intersection);
    	System.out.println("Union: " + union);
		return (float) intersection.size() / (float) union.size();
    }//end Jaccard
    
    public static float Cosine(String a, String b, int q) {
    	Map<String, Integer> gram1 = stringToQGramMap(a, q);
    	Map<String, Integer> gram2 = stringToQGramMap(b, q);
    	Collection<String> union = union(gram1.keySet(), gram2.keySet());
    	
    	float[] vector1 = new float[union.size()];
    	float[] vector2 = new float[union.size()];
    	Iterator<String> itr =  union.iterator(); int i = 0;
    	while (itr.hasNext()) {
    		String key = itr.next();
    		vector1[i] = gram1.getOrDefault(key, 0);
    		vector2[i] = gram2.getOrDefault(key, 0);
    		i++;
    	}//end while
    	    	
    	float dot = innerproduct(vector1, vector2);
    	float mag1 = magnitude(vector1);
    	float mag2 = magnitude(vector2);
    	
    	return dot/(mag1*mag2);
    	
    }
}//end StringSimilarityTest