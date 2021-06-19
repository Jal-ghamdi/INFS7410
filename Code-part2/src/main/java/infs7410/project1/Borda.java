package infs7410.project1;
import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;
import java.util.HashMap;
import java.util.List;
import java.util.*;
public class Borda extends fusion{

        @Override
        public TrecResults Fuse(List<TrecResults> resultsLists) {
            HashMap<String, TrecResult> seen = new HashMap<>();

            for (TrecResults results : resultsLists) {
                double n = results.size();
                for (int i = 0; i < results.size(); i++) {
                    TrecResult result = results.get(i);

                    double score = ((n-(result.getRank())+1)/n);
                    // TODO: IMPLEMENT ME!
                    if (seen.containsKey(result.getDocID())){
                        seen.put(result.getDocID(), new TrecResult(result.getTopic(), result.getDocID(), 0, seen.get(result.getDocID()).getScore() + score, null));
                    } else {
                        seen.put(result.getDocID(), new TrecResult(result.getTopic(), result.getDocID(), 0, score, null));
                    }
                }
            }

            return flatten(seen);
        }
    }



