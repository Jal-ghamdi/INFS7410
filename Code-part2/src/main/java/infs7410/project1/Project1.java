package infs7410.project1;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import infs7410.project1.Borda;
import infs7410.project1.CombMNZ;
import infs7410.project1.CombSUM;
import infs7410.project1.fusion;
import infs7410.project1.MinMax;
import infs7410.project1.normalizer;
import org.terrier.matching.models.TF_IDF;
import org.terrier.matching.models.BM25;
import org.terrier.structures.Index;
import org.apache.commons.math3.stat.inference.TTest;
import java.io.IOException;
import org.terrier.querying.SearchRequest;
import org.terrier.querying.Manager;
import org.terrier.indexing.Collection;
import org.terrier.indexing.SimpleXMLCollection;
import org.terrier.querying.*;
import org.terrier.structures.Index;
import org.terrier.structures.IndexFactory;
import org.terrier.structures.indexing.Indexer;
import org.terrier.structures.indexing.classical.BasicIndexer;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;



public class Project1 {
 public static void main(String[] args) throws Exception {

   //The following is to extract the already created boolean querie from file
   File queryFile = new File("/Users/jewelsalghamdi/Downloads/Project1/project/var/results/boolean-query/2018TrainingQuery.txt"); // 2
   FileInputStream fis = new FileInputStream(queryFile);
   byte[] bytes = new byte[(int) queryFile.length()];
   fis.read(bytes);
   fis.close();
   String queryFileText = new String(bytes, "UTF-8"); // Full text of the file
   // Extracting query text from the file
   String query = queryFileText.substring(queryFileText.indexOf(p.getTopic())+p.getTopic().length()+1,
           queryFileText.indexOf("\n",queryFileText.indexOf(p.getTopic()))); //Query of the file
   String[] queryTerms = query.split(" ");



   for (int counter = 0; counter < p.query.size(); counter = counter + 1) {

   querytmp[counter] = p.query.get(counter);
   }

   for (int counter = 0; counter < p.pids.size(); counter = counter + 1) {

    pidstmp[counter] = p.pids.get(counter);
   }






   //The name of the file to oup
   String outputFilename = new String();

   //List of filenames of results.
   List<String> resultFilenames = new ArrayList<>();

               resultFilenames.add("/../part-2/KLI/Runs/2017TestingTitleQueries-BM25-ReducedWithKLI0.3.res");
               resultFilenames.add("/../2018TestingTitleQueries-BM25-ReducedWithR0.3.res");
//             resultFilenames.add("/../2017TestingTitleQueries-BM25-ReducedWithR0.85.res");
//             resultFilenames.add("/../2017TestingTitleQueries-TF_IDF-ReducedWithR0.85.res");


//             resultFilenames.add("/../2017TestingTitleQueries-TF_IDF-ReducedWithR0.3.res");
//             resultFilenames.add("/../2017TestingTitleQueries-BM25-ReducedWithR0.3.res");
//             resultFilenames.add("/../2018/ECNU_TASK2_RUN1_TFIDF.task2.res");
//             resultFilenames.add("/../2018/sheffield-general_terms.task2.res");
//             resultFilenames.add("/../2018/sheffield-query_terms.task2.res");
// other runs for fusion
//             resultFilenames.add("/../2017/BASELINE.pubmed.random.res");
//             resultFilenames.add("/../2017/uos.sis.TMBEST_BM25.res");
//             resultFilenames.add("/../2017/qut.result_bool_es_test.res");
//             resultFilenames.add("/../2017/qut.result_pico_es_test.res");
//             resultFilenames.add("/../2017/sheffield.run4.res");
//             resultFilenames.add("/../2017/sheffield.run1.res");
//             resultFilenames.add("/../2017/sheffield.run2.res");
//             resultFilenames.add("/../2017/BASELINE.BM25.res");


   // Parse each of the results files and add them to a list.
   List<TrecResults> results = new ArrayList<>(resultFilenames.size());
   for (String filename : resultFilenames) {
    results.add(new TrecResults(filename));
   }

   // Set the normaliser and fusion method to use.
   normalizer norm = new MinMax();

   // fusion methods
   fusion f1 = new Borda();
   fusion f2 = new CombSUM();
   fusion f3 = new CombMNZ();


   // Normalise the scores of each run.
   for (TrecResults trecResults : results) {
    norm.init(trecResults);
    for (int j = 0; j < trecResults.getTrecResults().size(); j++) {
     double normScore = norm.normalise(trecResults.getTrecResults().get(j));
     trecResults.getTrecResults().get(j).setScore(normScore);
    }
   }
   Logger logger = Logger.getLogger(Project1.class);

   logger.info("fusing results for each topic");

   Set<String> topics = results.get(0).getTopics();

   TrecResults fusedBorda = new TrecResults();
   TrecResults fusedCombSUM = new TrecResults();
   TrecResults fusedCombMNZ = new TrecResults();


   for (String topic : topics) {
    logger.info(topic);
    List<TrecResults> topicResults = new ArrayList<>();
    for (TrecResults r : results) {
     topicResults.add(new TrecResults(r.getTrecResults(topic)));
    }

    fusedBorda.getTrecResults().addAll(f1.Fuse(topicResults).getTrecResults());
    //Collections.sort(fusedBorda.getTrecResults());
    //Collections.reverse(fusedBorda.getTrecResults());

    fusedCombSUM.getTrecResults().addAll(f2.Fuse(topicResults).getTrecResults());
    //Collections.sort(fusedCombSUM.getTrecResults());
    //Collections.reverse(fusedCombSUM.getTrecResults());

    fusedCombMNZ.getTrecResults().addAll(f3.Fuse(topicResults).getTrecResults());
    ////Collections.sort(fusedCombMNZ.getTrecResults());
    //Collections.reverse(fusedCombMNZ.getTrecResults());
   }

   logger.info("writing results to disk");
   fusedBorda.setRunName("FusedBorda2017TestingBooleanQueriesBM25-TF-IDFFusion");
   fusedBorda.write("FusionBorda2017TestingBooleanQueriesBM25-TF-IDFFusion.fused");


   fusedCombSUM.setRunName("FusedCombSUM2017TestingBooleanQueriesBM25-TF-IDFFusion");
   fusedCombSUM.write("FusionCombSUM2017TestingBooleanQueriesBM25-TF-IDFFusion.fused");

   fusedCombMNZ.setRunName("FusedCombANZ2018TestingTitleQueriesBM25-TF-IDFFusion");
   fusedCombMNZ.write("FusionCombANZ2018TestingTitleQueriesBM25-TF-IDFFusion.fused");

   // Fuse the results together and write the new results list to disk.
   //fusedResults.getTrecResults().addAll(fusion.Fuse(topicResults).getTrecResults());

  }
 }

}

      
