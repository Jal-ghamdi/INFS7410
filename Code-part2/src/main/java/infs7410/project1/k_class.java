package infs7410.project1;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.terrier.querying.IndexRef;
import org.terrier.querying.Manager;
import org.terrier.querying.ManagerFactory;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.*;
import org.terrier.structures.postings.IterablePosting;


class k_class {
    public k_class() {

    }


    public String reduceFunction(String[] terms, String[] docIds, int K, Index index) throws IOException {
        //String[] terms = query.split(" +");
         PostingIndex inverted = index.getInvertedIndex();
         MetaIndex m = index.getMetaIndex();
         HashSet<String> docIdSet = new LinkedHashSet<>(Arrays.asList(docIds));
         Lexicon<String> lexicon = index.getLexicon();
         HashMap<String, Integer> docNoToDocId = new HashMap<>();
         double numberOfTerms = index.getCollectionStatistics().getNumberOfDocuments();
         double cl = index.getCollectionStatistics().getNumberOfTokens();
         List<Pair> scoredTerms = new ArrayList<>(terms.length);



         for (String term : terms) {
//             double sumDocFrq = 0;

             LexiconEntry entry = lexicon.getLexiconEntry(term);
             if (entry == null) {
                 scoredTerms.add(new Pair(term, 0));
                 continue;
             }


             double docFreq = entry.getFrequency();

             IterablePosting p = inverted.getPostings(entry);


             double TDocFreq = 0;
             while (p.next() != IterablePosting.EOL) {
                 String DocId = m.getItem("docno", p.getId());
                 if (docIdSet.contains(DocId)) {
                      TDocFreq+= 1;
//                     sumDocFrq = sumDocFrq + 1;

                 }
             }
             double PTD = (double) TDocFreq / (double) docIds.length;
             double PTC = docFreq / cl;
             double KLI = PTD * Math.log(PTD / PTC);
             scoredTerms.add(new Pair(term, KLI));

         }
         Collections.sort(scoredTerms);
         Collections.reverse(scoredTerms);
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < K && i < scoredTerms.size(); i++) {
             sb.append(scoredTerms.get(i).term).append(" ");
         }

         return sb.toString();
     }

     static class Pair implements Comparable<Pair> {
         String term;
         double score;

         public Pair(String term, double score) {
             this.term = term;
             this.score = score;

         }

         @Override
         public int compareTo(Pair o) {
             return Double.compare(score, o.score);
         }
     }
     private static double round(double value, int places) {
         if (places < 0) throw new IllegalArgumentException();

         BigDecimal bd = new BigDecimal(Double.toString(value));
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
     }
 }



// Another attempt

//        PostingIndex inverted = index.getInvertedIndex();
//        MetaIndex m = index.getMetaIndex();
//        HashSet<String> docIdSet = new LinkedHashSet<>(Arrays.asList(docIds));
//        Lexicon<String> lexicon = index.getLexicon();
//        HashMap<String, Integer> docNumberToDocId = new HashMap<>();
//        double numberOfTerms = index.getCollectionStatistics().getNumberOfDocuments();
//        List<Pair> scoredTerms = new ArrayList<>(terms.length);
//
//        // Run a search request using the original query.
//        //Manager queryManager = ManagerFactory.from(ref);
//
//        double sumLen = 0;
//
//        for (int idx = 0; idx < numberOfTerms; idx++) {
//            String docno = m.getItem("docno", idx);
//            if (!docNumberToDocId.containsKey(docno)) {
//                docNumberToDocId.put(docno, idx);
//            }
//        }
//        for (String doc : docIds) {
//            int docId = docNumberToDocId.get(doc);
//            sumLen += index.getDocumentIndex().getDocumentLength(docId);
//        }
//            for (String term : terms) {
//                double sumDocFrq = 0;
//                LexiconEntry entry = lexicon.getLexiconEntry(term);
//                if (entry == null) {
//                    scoredTerms.add(new Pair(term, 0));
//                    continue;
//                }
//
//                double docFreq = entry.getDocumentFrequency();
//
//                IterablePosting p = inverted.getPostings(entry);
//                double collection_Freq = entry.getWritableEntryStatistics().getFrequency();
//                double cl = index.getCollectionStatistics().getNumberOfTokens();
//                int TermFreDoc = 0;
//                int DocLength = 0;
//                while (p.next() != IterablePosting.EOL) {
//                    String DocId = m.getItem("docno", p.getId());
//
//                    int TermFrq = p.getFrequency();
//                    int docLength = p.getDocumentLength();
//
//                    if (docIdSet.contains(DocId)) {
//                        double DocFreq = p.getFrequency();
//                        sumDocFrq = sumDocFrq + 1;
////                        TermFreDoc = TermFreDoc + TermFrq;
////                        DocLength = DocLength + docLength;
//                    }
//                }
//
//                double PTD = (double) sumDocFrq / (double) sumLen;
////    double PTD = round((double)TermFreDoc / (double)DocLength,5);
//                double PTC = collection_Freq / cl;
//                double KLI = PTD * Math.log(PTD / PTC);
//
//
//                scoredTerms.add(new Pair(term, KLI));
//
//
//            }
//
//            Collections.sort(scoredTerms);
//            Collections.reverse(scoredTerms);
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < K && i < scoredTerms.size(); i++) {
//                sb.append(scoredTerms.get(i).term).append(" ");
//            }
//
//            return sb.toString();
//        }
//
//        static class Pair implements Comparable<Pair> {
//            String term;
//            double score;
//
//            public Pair(String term, double score) {
//                this.term = term;
//                this.score = score;
//
//            }
//
//            @Override
//            public int compareTo(Pair o) {
//                return Double.compare(score, o.score);
//            }
//        }
//        private static double round ( double value, int places){
//            if (places < 0) throw new IllegalArgumentException();
//
//            BigDecimal bd = new BigDecimal(Double.toString(value));
//            bd = bd.setScale(places, RoundingMode.HALF_UP);
//            return bd.doubleValue();
//        }
//    }
