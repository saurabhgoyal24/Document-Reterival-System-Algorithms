/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Business;

import java.util.ArrayList;

/**
 *
 * @author Saurabh Goyal
 */
public class DocTermInfo {
    private String term;
    private TermInfo termInfo;
    private ArrayList<Integer> positions;
    private double finalCoffecient;
    private double tfIdf;
    
    public DocTermInfo(TermInfo termInfo, int position) {
        this.term = termInfo.getTerm();
        this.termInfo = termInfo;
        positions = new ArrayList<>();
        positions.add(position);
    }
    
    public void lengthNormalise(double lengthNormaliseCoffecient){
        
    }

    public TermInfo getTermInfo() {
        return termInfo;
    }
    
    public ArrayList<Integer> getPositions() {
        return positions;
    }
    
    public double getTermFrequencyWeigth() {
        if (positions.size() > 0) {
            return (1 + Math.log(positions.size()) / Math.log(10));
        } else {
            return 0;
        }
    }
    
    public double getTfIdfWeight(double documentFrequencyWeight) {
        tfIdf =  getTermFrequencyWeigth() * documentFrequencyWeight;
        return tfIdf;
    }

    public double getTfIdf() {
        return tfIdf;
    }
    
    public double setFinalCoffecient(double coffecient){
        finalCoffecient = tfIdf/coffecient;
        return finalCoffecient;
    }

    public double getFinalCoffecient() {
        return finalCoffecient;
    }

    public String getTerm() {
        return term;
    }
    
    
}
