/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author shirnili
 */
public class Clique_Sequence implements Comparator<Clique_Sequence>{
    String seqID;
    final ArrayList<String> nodes;
    final ArrayList<Integer> degrees;
    final ArrayList<Integer> cncSet;//common-neighbors count
    final String community;
 
    public Clique_Sequence(ArrayList<String> nodes, ArrayList<Integer> degrees, ArrayList<Integer> cnc, String community){        
        this.nodes = nodes;
        this.degrees = degrees;
     //   this.seqID = generateSequenceID();
        this.cncSet = cnc;
        this.community = community;
        //generateSequenceID();
    }
    public void generateSequenceID(){
    ArrayList<Integer> nodesInt = order_nodes();
    seqID = nodesInt.get(0) + "";
    for(int i=1; i< nodesInt.size();i++){
        seqID = seqID + "-" + nodesInt.get(i) ;
    }
       
    }

    public ArrayList<Integer> order_nodes(){
        ArrayList<Integer> nodesInt = new ArrayList<Integer>();
         for(String n: nodes){
             nodesInt.add(Integer.parseInt(n));
         }
        Collections.sort(nodesInt);
        Collections.reverse(nodesInt);
        return(nodesInt);
    }
    public void order_degrees(){
         Collections.sort(degrees);
         Collections.reverse(degrees);
    }
    public void order_cncSet(){
        Collections.sort(cncSet);
        Collections.reverse(cncSet);
    }

    public String getSeqID(){
        return(this.seqID);
    }
    public ArrayList<Integer> getDegrees(){
        return(this.degrees);
    }
     public ArrayList<Integer> getcncSet(){
        return(this.cncSet);
    }
       public ArrayList<String> getNodes(){
        return(this.nodes);
    }

     public int hammingDistance(Clique_Sequence other){
         int dist = 0;
         int minSize = this.degrees.size();
         if(minSize > other.degrees.size())
             minSize = other.degrees.size();
         for(int i=0; i<minSize; i++){
            if(this.degrees.get(i) != other.degrees.get(i))
                dist++;
         }
         minSize = this.cncSet.size();
         if(minSize > other.cncSet.size())
             minSize = other.cncSet.size();
         for(int i=0; i<minSize; i++){
            if(this.cncSet.get(i) != other.cncSet.get(i))
                dist++;
         }
         return(dist);
     }
     public boolean compareNodes(Clique_Sequence other) {
        boolean equal = false;
        Set nodes1 = new HashSet();
        Set nodes2 = new HashSet();
        nodes1.addAll(this.nodes);
        nodes2.addAll(other.nodes);
        nodes1.retainAll(nodes2);
        if(nodes1.size() == this.nodes.size())
            equal = true;
       return (equal);
    }
    static private int compareList(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        for(int i=0; i< list1.size(); i++){
            int d1 = list1.get(i);
            int d2 = list2.get(i);
            if(d1 != d2)
                return new Integer(d1).compareTo(d2);
        }
    /*   for(Integer d1: list1){
           for(Integer d2: list2)
               if(d1 != d2)
                    return new Integer(d1).compareTo(d2);
       }
     * 
     */
       return new Integer(list1.get(0)).compareTo(list2.get(0));
    }

    public int compare(Clique_Sequence seq1, Clique_Sequence seq2) {
        int compar_result = compareList(seq1.degrees, seq2.degrees);
        if(compar_result == 0){
            compar_result = compareList(seq1.cncSet, seq2.cncSet);
        }
       return compar_result;
    }
}


