package edu.jhu.compgenomics.dvdhit;

/**
 * Created by Phani on 11/23/2015.
 */
public class Sequence {

    private final String sequence;
    private final String name;
    private final String description;

    public Sequence(String name, String description, String sequence) {
        this.sequence = sequence;
        this.name = name;
        this.description = description;
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
