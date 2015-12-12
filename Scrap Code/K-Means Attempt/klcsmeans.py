# -*- coding: utf-8 -*-
import time
from random import randrange
import operator
import itertools

class Mean_LCS:
    """Object Mean"""
    
    def __init__(self, lcs, similar_reads):
        """Object Constructor for Mediod. Creates a Mediod"""
        self.lcs = lcs
        self.similar_reads = similar_reads
    
    def getLCS(self):
        """Returns the mean"""
        return self.lcs

    def getSimilarReads(self):
        """Returns a list of the closest coordinates"""
        return self.similar_reads

    def setLCS(self, lcs):
        """Sets the mean"""
        self.lcs = lcs

    def appendSimilarReads(self, reads):
        """appends a coordinate to the list of closest coordinates of a mean"""
        self.similar_reads.append(reads)

    def setSimilarReads(self, similar_reads):
        """Sets the most similar reads to a neighborhood"""
        self.similar_reads = similar_reads

    def __str__(self):
        """Prints out Mediod with its most similar reads"""
        return 'Mean: {0}\nSimilar Reads: {1}'.format(self.lcs, self.similar_reads)

def getReads(f):
    '''Parses a reads file'''
    reads = []
    while True:
        first_line =  f.readline()
        if len(first_line) == 0:
            break  # end of file
        name = first_line[1:].rstrip()
        seq = f.readline().rstrip()
        # f.readline()  # ignore line starting with +
        # qual = f.readline().rstrip()
        reads.append(seq)
    return reads

def llcs5(a,b):
    '''Scores the Longest Common Subsequence between two strings'''
    return float(len(lcs5(a,b)))/float((min([len(a), len(b)])))
    
def lcs_lens(xs, ys):
    curr = list(itertools.repeat(0, 1 + len(ys)))
    for x in xs:
        prev = list(curr)
        for i, y in enumerate(ys):
            if x == y:
                curr[i + 1] = prev[i] + 1
            else:
                curr[i + 1] = max(curr[i], prev[i + 1])
    return curr

def lcs5(xs, ys):
    '''Calculates Longest Common Subsequnce'''
    nx, ny = len(xs), len(ys)
    if nx == 0:
        return ""
    elif nx == 1:
        return xs[0] if xs[0] in ys else ""
    else:
        i = nx // 2
        xb, xe = xs[:i], xs[i:]
        ll_b = lcs_lens(xb, ys)
        ll_e = lcs_lens(xe[::-1], ys[::-1])
        
        temp = [(ll_b[j] + ll_e[ny - j], j) for j in range(ny + 1)]
        _, k = max(temp)

        yb, ye = ys[:k], ys[k:]
        return lcs5(xb, yb) + lcs5(xe, ye)

def averageCluster(cluster):
    if len(cluster) < 2:
        return cluster
    else:
        #curr_avg = lcs5(cluster[0], cluster[1])
        curr_avg = cluster[0]
        for i in range(1, len(cluster)):
            if llcs5(curr_avg, cluster[i]) >= 0:    
                curr_avg = lcs5(curr_avg, cluster[i])
            
        return curr_avg
        
def kmean_LCS(K_LCS_Means, reads, t):
    curr_lcs = []
    next_lcs = []
    read2lcs_similarities = []
    count = 1
    
    for mean in K_LCS_Means:
        curr_lcs.append(mean.getLCS())
        
    start = time.time()

    #This while loop continues until the old mean list is no longer the new mean list.
    while True:
        #This for loop assigns every coordinate to the closest mean.
        for i, read in enumerate(reads):
            #This for loop creates a list of distances of a cooordinate to the different means respectively
            for mean in K_LCS_Means:
                read2lcs_similarities.append(llcs5(read, mean.getLCS()))
            
            most_similar_index, _ = max(enumerate(read2lcs_similarities), key=operator.itemgetter(1))
            K_LCS_Means[most_similar_index].appendSimilarReads(read)
            read2lcs_similarities = []
            print i, ": ", time.time() - start # This line prints out the current time step of the algorithm

        #Step Check.            
        kmeans_LCS_stepanalyze(K_LCS_Means, t, count)

        #Averages all the coordinate points for each mean
        for mean in K_LCS_Means:
            if len(mean.getSimilarReads()) < 2:
                next_lcs.append(mean.getLCS())
            else:
                next_lcs.append(averageCluster(mean.getSimilarReads()))

       
        #if the new mean is equal to the old mean, break from loop
        if curr_lcs == next_lcs:
            break
        else:
            count += 1
            
        if count > 20:
            return K_LCS_Means, False

        #Updates mean_list and only_means, resets only_new_means
        curr_lcs = []
        for mean in K_LCS_Means:
            mean.setLCS(next_lcs.pop(0))
            curr_lcs.append(mean.getLCS())
            mean.setSimilarReads([])
        next_lcs = []
    return K_LCS_Means, True
    
