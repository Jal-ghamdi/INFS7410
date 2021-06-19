import sys

import matplotlib.pyplot as plt
import numpy as np
from trectools import TrecRes

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("not enough arguments specified")
        sys.exit(1)
    fname1 = sys.argv[1]
    fname2 = sys.argv[2]
    measure = sys.argv[3]
    output = sys.argv[4]

    ev1 = TrecRes(fname1)
    ev2 = TrecRes(fname2)

    r1 = ev1.get_results_for_metric(measure)
    r2 = ev2.get_results_for_metric(measure)

    ind = np.arange(len(r1))
    width = 0.45
    
    plt.bar(ind, list(r1.values()),label= 'BM25')
    plt.bar(ind, list(r2.values()),label= 'TF_IDF')
    plt.xticks(ind, list(r1.keys()), rotation="vertical")
    #plt.xticks(ind, list(r2.keys()), rotation="vertical")
    plt.ylim(0, 1)
    plt.title('{} and {}'.format(fname1, fname2))
    plt.ylabel(measure)
    plt.legend(loc = 'upper left')
    plt.tight_layout()
    plt.savefig(output)
