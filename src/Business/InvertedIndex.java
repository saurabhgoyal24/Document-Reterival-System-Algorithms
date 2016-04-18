/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import org.apache.commons.io.FileUtils;
import org.tartarus.snowball.ext.EnglishStemmer;

/**
 *
 * @author Saurabh Goyal
 */
public class InvertedIndex {

    private HashMap<String, TermInfo> invertedIndex = new HashMap<>();
    private HashMap<String, DocInfo> documents = new HashMap<>();
    private JTextArea textArea;
    private EnglishStemmer es = new EnglishStemmer();

    public InvertedIndex(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void readFile() throws IOException {

        File folder = new File("F:\\NEU\\spring 2015\\Algorithms\\Project\\Information Reterival\\project\\commons-io-2.4\\sample txts4");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String fileName = file.getName();
                DocInfo docInfo;
                if (documents.containsKey(fileName)) {
                    docInfo = documents.get(fileName);
                } else {
                    documents.put(fileName, new DocInfo(fileName, file, textArea));
                    docInfo = documents.get(fileName);
                }
                String content = FileUtils.readFileToString(file).toLowerCase();
//                System.out.println(content);
                String content1 = content.replaceAll("[\\\t|\\\n|\\\r]"," ");
//                System.out.println(content1);
//                Pattern pattern = Pattern.compile("\\w+(\\.?\\w+)*");
//                Matcher matcher = pattern.matcher(content);
                String[] wordsInADoc = content1.split(" ");
                populateInvertedIndex(wordsInADoc/*matcher*/, fileName, docInfo);
            }
        }
        System.out.println("done reading file(s)");
    }

    private void populateInvertedIndex(String[] wordsInDoc/*Matcher matcher*/, String docId, DocInfo docInfo) {

        int position = 0;
        for (String word : wordsInDoc) {
//        while(matcher.find()){
//            String word = matcher.group().trim();
            String word2 = word.replaceAll("[^\\w]", "");
            es.setCurrent(word2);
            es.stem();
            String word1 = es.getCurrent();
            position++;
            if (invertedIndex.containsKey(word1)) {
                invertedIndex.get(word1).putDocInfo(docId, position, docInfo);
            } else {
                invertedIndex.put(word1, new TermInfo(word1));
                invertedIndex.get(word1).putDocInfo(docId, position, docInfo);
                //invertedIndex.get(word).get(docId).getPositions().add(position);
            }
        }
    }

    public List findReleventDocs(/*Matcher matcher*/String[] queryTerms) {

        DocInfo queryDoc = new DocInfo("queryTerm", null, null);

        populateInvertedIndex(/*matcher*/queryTerms, "qureyTerm", queryDoc);
        documents.put("queryTerm", queryDoc);
//        queryDoc.calculateLengthNormalisationCoffecient(documents.size());
        queryDoc.lengthNormalize(documents.size());

        Collection<DocTermInfo> queryTermsInfo = queryDoc.getTermsInADocument().values();
        Iterator i = queryTermsInfo.iterator();
        HashMap<DocInfo, Double> scores = new HashMap<>();

        while (i.hasNext()) {
            DocTermInfo queryTerm = (DocTermInfo) i.next();
            Collection docs = queryTerm.getTermInfo().getDocuments().values();

            Iterator docIt = docs.iterator();

            while (docIt.hasNext()) {
                DocInfo doc = (DocInfo) docIt.next();
                if (doc.getDocId() != "queryTerm") {
//                    doc.calculateLengthNormalisationCoffecient(documents.size());
                    doc.lengthNormalize(documents.size());
                    if (scores.containsKey(doc)) {
                        double score = scores.get(doc) + (doc.getTermsInADocument().get(queryTerm.getTerm()).getFinalCoffecient()) * (queryTerm.getFinalCoffecient());
                        scores.put(doc, score);
                    } else {
                        double score = (doc.getTermsInADocument().get(queryTerm.getTerm()).getFinalCoffecient()) * (queryTerm.getFinalCoffecient());
                        scores.put(doc, score);
                    }
                }
            }
        }

        Set<DocInfo> set = scores.keySet();
        List<DocInfo> sortedDocs = new ArrayList<>(set);

        Collections.sort(sortedDocs, new Comparator<DocInfo>() {

            @Override
            public int compare(DocInfo s1, DocInfo s2) {
                return Double.compare(scores.get(s2), scores.get(s1));
            }
        });
        return sortedDocs;
    }

    public List searchForWholePhrase(List<DocInfo> docList, String[] queryTerms) {

        HashMap<DocInfo, Integer> documentPhraseScore = new HashMap<>();

        

        for (DocInfo doc : docList) {
            documentPhraseScore.put(doc, 0);
            int count = 0;
            for (String word1 : queryTerms) {
                String word2 = word1.replaceAll("[^\\w]", "");
                es.setCurrent(word2);
                es.stem();
                String queryTerm = es.getCurrent();
                HashSet<Integer> positions = new HashSet<>();
                
                if (doc.getTermsInADocument().containsKey(queryTerm)) {
                    if (positions.isEmpty()) {
                        positions.addAll(doc.getTermsInADocument().get(queryTerm).getPositions());
                    } else {
                        ArrayList<Integer> newPositions = doc.getTermsInADocument().get(queryTerm).getPositions();
                        ArrayList<Integer> tempList = new ArrayList<>();
                        boolean b = false;
                        for (int po : newPositions) {

                            if (positions.contains(po - 1)) {
                                tempList.add(po);
                                int currentScore = documentPhraseScore.get(doc);
                                int newScore = currentScore + count;
                                documentPhraseScore.put(doc, newScore);
                                b = true;
                            }
                        }
                        if (b) {
                            positions.clear();
                            positions.addAll(tempList);
                        }
                    }
                }
                if (count == 0) {
                    count++;
                } else {
                    count = count * 10;
                }
            }

        }

        Set<DocInfo> set = documentPhraseScore.keySet();
        List<DocInfo> sortedDocs = new ArrayList<>(set);

        Collections.sort(sortedDocs, (DocInfo s1, DocInfo s2) -> Double.compare(documentPhraseScore.get(s2), documentPhraseScore.get(s1)));
        return sortedDocs;
    }
}
