package infs7410.project1;
import org.apache.log4j.Logger;
import org.terrier.matching.models.TF_IDF;
import org.terrier.matching.models.BM25;
import org.terrier.matching.models.WeightingModel;
import org.terrier.structures.Index;
import org.terrier.terms.PorterStemmer;
import org.terrier.terms.Stopwords;
import org.terrier.utility.ApplicationSetup;
import org.terrier.applications.batchquerying.TRECQuerying;
import org.terrier.querying.IndexRef;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class RunBuilder {
    public static void main(String[] args) throws IOException {

        Logger logger = Logger.getLogger(Project1.class);
        Index index = Index.createIndex("/../var/index", "pubmed");
        File folder = new File(" ");

        // The following is to build the topic files from the Titles Block
         folder = new File("/../tar2/2017-TAR/training/topics"); // 1
         System.out.println("Building 2017 Training Topic Files From Title");
         buildFromTitle(index,folder,"2017TrainingTitleQueries-TF_IDF-version2",new TF_IDF());
         buildFromTitle(index,folder,"2017TrainingTitleQueries-BM25-version2",new BM25());
//
        folder = new File("/../tar2/2017-TAR/testing/topics"); // 2
        System.out.println("Building 2017 Testing Topic Files From Title");
        buildFromTitle(index,folder,"2017TestingTitleQueries-TF_IDF",new TF_IDF());
        buildFromTitle(index,folder,"2017TestingTitleQueries-BM25",new BM25());
//
        folder = new File("/../tar2/2018-TAR/training/topics"); // 3
        System.out.println("Building 2018 Training Topic Files From Title");
        buildFromTitle(index,folder,"2018TrainingTitleQueries-TF_IDF-version2",new TF_IDF());
        buildFromTitle(index,folder,"2018TrainingTitleQueries-BM25-version2",new BM25());
//
        folder = new File("/../tar2/2018-TAR/testing/topics"); // 4
        System.out.println("Building 2018 Testing Topic Files From Title");
        buildFromTitle(index,folder,"2018TestingTitleQueries-TF_IDF-version2",new TF_IDF());
        buildFromTitle(index,folder,"2018TestingTitleQueries-BM25-version2",new BM25());


//        // Fine Tuning for 2017 Training
        System.out.println("Processing fine tuning runs for 2017 Training");
        for (double b = 0.25; b < 1.3; b = b + 0.25) {
            //Skipping the default value of b, because we already have it
            if (b==0.75)
                continue;
            // The following is to build the topic files from the Query Block
            folder = new File("/../tar2/2017-TAR/training/topics"); // 1
            WeightingModel bm25 = new BM25();
            bm25.setParameter(b);
            buildFromTitle(index,folder,"2017TrainingBooleanQueries-BM25" + "-withB" + b ,bm25);
        }
        System.out.println("Completed fine tuning runs for 2017 Training");


//        // Fine Tuning for 2017 Testing
        System.out.println("Processing fine tuning runs for 2017 Testing");
        for (double b = 0.25; b < 1.3; b = b + 0.25) {
            //Skipping the default value of b, because we already have it
            if (b==0.75)
                continue;
            // The following is to build the topic files from the Query Block
            folder = new File("/../tar2/2017-TAR/testing/topics"); // 1
            WeightingModel bm25 = new BM25();
            bm25.setParameter(b);
            buildFromTitle(index,folder,"2017TestingBooleanQueries-BM25" + "-withB" + b ,bm25);
        }
        System.out.println("Completed fine tuning runs for 2017 Testing");


//        // Fine Tuning for 2018 Training
        System.out.println("Processing fine tuning runs for 2018 Training");
        for (double b = 0.25; b < 1.3; b = b + 0.25) {
            //Skipping the default value of b, because we already have it
            if (b==0.75)
                continue;
            // The following is to build the topic files from the Query Block
            folder = new File("/../tar2/2018-TAR/training/topics"); // 1
            WeightingModel bm25 = new BM25();
            bm25.setParameter(b);
            buildFromTitle(index,folder,"2018TrainingBooleanQueries-BM25" + "-withB" + b ,bm25);
        }
        System.out.println("Completed fine tuning runs for 2018 Training");


//        // Fine Tuning for 2018 Testing
        System.out.println("Processing fine tuning runs for 2018 Testing");
        for (double b = 0.25; b < 1.3; b = b + 0.25) {
            //Skipping the default value of b, because we already have it
            if (b==0.75)
                continue;
            // The following is to build the topic files from the Query Block
            folder = new File("/../tar2/2018-TAR/testing/topics"); // 1
            WeightingModel bm25 = new BM25();
            bm25.setParameter(b);
            buildFromTitle(index,folder,"2018TestingBooleanQueries-BM25" + "-withB" + b ,bm25);
        }


//         The following is to build the topic files from the Query Block
//        // PART1
//        // Now we want to run this section to produce the run files for 2017 Training Boolean Queries for BM25 and TF_IDF
        folder = new File("/../tar2/2017-TAR/training/topics"); //
        File queryFile = new File("/../var/results/boolean-query/2017TrainingQuery.txt"); //
        logger.info("Building 2017 Training Topic Files From Query");
        System.out.println("Building 2017 Training Topic Files From Query");
        buildFromQuery(index,folder,queryFile,"2017TrainingBooleanQueries-TF_IDF-version2",new TF_IDF());

        buildFromQuery(index,folder,queryFile,"2017TrainingBooleanQueries-BM25-version2",new BM25());

//
//        //PART2
//        //Once the above section is done, comment and uncomment the following lines
//        //Now we want to run this section to produce the run files for 2017 Training Boolean Queries for BM25 and TF_IDF
        folder = new File("/../tar2/2017-TAR/testing/topics"); // 2
        queryFile = new File("/../var/results/boolean-query/2017TestingQuery.txt"); // 2
        logger.info("Building 2017 Testing Topic Files From Query");
        System.out.println("Building 2017 Testing Topic Files From Query");
        buildFromQuery(index,folder,queryFile,"2017TestingBooleanQueries-TF_IDF-version2",new TF_IDF());

        buildFromQuery(index,folder,queryFile,"2017TestingBooleanQueries-TF-BM25-version2",new BM25());

//
//        //PART3
        folder = new File("/../tar2/2018-TAR/training/topics"); // 3
        queryFile = new File("/../var/results/boolean-query/2018TrainingQuery.txt"); // 3
        logger.info("Building 2018 Training Topic Files From Query");
        System.out.println("Building 2018 Training Topic Files From Query");
        buildFromQuery(index,folder,queryFile,"2018TrainingBooleanQueries-TF_IDF-version2",new TF_IDF());

        buildFromQuery(index,folder,queryFile,"2018TrainingBooleanQueries-BM25-version2",new BM25());

//
//        //PART4
        folder = new File("/../tar2/2018-TAR/testing/topics"); // 4
        queryFile = new File("/../var/results/boolean-query/2018TestingQuery.txt"); // 4
        logger.info("Building 2018 Testing Topic Files From Query");
        System.out.println("Building 2018 Testing Topic Files From Query");
        buildFromQuery(index,folder,queryFile,"2018TestingBooleanQueries-TF_IDF-version2",new TF_IDF());

        buildFromQuery(index,folder,queryFile,"2018TestingBooleanQueries-BM25-version2",new BM25());


         //The following is to build the topic files from the Titles Block and applying reduction on title With K_Class
         //2017Training
         folder = new File("/../tar2/2017-TAR/training/topics"); // 1
         System.out.println("Building 2017 Training Topic Files From Title");
         double reductions[] = new double[] {0.3,0.5,0.85};
         for (int b = 0; b < reductions.length; b++) {
             double r = reductions[b];
             WeightingModel bm25 = new BM25();
             bm25.setParameter(0.25);
             buildFromTitleReduced(index, folder, "2017TrainingTitleQueries-BM25-ReducedWithKLIversion2"+r,bm25,r);
             buildFromTitleReduced(index, folder, "2017TrainingTitleQueries-TF_IDF-ReducedWithKLIversion2"+r, new TF_IDF(), r);
         }

        //2017Testing
        folder = new File("/../tar2/2017-TAR/testing/topics"); // 1
        System.out.println("Building 2017 Testing Topic Files From Title");
        for (int b = 0; b < reductions.length; b++) {
            double r = reductions[b];
            WeightingModel bm25 = new BM25();
            bm25.setParameter(0.25);
            buildFromTitleReduced(index, folder, "2017TestingTitleQueries-BM25-ReducedWithKLIversion2"+r, bm25, r);
            buildFromTitleReduced(index, folder, "2017TestingTitleQueries-TF_IDF-ReducedWithKLIversion2"+r, new TF_IDF(), r);
        }

        //2018Training
        folder = new File("/../tar2/2018-TAR/training/topics"); // 1
        System.out.println("Building 2018 Training Topic Files From Title");
        for (int b = 0; b < reductions.length; b++) {
            double r = reductions[b];
            WeightingModel bm25 = new BM25();
            bm25.setParameter(0.25);
            buildFromTitleReduced(index, folder, "2018TrainingTitleQueries-BM25-ReducedWithKLIversion2"+r, bm25, r);
            buildFromTitleReduced(index, folder, "2018TrainingTitleQueries-TF_IDF-ReducedWithKLIversion2"+r, new TF_IDF(), r);
        }

        //2018Testing
        folder = new File("/../tar2/2018-TAR/testing/topics"); // 1
        System.out.println("Building 2018 Testing Topic Files From Title");
        for (int b = 0; b < reductions.length; b++) {
            double r = reductions[b];
            WeightingModel bm25 = new BM25();
            bm25.setParameter(0.25);
            buildFromTitleReduced(index, folder, "2018TestingTitleQueries-BM25-ReducedWithKLI-version2"+r, bm25, r);
            buildFromTitleReduced(index, folder, "2018TestingTitleQueries-TF_IDF-ReducedWithKLIversion2"+r, new TF_IDF(), r);
        }

    }

    /**
     * Build the query required for part 1 of the project from the topic, title and pids
     *
     * @param  indx  the index path
     * @param  folder  an instance of File that has the folder path of the topic files to be processed
     * @param  filename the output folder name for the file to write
     * @param  wm the required weightingModel (TF_IDF or BM25)
     */
    public static void buildFromTitle(Index indx, File folder,String filename, WeightingModel wm) throws IOException {


        Logger logger = Logger.getLogger(Project1.class);
        Index index = indx;
        Reranker reranker = new Reranker(index);

        File[] listOfFiles = folder.listFiles();

        // To print the time for each file process
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


        List<String> stopwords = Files.readAllLines(Paths.get("/../var/results/boolean-query/Stopwords.txt"));
        // so if the above words appear in the title list of terms, they are removed in the following loop
        // to add manually to the list
        stopwords.add("patient");
        stopwords.add("exp");


        for (int i = 0; i < listOfFiles.length; i++) { //listOfFiles.length


            File file = new File(listOfFiles[i].toString());
//            ٍSystem.out.println(listOfFiles[i].toString());
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            //to prevent reading hidden file and throwing error
            if (listOfFiles[i].toString().contains(".DS_Store")){
               continue;
           }

            // Extracting the content of the file
            String text = new String(bytes, "UTF-8"); // Full text of the file
            String topic = text.substring(7,text.indexOf("\n")-1); // Topic of the file
            String title = text.substring(text.indexOf("Title:")+7,text.indexOf("Query:"));

            //Processing the title text
            title = title.toLowerCase();
            title = title.replaceAll("\n","");
            // Importing custom stopwords files to use and adding to them some extra words specific to the list

            // Here we are removing the terms that appear in the stopword list of words
            for (String word : title.split(" ")){
               if (stopwords.contains(word)){
                   title = title.replaceAll(word,"");
               }
            }
            title = title.replaceAll(" +", " "); //removing more than one space

            String[] titleTerms = title.split(" ");
            PorterStemmer stemmer = new PorterStemmer();
            // Stemming all words in TitleTerms (this way because stemmer does not accept full text must be words)
            for (int j = 0; j< titleTerms.length;j++){
               titleTerms[j] = stemmer.stem(titleTerms[j]);
            }

            // Extracting Pids list
            String pid = text.substring(text.indexOf("Pids:")+7); // Full pid text of the file
            // Removing all extra spaces from pid
            pid = pid.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
            // To extract the Pids text:
            String[] pids = pid.split("\n");







//            // Applyting IDF Reduction
//            IDFReduction r = new IDFReduction();
//            int numberOfTerms = (int) Math.round(title.length() * 0.85);
//
//            //The following is to extract the already created boolean querie from file
//            File queryFile = new File("/Users/jewelsalghamdi/Downloads/Project1/project/var/results/boolean-query/2018TrainingQuery.txt"); // 2
//            FileInputStream fis = new FileInputStream(queryFile);
//            byte[] bytes = new byte[(int) queryFile.length()];
//            fis.read(bytes);
//            fis.close();
//            String queryFileText = new String(bytes, "UTF-8"); // Full text of the file
//            // Extracting query text from the file
//            String query = queryFileText.substring(queryFileText.indexOf(p.getTopic())+p.getTopic().length()+1,
//             queryFileText.indexOf("\n",queryFileText.indexOf(p.getTopic()))); //Query of the file
//            String[] queryTerms = query.split(" ");
//
//            String TitleReduced = r.reduce(titletmp, numberOfTerms, index);
//
//            //String QueryReduced = r.reduce(queryTerms, 3, index);
//            System.out.println(" @@@@@@@@@@@@@@@@@@ Query Reduced are: " + TitleReduced);
//
//            //
////            String[] tokenized = TitleReduced.split(" ");
//            String[] tokenized = TitleReduced.split(" ");
//
////            System.out.println(dtf.format(LocalDateTime.now()));
////            System.out.println("Processing Reranker for " + topic);







            System.out.println("Processing: " + topic);
            TrecResults results = reranker.rerank(
                   topic,
                   titleTerms,
                   pids,
                   wm);

            if (wm.getInfo().equals("TF_IDF")){
               results.setRunName("TF_IDF");
            }
            else
               results.setRunName("BM25");

            results.write( filename + ".res", true);


            System.out.println(topic + "\t" + title);
//            System.out.println(Arrays.toString(pids));
            System.out.println("\t Number of files processed: " + pids.length);
//            System.out.println();
//            System.out.println("File " + topic + " has been processed");
       }
   }

    /**
     * Build the query required for part 2 of the project from the topic, query and pids
     * It will also call the required API to build the full terms of the query
     *
     * @param  indx  the index path
     * @param  foldr  an instance of File that has the folder path of the topic files to be processed
     * @param  filename the output folder name for the file to write
     * @param  wm the required weightingModel (TF_IDF or BM25)
     */
    public static void buildFromQuery(Index indx, File foldr, File qf, String filename, WeightingModel wm) throws IOException{


        Logger logger = Logger.getLogger(Project1.class);

        Index index = indx;
        Reranker reranker = new Reranker(index);

        File folder = foldr;

        File queryFile = qf;


        logger.info(filename);

        FileInputStream fis = new FileInputStream(queryFile);
        byte[] bytes = new byte[(int) queryFile.length()];
        fis.read(bytes);
        fis.close();
        String queryFileText = new String(bytes, "UTF-8"); // Full text of the file

        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) { //listOfFiles.length


            File file = new File(listOfFiles[i].toString());
            fis = new FileInputStream(file);
            bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            //to prevent reading hidden file and throwing error
            if (listOfFiles[i].toString().contains(".DS_Store")){
               continue;
            }

            // Extracting the content of the file
            String text = new String(bytes, "UTF-8"); // Full text of the file
            String topic = text.substring(7,text.indexOf("\n")-1); // Topic of the file

            // Extracting query text from the file
            String query = queryFileText.substring(queryFileText.indexOf(topic)+topic.length()+2,
                   queryFileText.indexOf("\n",queryFileText.indexOf(topic))); //Query of the file
            String[] queryTerms = query.split(" ");

            // Extracting Pids list
            String pid = text.substring(text.indexOf("Pids:")+7); // Full pid text of the file
            // Removing all spaces from pid
            pid = pid.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
            // To extract the Pids text:
            String[] pids = pid.split("\n");


            System.out.println("Processing: " + topic);
//            System.out.println(java.util.Arrays.toString(queryTerms));
//            System.out.println(java.util.Arrays.toString(pids));

            TrecResults results = reranker.rerank(
                   topic,
                   queryTerms,
                   pids,
                   wm);

            if (wm.getInfo().equals("TF_IDF")){
                results.setRunName("TF_IDF");
            }
            else
               results.setRunName("BM25");

            results.write( filename + ".res", true);




        }

   }

    public static void buildFromTitleReduced(Index indx, File folder,String filename, WeightingModel wm, double retantionRate) throws IOException {

        Logger logger = Logger.getLogger(Project1.class);
        Index index = indx;
        Reranker reranker = new Reranker(index);

        File[] listOfFiles = folder.listFiles();


        List<String> stopwords = Files.readAllLines(Paths.get("/../var/results/boolean-query/Stopwords.txt"));
        // so if the above words appear in the title list of terms, they are removed in the following loop
        // to add manually to the list
        stopwords.add("patient");
        stopwords.add("exp");


        for (int i = 0; i < listOfFiles.length; i++) { //listOfFiles.length


            File file = new File(listOfFiles[i].toString());
//            ٍSystem.out.println(listOfFiles[i].toString());
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            //to prevent reading hidden file and throwing error
            if (listOfFiles[i].toString().contains(".DS_Store")){
                continue;
            }

            // Extracting the content of the file
            String text = new String(bytes, "UTF-8"); // Full text of the file
            String topic = text.substring(7,text.indexOf("\n")-1); // Topic of the file
            String title = text.substring(text.indexOf("Title:")+7,text.indexOf("Query:"));

            // Processing the title text
            title = title.toLowerCase();
            title = title.replaceAll("\n","");
            // Importing custom stopwords files to use and adding to them some extra words specific to the list

            // Here we are removing the terms that appear in the stopword list of words
            for (String word : title.split(" ")){
                if (stopwords.contains(word)){
                    title = title.replaceAll(word,"");
                }
            }
            title = title.replaceAll(" +", " "); //removing more than one space

            String[] titleTerms = title.split(" ");

            PorterStemmer stemmer = new PorterStemmer();
            // Stemming all words in TitleTerms (this way because stemmer does not accept full text must be words)
            for (int j = 0; j< titleTerms.length;j++){
                titleTerms[j] = stemmer.stem(titleTerms[j]);
            }


            // Extracting Pids list
            String pid = text.substring(text.indexOf("Pids:")+7); // Full pid text of the file
            // Removing all extra spaces from pid
            pid = pid.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
            // To extract the Pids text:
            String[] pids = pid.split("\n");


//            // Applyting the IDF and KLI Reduction
//           IDFReduction r = new IDFReduction();


            int titleNumberofWords = title.split(" ").length;
            int numberOfTerms = (int) Math.ceil( (retantionRate)*titleNumberofWords );

//            String TitleReduced = r.reduce(titleTerms, numberOfTerms, index);
            //String QueryReduced = r.reduce(queryTerms, 3, index);
            k_class kReduction = new k_class();
            String TitleReducedKli = kReduction.reduceFunction(titleTerms,pids, numberOfTerms, index);
            System.out.println("  Query Reduced are: " + TitleReducedKli);


//            String[] tokenized = TitleReduced.split(" ");
            String[] tokenized = TitleReducedKli.split(" ");

//            System.out.println(dtf.format(LocalDateTime.now()));
//            System.out.println("Processing Reranker for " + topic);

            System.out.println("Processing: " + topic);
            TrecResults results = reranker.rerank(
                    topic,
                    tokenized,
                    pids,
                    wm);

            if (wm.getInfo().equals("TF_IDF")){
                results.setRunName("TF_IDF");
            }
            else
                results.setRunName("BM25");

            results.write( filename + ".res", true);


            System.out.println(topic + "\t" + title);
            System.out.println("\ttitleReduced: "+ TitleReducedKli );
//            System.out.println(Arrays.toString(pids));
            System.out.println("\t Number of files processed: " + pids.length);
//            System.out.println();
//            System.out.println("File " + topic + " has been processed");
        }
    }

}
