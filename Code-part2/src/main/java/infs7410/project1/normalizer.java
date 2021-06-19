package infs7410.project1;
import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;
import java.util.List;

public interface normalizer {
        void init(TrecResults items);

        double normalise(TrecResult result);
    }

