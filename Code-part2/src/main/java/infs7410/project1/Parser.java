package infs7410.project1;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<String> title) {
        this.title = title;
    }

    public ArrayList<String> getQuery() {
        return query;
    }

    public void setQuery(ArrayList<String> query) {
        this.query = query;
    }

    public ArrayList<String> getPids() {
        return pids;
    }

    public void setPids(ArrayList<String> pids) {
        this.pids = pids;
    }

    String topic;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> query = new ArrayList<>();
    ArrayList<String> pids = new ArrayList<>();


    public Parser(String file) {

        String fileName = file;

        String line = null;

        try {

            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                if (line.contains("Topic"))
                    topic = line.substring(line.indexOf(":") + 2);


                if (line.contains("Title")){

                    String sentence = line.substring(line.indexOf(":") + 2);

                    String [] splitter = sentence.split(" ");

                    for(int counter = 0; counter < splitter.length; counter = counter + 1){
                        title.add(cleaner(splitter[counter].trim()));
                    }


                }
                if (line.contains("Query")){

                    while((line = bufferedReader.readLine()) != null && !line.contains("Pids")) {


                        String[] splitter = line.split(" ");

                        for (int counter = 0; counter < splitter.length && line.length() > 0; counter = counter + 1) {

                            String tmp = cleaner(splitter[counter].trim());

                            if(!stoppingWordValidator(tmp)) {
                                query.add(cleaner(tmp));
                            }
                        }

                    }


                }
                if (line.contains("Pids")){

                    while((line = bufferedReader.readLine()) != null) {

                        if(line.length() > 0){
                                pids.add(line.trim());
                        }
                    }

                }



            }

            bufferedReader.close();
            fileReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '" + fileName + "'");
            
        }
    }

    public String cleaner (String word){

        word = word.toLowerCase();

        String tmp = word;

        if(tmp.contains(".mp."))
            tmp = tmp.replace(".mp.", "");
        if(tmp.contains("exp"))
            tmp = tmp.replace("exp", "");
        if(tmp.contains("ot."))
            tmp = tmp.replace("ot.", "");
        if(tmp.contains("("))
            tmp = tmp.replace("(", "");
        if(tmp.contains(")"))
            tmp = tmp.replace(")", "");
        if(tmp.contains(".tw"))
            tmp = tmp.replace(".tw", "");
        //if(word.contains("*"))
        //    return word.replace("*", "");
        if(tmp.contains("$"))
            tmp = tmp.replace("$", "");
        if(tmp.contains(" "))
            tmp = tmp.replace(" ", "");
        if(tmp.contains(".sh."))
            tmp = tmp.replace(".sh.", "");
        if(tmp.contains("adj5"))
            tmp = tmp.replace("adj5", "");
        //if(word.contains(")).tw"))
            //return word.replace(")).tw", "");
        //if(word.contains(").tw"))
            //return word.replace(").tw", "");

        if(tmp.contains(".tiab."))
            tmp = tmp.replace(".tiab.", "");
        if(tmp.contains(".tiab"))
            tmp = tmp.replace(".tiab", "");
        if(tmp.contains("[tw]"))
            tmp = tmp.replace("[tw]", "");
        if(tmp.contains(" .tiab. "))
            tmp = tmp.replace(" 3).tiab. ", "");
        if(tmp.contains("[mesh]"))
            tmp = tmp.replace("[mesh]", "");
        if(tmp.contains("[ti]"))
            tmp = tmp.replace("[ti]", "");
        if(tmp.contains("[sh]"))
            tmp = tmp.replace("[sh]", "");
        if(tmp.contains("[pt]"))
            tmp = tmp.replace("[pt]", "");
        if(tmp.contains("]"))
            tmp = tmp.replace("]", "");
        if(tmp.contains("["))
            tmp = tmp.replace("[", "");
        if(tmp.contains("[tiab"))
            tmp = tmp.replace("[tiab", "");
        if(tmp.contains("tiab."))
            tmp = tmp.replace("[tiab.", "");
        if(tmp.contains("ct."))
            tmp = tmp.replace("[ct.", "");
        if(tmp.contains("[]"))
            tmp = tmp.replace("[]", "");
        if(tmp.contains(".ab."))
            tmp = tmp.replace(".ab.", "");
        if(tmp.contains(";"))
            tmp = tmp.replace(";", "");
        if(tmp.contains("."))
            tmp = tmp.replace(".", "");
        if(tmp.contains(","))
            tmp = tmp.replace(",", "");
        if(tmp.contains("="))
            tmp = tmp.replace("=", "");
        if(tmp.contains("#"))
            tmp = tmp.replace("#", "");
        if(tmp.contains("@"))
            tmp = tmp.replace("@", "");
        if(tmp.contains("!"))
            tmp = tmp.replace("!", "");
        if(tmp.contains("/"))
            tmp = tmp.replace("/", "");
        if(tmp.contains("\""))
            tmp = tmp.replace("\"", "");
        if(tmp.contains(":"))
            tmp = tmp.replace(":", "");




        return tmp;
    }

    public boolean stoppingWordValidator (String word){

        word = word.toLowerCase();



        if(StringUtils.isNumeric(word))
            return true;

        if(word.equals("or"))
            return true;
        if(word.equals("of"))
            return true;
        if(word.equals("the"))
            return true;
        if(word.equals("and"))
            return true;
        if(word.equals("other"))
            return true;
        if(word.equals("in"))
            return true;
        if(word.equals("with"))
            return true;
        if(word.equals("for"))
            return true;
        if(word.equals("not"))
            return true;
        if(word.equals("to"))
            return true;
        if(word.equals("on"))
            return true;

        // my own stopping words
        if(word.equals("patient"))
            return true;


        return false;
    }

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
}
