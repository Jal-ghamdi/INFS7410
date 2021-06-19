package infs7410.project1;
import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;
import java.util.HashMap;
import java.util.List;


public class CombSUM extends fusion {
        @Override
        public TrecResults Fuse(List<TrecResults> resultsLists) {
            HashMap<String, TrecResult> seen = new HashMap<>();

            for (TrecResults trecResults : resultsLists) {
                for (TrecResult result : trecResults.getTrecResults()) {
                    if (!seen.containsKey(result.getDocID())) {
                        seen.put(result.getDocID(), result);
                    } else {
                        //double score = 0.0;
                        //score = seen.getScore();
                        //...

                        double score = result.getScore() + seen.get(result.getDocID()).getScore();

                        result.setScore(score);
                        seen.put(result.getDocID(), result);
                    }
                }
            }

            return flatten(seen);
        }
    }

