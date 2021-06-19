import sys

import matplotlib.pyplot as plt
import numpy as np
from trectools import TrecRes

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("not enough arguments specified")
        sys.exit(1)
    fname = sys.argv[1]
    measure = sys.argv[2]
    output = sys.argv[3]

    ev = TrecRes(fname)

    r = ev.get_results_for_metric(measure)

    ind = np.arange(len(r))
    plt.bar(ind, list(r.values()))
    plt.xticks(ind, list(r.keys()), rotation="vertical")
    plt.ylim(0, 1)
    plt.title(fname)
    plt.ylabel(measure)
    plt.tight_layout()
    plt.savefig(output)
