package Business;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Saurabh Goyal
 */
public class DocInfo {

    private String docId;
    private HashMap<String, DocTermInfo> termsInADocument;
    private double tfIdfCoffecient;
    private boolean flag;
    private File file;
    private JButton button;
    //private int frequency;
    public DocInfo(String docId, File file, JTextArea textArea) {
        flag = true;
        this.docId = docId;
        tfIdfCoffecient = 0;
        termsInADocument = new HashMap<String, DocTermInfo>();
        this.file = file;
        button = new JButton(docId);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.addActionListener(new TexFiedSet(textArea, file));
    }

    public String getDocId() {
        return docId;
    }

    public JButton getButton() {
        return button;
    }
    
    
    
    public void putTermInADocument(TermInfo term, int position) {

        if (termsInADocument.containsKey(term.getTerm())) {
            termsInADocument.get(term.getTerm()).getPositions().add(position);
        } else {
            termsInADocument.put(term.getTerm(), new DocTermInfo(term, position));
        }
    }

    private void calculateLengthNormalisationCoffecient(int totalDocuments) {

        Collection termCollection = termsInADocument.values();
        Iterator i = termCollection.iterator();

        while (i.hasNext()) {
            DocTermInfo dti = (DocTermInfo) i.next();
            double temp = dti.getTfIdfWeight(dti.getTermInfo().getDocumentFrequency(totalDocuments));

            tfIdfCoffecient = tfIdfCoffecient + temp * temp;
        }
    }

    public void lengthNormalize(int totalDocuments) {
        calculateLengthNormalisationCoffecient(totalDocuments);
        calculateLengthNormalisationCoffecient(totalDocuments);
        if (flag) {
            calculateLengthNormalisationCoffecient(totalDocuments);

            Collection termCollection = termsInADocument.values();
            Iterator i = termCollection.iterator();

            while (i.hasNext()) {
                DocTermInfo dti = (DocTermInfo) i.next();
                dti.setFinalCoffecient(tfIdfCoffecient);
            }
            flag = false;
        }
    }

    public HashMap<String, DocTermInfo> getTermsInADocument() {
        return termsInADocument;
    }

    @Override
    public String toString() {
        return docId;
    }

}
