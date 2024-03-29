package edu.jhu.compgenomics.dvdhit.model;

/**
 * A class representing a sequence. A sequence contains the name,
 * description, and the sequence content. The genus and species
 * are also parsed out from the description if possible.
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
        //Parse out genus and species if it's there
        String tmp = description.toLowerCase().replace("'", "").replace("[", "").replace("]", "").trim();
        if (tmp.matches("[a-z]+ [a-z]+ .+")) {
            genus = tmp.substring(0, tmp.indexOf(" "));
            tmp = tmp.substring(tmp.indexOf(" ") + 1);
            species = tmp.substring(0, tmp.indexOf(" "));
        } else {
            genus = "";
            species = "";
        }
    }

    public String getDescription() {
        return description;
    }

    public String getSequence() {
        return sequence;
    }

    public String getName() {
        return name;
    }

    public String getGenus() {
        return genus;
    }

    public String getSpecies() {
        return species;
    }

    @Override
    public String toString() {
        return getSequence();
    }
}
