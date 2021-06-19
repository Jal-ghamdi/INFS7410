package infs7410.project1;

import org.apache.commons.math3.stat.inference.TTest;

import java.io.PrintWriter;

public class Week2 {
    public static void main(String[] args) throws Exception {

        String filename1 = "/../2018TestingTitleQueries-KLI-0.3V2TFIDF.eval";
        String filename2 = "/../2018TestingTitleQueries-ReducedWithKLI-0.3v2BM25.eval";


        // Open the two files and extract the topics and measures from the eval files.
        TrecEvaluation file1 = new TrecEvaluation(filename1);
        TrecEvaluation file2 = new TrecEvaluation(filename2);


        // Create a new TTest object to perform significance testing.
        TTest tTest = new TTest();

        // Obtain the p-value for the two runs.
        // If you want to see all of the methods available, the Javadoc is located here:
        // https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/stat/inference/TTest.html


        String[] measures = new String[3];
        measures[0] = "map";
        measures[1] = "ndcg";
        measures[2] = "Rprec";
        String[] s1 = file1.getMeasures().toArray(new String[0]);
        String[] s2 = file2.getMeasures().toArray(new String[0]);

        PrintWriter writer = new PrintWriter("p-value-2018TestingTitleQueries-ReducedWithKLI-0.3v-TF_IDF-BM25.txt", "UTF-8");

        for (int i = 0; i < measures.length; ++i){

            double[] scores1 = file1.getScoresForMeasure(measures[i]);
            s1 = file1.getMeasures().toArray(new String[0]);
            double[] scores2 = file2.getScoresForMeasure(measures[i]);
          
            double pvalue = tTest.pairedTTest(scores1, scores2);
            System.out.printf("p-value for files %s and %s given measure %s: %f\n", filename1, filename2, measures[i], pvalue);
            String temp = "p-value for files " + filename1 + " and " + filename2 + " given measure " + measures[i] + ": " + pvalue;
            writer.println(temp);
        }

        writer.close();


    }
}
