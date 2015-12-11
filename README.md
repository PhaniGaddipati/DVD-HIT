# DVD-HIT
The next generation of CD-HIT (maybe)

# Project Structure
The code is entirely in Java. The code actually used is in the package edu.jhu.compgenomics.dvdhit. The file StringSimilarityTests is simply a file where we tested filters before actually integrating them. The main class is MainApp, which parses the command line arguments, and launches DVD-HIT on the fasta file.

###Packages
**Filters** - Each filter takes a Cluster and a Sequence and returns whether the sequence belongs in the given cluster using the representative sequence. Different filters are plugged in depending on the command line argument (below). This allows different similarity filters to be plugged in and chained.

**Model** - The objects representing a Cluster and a Sequence. A cluster is simply a list of sequences that keeps track of the longest sequence to use as the representative sequence. A sequence contains the sequence, name, description, and parsed genus and species from the labels in the FASTA file.

**Utils** - Static utility classes to provide clstr file reading and writing, fasta file parsing, and evaulation (see report).

# Usage
After compiling the JAR (provided), the usage is as follows. Java 8 is required. The file paths should be the absolute file paths.

    java -jar DVD_HIT.jar K threshold input_fasta output_clstr filterid...

K - The size of k-gram to use for relevant filters (all but LLCS)

threshold - The similarity threshold for the greedy clustering, on a scale of [0-1]

input_fasta - The input FASTA file containing the sequences

output_clstr - Where to write the output *.clstr file

filterid - A list of filters (ids) to use. After using the first filter, subsequent filters are used independantly on each cluster resulting from the previous filter.

##Filter IDs
| Filter            | ID |
|-------------------|----|
| Short Word        | 0  |
| Jaccard           | 1  |
| Cosine            | 2  |
| LLCS              | 3  |
| Spaced Short Word | 4  |
