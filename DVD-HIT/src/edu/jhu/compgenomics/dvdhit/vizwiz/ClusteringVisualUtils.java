package edu.jhu.compgenomics.dvdhit.vizwiz;

import edu.jhu.compgenomics.dvdhit.model.Cluster;
import edu.jhu.compgenomics.dvdhit.model.Sequence;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Phani on 12/15/2015.
 */
public class ClusteringVisualUtils {

    private static Random rand = new Random();

    public static void showClusteringVisual(List<Cluster> clusters) {
        Map<String, Color> genusColors = new HashMap<>();
        int numClusters = clusters.size();
        BufferedImage image = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        int numSeqs = 0;
        int maxR = 1000;
        for (Cluster cluster : clusters) {
            numSeqs += cluster.size();
        }
        double rPerSeq = ((double) maxR) / ((double) (numSeqs));
        for (Cluster cluster : clusters) {
            int r = (int) Math.max(50, rPerSeq * cluster.size());
            int x = (int) (Math.random() * (image.getWidth() - 2 * r) + r);
            int y = (int) (Math.random() * (image.getHeight() - 2 * r)) + r;
            paintCluster(cluster, g2d, x, y, r, genusColors);
        }
        try {
            ImageIO.write(image, "PNG", new File("C:\\Users\\Phani\\Desktop\\test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void paintCluster(Cluster cluster, Graphics2D g, int x, int y, int r, Map<String, Color> genusColors) {
        for (Sequence seq : cluster) {
            double angle = Math.random() * Math.PI * 2;
            double randR = (Math.random() * r);
            int sX = (int) (Math.cos(angle) * randR);
            int sY = (int) (Math.sin(angle) * randR);
            Color c = genusColors.get(seq.getGenus());
            if (c == null) {
                c = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                genusColors.put(seq.getGenus(), c);
            }
            g.setColor(c);
            g.fillOval(x + sX - 2, y + sY - 2, 5, 5);
        }
    }

}
