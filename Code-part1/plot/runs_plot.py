import sys

import matplotlib.pyplot as plt
import numpy as np
from trectools import TrecRes

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("not enough arguments specified")
        sys.exit(1)
    measure = sys.argv[1]
    output = sys.argv[2]
    fnames = sys.argv[3:]

    evs = []
    for fname in fnames:
        evs.append(TrecRes(fname))

    res = []
    for ev in evs:
        res.append(list(ev.get_results_for_metric(measure).values()))

    ind = np.arange(len(fnames))

    
    # HINT: https://matplotlib.org/3.1.1/api/_as_gen/matplotlib.pyplot.boxplot.html
    fig, ax = plt.subplots()
    plt.boxplot(res)
    plt.xticks(ind+1, fnames, rotation="vertical")
    plt.ylim(0, 1)
    plt.title('2017-2018 TrainingTitleQueries-ReducedWithKLI-0.85 Comparison')
    plt.ylabel(measure)
    plt.tight_layout()
    plt.savefig(output)
