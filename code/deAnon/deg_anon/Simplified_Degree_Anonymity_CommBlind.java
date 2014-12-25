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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class Simplified_Degree_Anonymity_CommBlind {
      HashMap<String, String> mapping;
    HashSet<String> mapped_g1;
    HashSet<String> notMapped_g1;
    HashSet<String> mapped_g2;
    HashSet<String> notMapped_g2;
    
    public Simplified_Degree_Anonymity_CommBlind(HashMap<String, String> map, MyGraph g1, MyGraph g2)
    {
        this.mapping = map;
        this.mappedNodes_in_graph1_and_graph2();
        this.notMappedNodes_in_graph1_and_graph2(g1, g2);
    }
    public Simplified_Degree_Anonymity_CommBlind(){        
    }
    public Simplified_Degree_Anonymity_CommBlind(HashMap<String, String> mapping, HashSet<String> mapped_g1,
        HashSet<String> notMapped_g1, HashSet<String> mapped_g2, HashSet<String> notMapped_g2){  
        this.mapping = mapping;
        this.mapped_g1 = mapped_g1;
        this.notMapped_g1 = notMapped_g1;
        this.mapped_g2 = mapped_g2;
        this.notMapped_g2 = notMapped_g2;
    }

    private void mappedNodes_in_graph1_and_graph2()
    {
        mapped_g1 = new  HashSet<String>();
        mapped_g2 = new  HashSet<String>();

        for(String node1: mapping.keySet()){
            mapped_g1.add(node1);
            String node2 = mapping.get(node1);
            mapped_g2.add(node2);
        }
    }
    private void notMappedNodes_in_graph1_and_graph2(MyGraph g1, MyGraph g2)
    {
        notMapped_g1 = new  HashSet<String>();
        notMapped_g2 = new  HashSet<String>();
        
        for(String node: g1.getNodeIds().keySet()){
            if(!mapped_g1.contains(node))
                notMapped_g1.add(node);
        }
        for(String node: g2.getNodeIds().keySet()){
            if(!mapped_g2.contains(node))
                notMapped_g2.add(node);
        }
    }

    private double prob_x_trueMapping_xprime_if_x_isMapped_xprime(){
        /*
         * p(x~x'|x<->x')
         */
        double prob = 0;
        int match = 0;
        for(String x : mapped_g1){
            if(x.equals(mapping.get(x))) match++;
        }
        if(!mapped_g1.isEmpty())
            prob = match / (double) mapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_in_notMapped_if_x_isMapped_xprime(){
        /*
         * p(x~y, y in N|x<->x')
         */
        double prob = 0;
        int wrongMatch = 0;
        for(String x : mapped_g1){
            String y = mapping.get(x);
            if(!x.equals(y)){
                if(notMapped_g2.contains(x))
                 wrongMatch++;
            }
        }
        if(!mapped_g1.isEmpty())
            prob = wrongMatch / (double) mapped_g1.size();
        return(prob);
    }
    private double prob_x_trueMapping_y_if_x_isMapped_xprime(){
        /*
         * p(x~y|x<->x')
         */
        double prob = 0;
        int wrongMatch = 0;
        for(String x : mapped_g1){
            String xprime = mapping.get(x);
            if(!x.equals(xprime)){
                String y = x;
                wrongMatch++;
            }
        }
        if(!mapped_g1.isEmpty())
            prob = wrongMatch / (double) mapped_g1.size();
        return(prob);
    } 
    private double prob_x_trueMapping_y_if_x_isNotMapped(){
        /*
         * p(x~y|x<-/>x')
         */
        double prob = 0;
         int notMatched = 0;
        for(String x : notMapped_g1)
            notMatched++;
    
        if(!notMapped_g1.isEmpty())
            prob = notMatched / (double) notMapped_g1.size();
       return(prob);
    }
     private double entropy_of_mappedNodes(double p_I, double p_M)
    {
        double H = 0;
        int size_g2 = mapped_g2.size() + notMapped_g2.size();
        
        if(size_g2-1>0)
            p_M = p_M / (size_g2 -1); // not considering I in M
  
        if(p_I!= 0)   p_I = p_I * (Math.log(1/p_I)/Math.log(2));
        if(p_M!= 0)   p_M = p_M * (Math.log(1/p_M)/Math.log(2));
        if(size_g2-1>0)
            p_M = (size_g2 -1)* p_M;
      
        H = p_I + p_M ;
        return(H);
    }
    private double entropy_of_noteMappedNodes(double p_M)
    {
        double H = 0;
        int size_g2 = mapped_g2.size() + notMapped_g2.size();
        
        if(size_g2>0) p_M = p_M / size_g2;
        if(p_M!= 0)   p_M = p_M * (Math.log(1/p_M)/Math.log(2));
        p_M = size_g2 * p_M;
     
        H = p_M ;
        return(H);
    }
    public double entropy_of_system(){
        double H_hat = 0;
        double d = 0;
        
        double p_I = prob_x_trueMapping_xprime_if_x_isMapped_xprime();
        double p_M = prob_x_trueMapping_y_if_x_isMapped_xprime();
        double H1 = entropy_of_mappedNodes(p_I, p_M);
        
        double p_M_2 = prob_x_trueMapping_y_if_x_isNotMapped();
        double H2 = entropy_of_noteMappedNodes(p_M_2); 
   
        H_hat = (mapped_g1.size() * H1 + notMapped_g1.size() * H2) /
            (mapped_g1.size() + notMapped_g1.size());
        return(H_hat);
    }
    public double entropy_of_system(String dir, String probFileName){
        double H_hat = 0;
        try {
             //Read File
             File file1 = new File(dir + probFileName);
             FileInputStream fstream1 = new FileInputStream(file1);
             DataInputStream in1 = new DataInputStream(fstream1);
             BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
             String splitter = "\t";
             String Oneline = br1.readLine(); //header
             Oneline = br1.readLine(); //probabilities
             String[] p = Oneline.split(splitter);
             //Find entropies
            double p_I = Double.parseDouble(p[0]);
            double p_M = Double.parseDouble(p[1]);
            double H1 = entropy_of_mappedNodes(p_I, p_M);

            double p_M_2 = Double.parseDouble(p[2]);
            double H2 = entropy_of_noteMappedNodes(p_M_2); 

            H_hat = (mapped_g1.size() * H1 + notMapped_g1.size() * H2) /
                (mapped_g1.size() + notMapped_g1.size());
         } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return(H_hat);
    }
    private double max_entropy(){
        double H_M = (Math.log(mapped_g1.size() + notMapped_g1.size()))/ Math.log(2);
        return(H_M);
    }
    
    public double degree_of_anonimity(double H_M){
          double H_hat = entropy_of_system();
          double d = 0;
          d =  H_hat/H_M;
          return(d);
    }
    public double degree_of_anonimity(double H_M, String dir, String probFileName){
          double H_hat = entropy_of_system(dir, probFileName);
          double d = 0;
          d =  H_hat/H_M;
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
    public void print_degree_of_anonymity(String dir, String fileName, String probFileName){
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("degree_of_anonimity" + "\n");
            fw1.write(degree_of_anonimity(max_entropy(), "ns-avg-prob/", probFileName) + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void compute_and_print_probabilities(String dir, String fileName){
        double p_I = prob_x_trueMapping_xprime_if_x_isMapped_xprime();
        double p_M = prob_x_trueMapping_y_if_x_isMapped_xprime();
        double p_M_2 = prob_x_trueMapping_y_if_x_isNotMapped();
        
        try {
            FileWriter fw1 = new FileWriter(dir + fileName, true);
            fw1.write("p_I\tp_M\tp_M_2\n");
            fw1.write(p_I + "\t" +p_M + "\t" +p_M_2 + "\n");
            fw1.close();
        } catch (IOException ex) {
            Logger.getLogger(Degree_Anonymity_CommBlind.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void test_1(){
        this.mapping = new HashMap<String, String>();
        this.mapping.put("1", "1");
        this.mapping.put("2", "2");
        this.mapping.put("3", "3");
        this.mapping.put("4", "5");
        this.mapping.put("5", "4");
        this.mapping.put("6", "8");
        mapped_g1 = new HashSet<String>();
        notMapped_g1 = new HashSet<String>();
        mapped_g2 = new HashSet<String>();
        notMapped_g2 = new HashSet<String>();
        mapped_g1.add("1");
        mapped_g1.add("2");
        mapped_g1.add("3");
        mapped_g1.add("4");
        mapped_g1.add("5");
        mapped_g1.add("6");

        notMapped_g1.add("7");
        notMapped_g1.add("8");

        mapped_g2.add("1");
        mapped_g2.add("2");
        mapped_g2.add("3");
        mapped_g2.add("4");
        mapped_g2.add("5");
        mapped_g2.add("8");

        notMapped_g2.add("6");
        notMapped_g2.add("7");

        print_degree_of_anonymity("" ,"test.deg");
    }
     private void test_2(){
         //Could map all correctly
        this.mapping = new HashMap<String, String>();
        this.mapping.put("1", "1");
        this.mapping.put("2", "2");
        this.mapping.put("3", "3");
        this.mapping.put("4", "4");
        this.mapping.put("5", "5");
        this.mapping.put("6", "6");
        this.mapping.put("7", "7");
        this.mapping.put("8", "8");
        mapped_g1 = new HashSet<String>();
        notMapped_g1 = new HashSet<String>();
        mapped_g2 = new HashSet<String>();
        notMapped_g2 = new HashSet<String>();
        mapped_g1.add("1");
        mapped_g1.add("2");
        mapped_g1.add("3");
        mapped_g1.add("4");
        mapped_g1.add("5");
        mapped_g1.add("6");
        mapped_g1.add("7");
        mapped_g1.add("8");

        mapped_g2.add("1");
        mapped_g2.add("2");
        mapped_g2.add("3");
        mapped_g2.add("4");
        mapped_g2.add("5");
        mapped_g2.add("6");
        mapped_g2.add("7");
        mapped_g2.add("8");


        print_degree_of_anonymity("" ,"test-2.deg");
     }
     private void test_4(){
        //Could not mapped at all
        this.mapping = new HashMap<String, String>();
        mapped_g1 = new HashSet<String>();
        notMapped_g1 = new HashSet<String>();
        mapped_g2 = new HashSet<String>();
        notMapped_g2 = new HashSet<String>();
        notMapped_g1.add("1");
        notMapped_g1.add("2");
        notMapped_g1.add("3");
        notMapped_g1.add("4");
        notMapped_g1.add("5");
        notMapped_g1.add("6");
        notMapped_g1.add("7");
        notMapped_g1.add("8");

        notMapped_g2.add("1");
        notMapped_g2.add("2");
        notMapped_g2.add("3");
        notMapped_g2.add("4");
        notMapped_g2.add("5");
        notMapped_g2.add("6");
        notMapped_g2.add("7");
        notMapped_g2.add("8");

        print_degree_of_anonymity("" ,"test-4.deg");
    }
      private void test_5(){
         // mapped all incorrectly
        this.mapping = new HashMap<String, String>();
        this.mapping.put("1", "2");
        this.mapping.put("2", "3");
        this.mapping.put("3", "4");
        this.mapping.put("4", "5");
        this.mapping.put("5", "6");
        this.mapping.put("6", "1");
        
        mapped_g1 = new HashSet<String>();
        notMapped_g1 = new HashSet<String>();
        mapped_g2 = new HashSet<String>();
        notMapped_g2 = new HashSet<String>();
        mapped_g1.add("1");
        mapped_g1.add("2");
        mapped_g1.add("3");
        mapped_g1.add("4");
        mapped_g1.add("5");
        mapped_g1.add("6");
       
        mapped_g2.add("1");
        mapped_g2.add("2");
        mapped_g2.add("3");
        mapped_g2.add("4");
        mapped_g2.add("5");
        mapped_g2.add("6");
      


        print_degree_of_anonymity("" ,"test-5.deg");
     }
      public static void main(String[] args) {
       Simplified_Degree_Anonymity_CommBlind deg = new Simplified_Degree_Anonymity_CommBlind();
       deg.test_4();
    }
}
