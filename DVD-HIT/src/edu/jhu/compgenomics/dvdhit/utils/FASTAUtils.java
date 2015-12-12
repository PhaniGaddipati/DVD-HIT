package edu.jhu.compgenomics.dvdhit.utils;

import edu.jhu.compgenomics.dvdhit.model.Sequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Phani on 12/7/2015.
 */
public class FASTAUtils {
    /**
     * Reads a FASTA file and returns all of the sequences.
     *
     * @param fileName
     * @return
     */
    public static List<Sequence> readSequences(File fileName) {
        List<Sequence> sequences = new ArrayList<>();
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
                    if (currentSeq != null)
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
