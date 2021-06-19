package infs7410.project1;

        import java.io.*;
        import java.net.URL;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.ArrayList;
        import java.util.List;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;


public class Parser2 {

    public static List<String> get_query(String word) {
        /*        In this method, I am calling Entrez API to find the terms of the wildcarded terms
                  Then will return them as a list of strings for further processing
        */

        // List of words to return
        List<String> terms = new ArrayList<>();

        try {
            String urlString = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
            // Building the URL
            urlString = urlString + word;
            URL url = new URL(urlString);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());

            Element rootElement = doc.getDocumentElement();
            NodeList nodes = rootElement.getChildNodes();

            // Extracting the content of TranslationStack
            for(int i=0; i<nodes.getLength(); i++){
                Node node = nodes.item(i);
                if (node.getNodeName() == "TranslationStack"){
                    NodeList child = node.getChildNodes();
                    for (int j=0; j<node.getChildNodes().getLength();j++){
                        NodeList childList = node.getChildNodes();
                        Node childNode = childList.item(j);
                        // Adding the content of TermSet to the list of words to return
                        if (childNode.getNodeName() == "TermSet"){
                            String name = child.item(j).getTextContent();
                            terms.add(name);
                        }
                    }
                }
            }

            // Removing the extra character from the list of Strings
            for (int i = 0; i < terms.size() ; ++i){
                String temp = terms.get(i);
                terms.set(i,temp.substring(0,temp.indexOf("[")));
            }

        } catch (Exception e) {
            //System.out.println("There is an error. Exception was caught");
        }
        // Returning the content of the list of strings
        return terms;
    }

    public static void main(String[] args) throws IOException{
        //For example now we want to get the expansion for the term "diagnos*"
        //What we will do is to call get_query function above.
        //The function will call the API and will return all the terms returned from the API into a list of strings as
        //follow

        List<String> expandedTerms = get_query("diagnos*");

        System.out.println(expandedTerms);
    }

    public static void main2(String[] args) throws IOException {
        // The 4 files here are the result of the below code
        // In this code, I am reading all files in 2017 and 2018 (Training and Test) and building the
        // boolean queries. After buidling the boolean queries, I searched for each term and when I
        // find a word that has the wild card "*", I pass it to the API to get the expanded terms
        // then I saved all the expanded boolean queries to the 4 files to save time when calling other
        // functions. Because calling the API and getting the results for all terms take a lot of time
        // something around 15-30 minutes. So, It is a better idea to have all these terms generated and
        // stored in a file. Then, you can use them later and only read the file instead of calling the
        // api from main java class

        /*        In this method, I am constructing the query files according to the topics in each
                  folder
        */

        // Initializing the folder containing the topics
          File folder = new File("/../tar2/2017-TAR/training/topics");
//        File folder = new File("/../tar2/2017-TAR/testing/topics");
//        File folder = new File("/../tar2/2018-TAR/training/topics");
//        File folder = new File("/../tar2/2018-TAR/testing/topics");

        File[] listOfFiles = folder.listFiles();

        // Importing custom stopwords files to use and adding to them some extra words specific to the list
        List<String> stopwords = Files.readAllLines(Paths.get("/../Stopwords.txt"));
        stopwords.add("exp");stopwords.add("or");stopwords.add("a");

        // Writing to file, we should comment and uncomment for each dataset
        PrintWriter writer = new PrintWriter("2017TrainingQuery.txt", "UTF-8");
//        PrintWriter writer = new PrintWriter("2017TestingQuery.txt", "UTF-8");
//        PrintWriter writer = new PrintWriter("2018TrainingQuery.txt", "UTF-8");
//        PrintWriter writer = new PrintWriter("2018TestingQuery.txt", "UTF-8");

        // looping over all document in the folder
        for (int i = 0; i < listOfFiles.length; i++) { //listOfFiles.length
            File file = new File(listOfFiles[i].toString());
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();
            String query = new String(bytes, "UTF-8");
            String topic = query.substring(7,query.indexOf("\n"));
            //System.out.println(listOfFiles[i].toString());
            if (listOfFiles[i].toString().contains(".DS_Store")){ //to prevent reading hidden file and throwing error
                continue;
            }
            // To extract the query text:
            query = query.substring(query.indexOf("Query:")+8, query.lastIndexOf("Pids:"));

            // Removing special characters and end of line characters
            query = query.replaceAll("\n", " ");
            //System.out.println(query);
            query = query.replaceAll(",", "");
            query = query.replaceAll("/", "");
            query = query.replaceAll("\\$", "");
            query = query.replaceAll("\\d", ""); //remove all digits
            query = query.replaceAll("\\?", ""); //remove ?
            query = query.replaceAll("\\=", ""); //remove =
            query = query.replaceAll("\\'", ""); //remove '
            query = query.replaceAll("-", ""); //remove all digits
            query = query.replaceAll(":", ""); //remove all digits
            query = query.replaceAll("\"",""); //remove "
            query = query.replaceAll("\\“",""); //remove "
            query = query.replaceAll("\\”",""); //remove "
            query = query.replaceAll("#",""); //remove #
            //query = query.replaceAll("/$", "");

            // Constructing list of all words to iterate over and remove stopwords
            String[] allWords = query.toLowerCase().split(" ");
            StringBuilder builder = new StringBuilder();

            // Removing stopwords from the query
            for(String word : allWords) {
                if (!stopwords.contains(word)) {
                    if (word.contains(".")) {
                        word = word.substring(0, word.indexOf("."));
                        builder.append(word);
                        builder.append(' ');
                    }
                    else if (word.contains("[")) {
                        word = word.substring(0, word.indexOf("["));
                        builder.append(word);
                        builder.append(' ');
                    }
                    else{
                        builder.append(word);
                        builder.append(' ');
                    }
                }
            }

            // Rebuilding the query and removing the extra unwanted characters and stopwords
            query = builder.toString().trim();
            query = query.replaceAll("\\(", ""); //remove brackets
            query = query.replaceAll("\\)", ""); //remove brackets
            query = query.replaceAll("\\]", ""); //remove brackets
            query = query.trim().replaceAll(" +", " "); //remove multiple spaces
            query = query.trim().replaceAll(" a ", " "); //remove a
            query = query.trim().replaceAll(" b ", " "); //remove b
            query = query.trim().replaceAll(" t ", " "); //remove t
            query = query.trim().replaceAll(" c ", " "); //remove t
            query = query.trim().replaceAll(" r ", " "); //remove t
            query = query.trim().replaceAll(" k ", " "); //remove t
            query = query.trim().replaceAll(" s ", " "); //remove t
            query = query.trim().replaceAll(" d ", " "); //remove t
            query = query.trim().replaceAll(" l ", " "); //remove t
            //System.out.println(query);

            // replacing wildcards with full variations
            // adding all words to list of Strings to iterate over and find wildcards
            allWords = query.toLowerCase().split(" ");
            builder = new StringBuilder();
            for(String word : allWords) {
                // If wildcards are detected, replace them from Entrez API
                if (word.contains("*")) {
                    List<String> terms = get_query(word);
                    for (int j=0;j<terms.size();j++){
                        builder.append(terms.get(j));
                        builder.append(' ');
                    }
                }
                else{
                    builder.append(word);
                    builder.append(' ');
                }

            }

            // Rebuilding the query back
            query = builder.toString().trim();
            // Writing topic and query to file
            writer.println(topic + " " + query);
            // Printing to screen for verification
            System.out.println(topic + " " + query);
        }

        // closing the writing file
        writer.close();

    }
}
