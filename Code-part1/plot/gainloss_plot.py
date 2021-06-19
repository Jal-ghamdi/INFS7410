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


    # HINT: https://docs.scipy.org/doc/numpy/reference/generated/numpy.subtract.html
    plt.bar(ind,np.subtract(list(r1.values()),list(r2.values())))
    plt.xticks(ind, list(r1.keys()), rotation="vertical")
    plt.ylim(-1, 1)

    plt.title("2018 Testing Title Queries using KLI at 0.5 - BM25 and TF_IDF")
    
    plt.ylabel(measure)
    plt.tight_layout()
    plt.savefig(output)