def avg(data):
    """Return the sample arithmetic mean of data."""
    n = len(data)
    if n < 1:
        raise ValueError('mean requires at least one data point')
    return sum(data)/float(n)

def _ss(data):
    """Return sum of square deviations of sequence data."""
    c = avg(data)
    ss = sum((x-c)**2 for x in data)
    return ss

def pstdev(data):
    """Calculates the population standard deviation."""
    n = len(data)
    if n < 2:
        raise ValueError('variance requires at least two data points')
    ss = _ss(data)
    pvar = ss/n # the population variance
    return pvar**0.5

def kmeans_LCS_stepanalyze(converged_K_LCS_Means, t, i):
    # Writing results for each test
    outfile = open("kmeansTest" + str(t) + "." + str(i) + ".txt", "w")
    outfile.write("Iteration " + str(t) + "\nStep: " + str(i) + "\n")        
        
    # Writing all of the LCS Means
    for i, mean in enumerate(converged_K_LCS_Means):
        outfile.write(str(i+1) + ". " + mean.getLCS() + "\t" + str(len(mean.getSimilarReads())) + "\n")

    # Writing the clusters
    outfile.write("\nClustering Information:\n")
    for i, mean in enumerate(converged_K_LCS_Means):
        lcs = mean.getLCS()
        #diffs = []
        outfile.write(str(i+1) + ". " + lcs + "\n")
        
        for j, read in enumerate(mean.getSimilarReads()):
            #diff = abs(len(read) - len(lcs))
            #diffs.append(diff)
            outfile.write(str(i+1) + "." + str(j+1) + " " + read + " " + str(llcs5(lcs, read)) + "\n")
        #if len(diffs) > 1:
            #outfile.write("Avg Edits: " + str(avg(diffs)) + "\tStdev Edits: " + str(pstdev(diffs)) + "\n")              
        outfile.write("\n")

def kmeans_analyze(k, reads, testlength = 10):
    for t in range(1, testlength):
        print "Iteration", t
        K_LCS_Means = []
        indices = []
        for i in range(k):
            rand = randrange(0, len(reads))
            while (rand in indices) == True:
                rand = randrange(0, len(reads))
            indices.append(rand)
            K_LCS_Means.append(Mean_LCS(reads[rand],[]))
        
        start = time.time()
        converged_K_LCS_Means, passed = kmeans_LCS(K_LCS_Means, reads, t)
        runtime = time.time() - start
        
        # Writing results for each test
        outfile = open("kmeansTest" + str(t) + ".txt", "w")
        outfile.write("********************RESULTS********************\n")
        
        # Writing results of convergence
        outfile.write("Convergence: FAILED TO CONVERGE.\n") if passed == False else outfile.write("Convergence: CONVERGED!\n")
            
        # Runtime
        outfile.write("\nRun Time: " + str(runtime) + "\n")
            
        # Writing all of the LCS Means
        outfile.write("\nMeans: " + "\n")
        for i, mean in enumerate(converged_K_LCS_Means):
            outfile.write(str(i+1) + ". " + mean.getLCS() + "\t" + str(len(mean.getSimilarReads())) + "\n")
            
        # Writing the clusters
        outfile.write("\nClustering Information:\n")
        for i, mean in enumerate(converged_K_LCS_Means):
            lcs = mean.getLCS()
            #diffs = []
            outfile.write(str(i+1) + ". " + lcs + "\n")
            
            for j, read in enumerate(mean.getSimilarReads()):
                #diff = abs(len(read) - len(lcs))
                #diffs.append(diff)
                outfile.write(str(i+1) + "." + str(j+1) + " " + read + " " + str(llcs5(lcs, read)) + "\n")
            #if len(diffs) > 1:
                #outfile.write("Avg Edits: " + str(avg(diffs)) + "\tStdev Edits: " + str(pstdev(diffs)) + "\n")              
            outfile.write("\n")

def main():
    text = open('f2014_hw4_reads.fa')
    reads = getReads(text)
    kmeans_analyze(10, reads, 2)
main()