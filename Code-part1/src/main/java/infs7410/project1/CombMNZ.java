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

//        @Override
//        public TrecResults Fuse(List<TrecResults> resultsLists) {
//            HashMap<String, TrecResult> seen = new HashMap<>();
//
//            HashMap<String, Integer> counterOccurrences = new HashMap<>();
//
//            for (TrecResults trecResults : resultsLists) {
//                for (TrecResult result : trecResults.getTrecResults()) {
//                    if (!seen.containsKey(result.getDocID())) {
//                        seen.put(result.getDocID(), result);
//                        counterOccurrences.put(result.getDocID(),1);
//
//                    } else
//                    {
//                        //double score = 0.0;
//                        //int counter=1;
//                        //counter ++;
//
//                        //result.setScore((result.getScore() + score)*counter);
//                        //seen.put(result.getDocID(), result);
//
//
//                        Integer temp_counter = counterOccurrences.get(result.getDocID());
//                        counterOccurrences.replace(result.getDocID(),temp_counter+1);
//                        double score = (result.getScore() + seen.get(result.getDocID()).getScore());
//
//                        result.setScore(score);
//                        seen.put(result.getDocID(), result);
//
//
//                    }
//
//                }
//
//            }
//
//            for(String docId : seen.keySet()){
//                seen.get(docId).setScore(seen.get(docId).getScore() * counterOccurrences.get(docId));
//            }
//            return flatten(seen);
//        }
//    }

