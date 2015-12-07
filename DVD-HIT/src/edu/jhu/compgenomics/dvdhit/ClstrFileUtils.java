package edu.jhu.compgenomics.dvdhit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * Created by Phani on 11/23/2015.
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
     * @return A list of Clusters
     */
    public static List<Cluster> readClstrFile(File clstrFile) {
        //TODO
        return Collections.emptyList();
    }

}
