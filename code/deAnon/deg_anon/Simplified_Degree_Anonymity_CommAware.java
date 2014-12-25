/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.deg_anon;

import deAnon.matching.MyGraph;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class Simplified_Degree_Anonymity_CommAware {
    private HashMap<String, String> nodeMapping =  new HashMap<String, String>();
    private HashMap<String, String> cMapping = new HashMap<String, String>();
    private HashMap<String, String> nodeComm_g1; //<nodeID, cID>
    private HashMap<String, String> nodeComm_g2; //<nodeID, cID>
    private HashMap<String, Vector<String>> commNodes_g1;
     private HashMap<String, Vector<String>> commNodes_g2;

    private HashSet<String> xMapped_cNotMapped_g1;
    private HashSet<String> xNotMapped_cNotMapped_g1;
    private HashSet<String> xMapped_cMapped_g1;
    private HashSet<String> xNotMapped_cMapped_g1;
    private HashSet<String> yMapped_cNotMapped_g2;
    private HashSet<String> yNotMapped_cNotMapped_g2;
    private HashSet<String> yMapped_cMapped_g2;
    private HashSet<String> yNotMapped_cMapped_g2;
 
    public Simplified_Degree_Anonymity_CommAware(HashMap<String, String> nodeMapping,
            HashMap<String, String> cMapping, MyGraph g1, MyGraph g2)
    {
     this.nodeMapping = nodeMapping;
     this.cMapping = cMapping;
     this.commNodes_g1 = g1.get_commNodes();
     this.commNodes_g2 = g2.get_commNodes();
     this.xMapped_cNotMapped_g1 = xMapped_cNotMapped_graph(g1);
     this.yMapped_cNotMapped_g2 = yMapped_cNotMapped_graph(g2);
     this.xNotMapped_cNotMapped_g1 = xNotMapped_cNotMapped_graph(g1);
     this.yNotMapped_cNotMapped_g2 = yNotMapped_cNotMapped_graph(g2);
     this.xMapped_cMapped_g1 = xMapped_cMapped_graph(g1);
     this.yMapped_cMapped_g2 = yMapped_cMapped_graph(g2);
     this.xNotMapped_cMapped_g1 = xNotMapped_cMapped_graph(g1);
     this.yNotMapped_cMapped_g2 = yNotMapped_cMapped_graph(g2);
     
     this.nodeComm_g1 = g1.get_nodeComm();
     this.nodeComm_g2 = g2.get_nodeComm();
    }
    public Simplified_Degree_Anonymity_CommAware(){
        
    }

    private HashSet<String> xMapped_cNotMapped_graph(MyGraph g)
    {
        HashSet<String> xMapped_cNotMapped = new HashSet<String>();
        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(!cMapping.containsKey(comm)){
                 if(nodeMapping.containsKey(node))
                     xMapped_cNotMapped.add(node);
            }
        }
        return(xMapped_cNotMapped);
    }
     private HashSet<String> yMapped_cNotMapped_graph(MyGraph g)
    {
        HashSet<String> yMapped_cNotMapped = new HashSet<String>();
        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(!cMapping.containsValue(comm)){
                if(nodeMapping.containsValue(node))
                    yMapped_cNotMapped.add(node);
            }
        }
        return(yMapped_cNotMapped);
     }
    private HashSet<String> xNotMapped_cNotMapped_graph(MyGraph g)
    {
        HashSet<String> xNotMapped_cNotMapped = new HashSet<String>();

        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(!cMapping.containsKey(comm)){
                if(!nodeMapping.containsKey(node)){
                    xNotMapped_cNotMapped.add(node);
                 }
            }
        }
        return(xNotMapped_cNotMapped);
     }
     private HashSet<String> yNotMapped_cNotMapped_graph(MyGraph g)
    {
        HashSet<String> yNotMapped_cNotMapped = new HashSet<String>();

        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(!cMapping.containsValue(comm)){
                if(!nodeMapping.containsValue(node))
                    yNotMapped_cNotMapped.add(node);
            }
        }
        return(yNotMapped_cNotMapped);
     }

     private HashSet<String> xMapped_cMapped_graph(MyGraph g)
    {
        HashSet<String> xMapped_cMapped = new HashSet<String>();

        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(cMapping.containsKey(comm))
                if(nodeMapping.containsKey(node))
                  xMapped_cMapped.add(node);
        }
        return(xMapped_cMapped);
    }
    private HashSet<String> yMapped_cMapped_graph(MyGraph g)
    {
        HashSet<String> yMapped_cMapped = new HashSet<String>();

        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(cMapping.containsValue(comm))
                if(nodeMapping.containsValue(node))
                  yMapped_cMapped.add(node);
        }
        return(yMapped_cMapped);
    }
    private HashSet<String> xNotMapped_cMapped_graph(MyGraph g)
    {
        HashSet<String> xNotMapped_cMapped = new HashSet<String>();

         for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(cMapping.containsKey(comm)){
                 if(!nodeMapping.containsKey(node)){
                     xNotMapped_cMapped.add(node);
                 }
            }
        }
        return(xNotMapped_cMapped);
     }
     private HashSet<String> yNotMapped_cMapped_graph(MyGraph g)
    {
        HashSet<String> yNotMapped_cMapped = new HashSet<String>();
        for(String node : g.get_nodeComm().keySet()){
            String comm = g.getNodeComm(node);
            if(cMapping.containsValue(comm)){
                if(!nodeMapping.containsValue(node))
                    yNotMapped_cMapped.add(node);
            }
        }
        return(yNotMapped_cMapped);
     }
 

    /*
     * x <->x', c </-> c'
     */
    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isNotMapped(){
        // p(x ~x' | x<->x', c</->c')
        double prob = 0;
        int match = 0;
        for(String x : xMapped_cNotMapped_g1){
           if(x.equals(nodeMapping.get(x)))
              match++;
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = match / (double) xMapped_cNotMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_if_x_isMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y | x<->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime))
                wrongMatch++;
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cNotMapped_g1.size();

        return(prob);
    }
    
     /*
     * x </->x', c </-> c'
     */
      private double prob_x_trueMapping_y_if_x_isNotMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y| x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime))
                    wrongMatch++;
        }
        if(!xNotMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cNotMapped_g1.size();
        return(prob);

      }
     /*
     * x <->x', c <-> c', x' in c' (comm(x') == c')
     */
    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        // p(x ~x' | x<->x', c<->c', x' in c')
        double prob = 0;
        int match = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(cprime.equals(nodeComm_g2.get(xprime))){ //x' in cprime
                if(x.equals(xprime))
                    match++;
                count++;
            }
        }
        if(count != 0)
            prob = match / count;
        return(prob);
    }
    /*private double prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, C(y) = c' | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                String cprime = cMapping.get(nodeComm_g1.get(x));
                if(cprime.equals(nodeComm_g2.get(y)))
                   wrongMatch++;
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    */
   private double prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, C(y) = c' | x<->x', c<->c', x' in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(cprime.equals(nodeComm_g2.get(xprime))){ //x' in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(cprime.equals(nodeComm_g2.get(y)))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, C(y) = c' | x<->x', c<->c', x' in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(cprime.equals(nodeComm_g2.get(xprime))){ //x' in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(!cprime.equals(nodeComm_g2.get(y)))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
  /*private double prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) in C_M-{c'} | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                String cprime = cMapping.get(nodeComm_g1.get(x));
                if(!cprime.equals(nodeComm_g2.get(y)))
                    wrongMatch++;
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
*/
    /*
     * x <->x', c <-> c', x' in c' (comm(x') == c')
     */
    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        // p(x ~x' | x<->x', c<->c', x' in c')
        double prob = 0;
        int match = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' Not in cprime
                if(x.equals(xprime))
                    match++;
                count++;
            }
        }
        if(count != 0)
            prob = match / count;
        return(prob);
    }
   private double prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, C(y) = c' | x<->x', c<->c', x' in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' Not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(cprime.equals(nodeComm_g2.get(y)))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, C(y) = c' | x<->x', c<->c', x' in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' Not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(!cprime.equals(nodeComm_g2.get(y)))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    } 
    /*
     * x </->x', c <-> c'
     */

    private double prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, C(y) = c' | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                String cprime = cMapping.get(nodeComm_g1.get(x));
                if(cprime.equals(nodeComm_g2.get(y)))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, C(y) in C_M-{c'} | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                String cprime = cMapping.get(nodeComm_g1.get(x));
                if(!cprime.equals(nodeComm_g2.get(y)))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }

    private double entropy_of_mappedNodes_and_notMappedComms(double p_1, double p_2)
    {
        double H = 0;
        int size_g2 = yMapped_cMapped_g2.size() + yMapped_cNotMapped_g2.size() +
                yNotMapped_cMapped_g2.size() + yNotMapped_cNotMapped_g2.size();
       
        if(size_g2-1 >0 )
            p_2 = p_2 / (size_g2 -1); // not considering I in M

        if(p_1!= 0)   p_1 = p_1 * Math.log(1/p_1);
        if(p_2!= 0)   p_2 = p_2 * (Math.log(1/p_2)/Math.log(2));
        
        if(size_g2 -1 >0 )
            p_2 = p_2 * (size_g2 -1);
        
        H = p_1 + p_2 ;
        return(H);
    }

    private double entropy_of_mappedNodes_and_mappedComms(String x, double p_1, double p_2, double p_3 )
    {
        double H = 0;
        String cprime = cMapping.get(nodeComm_g1.get(x));

        if(commNodes_g2.get(cprime).size() -1>0)
             p_2 = p_2 / (commNodes_g2.get(cprime).size() -1);
    
        int size_g2 = yMapped_cMapped_g2.size() + yMapped_cNotMapped_g2.size() +
                yNotMapped_cMapped_g2.size() + yNotMapped_cNotMapped_g2.size();
        
        if(size_g2 - commNodes_g2.get(cprime).size() >0)
             p_3 = p_3 / (size_g2 - commNodes_g2.get(cprime).size());

        if(p_1!= 0)   p_1 = p_1 * (Math.log(1/p_1)/Math.log(2));
        if(p_2!= 0)   p_2 = p_2 * (Math.log(1/p_2)/Math.log(2));
        if(p_3!= 0)   p_3 = p_3 * (Math.log(1/p_3)/Math.log(2));

        if(commNodes_g2.get(cprime).size() -1>0)
            p_2 = p_2 * (commNodes_g2.get(cprime).size() -1);

        if(size_g2 - commNodes_g2.get(cprime).size() >0)
            p_3 = p_3 * (size_g2 - commNodes_g2.get(cprime).size());
        
        H = p_1 + p_2 + p_3 ;
      //  System.out.print("x: " + x + ", H: " + H + "\n");
        return(H);
    }
    private double entropy_of_mappedNodes_and_mappedComms_prime(String x, double p_1, double p_2, double p_3 )
    {
        double H = 0;
        String cprime = cMapping.get(nodeComm_g1.get(x));

        if(commNodes_g2.get(cprime).size()>0)
             p_2 = p_2 / commNodes_g2.get(cprime).size();
    
        int size_g2 = yMapped_cMapped_g2.size() + yMapped_cNotMapped_g2.size() +
                yNotMapped_cMapped_g2.size() + yNotMapped_cNotMapped_g2.size();
        
        if(size_g2 - commNodes_g2.get(cprime).size() -1 >0)
             p_3 = p_3 / (size_g2 - commNodes_g2.get(cprime).size() - 1);

        if(p_1!= 0)   p_1 = p_1 * (Math.log(1/p_1)/Math.log(2));
        if(p_2!= 0)   p_2 = p_2 * (Math.log(1/p_2)/Math.log(2));
        if(p_3!= 0)   p_3 = p_3 * (Math.log(1/p_3)/Math.log(2));

        if(commNodes_g2.get(cprime).size()>0)
            p_2 = p_2 * commNodes_g2.get(cprime).size();

        if(size_g2 - commNodes_g2.get(cprime).size() -1 >0)
            p_3 = p_3 * (size_g2 - commNodes_g2.get(cprime).size() - 1);
        
        H = p_1 + p_2 + p_3 ;
      //  System.out.print("x: " + x + ", H: " + H + "\n");
        return(H);
    }
     private double entropy_of_notMappedNodes_and_mappedComms(String x, double p_1, double p_2)
    {
        double H = 0;
        String cprime = cMapping.get(nodeComm_g1.get(x));
        
        int size_g2 = yMapped_cMapped_g2.size() + yMapped_cNotMapped_g2.size() +
                yNotMapped_cMapped_g2.size() + yNotMapped_cNotMapped_g2.size();
        
        if(commNodes_g2.containsKey(cprime)){
            if(commNodes_g2.get(cprime).size()>0)
                p_1 = p_1 / commNodes_g2.get(cprime).size() ;
            
            if(size_g2 - commNodes_g2.get(cprime).size()>0)
                p_2 = p_2 / (size_g2 - commNodes_g2.get(cprime).size());
        }
        
        if(p_1!= 0)   p_1 = p_1 * (Math.log(1/p_1)/Math.log(2));
        if(p_2!= 0)   p_2 = p_2 * (Math.log(1/p_2)/Math.log(2));

        if(commNodes_g2.containsKey(cprime)){
            if(commNodes_g2.get(cprime).size()>0)
                p_1 = p_1 * commNodes_g2.get(cprime).size();
       
            if(size_g2 - commNodes_g2.get(cprime).size()>0)
                p_2 = p_2 * (size_g2 - commNodes_g2.get(cprime).size());
        }

        H = p_1 + p_2 ;
        
        return(H);
    }
    private double entropy_of_notMappedNodes_and_notMappedComms(double p)
    {
        double H = 0;
        int size_g2 = yMapped_cMapped_g2.size() + yMapped_cNotMapped_g2.size() +
                yNotMapped_cMapped_g2.size() + yNotMapped_cNotMapped_g2.size();

        if(size_g2 !=0) p = p / size_g2;
        if(p != 0)   p = p * (Math.log(1/p)/Math.log(2));
        if(size_g2 !=0) p = p * size_g2;
     
        H = p;
        return(H);
    }
    private double max_entropy(){
        int num_nodes = xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +
                  xMapped_cMapped_g1.size() +xNotMapped_cMapped_g1.size();
        
        double H_M = (Math.log(num_nodes))/ Math.log(2);
        return(H_M);
    }
    public double entropy_of_system(){
        /*
        When at each run, probabilities are calculated and used for finding degree of anonymity
        */
          double H_hat = 0;
          
          double p_1 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isNotMapped();
          double p_2 = prob_x_trueMapping_y_if_x_isMapped_xprime_and_c_isNotMapped();
          double H1 = entropy_of_mappedNodes_and_notMappedComms(p_1, p_2);
          
          double p_3 = prob_x_trueMapping_y_if_x_isNotMapped_xprime_and_c_isNotMapped();
          double H2 = entropy_of_notMappedNodes_and_notMappedComms(p_3); 
          
          double H3 = 0, H3prime = 0 , H4 = 0;

          double p_4 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_5 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_6 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          
          for(String x: xMapped_cMapped_g1)
                H3 = H3 + entropy_of_mappedNodes_and_mappedComms(x, p_4, p_5, p_6);
         
          double p_7 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_8 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_9 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          
          for(String x: xMapped_cMapped_g1)
                H3prime = H3prime + entropy_of_mappedNodes_and_mappedComms_prime(x, p_7, p_8, p_9);
         
          double p_10 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
          double p_11 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
         
          for(String x: xNotMapped_cMapped_g1){
              H4 = H4 + entropy_of_notMappedNodes_and_mappedComms(x, p_10, p_11);
          }
          
          H_hat = ((xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2) + H3 + H3prime + H4 )/ (double)
                  (xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +  xMapped_cMapped_g1.size() + xNotMapped_cMapped_g1.size() ) ; //(xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2)
         
          return(H_hat);
    }
    
    public double degree_of_anonimity(double H_M){
          double H_hat = entropy_of_system();
          double d =  H_hat/H_M;
          return(d);
    }
   
    public double entropy_of_system(String dir, String fileName){
        /*
            Read the avg probabilities from a file and compute the degree of anonymity
        */
         double H_hat = 0;
         try {
             //Read File
             File file1 = new File(dir + fileName);
             FileInputStream fstream1 = new FileInputStream(file1);
             DataInputStream in1 = new DataInputStream(fstream1);
             BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
             String splitter = "\t";
             String Oneline = br1.readLine(); //header
             Oneline = br1.readLine(); //probabilities
             String[] p = Oneline.split(splitter);
             //Find entropies
              double H1 = entropy_of_mappedNodes_and_notMappedComms(Double.parseDouble(p[0]), Double.parseDouble(p[1]));

              double H2 = entropy_of_notMappedNodes_and_notMappedComms(Double.parseDouble(p[2])); 
              double H3 = 0, H3prime = 0 , H4 = 0;

              for(String x: xMapped_cMapped_g1)
                    H3 = H3 + entropy_of_mappedNodes_and_mappedComms(x, Double.parseDouble(p[3]),
                            Double.parseDouble(p[4]), Double.parseDouble(p[5]));

              for(String x: xMapped_cMapped_g1)
                    H3prime = H3prime + entropy_of_mappedNodes_and_mappedComms_prime(x, Double.parseDouble(p[6]), Double.parseDouble(p[7]), Double.parseDouble(p[8]));

              for(String x: xNotMapped_cMapped_g1)
                  H4 = H4 + entropy_of_notMappedNodes_and_mappedComms(x, Double.parseDouble(p[9]), Double.parseDouble(p[10]));
              //average entropy
              H_hat = ((xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2) + H3 + H3prime + H4 )/ (double)
                      (xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +  xMapped_cMapped_g1.size() + xNotMapped_cMapped_g1.size() ) ; //(xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2)

            } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }    
          return(H_hat);
          
    }
    
    public double degree_of_anonimity(double H_M, String dir, String probFileName){
          double H_hat = entropy_of_system(dir, probFileName);
          double d =  H_hat/H_M;
          return(d);
    }
    public void print_degree_of_anonymity(String dir, String fileName){
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("degree_of_anonimity" + "\n");
            fw1.write(degree_of_anonimity(max_entropy()) + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void print_degree_of_anonymity(String dir, String fileName, double H_rnd){
        
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("degree_of_anonimity" + "\n");
            fw1.write(degree_of_anonimity(H_rnd) + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void print_degree_of_anonymity(String dir, String fileName, String probFileName){
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("degree_of_anonimity" + "\n");
            fw1.write(degree_of_anonimity(max_entropy(), "comm-avg-prob/", probFileName) + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void compute_and_print_probabilities(String dir, String fileName){
        double p_1 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isNotMapped();
        double p_2 = prob_x_trueMapping_y_if_x_isMapped_xprime_and_c_isNotMapped();
        
        double p_3 = prob_x_trueMapping_y_if_x_isNotMapped_xprime_and_c_isNotMapped();
        
        double p_4 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_5 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_6 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        
        double p_7 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_8 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_9 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        
        double p_10 = prob_x_trueMapping_y_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
        double p_11 = prob_x_trueMapping_y_and_Cy_isNot_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
        
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);//"probabilities-comm-simple.txt"
            fw1.write("p_1\tp_2\tp_3\tp_4\tp_5\tp_6\tp_7\tp_8\tp_9\tp_10\tp_11\n");
            fw1.write(p_1 + "\t" +p_2 + "\t"+ p_3 + "\t" +p_4 + "\t"+ p_5 + "\t" +p_6 + "\t"+
                    p_7 + "\t" +p_8 + "\t" + p_9 + "\t" +p_10 + "\t"+ p_11 + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 private void test_8(){
        //no node is mapped but all communities are mapped randomly (incorrectly)
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();
         commNodes_g1 = new HashMap<String, Vector<String>>();
         commNodes_g2 = new HashMap<String, Vector<String>>();

         Vector<String> nodes = new Vector<String>();
         nodes.add("1");
         nodes.add("2");
         commNodes_g1.put("1", nodes);
         commNodes_g2.put("1", nodes);
         nodes = new Vector<String>();
         nodes.add("3");
         nodes.add("4");
         commNodes_g1.put("2", nodes);
         commNodes_g2.put("2", nodes);
         nodes = new Vector<String>();
         nodes.add("5");
         nodes.add("6");
         commNodes_g1.put("3", nodes);
         commNodes_g2.put("3", nodes);

         cMapping.put("1", "3");
         cMapping.put("2", "1");
         cMapping.put("3", "2");

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "2");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "3");
         nodeComm_g1.put("6", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "2");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "3");
         nodeComm_g2.put("6", "3");
         
         xMapped_cNotMapped_g1 = new HashSet<String>();
         yMapped_cNotMapped_g2 = new HashSet<String>();
         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         xMapped_cMapped_g1 = new HashSet<String>();
         yMapped_cMapped_g2 = new HashSet<String>();

         xNotMapped_cMapped_g1 = new HashSet<String>();
         xNotMapped_cMapped_g1.add("1");
         xNotMapped_cMapped_g1.add("2");
         xNotMapped_cMapped_g1.add("3");
         xNotMapped_cMapped_g1.add("4");
         xNotMapped_cMapped_g1.add("5");
         xNotMapped_cMapped_g1.add("6");
      
         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("1");
         yNotMapped_cMapped_g2.add("2");
         yNotMapped_cMapped_g2.add("3");
         yNotMapped_cMapped_g2.add("4");
         yNotMapped_cMapped_g2.add("5");
         yNotMapped_cMapped_g2.add("6");

        print_degree_of_anonymity("" ,"test-8-simple-comm.deg");
    }
    public static void main(String[] args) {
       Simplified_Degree_Anonymity_CommAware deg = new Simplified_Degree_Anonymity_CommAware();
       deg.test_8();
    }
}
