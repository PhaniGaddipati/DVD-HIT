import java.util.*;

/*
 * Playground for different string similarity functions.
 * 1. LLCS using Hirschberg's algorithm: O(mn) time, O(n) space
 * 2. Jaccard similarity:
 */
public class StringSimilarityTests {
	public static void main(String[] args) {
		String c = "chimpanzee"; String h = "human";
		System.out.println("Comparing " + h + " and " + c + "...");
		System.out.println("1. LLCS: " + LLCS(h, c));
		System.out.println("2. Jaccard: " + Jaccard(h, c));		
	}//end main
	
	/***********************************************************************************
	************************************* 1. LCS ***************************************
	***********************************************************************************/
	public static ArrayList<Integer> lcs_lens(String xs, String ys) {
		ArrayList<Integer> curr = new ArrayList<>(Collections.nCopies(ys.length() + 1, 0));
		for (char x : xs.toCharArray()) {
    		for (int i = 0; i < ys.length(); i++) {
    			char y = ys.charAt(i);
				if (x == y) curr.set(i + 1, curr.get(i) + 1);
				else curr.set(i + 1, Math.max(curr.get(i), curr.get(i + 1)));
			}//end for
    	}//end for
    	return curr;
    }
    
    public static String LCS(String xs, String ys) {
    	int nx = xs.length(); int ny = ys.length();
    	
    	if (nx == 0) return "";
    	else if (nx == 1) { //returns letters of the longest common subsequence in order
    		String i = xs.substring(0, 1);
			if (ys.contains(i)) return i;
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
    ************************************* 2. Jaccard Similarity ************************
    ***********************************************************************************/
	public static Collection<Character> stringToCharacterSet(String string){
		Collection<Character> charSet = new HashSet<>();
		for(Character character : string.toCharArray()) charSet.add(character);
		return charSet;
	}//end stringtoCharacterSet
	
	public static Collection<Character> uniqueCharacters(Collection<Character> vector){
		Collection<Character> uniqueSet = new HashSet<>();
		//end for
		vector.stream().filter(c -> !uniqueSet.contains(c)).forEach(uniqueSet::add);
		return uniqueSet;
	}//end uniqueCharacters
	
	public static Collection<Character> union(String string1, String string2){
		Collection<Character> mergedVector = new TreeSet<>();
		mergedVector.addAll(stringToCharacterSet(string1));
		mergedVector.addAll(stringToCharacterSet(string2));
		return uniqueCharacters(mergedVector);
	}//end union
	
	public static Collection<Character> intersect(String string1, String string2){
		//string1.
		Collection<Character> vector1 = uniqueCharacters(stringToCharacterSet(string1));
		Collection<Character> vector2 = uniqueCharacters(stringToCharacterSet(string2));
		Collection<Character> intersectVector = new TreeSet<>();
		for(Character c1 : vector1) {
			for(Character c2 : vector2) {
				if(c1.equals(c2)) intersectVector.add(c1);
			}//end for
		}//end for
		return intersectVector;
	}//end intersect
    
    public static float Jaccard(String a, String b) {
		return (float) intersect(a, b).size() / (float) union(a, b).size();
    }//end Jaccard
}//end StringSimilarityTest