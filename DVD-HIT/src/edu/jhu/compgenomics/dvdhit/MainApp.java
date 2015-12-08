package edu.jhu.compgenomics.dvdhit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phani on 11/17/2015.
 */
public class MainApp {

    private static final String REF_CLSTR =
            "C:\\Users\\Phani\\OneDrive\\School\\College\\Computational Genomics\\DVD-HIT\\data\\16s_cdhit_k_8_thresh_90.clstr";
    private static SequenceSimilarityFilter[] filters = new SequenceSimilarityFilter[]
            {new ShortWordSimilarityFilter(), new JaccardFilter()};

    public static void main(String[] args) {
        if (validateArgs(args)) {
            try {
                System.out.println("Clustering...");
                DVD_HIT dvd_hit = new DVD_HIT();
                File inFile = new File(args[0]);
                File outFile = new File(args[1]);
                List<SequenceSimilarityFilter> filterList = new ArrayList<SequenceSimilarityFilter>();
                for (int i = 2; i < args.length; i++) {
                    filterList.add(filters[Integer.parseInt(args[i])]);
                }
                long startTime = System.currentTimeMillis();
                List<Cluster> clusters = dvd_hit.cluster(inFile, filterList);
                long stopTime = System.currentTimeMillis();
                System.out.println("Finished in " + (stopTime - startTime) / 1000 + " seconds.");
                System.out.println("Resulted in " + clusters.size() + " clusters.");
                System.out.println("Genus clustering score: " + EvaluationUtils.getGenusScore(clusters));
                printRef(inFile);
                System.out.println("Writing '" + outFile.getAbsolutePath() + "'...");
                ClstrFileUtils.writeClstrFile(clusters, outFile);
                System.out.println("Done");
            } catch (IOException e) {
                System.out.println("Error using the specified file!");
            }
        } else {
            printUsage();
        }
    }

    private static void printRef(File fasta) {
        File file = new File(REF_CLSTR);
        if (file.exists()) {
            try {
                List<Cluster> clusters = ClstrFileUtils.readClstrFile(file, fasta);
                System.out.println("CD-HIT Genus clustering score: " + EvaluationUtils.getGenusScore(clusters));
            } catch (IOException e) {
                System.out.println("Couldn't print CD-HIT Reference");
            }
        }
    }


    private static boolean validateArgs(String[] args) {
        if (args.length < 3) {
            return false;
        }
        try {
            for (int i = 2; i < args.length; i++) {
                int filter = Integer.parseInt(args[i]);
                if (filter < 0 || filter >= filters.length) {
                    return false;
                }
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        File inFile = new File(args[0]);
        if (!inFile.exists() || inFile.isDirectory()) {
            return false;
        }
        return true;
    }

    private static void printUsage() {
        System.out.println("Usage:\n\tDVD_HIT <input_file> <output_file> <filter1_id> <filter2_id> ...");
        System.out.println();
        System.out.println("Filter IDs: ");
        for (int i = 0; i < filters.length; i++) {
            System.out.println("\t" + i + ":\t" + filters[i].getName());
        }
        System.out.println();
    }

}
