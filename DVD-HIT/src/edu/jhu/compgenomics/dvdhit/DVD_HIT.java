package edu.jhu.compgenomics.dvdhit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Phani on 11/17/2015.
 */
public class DVD_HIT {

    /**
     * Clusters the given sequences into the given list of clusters, adding more
     * clusters as needed. Greedly clustering with the given filter
     *
     * @param clusters
     * @param initialSequences
     * @param filter
     */
    private void cluster(List<Cluster> clusters, List<Sequence> initialSequences, SequenceSimilarityFilter filter) {
        //Copy the sequence list
        List<Sequence> sequences = new ArrayList<Sequence>(initialSequences);
        //Step 1, Sort by decreasing length
        Collections.sort(sequences, new Comparator<Sequence>() {
            @Override
            public int compare(Sequence o1, Sequence o2) {
                return -Integer.compare(o1.getSequence().length(), o2.getSequence().length());
            }
        });

        //At this points, sequences is a list of to-be-clustered seqs
        int lastSize = sequences.size();
        while (sequences.size() > 0) {
            if (sequences.size() < (lastSize - 100)) {
                System.out.println(sequences.size() + " left to cluster");
                lastSize = sequences.size();
            }
            //Start cluster
            Cluster cluster = new Cluster();
            cluster.add(sequences.remove(0));

            // Add any sequences that are similar to the cluster
            for (Iterator<Sequence> it = sequences.iterator(); it.hasNext(); ) {
                Sequence seq = it.next();
                if (filter.isSimilar(cluster, seq)) {
                    cluster.add(seq);
                    it.remove();
                }
            }

            clusters.add(cluster);
        }

        // Sort by descending cluster size
        Collections.sort(clusters, new Comparator<Cluster>() {
            @Override
            public int compare(Cluster o1, Cluster o2) {
                return -Integer.compare(o1.size(), o2.size());
            }
        });

    }

    /**
     * Clusters the file using a multi-leveled approach.
     * The first of the filters is used for the initial clustering. Each subsequent
     * filter is used to subdivide each cluster into more.
     *
     * @param filters
     * @return
     */
    public List<Cluster> cluster(File file, List<SequenceSimilarityFilter> filters) {
        List<Cluster> clusters = new ArrayList<Cluster>();
        List<Sequence> sequences = readSequences(file);

        if (filters == null || filters.size() == 0) {
            Cluster cluster = new Cluster();
            cluster.addAll(sequences);
            clusters.add(cluster);
            return clusters;
        }

        //Initial clustering
        cluster(clusters, sequences, filters.get(0));

        if (filters.size() > 1) {
            // For each next filter, take each cluster, re-cluster it independently
            // with the new filter into splitClusters, and add the new clusters
            // to the newClusters list
            List<Cluster> newClusters = new ArrayList<Cluster>();
            for (int i = 1; i < filters.size(); i++) {
                for (Cluster c : clusters) {
                    List<Cluster> splitClusters = new ArrayList<Cluster>();
                    cluster(splitClusters, c, filters.get(i));
                    newClusters.addAll(splitClusters);
                }
            }
            clusters = newClusters;
        }

        return clusters;
    }

    private List<Sequence> readSequences(File fileName) {
        List<Sequence> sequences = new ArrayList<Sequence>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder currentSeq = null;
            String seqName = null;
            String seqDescription = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    seqName = line.substring(1, line.indexOf(" "));
                    seqDescription = line.substring(line.indexOf(" ") + 1);
                    if (currentSeq != null) {
                        sequences.add(new Sequence(seqName, seqDescription, currentSeq.toString()));
                    }
                    currentSeq = new StringBuilder();
                } else {
                    currentSeq.append(line.trim());
                }
            }
            if (currentSeq != null) {
                sequences.add(new Sequence(seqName, seqDescription, currentSeq.toString()));
            }
            br.close();
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return sequences;
    }

}
