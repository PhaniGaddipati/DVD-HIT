package edu.jhu.compgenomics.dvdhit;

/**
 * Created by Phani on 11/23/2015.
 */
public class Sequence {

    private final String sequence;
    private final String name;
    private final String description;
    private final String genus;
    private final String species;

    public Sequence(String name, String description, String sequence) {
        this.sequence = sequence;
        this.name = name;
        this.description = description;
        String tmp = description.toLowerCase().replace("'", "").replace("[", "").replace("]", "").trim();
        genus = tmp.substring(0, tmp.indexOf(" "));
        tmp = tmp.substring(tmp.indexOf(" ") + 1);
        species = tmp.substring(0, tmp.indexOf(" "));
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getSequence();
    }
}
