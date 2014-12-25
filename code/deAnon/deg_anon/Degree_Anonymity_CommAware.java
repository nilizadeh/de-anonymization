/*
 * To change this template, choose Tools | Templates
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
public class Degree_Anonymity_CommAware {
 //   private HashMap<String, HashMap<String, String>> mapping = new HashMap<String, HashMap<String, String>>();
    private HashMap<String, String> nodeMapping =  new HashMap<String, String>();
    private HashMap<String, String> cMapping = new HashMap<String, String>();
   // private HashMap<String, String> cMapping_in_reverse = new HashMap<String, String>();
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
    private HashMap<String, HashSet<String>> mappedNodes_in_mappedComms_g2; //<mapped comm, <mapped nodes>>
    private HashMap<String, HashSet<String>> notMappedNodes_in_mappedComms_g2;//<mapped comm, <not mapped nodes>>

    public Degree_Anonymity_CommAware(HashMap<String, String> nodeMapping,
            HashMap<String, String> cMapping, MyGraph g1, MyGraph g2)
    {
     this.nodeMapping = nodeMapping;
     this.cMapping = cMapping;
     this.commNodes_g1 = g1.get_commNodes();
     this.commNodes_g2 = g2.get_commNodes();
  //   this.cMapping_in_reverse = cMapping_in_reverse();
 //    this.nodeMapping = set_nodeMapping();
     this.xMapped_cNotMapped_g1 = xMapped_cNotMapped_graph(g1);
     this.yMapped_cNotMapped_g2 = yMapped_cNotMapped_graph(g2);
     this.xNotMapped_cNotMapped_g1 = xNotMapped_cNotMapped_graph(g1);
     this.yNotMapped_cNotMapped_g2 = yNotMapped_cNotMapped_graph(g2);
     this.xMapped_cMapped_g1 = xMapped_cMapped_graph(g1);
     this.yMapped_cMapped_g2 = yMapped_cMapped_graph(g2);
     this.xNotMapped_cMapped_g1 = xNotMapped_cMapped_graph(g1);
     this.yNotMapped_cMapped_g2 = yNotMapped_cMapped_graph(g2);
     this.mappedNodes_in_mappedComms_g2(g2);

     this.nodeComm_g1 = g1.get_nodeComm();
     this.nodeComm_g2 = g2.get_nodeComm();
 //    this.commNodes_g2 = g2.get_commNodes();
    }
    public Degree_Anonymity_CommAware(){
        
    }

   public HashMap<String, String> cMapping_in_reverse(){
        HashMap<String, String> rev = new HashMap<String, String>();
        for(String c : cMapping.keySet())
            rev.put(cMapping.get(c), c);

        return(rev);
   }
 /*   private HashMap<String, String> set_nodeMapping(){
        HashMap<String, String> nodeMapping = new HashMap<String, String>();
        for(String comm: mapping.keySet()){
            HashMap<String, String> nodes_in_comm = mapping.get(comm);
            nodeMapping.putAll(nodes_in_comm);
        }
        return(nodeMapping);
    }
   */
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
    private void mappedNodes_in_mappedComms_g2(MyGraph g2){
        mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
        notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
        for(String comm: cMapping.values()){ //mapped comms in g2
            HashSet<String> mappedNodes = new HashSet<String>();
            HashSet<String> notMappedNodes = new HashSet<String>();
            if(g2.get_commNodes() != null){
                for(String node: g2.get_commNodes().get(comm)){
                    if(nodeMapping.containsValue(node))
                        mappedNodes.add(node);
                    else
                        notMappedNodes.add(node);
                }
            }
            if(!mappedNodes.isEmpty())
                mappedNodes_in_mappedComms_g2.put(comm, mappedNodes);
            if(!notMappedNodes.isEmpty())
                notMappedNodes_in_mappedComms_g2.put(comm, notMappedNodes);
        } 
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
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in M, C(y) in C_M | x<->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cNotMapped_g1.size();

        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in N, C(y) in C_M | x<->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cNotMapped_g1.size();
        return(prob);
    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in M, C(y) in C_N | x<->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cNotMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cNotMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in N, C(y) in C_N | x<->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cNotMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cNotMapped_g1.size();
        return(prob);
    }
    
     /*
     * x </->x', c </-> c'
     */
      private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in M, C(y) in C_M | x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cNotMapped_g1.size();
        return(prob);

    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in N, C(y) in C_M | x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cNotMapped_g1.size();
        return(prob);

    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in M, C(y) in C_N | x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cNotMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cNotMapped_g1.size();
        return(prob);

    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped(){
        //  p(x ~y, y in N, C(y) in C_N | x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cNotMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cNotMapped_g2.contains(y))
                    wrongMatch++;
            }
        }
        if(!xNotMapped_cNotMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cNotMapped_g1.size();
        return(prob);

    }
     /*
     * x <->x', c <-> c'
     
    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped(){
        // p(x ~x' | x<->x', c<->c')
        double prob = 0;
        int match = 0;
        for(String x : xMapped_cMapped_g1){
                if(x.equals(nodeMapping.get(x)))
                    match++;
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = match / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) = c' | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) = c' | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) in C_M-{c'} | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(!cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) in C_M-{c'} | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(!cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) in C_N | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cNotMapped_g2.contains(y))
                   wrongMatch++;
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) in C_N | x<->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cNotMapped_g2.contains(y))
                   wrongMatch++;
            }
        }
        if(!xMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xMapped_cMapped_g1.size();
        return(prob);
    }
*/
       /*
     * x <->x', c <-> c', x' in c'
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
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in M, C(y) = c' | x<->x', c<->c', x' in c')
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
                    if(yMapped_cMapped_g2.contains(y)){
                        if(cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
               count++; 
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in N, C(y) = c' | x<->x', c<->c', x' in c')
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
                    if(yNotMapped_cMapped_g2.contains(y)){
                        if(cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in M, C(y) in C_M-{c'} | x<->x', c<->c', x' in c')
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
                    if(yMapped_cMapped_g2.contains(y)){
                        if(!cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in N, C(y) in C_M-{c'} | x<->x', c<->c', x' in c')
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
                    if(yNotMapped_cMapped_g2.contains(y)){
                        if(!cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in M, C(y) in C_N | x<->x', c<->c', x' in c')
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
                    if(yMapped_cNotMapped_g2.contains(y))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime(){
        //  p(x ~y, y in N, C(y) in C_N | x<->x', c<->c', x' in c')
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
                    if(yNotMapped_cNotMapped_g2.contains(y))
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
     * x <->x', c <-> c', x' in c'
     */
    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        // p(x ~x' | x<->x', c<->c', x' not in c')
        double prob = 0;
        int match = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
               if(x.equals(xprime))
                    match++;
               count++; 
            }
            
        }
        if(count != 0)
            prob = match / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in M, C(y) = c' | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yMapped_cMapped_g2.contains(y)){
                        if(cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
               count++; 
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in N, C(y) = c' | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yNotMapped_cMapped_g2.contains(y)){
                        if(cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in M, C(y) in C_M-{c'} | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yMapped_cMapped_g2.contains(y)){
                        if(!cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in N, C(y) in C_M-{c'} | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yNotMapped_cMapped_g2.contains(y)){
                        if(!cprime.equals(nodeComm_g2.get(y)))
                            wrongMatch++;
                    }
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in M, C(y) in C_N | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yMapped_cNotMapped_g2.contains(y))
                       wrongMatch++;
                }
                count++;
            }
        }
        if(count != 0)
            prob = wrongMatch / count;
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime(){
        //  p(x ~y, y in N, C(y) in C_N | x<->x', c<->c', x' not in c')
        double prob = 0;
        int wrongMatch = 0;
        int count = 0;
        for(String x : xMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            String c = nodeComm_g1.get(x);
            String cprime = cMapping.get(c);
            if(!cprime.equals(nodeComm_g2.get(xprime))){ //x' not in cprime
                if(!x.equals(xprime)){
                    String y = x;
                    if(yNotMapped_cNotMapped_g2.contains(y))
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

    private double prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) = c' | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) = c' | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) in C_M-{c'} | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(!cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) in C_M-{c'} | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cMapped_g2.contains(y)){
                    String cprime = cMapping.get(nodeComm_g1.get(x));
                    if(!cprime.equals(nodeComm_g2.get(y)))
                        wrongMatch++;
                }
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
   private double prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in M, C(y) in C_N | x</->x', c<->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yMapped_cNotMapped_g2.contains(y))
                        wrongMatch++;
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped(){
        //  p(x ~y, y in N, C(y) in C_N | x</->x', c</->c')
        double prob = 0;
        int wrongMatch = 0;
        for(String x : xNotMapped_cMapped_g1){
            String xprime = nodeMapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                if(yNotMapped_cNotMapped_g2.contains(y))
                        wrongMatch++;
            }
        }
        if(!xNotMapped_cMapped_g1.isEmpty())
            prob = wrongMatch / (double) xNotMapped_cMapped_g1.size();
        return(prob);
    }

    private double entropy_of_mappedNodes_and_notMappedComms(double p_I, double p_M_M, 
            double p_M_N, double p_N_M, double p_N_N)
    {
        double H = 0;
       
        if(!yMapped_cMapped_g2.isEmpty())
            if(yMapped_cMapped_g2.size() -1 >0 )
            p_M_M = p_M_M / (yMapped_cMapped_g2.size() -1); // not considering I in M
        if(!yNotMapped_cMapped_g2.isEmpty())
            p_N_M = p_N_M / yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N / yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N / yNotMapped_cNotMapped_g2.size();

        if(p_I!= 0)   p_I = p_I * Math.log(1/p_I);
        if(p_M_M!= 0)   p_M_M = p_M_M * (Math.log(1/p_M_M)/Math.log(2));
        if(p_N_M!= 0)   p_N_M = p_N_M * (Math.log(1/p_N_M)/Math.log(2));
        if(p_M_N!= 0)   p_M_N = p_M_N * (Math.log(1/p_M_N)/Math.log(2));
        if(p_N_N!= 0)   p_N_N = p_N_N * (Math.log(1/p_N_N)/Math.log(2));

        if(!yMapped_cMapped_g2.isEmpty())
            if(yMapped_cMapped_g2.size() -1 >0 )
                p_M_M = p_M_M * (yMapped_cMapped_g2.size() -1);
        if(!yNotMapped_cMapped_g2.isEmpty())
            p_N_M = p_N_M * yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N * yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N * yNotMapped_cNotMapped_g2.size();

        H = p_I + p_M_M + p_N_M + p_M_N + p_N_N;
        return(H);
    }
    private double entropy_of_notMappedNodes_and_notMappedComms(double p_M_M, 
            double p_M_N, double p_N_M, double p_N_N)
    {
        double H = 0;
       
        if(!yMapped_cMapped_g2.isEmpty())
            p_M_M = p_M_M / yMapped_cMapped_g2.size();
        if(!yNotMapped_cMapped_g2.isEmpty())
            p_N_M = p_N_M / yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N / yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N / yNotMapped_cNotMapped_g2.size();

        if(p_M_M!= 0)   p_M_M = p_M_M * (Math.log(1/p_M_M)/Math.log(2));
        if(p_N_M!= 0)   p_N_M = p_N_M * (Math.log(1/p_N_M)/Math.log(2));
        if(p_M_N!= 0)   p_M_N = p_M_N * (Math.log(1/p_M_N)/Math.log(2));
        if(p_N_N!= 0)   p_N_N = p_N_N * (Math.log(1/p_N_N)/Math.log(2));

        if(!yMapped_cMapped_g2.isEmpty())
            p_M_M = p_M_M * yMapped_cMapped_g2.size();
        if(!yNotMapped_cMapped_g2.isEmpty())
            p_N_M = p_N_M * yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N * yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N * yNotMapped_cNotMapped_g2.size();

        H = p_M_M + p_N_M + p_M_N + p_N_N;
        return(H);
    }

    private double entropy_of_mappedNodes_and_mappedComms(String x, double p_I,
            double p_M_C, double p_N_C, double p_M_M, double p_M_N, double p_N_M, double p_N_N  )
    {
        double H = 0;
        String cprime = cMapping.get(nodeComm_g1.get(x));

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime))// not considering I in M_C
            if(mappedNodes_in_mappedComms_g2.get(cprime).size() -1>0)
                p_M_C = p_M_C / (mappedNodes_in_mappedComms_g2.get(cprime).size() -1);

        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_N_C = p_N_C / notMappedNodes_in_mappedComms_g2.get(cprime).size();

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime)){
            if(!yMapped_cMapped_g2.isEmpty() && (yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size() - 1)>0)
                p_M_M = p_M_M / (yMapped_cMapped_g2.size() -
                    mappedNodes_in_mappedComms_g2.get(cprime).size() - 1);
        }
        else
            if(yMapped_cMapped_g2.size() - 1>0)
                p_M_M = p_M_M / (yMapped_cMapped_g2.size() - 1);
        
        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime)){
            if(!yNotMapped_cMapped_g2.isEmpty() && yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size() >0)
                p_N_M = p_N_M / (yNotMapped_cMapped_g2.size() -
                    notMappedNodes_in_mappedComms_g2.get(cprime).size());
        }else if(!yNotMapped_cMapped_g2.isEmpty())
             p_N_M = p_N_M / yNotMapped_cMapped_g2.size();


        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N / yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N / yNotMapped_cNotMapped_g2.size();

        if(p_I!= 0)   p_I = p_I * (Math.log(1/p_I)/Math.log(2));
        if(p_M_C!= 0)   p_M_C = p_M_C * (Math.log(1/p_M_C)/Math.log(2));
        if(p_N_C!= 0)   p_N_C = p_N_C * (Math.log(1/p_N_C)/Math.log(2));
        if(p_M_M!= 0)   p_M_M = p_M_M * (Math.log(1/p_M_M)/Math.log(2));
        if(p_N_M!= 0)   p_N_M = p_N_M * (Math.log(1/p_N_M)/Math.log(2));
        if(p_M_N!= 0)   p_M_N = p_M_N * (Math.log(1/p_M_N)/Math.log(2));
        if(p_N_N!= 0)   p_N_N = p_N_N * (Math.log(1/p_N_N)/Math.log(2));

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime))// not considering I in M_C
            if(mappedNodes_in_mappedComms_g2.get(cprime).size() -1>0)
                p_M_C = p_M_C * (mappedNodes_in_mappedComms_g2.get(cprime).size() -1);

        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_N_C = p_N_C * notMappedNodes_in_mappedComms_g2.get(cprime).size();

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime)){
            if(!yMapped_cMapped_g2.isEmpty() && (yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size() - 1)>0)
                p_M_M = p_M_M * (yMapped_cMapped_g2.size() -
                    mappedNodes_in_mappedComms_g2.get(cprime).size() - 1);
        }
        else
            if(yMapped_cMapped_g2.size() - 1>0)
                p_M_M = p_M_M * (yMapped_cMapped_g2.size() - 1);

        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime)){
            if(!yNotMapped_cMapped_g2.isEmpty() && yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size() >0)
                p_N_M = p_N_M * (yNotMapped_cMapped_g2.size() -
                    notMappedNodes_in_mappedComms_g2.get(cprime).size());
        }else
             p_N_M = p_N_M * yNotMapped_cMapped_g2.size();


        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N * yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N * yNotMapped_cNotMapped_g2.size();


        H = p_I + p_M_C + p_N_C + p_M_M + p_N_M + p_M_N + p_N_N;
      //  System.out.print("x: " + x + ", H: " + H + "\n");
        return(H);
    }
    private double max_entropy_for(String x, int num_nodes_g){
        double H = 0;
        // String comm_x = nodeComm_g1.get(x);
        //  int comm_x_size = commNodes_g1.get(comm_x).size();
        // H =  (Math.log(comm_x_size)/Math.log(2)) + (Math.log(num_nodes_g - comm_x_size)/Math.log(2));
        String cprime = cMapping.get(nodeComm_g1.get(x));
        int cprime_size = commNodes_g2.get(cprime).size();
        H = Math.log(num_nodes_g - cprime_size)/Math.log(2);
      
        return(H);
    }
     private double entropy_of_notMappedNodes_and_mappedComms(String x,
            double p_M_C, double p_N_C, double p_M_M, double p_M_N, double p_N_M, double p_N_N  )
    {
        double H = 0;
        String cprime = cMapping.get(nodeComm_g1.get(x));

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_M_C = p_M_C / mappedNodes_in_mappedComms_g2.get(cprime).size();

        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_N_C = p_N_C / notMappedNodes_in_mappedComms_g2.get(cprime).size();

        if(!yMapped_cMapped_g2.isEmpty())
            if(mappedNodes_in_mappedComms_g2.containsKey(cprime)){
                if(yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size()>0)
                    p_M_M = p_M_M / (yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size());
            }else
                p_M_M = p_M_M / yMapped_cMapped_g2.size();

        if(!yNotMapped_cMapped_g2.isEmpty())
            if(notMappedNodes_in_mappedComms_g2.containsKey(cprime)){
                if(yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size() >0)
                    p_N_M = p_N_M / (yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size());
            }else
                p_N_M = p_N_M / yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N / yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N / yNotMapped_cNotMapped_g2.size();
        
        if(p_M_C!= 0)   p_M_C = p_M_C * (Math.log(1/p_M_C)/Math.log(2));
        if(p_N_C!= 0)   p_N_C = p_N_C * (Math.log(1/p_N_C)/Math.log(2));
        if(p_M_M!= 0)   p_M_M = p_M_M * (Math.log(1/p_M_M)/Math.log(2));
        if(p_N_M!= 0)   p_N_M = p_N_M * (Math.log(1/p_N_M)/Math.log(2));
        if(p_M_N!= 0)   p_M_N = p_M_N * (Math.log(1/p_M_N)/Math.log(2));
        if(p_N_N!= 0)   p_N_N = p_N_N * (Math.log(1/p_N_N)/Math.log(2));

        if(mappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_M_C = p_M_C * mappedNodes_in_mappedComms_g2.get(cprime).size();

        if(notMappedNodes_in_mappedComms_g2.containsKey(cprime))
            p_N_C = p_N_C * notMappedNodes_in_mappedComms_g2.get(cprime).size();

        if(!yMapped_cMapped_g2.isEmpty())
            if(mappedNodes_in_mappedComms_g2.containsKey(cprime)){
                if(yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size()>0)
                    p_M_M = p_M_M * (yMapped_cMapped_g2.size() - mappedNodes_in_mappedComms_g2.get(cprime).size());
            }else
                p_M_M = p_M_M * yMapped_cMapped_g2.size();

        if(!yNotMapped_cMapped_g2.isEmpty())
            if(notMappedNodes_in_mappedComms_g2.containsKey(cprime)){
                if(yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size() >0)
                    p_N_M = p_N_M * (yNotMapped_cMapped_g2.size() - notMappedNodes_in_mappedComms_g2.get(cprime).size());
            }else
                p_N_M = p_N_M * yNotMapped_cMapped_g2.size();
        if(!yMapped_cNotMapped_g2.isEmpty())
            p_M_N = p_M_N * yMapped_cNotMapped_g2.size();
        if(!yNotMapped_cNotMapped_g2.isEmpty())
            p_N_N = p_N_N * yNotMapped_cNotMapped_g2.size();


        H = p_M_C + p_N_C + p_M_M + p_N_M + p_M_N + p_N_N;
        
        return(H);
    }
    private double entropy_of_randomize_mapping(){
        double H_rnd = 0;
        return(H_rnd);
    } 
    private double max_entropy(){
        int num_nodes = xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +
                  xMapped_cMapped_g1.size() +xNotMapped_cMapped_g1.size();
        
        double H_M = (Math.log(num_nodes))/ Math.log(2);
        return(H_M);
    }
    public double entropy_of_system(){
          double H_hat = 0;
          double p_I = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isNotMapped();
          double p_M_M = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped();
          double p_M_N = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped() ;
          double p_N_M = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped();
          double p_N_N = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped();
          double H1 = entropy_of_mappedNodes_and_notMappedComms(p_I, p_M_M, p_M_N, p_N_M, p_N_N);
          
          double p_M_M_2 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
          double p_M_N_2 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped() ;
          double p_N_M_2 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
          double p_N_N_2 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
          double H2 = entropy_of_notMappedNodes_and_notMappedComms(p_M_M_2, p_M_N_2, p_N_M_2, p_N_N_2);
          
          double H3 = 0, H3prime=0, H4 = 0;

          double p_I_3 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_M_C_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_N_C_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_M_M_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_M_N_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime() ;
          double p_N_M_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          double p_N_N_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
          for(String x: xMapped_cMapped_g1)
                H3 = H3 + entropy_of_mappedNodes_and_mappedComms(x, p_I_3, p_M_C_3, p_N_C_3, p_M_M_3, p_M_N_3, p_N_M_3, p_N_N_3);
          
          double p_I_3_prime = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_M_C_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_N_C_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_M_M_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_M_N_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime() ;
          double p_N_M_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          double p_N_N_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
          for(String x: xMapped_cMapped_g1)
                H3prime = H3prime + entropy_of_mappedNodes_and_mappedComms(x, p_I_3_prime, p_M_C_3_prime, p_N_C_3_prime,
                        p_M_M_3_prime, p_M_N_3_prime, p_N_M_3_prime, p_N_N_3_prime);
         
          double p_M_C_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
          double p_N_C_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
          double p_M_M_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped();
          double p_M_N_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped() ;
          double p_N_M_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped();
          double p_N_N_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped();
          
         // double H_M_2 = 0;
          for(String x: xNotMapped_cMapped_g1){
              H4 = H4 + entropy_of_notMappedNodes_and_mappedComms(x, p_M_C_4, p_N_C_4, p_M_M_4, p_M_N_4, p_N_M_4, p_N_N_4);
             // H_M_2 = H_M_2 + max_entropy_for(x, num_nodes);
          }
          
          H_hat = (((xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2)) + H3 + H3prime + H4 )/ (double)
                  (xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +  xMapped_cMapped_g1.size() +xNotMapped_cMapped_g1.size() ) ;
         // H_M_2 =  H_M_2 /num_nodes;
          return(H_hat);
    }
    
     public double entropy_of_system(String dir, String fileName){
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
             double p_I = Double.parseDouble(p[0]);
             double p_M_M = Double.parseDouble(p[1]);
             double p_M_N = Double.parseDouble(p[2]);
             double p_N_M = Double.parseDouble(p[3]);
             double p_N_N = Double.parseDouble(p[4]);
             double H1 = entropy_of_mappedNodes_and_notMappedComms(p_I, p_M_M, p_M_N, p_N_M, p_N_N);
              
              double p_M_M_2 = Double.parseDouble(p[5]);
              double p_M_N_2 = Double.parseDouble(p[6]);
              double p_N_M_2 = Double.parseDouble(p[7]);
              double p_N_N_2 = Double.parseDouble(p[8]);
              double H2 = entropy_of_notMappedNodes_and_notMappedComms(p_M_M_2, p_M_N_2, p_N_M_2, p_N_N_2);
              
              double H3 = 0, H3prime=0, H4 = 0;

              double p_I_3 = Double.parseDouble(p[9]);
              double p_M_C_3 = Double.parseDouble(p[10]);
              double p_N_C_3 = Double.parseDouble(p[11]);
              double p_M_M_3 = Double.parseDouble(p[12]);
              double p_M_N_3 = Double.parseDouble(p[13]);
              double p_N_M_3 = Double.parseDouble(p[14]);
              double p_N_N_3 = Double.parseDouble(p[15]);
              for(String x: xMapped_cMapped_g1)
                    H3 = H3 + entropy_of_mappedNodes_and_mappedComms(x, p_I_3, p_M_C_3, p_N_C_3, p_M_M_3, p_M_N_3, p_N_M_3, p_N_N_3);

              double p_I_3_prime = Double.parseDouble(p[16]);
              double p_M_C_3_prime = Double.parseDouble(p[17]);
              double p_N_C_3_prime = Double.parseDouble(p[18]);
              double p_M_M_3_prime = Double.parseDouble(p[19]);
              double p_M_N_3_prime = Double.parseDouble(p[20]);
              double p_N_M_3_prime = Double.parseDouble(p[21]);
              double p_N_N_3_prime = Double.parseDouble(p[22]);
              for(String x: xMapped_cMapped_g1)
                    H3prime = H3prime + entropy_of_mappedNodes_and_mappedComms(x, p_I_3_prime, p_M_C_3_prime, p_N_C_3_prime,
                        p_M_M_3_prime, p_M_N_3_prime, p_N_M_3_prime,p_N_N_3_prime);
         
              double p_M_C_4 = Double.parseDouble(p[23]);
              double p_N_C_4 = Double.parseDouble(p[24]);
              double p_M_M_4 = Double.parseDouble(p[25]);
              double p_M_N_4 = Double.parseDouble(p[26]);
              double p_N_M_4 = Double.parseDouble(p[27]);
              double p_N_N_4 = Double.parseDouble(p[28]);
             // double H_M_2 = 0;
              for(String x: xNotMapped_cMapped_g1){
                  H4 = H4 + entropy_of_notMappedNodes_and_mappedComms(x, p_M_C_4, p_N_C_4, p_M_M_4, p_M_N_4, p_N_M_4, p_N_N_4);
              }

              H_hat = (((xMapped_cNotMapped_g1.size() * H1) + (xNotMapped_cNotMapped_g1.size() * H2)) + H3 + H3prime + H4 )/ (double)
                      (xMapped_cNotMapped_g1.size() + xNotMapped_cNotMapped_g1.size() +  xMapped_cMapped_g1.size() + xNotMapped_cMapped_g1.size() ) ;

           } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }  
          return(H_hat);
    }
    public double degree_of_anonimity(double H_M){
          double H_hat = entropy_of_system();
          double d =  H_hat/H_M;
          return(d);
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
     public void print_degree_of_anonymity(String dir, String fileName, String probfileName){
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("degree_of_anonimity" + "\n");
            fw1.write(degree_of_anonimity(max_entropy(), "comm-avg-prob/", probfileName) + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void compute_and_print_probabilities(String dir, String fileName){
        double p_I = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isNotMapped();
        double p_M_M = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped();
        double p_M_N = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped() ;
        double p_N_M = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isNotMapped();
        double p_N_N = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isNotMapped();
        
        double p_M_M_2 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
        double p_M_N_2 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped() ;
        double p_N_M_2 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
        double p_N_N_2 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isNotMapped();
        
        double p_I_3 = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_M_C_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_N_C_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_M_M_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_M_N_3 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime() ;
        double p_N_M_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        double p_N_N_3 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_in_cprime();
        
        double p_I_3_prime = prob_x_trueMapping_xprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_M_C_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_N_C_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_M_M_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_M_N_3_prime = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime() ;
        double p_N_M_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        double p_N_N_3_prime = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isMapped_xprime_and_c_isMapped_cprime_and_xprime_notIn_cprime();
        
        double p_M_C_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
        double p_N_C_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_is_cprime_if_x_isNotMapped_xprime_and_c_isMapped();
        double p_M_M_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped();
        double p_M_N_4 = prob_x_trueMapping_y_in_Mapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped() ;
        double p_N_M_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_isMapped_if_x_isNotMapped_xprime_and_c_isMapped();
        double p_N_N_4 = prob_x_trueMapping_y_in_notMapped_and_Cy_isNotMapped_if_x_isNotMapped_xprime_and_c_isMapped();
        
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);//"probabilities-comm.txt"
            fw1.write("p_I\tp_M_M\tp_M_N\tp_N_M\tp_N_N\t");
            fw1.write("p_M_M_2\tp_M_N_2\tp_N_M_2\tp_N_N_2\t");
            fw1.write("p_I_3\tp_M_C_3\tp_N_C_3\tp_M_M_3\tp_M_N_3\tp_N_M_3\tp_N_N_3\t");
            fw1.write("p_I_3_prime\tp_M_C_3_prime\tp_N_C_3_prime\tp_M_M_3_prime\tp_M_N_3_prime\tp_N_M_3_prime\tp_N_N_3_prime\t");
            fw1.write("p_M_C_4\tp_N_C_4\tp_M_M_4\tp_M_N_4\tp_N_M_4\tp_N_N_4\n");
            
            fw1.write(p_I + "\t" +p_M_M + "\t"+ p_M_N + "\t" +p_N_M + "\t" + p_N_N + "\t");
            fw1.write(p_M_M_2 + "\t"+ p_M_N_2 + "\t" + p_N_M_2 + "\t" + p_N_N_2 + "\t");
            fw1.write(p_I_3 + "\t" + p_M_C_3 + "\t" + p_N_C_3 + "\t" + p_M_M_3 + "\t"+ p_M_N_3 + "\t" + p_N_M_3 + "\t" + p_N_N_3 + "\t");
            fw1.write(p_I_3_prime + "\t" + p_M_C_3_prime + "\t" + p_N_C_3_prime + "\t" + p_M_M_3_prime + "\t"+ p_M_N_3_prime + "\t" + p_N_M_3_prime + "\t" + p_N_N_3_prime + "\t");
            fw1.write(p_M_C_4 + "\t" + p_N_C_4 + "\t" + p_M_M_4 + "\t"+ p_M_N_4 + "\t" + p_N_M_4 + "\t" + p_N_N_4 + "\n");
            
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void test_1(){
        // mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         cMapping.put("1", "1");
         cMapping.put("2", "2");
         cMapping.put("3", "3");

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "1");
         nodeComm_g1.put("5", "1");
         nodeComm_g1.put("6", "2");
         nodeComm_g1.put("7", "2");
         nodeComm_g1.put("8", "3");
         nodeComm_g1.put("9", "3");
         nodeComm_g1.put("10", "3");
         nodeComm_g1.put("11", "3");
         nodeComm_g1.put("12", "4");
         nodeComm_g1.put("13", "4");
         nodeComm_g1.put("14", "4");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("4", "1");
         nodeComm_g2.put("3", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "2");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");
         nodeComm_g2.put("9", "3");
         nodeComm_g2.put("10", "4");
         nodeComm_g2.put("11", "4");
         nodeComm_g2.put("12", "5");
         nodeComm_g2.put("13", "5");
         nodeComm_g2.put("14", "5");


         HashMap<String, String> tmp = new HashMap<String, String>();
         tmp.put("1", "1");
         tmp.put("2", "2");
         tmp.put("3", "3");
         tmp.put("5", "4");
         nodeMapping.putAll(tmp);
       //  mapping.put("1", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("6", "8");
         nodeMapping.putAll(tmp);
       //  mapping.put("2", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("9", "9");
         tmp.put("10", "10");
         nodeMapping.putAll(tmp);
       //  mapping.put("3", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("12", "11");
         tmp.put("13", "13");
         nodeMapping.putAll(tmp);
       //  mapping.put("4", tmp);

         xMapped_cNotMapped_g1 = new HashSet<String>();
         xMapped_cNotMapped_g1.add("12");
         xMapped_cNotMapped_g1.add("13");

         yMapped_cNotMapped_g2 = new HashSet<String>();
         yMapped_cNotMapped_g2.add("10");
         yMapped_cNotMapped_g2.add("11");
         yMapped_cNotMapped_g2.add("13");

         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         xNotMapped_cNotMapped_g1.add("14");

         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         yNotMapped_cNotMapped_g2.add("12");
         yNotMapped_cNotMapped_g2.add("14");

         xMapped_cMapped_g1 = new HashSet<String>();
         xMapped_cMapped_g1.add("1");
         xMapped_cMapped_g1.add("2");
         xMapped_cMapped_g1.add("3");
         xMapped_cMapped_g1.add("5");
         xMapped_cMapped_g1.add("6");
         xMapped_cMapped_g1.add("9");
         xMapped_cMapped_g1.add("10");

         yMapped_cMapped_g2 = new HashSet<String>();
         yMapped_cMapped_g2.add("1");
         yMapped_cMapped_g2.add("2");
         yMapped_cMapped_g2.add("3");
         yMapped_cMapped_g2.add("4");
         yMapped_cMapped_g2.add("8");
         yMapped_cMapped_g2.add("9");

         xNotMapped_cMapped_g1 = new HashSet<String>();
         xNotMapped_cMapped_g1.add("4");
         xNotMapped_cMapped_g1.add("7");
         xNotMapped_cMapped_g1.add("8");
         xNotMapped_cMapped_g1.add("11");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("5");
         yNotMapped_cMapped_g2.add("6");
         yNotMapped_cMapped_g2.add("7");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String> mappedNodes = new HashSet<String>();
         mappedNodes.add("1");
         mappedNodes.add("2");
         mappedNodes.add("4");
         mappedNodes_in_mappedComms_g2.put("1", mappedNodes);

         mappedNodes = new HashSet<String>();
         mappedNodes.add("3");
         mappedNodes_in_mappedComms_g2.put("2", mappedNodes);

         mappedNodes = new HashSet<String>();
         mappedNodes.add("8");
         mappedNodes.add("9");
         mappedNodes_in_mappedComms_g2.put("3", mappedNodes);

         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
        // notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         //notMappedNodes = new HashSet<String>();
         notMappedNodes.add("5");
         notMappedNodes.add("6");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("7");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);

        print_degree_of_anonymity("" ,"test-comm.deg");
    }

    private void test_2(){
        //no node is mapped and no community is mapped
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "1");
         nodeComm_g1.put("5", "1");
         nodeComm_g1.put("6", "2");
         nodeComm_g1.put("7", "2");
         nodeComm_g1.put("8", "3");
        
         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("4", "1");
         nodeComm_g2.put("3", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "2");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");

         xMapped_cNotMapped_g1 = new HashSet<String>();
         yMapped_cNotMapped_g2 = new HashSet<String>();
       
         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         xNotMapped_cNotMapped_g1.add("1");
         xNotMapped_cNotMapped_g1.add("2");
         xNotMapped_cNotMapped_g1.add("3");
         xNotMapped_cNotMapped_g1.add("4");
         xNotMapped_cNotMapped_g1.add("5");
         xNotMapped_cNotMapped_g1.add("6");
         xNotMapped_cNotMapped_g1.add("7");
         xNotMapped_cNotMapped_g1.add("8");

         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         yNotMapped_cNotMapped_g2.add("1");
         yNotMapped_cNotMapped_g2.add("2");
         yNotMapped_cNotMapped_g2.add("3");
         yNotMapped_cNotMapped_g2.add("4");
         yNotMapped_cNotMapped_g2.add("5");
         yNotMapped_cNotMapped_g2.add("6");
         yNotMapped_cNotMapped_g2.add("7");
         yNotMapped_cNotMapped_g2.add("8");

         xMapped_cMapped_g1 = new HashSet<String>();
         yMapped_cMapped_g2 = new HashSet<String>();
         xNotMapped_cMapped_g1 = new HashSet<String>();
         yNotMapped_cMapped_g2 = new HashSet<String>();
         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
        
        print_degree_of_anonymity("" ,"test-comm.deg");
    }
      private void test_3(){
        //no node is mapped but all communities are mapped "correctly"
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         cMapping.put("1", "1");
         cMapping.put("2", "2");
         cMapping.put("3", "3");

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "2");
         nodeComm_g1.put("6", "3");
         nodeComm_g1.put("7", "3");
         nodeComm_g1.put("8", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "1");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "3");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");

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
         xNotMapped_cMapped_g1.add("7");
         xNotMapped_cMapped_g1.add("8");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("1");
         yNotMapped_cMapped_g2.add("2");
         yNotMapped_cMapped_g2.add("3");
         yNotMapped_cMapped_g2.add("4");
         yNotMapped_cMapped_g2.add("5");
         yNotMapped_cMapped_g2.add("6");
         yNotMapped_cMapped_g2.add("7");
         yNotMapped_cMapped_g2.add("8");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
         notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes.add("3");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("4");
         notMappedNodes.add("5");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("6");
         notMappedNodes.add("7");
         notMappedNodes.add("8");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);

        print_degree_of_anonymity("" ,"test-3-comm.deg");
    }

     private void test_4(){
        //no node is mapped but some communities are mapped 
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         cMapping.put("1", "1");
         cMapping.put("2", "2");
        // cMapping.put("3", "3");

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "2");
         nodeComm_g1.put("6", "3");
         nodeComm_g1.put("7", "3");
         nodeComm_g1.put("8", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "1");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "3");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");

         xMapped_cNotMapped_g1 = new HashSet<String>();
         yMapped_cNotMapped_g2 = new HashSet<String>();

         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         xNotMapped_cNotMapped_g1.add("6");
         xNotMapped_cNotMapped_g1.add("7");
         xNotMapped_cNotMapped_g1.add("8");

         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         yNotMapped_cNotMapped_g2.add("6");
         yNotMapped_cNotMapped_g2.add("7");
         yNotMapped_cNotMapped_g2.add("8");

         xMapped_cMapped_g1 = new HashSet<String>();
         yMapped_cMapped_g2 = new HashSet<String>();

         xNotMapped_cMapped_g1 = new HashSet<String>();
         xNotMapped_cMapped_g1.add("1");
         xNotMapped_cMapped_g1.add("2");
         xNotMapped_cMapped_g1.add("3");
         xNotMapped_cMapped_g1.add("4");
         xNotMapped_cMapped_g1.add("5");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("1");
         yNotMapped_cMapped_g2.add("2");
         yNotMapped_cMapped_g2.add("3");
         yNotMapped_cMapped_g2.add("4");
         yNotMapped_cMapped_g2.add("5");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
         notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes.add("3");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("4");
         notMappedNodes.add("5");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);

        print_degree_of_anonymity("" ,"test-4-comm.deg");
    }

        private void test_5(){
        //no node is mapped and one community is mapped
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         cMapping.put("2", "2");
         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "2");
         nodeComm_g1.put("6", "3");
         nodeComm_g1.put("7", "3");
         nodeComm_g1.put("8", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "1");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "3");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");

        /*HashMap<String, String> tmp = new HashMap<String, String>();
         tmp.put("1", "1");
         tmp.put("2", "2");
         tmp.put("3", "3");
         tmp.put("5", "4");
         nodeMapping.putAll(tmp);
         mapping.put("1", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("6", "8");
         nodeMapping.putAll(tmp);
         mapping.put("2", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("9", "9");
         tmp.put("10", "10");
         nodeMapping.putAll(tmp);
         mapping.put("3", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("12", "11");
         tmp.put("13", "13");
         nodeMapping.putAll(tmp);
         mapping.put("4", tmp);
         */

         xMapped_cNotMapped_g1 = new HashSet<String>();
         yMapped_cNotMapped_g2 = new HashSet<String>();

         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         xNotMapped_cNotMapped_g1.add("1");
         xNotMapped_cNotMapped_g1.add("2");
         xNotMapped_cNotMapped_g1.add("3");
         xNotMapped_cNotMapped_g1.add("6");
         xNotMapped_cNotMapped_g1.add("7");
         xNotMapped_cNotMapped_g1.add("8");

         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         yNotMapped_cNotMapped_g2.add("1");
         yNotMapped_cNotMapped_g2.add("2");
         yNotMapped_cNotMapped_g2.add("3");
         yNotMapped_cNotMapped_g2.add("6");
         yNotMapped_cNotMapped_g2.add("7");
         yNotMapped_cNotMapped_g2.add("8");

         xMapped_cMapped_g1 = new HashSet<String>();
         yMapped_cMapped_g2 = new HashSet<String>();

         xNotMapped_cMapped_g1 = new HashSet<String>();
         xNotMapped_cMapped_g1.add("4");
         xNotMapped_cMapped_g1.add("5");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("4");
         yNotMapped_cMapped_g2.add("5");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
        /* notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes.add("3");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         */
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("4");
         notMappedNodes.add("5");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         /* notMappedNodes = new HashSet<String>();
         notMappedNodes.add("6");
         notMappedNodes.add("7");
         notMappedNodes.add("8");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);
         */

        print_degree_of_anonymity("" ,"test-5-comm.deg");
    }

    private void test_6(){
        //no node is mapped and one community is mapped and a node is mapped
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();

         cMapping.put("2", "2");
         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "2");
         nodeComm_g1.put("6", "3");
         nodeComm_g1.put("7", "3");
         nodeComm_g1.put("8", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "1");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "3");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");

         HashMap<String, String> tmp = new HashMap<String, String>();
         tmp.put("1", "1");
         tmp.put("2", "2");
         nodeMapping.putAll(tmp);
       //  mapping.put("1", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("4", "4");
         nodeMapping.putAll(tmp);
        // mapping.put("2", tmp);
       /*  tmp =  new HashMap<String, String>();
         tmp.put("9", "9");
         tmp.put("10", "10");
         nodeMapping.putAll(tmp);
         mapping.put("3", tmp);
         tmp =  new HashMap<String, String>();
         tmp.put("12", "11");
         tmp.put("13", "13");
         nodeMapping.putAll(tmp);
         mapping.put("4", tmp);
        */
         

         xMapped_cNotMapped_g1 = new HashSet<String>();
         xMapped_cNotMapped_g1.add("1");
         xMapped_cNotMapped_g1.add("2");

         yMapped_cNotMapped_g2 = new HashSet<String>();
         yMapped_cNotMapped_g2.add("1");
         yMapped_cNotMapped_g2.add("2");

         xNotMapped_cNotMapped_g1 = new HashSet<String>();
         xNotMapped_cNotMapped_g1.add("3");
         xNotMapped_cNotMapped_g1.add("6");
         xNotMapped_cNotMapped_g1.add("7");
         xNotMapped_cNotMapped_g1.add("8");

         yNotMapped_cNotMapped_g2 = new HashSet<String>();
         yNotMapped_cNotMapped_g2.add("3");
         xNotMapped_cNotMapped_g1.add("6");
         yNotMapped_cNotMapped_g2.add("7");
         yNotMapped_cNotMapped_g2.add("8");

         xMapped_cMapped_g1 = new HashSet<String>();
         xMapped_cMapped_g1.add("4");

         yMapped_cMapped_g2 = new HashSet<String>();
         yMapped_cMapped_g2.add("4");

         xNotMapped_cMapped_g1 = new HashSet<String>();
         xNotMapped_cMapped_g1.add("5");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("5");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
         notMappedNodes.add("4");
         mappedNodes_in_mappedComms_g2.put("2", notMappedNodes);

         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
        /* notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes.add("3");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         */
         notMappedNodes = new HashSet<String>();
       //  notMappedNodes.add("4");
         notMappedNodes.add("5");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         /* notMappedNodes = new HashSet<String>();
         notMappedNodes.add("6");
         notMappedNodes.add("7");
         notMappedNodes.add("8");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);
         */

        print_degree_of_anonymity("" ,"test-6-comm.deg");
    }
     private void test_7(){
        //no node is mapped but all communities are mapped randomly (incorrectly)
       
       //  mapping = new HashMap<String, HashMap<String, String>>();
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();
         commNodes_g1 = new HashMap<String, Vector<String>>();

         Vector<String> nodes = new Vector<String>();
         nodes.add("1");
         nodes.add("2");
         nodes.add("3");
         commNodes_g1.put("1", nodes);
         nodes = new Vector<String>();
         nodes.add("4");
         nodes.add("5");
         nodes.add("6");
         commNodes_g1.put("2", nodes);
         nodes = new Vector<String>();
         nodes.add("7");
         nodes.add("8");
         nodes.add("9");
         commNodes_g1.put("3", nodes);

         cMapping.put("1", "3");
         cMapping.put("2", "1");
         cMapping.put("3", "2");

         //<nodeID, CID>
         nodeComm_g1.put("1", "1");
         nodeComm_g1.put("2", "1");
         nodeComm_g1.put("3", "1");
         nodeComm_g1.put("4", "2");
         nodeComm_g1.put("5", "2");
         nodeComm_g1.put("6", "2");
         nodeComm_g1.put("7", "3");
         nodeComm_g1.put("8", "3");
         nodeComm_g1.put("9", "3");

         nodeComm_g2.put("1", "1");
         nodeComm_g2.put("2", "1");
         nodeComm_g2.put("3", "1");
         nodeComm_g2.put("4", "2");
         nodeComm_g2.put("5", "2");
         nodeComm_g2.put("6", "2");
         nodeComm_g2.put("7", "3");
         nodeComm_g2.put("8", "3");
         nodeComm_g2.put("9", "3");

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
         xNotMapped_cMapped_g1.add("7");
         xNotMapped_cMapped_g1.add("8");
         xNotMapped_cMapped_g1.add("9");

         yNotMapped_cMapped_g2 = new HashSet<String>();
         yNotMapped_cMapped_g2.add("1");
         yNotMapped_cMapped_g2.add("2");
         yNotMapped_cMapped_g2.add("3");
         yNotMapped_cMapped_g2.add("4");
         yNotMapped_cMapped_g2.add("5");
         yNotMapped_cMapped_g2.add("6");
         yNotMapped_cMapped_g2.add("7");
         yNotMapped_cMapped_g2.add("8");
         yNotMapped_cMapped_g2.add("9");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
         notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes.add("3");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("4");
         notMappedNodes.add("5");
         notMappedNodes.add("6");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("7");
         notMappedNodes.add("8");
         notMappedNodes.add("9");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);

        print_degree_of_anonymity("" ,"test-7-comm.deg");
    }
      private void test_8(){
        //no node is mapped but all communities are mapped randomly (incorrectly)
       
     //    mapping = new HashMap<String, HashMap<String, String>>();
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

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  notMappedNodes = new HashSet<String>();
         notMappedNodes.add("1");
         notMappedNodes.add("2");
         notMappedNodes_in_mappedComms_g2.put("1", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("3");
         notMappedNodes.add("4");
         notMappedNodes_in_mappedComms_g2.put("2", notMappedNodes);
         notMappedNodes = new HashSet<String>();
         notMappedNodes.add("5");
         notMappedNodes.add("6");
         notMappedNodes_in_mappedComms_g2.put("3", notMappedNodes);

        print_degree_of_anonymity("" ,"test-8-comm.deg");
    }
     private void test_9(){
        //all nodes are mapped incorrectly and all communities are mapped incorrectly
         nodeMapping =  new HashMap<String, String>();
         cMapping = new HashMap<String, String>();
         nodeComm_g1 = new HashMap<String, String>();
         nodeComm_g2 = new HashMap<String, String>();
         commNodes_g1 = new HashMap<String, Vector<String>>();
         commNodes_g2 = new HashMap<String, Vector<String>>();

         nodeMapping.put("1", "2");
         nodeMapping.put("2", "3");
         nodeMapping.put("3", "4");
         nodeMapping.put("4", "5");
         nodeMapping.put("5", "6");
         nodeMapping.put("6", "1");
         
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
         xNotMapped_cMapped_g1 = new HashSet<String>();
         yNotMapped_cMapped_g2 = new HashSet<String>();

         xMapped_cMapped_g1 = new HashSet<String>();
         xMapped_cMapped_g1.add("1");
         xMapped_cMapped_g1.add("2");
         xMapped_cMapped_g1.add("3");
         xMapped_cMapped_g1.add("4");
         xMapped_cMapped_g1.add("5");
         xMapped_cMapped_g1.add("6");
      
         yMapped_cMapped_g2 = new HashSet<String>();
         yMapped_cMapped_g2.add("1");
         yMapped_cMapped_g2.add("2");
         yMapped_cMapped_g2.add("3");
         yMapped_cMapped_g2.add("4");
         yMapped_cMapped_g2.add("5");
         yMapped_cMapped_g2.add("6");

         mappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         notMappedNodes_in_mappedComms_g2 = new HashMap<String, HashSet<String>>();
         HashSet<String>  mappedNodes = new HashSet<String>();
         mappedNodes.add("1");
         mappedNodes.add("2");
         mappedNodes_in_mappedComms_g2.put("1", mappedNodes);
         mappedNodes = new HashSet<String>();
         mappedNodes.add("3");
         mappedNodes.add("4");
         mappedNodes_in_mappedComms_g2.put("2", mappedNodes);
         mappedNodes = new HashSet<String>();
         mappedNodes.add("5");
         mappedNodes.add("6");
         mappedNodes_in_mappedComms_g2.put("3", mappedNodes);

        print_degree_of_anonymity("" ,"test-9-comm.deg");
    }  
    public static void main(String[] args) {
       Degree_Anonymity_CommAware deg = new Degree_Anonymity_CommAware();
       deg.test_9();
    }

}
