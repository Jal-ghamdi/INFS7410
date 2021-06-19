package infs7410.project1;
import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;
import java.util.HashMap;
import java.util.List;

public class CombMNZ extends fusion{
    @Override
    public TrecResults Fuse(List<TrecResults> resultsLists) {
        HashMap<String, TrecResult> seen = new HashMap<>();
        HashMap<String, Integer> occur = new HashMap<>();

        for (TrecResults trecResults : resultsLists) {
            for (TrecResult result : trecResults.getTrecResults()) {

                if (!seen.containsKey(result.getDocID())) {
                    seen.put(result.getDocID(), result);
                    occur.put(result.getDocID(), 1);
                } else {
                    double score = seen.get(result.getDocID()).getScore(); // TODO: IMPLEMENT ME.
                    result.setScore(result.getScore() + score);
                    seen.put(result.getDocID(), result);
                    if (result.getScore() > 0) {
                        occur.put(result.getDocID(), occur.get(result.getDocID()) + 1);
                    }
                }
            }
        }

        for (TrecResults trecResults : resultsLists) {
            for (TrecResult result : trecResults.getTrecResults()) {

                if (occur.containsKey(result.getDocID())) {
                    result.setScore(result.getScore() * occur.get(result.getDocID()));
                    seen.put(result.getDocID(), result);
                }
            }
        }

        return flatten(seen);
    }
}
