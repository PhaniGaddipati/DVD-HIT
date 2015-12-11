package edu.jhu.compgenomics.dvdhit;

import java.io.*;
import java.util.*;

/**
 * Utils to read and write cluster files.
 */
public class ClstrFileUtils {

    /**
     * Writes a file in the clstr format
     *
     * @param clusters The clusters to write
     * @param outFile  The file to write to
     * @throws FileNotFoundException
     */
    public static void writeClstrFile(List<Cluster> clusters, File outFile) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(outFile);
        for (int i = 0; i < clusters.size(); i++) {
            writer.println(">Cluster " + i);
            int seq = 0;
            for (Sequence s : clusters.get(i)) {
                writer.print(seq);
                writer.print("\t");
                writer.print(s.getSequence().length());
                writer.print("nt, >");
                writer.print(s.getName().substring(0, Math.min(s.getName().length(), 30)));
                writer.print("...");
                writer.print(" ");
                if (s == clusters.get(i).getLongestSequence()) {
                    writer.print("*");
                }
                writer.println();
                seq++;
            }
        }
    }

    /**
     * Reads a clstr file. The sequences themselves  will be empty as the clstr file
     * doesn't contain the actual sequences, just the names. To use for comparing
     * clustering results.
     *
     * @param clstrFile
     * @param fastaFile File to fill in truncated description
     * @return A list of Clusters
     */
    public static List<Cluster> readClstrFile(File clstrFile, File fastaFile) throws IOException {
        List<Sequence> sequences = Collections.emptyList();
        Map<String, Sequence> nameSeqMap = new HashMap<String, Sequence>();
        if (fastaFile != null) {
            sequences = FASTAUtils.readSequences(fastaFile);
            for (Sequence s : sequences) {
                nameSeqMap.put(s.getName(), s);
            }
        }
        List<Cluster> clusters = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(clstrFile));
        String line;
        Cluster currentCluster = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(">")) {
                if (currentCluster != null) {
                    clusters.add(currentCluster);
                }
                currentCluster = new Cluster();
            } else {
                line = line.substring(line.indexOf('>') + 1);
                line = line.substring(0, line.lastIndexOf('|') + 1);
                String description = "";
                String seq = "";
                if (nameSeqMap.containsKey(line)) {
                    Sequence s = nameSeqMap.get(line);
                    description = s.getDescription();
                    seq = s.getSequence();
                }
                Sequence sequence = new Sequence(line, description, seq);
                currentCluster.add(sequence);
            }
        }
        if (currentCluster != null) {
            clusters.add(currentCluster);
        }
        reader.close();
        return clusters;
    }

}
