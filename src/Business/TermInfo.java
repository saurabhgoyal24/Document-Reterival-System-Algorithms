/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.HashMap;

/**
 *
 * @author Saurabh Goyal
 */
public class TermInfo {

    private String term;
    private HashMap<String, DocInfo> documents;
    private double documentFrequency;

    public String getTerm() {
        return term;
    }
    
    public double getDocumentFrequency(int totalDocuments) {
        documentFrequency =  Math.log(totalDocuments / documents.size()) / Math.log(10);
        return documentFrequency;
    }

    public TermInfo(String term) {
        this.term = term;
        documents = new HashMap<String, DocInfo>();
    }

    public HashMap<String, DocInfo> getDocuments() {
        return documents;
    }
    
    public void putDocInfo(String docId, int position, DocInfo docInfo){
        if(documents.containsKey(docId)){
            documents.get(docId).putTermInADocument(this, position);
        }else{
            documents.put(docId, docInfo);
            documents.get(docId).putTermInADocument(this, position);
        }
    }
}
