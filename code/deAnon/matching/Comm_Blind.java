/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import deAnon.deg_anon.Degree_Anonymity_CommBlind;
import deAnon.deg_anon.Simplified_Degree_Anonymity_CommBlind;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class Comm_Blind {
    
    MyGraph g1;
    MyGraph g2;
    /*the key shows a vertex in g1.graph and the value shows its match in g2.graph*/
    HashMap<String, String> map;
    Integer maxID;
    /* random generator */
    Random rnd;
    public Comm_Blind(MyGraph g1, MyGraph g2, HashMap<String, String> map){
        this.g1 = g1;
        this.g2 = g2;
        this.map = map;
        rnd = new Random(7481);
    }
    public Comm_Blind(){
        
    }

    public void g1(Graph<String, Edge> g){
         UndirectedSparseGraph<String, Edge> graph = new UndirectedSparseGraph<String, Edge>();
         for (String v : g.getVertices())
            graph.addVertex(v);

        for (Edge e : g.getEdges())
            graph.addEdge(e, g.getIncidentVertices(e));

        g1.put_graph(graph);
     }

     public void g2(Graph<String, Edge> g){
           UndirectedSparseGraph<String, Edge> graph = new UndirectedSparseGraph<String, Edge>();
         for (String v : g.getVertices())
            graph.addVertex(v);

        for (Edge e : g.getEdges())
            graph.addEdge(e, g.getIncidentVertices(e));
        g2.put_graph(graph);
     }
     
     public void map(HashMap<String, String> m){
         map = new HashMap<String, String>();
         for (String n : m.keySet())
             map.put(n, m.get(n));
     }
     public void setMaxID(Integer id){
         maxID = id;
     }
     
    public int evaluation(HashMap<String, String> map){
        int match = 0;
        for(String u : map.keySet()){
           String u_name = g1.getNodeName(u);
            String uprime_name = g2.getNodeName(map.get(u));
            if(u_name.equals(uprime_name)) match++;
          //  if(u.equals(map.get(u))) match++;
        }
        return(match);
    }
   public int get_numMappedNodes_among_overlappedNodes(HashMap<String, String> map, HashMap<String, String> overlappedNodesIDs){
        int match = 0;
        for(String u : map.keySet()){
            if(overlappedNodesIDs.containsKey(u)) 
              match++;
        }
        return(match);
    }
    public HashMap<String, Float> calculate_scores_considering_weights(HashMap<String, String> mapping,
            Graph<String, Edge> g1, Graph<String, Edge> g2, 
            HashMap<String, HashMap<String, Float>> edgesWeights1,
            HashMap<String, HashMap<String, Float>> edgesWeights2, String u)
    {
        ArrayList<String> nghbrs1 = new ArrayList(g1.getNeighbors(u));
        HashMap<String, Float> scores = new HashMap<String, Float>();
        for(String nbr1 : nghbrs1){
            if(mapping.containsKey(nbr1)){
                String nbr2 = mapping.get(nbr1);
                if(g2.getNeighbors(nbr2) != null){
                    ArrayList<String> nghbrs2 = new ArrayList(g2.getNeighbors(nbr2));
                    for(String v : nghbrs2){
                        if(!mapping.containsValue(v)){
                                Float w1 = edgesWeights1.get(nbr1).get(u);
                                Float w2 = edgesWeights2.get(nbr2).get(v);
                                                  
                                int d1 = g1.getNeighborCount(u);
                                int d2 = g2.getNeighborCount(v);
                                
                                float val = 0;
                                if(scores.containsKey(v))
                                    val = scores.get(v);

                                val += (1- Math.abs(Math.sqrt(w1)-Math.sqrt(w2)))/(Math.sqrt(d1)*Math.sqrt(d2)); // the best 23-24%
                
                                scores.remove(v);
                                scores.put(v, val);                        
                        }
                    }
               }
            }
        }
        return(scores);
    }
    public HashMap<String, Float> matchScores(HashMap<String, String> mapping,
            Graph<String, Edge> g1, Graph<String, Edge> g2, String u)
    {
        ArrayList<String> nghbrs1 = new ArrayList(g1.getNeighbors(u));
       HashMap<String, Float> scores = new HashMap<String, Float>();
        for(String nbr1 : nghbrs1){
            if(mapping.containsKey(nbr1)){
                String nbr2 = mapping.get(nbr1);
                if(g2.getNeighbors(nbr2) != null){
                    ArrayList<String> nghbrs2 = new ArrayList(g2.getNeighbors(nbr2));
                    for(String v : nghbrs2){
                        if(!mapping.containsValue(v)){
                            int counts = g2.getNeighborCount(v);
                            int counts2 = g1.getNeighborCount(u);
                            float val = 0;
                            if(scores.containsKey(v))
                                val = scores.get(v);
                            val += 1/ (Math.sqrt(counts)*Math.sqrt(counts2));
                            scores.remove(v);
                            scores.put(v, val);
                        }
                    }
               }
            }
        }
        return(scores);
    }
    public float std_dev(Float[] items){
        float sum = 0;
        for(float i: items)
            sum += i;
	float avg = sum /items.length;

        sum = 0;
	for(float i: items)
            sum += Math.pow((i - avg), 2);

	float sd = (float) Math.sqrt(sum/items.length);
        return (sd);
    }
    public float eccentricity(HashMap<String,Float> list){
        Float []items = new Float[list.size()];
        list.values().toArray(items);
        if(items.length>1){
            Arrays.sort(items);
            float std = std_dev(items);
            float num = (items[items.length-1] - items[items.length-2]);
            float ecc = num /std;
        //    if(ecc> 1)
          //      System.out.print("wait");
            return(ecc);
        }else if(items.length>0){
            return((items[items.length-1])/std_dev(items));
        }else{
            return(-1);
        }
    }
    public Integer pickNode(HashMap<String,Float> scores){
        String max = "";
        if(scores.keySet().iterator().hasNext())
            max = scores.keySet().iterator().next();
        if(!max.equals(""))
            for(String key: scores.keySet()){
                if(scores.get(key)>scores.get(max)){
                                max = key;
                }
            }
        return(Integer.parseInt(max));
    }

    public Integer pickSecondNode(int max1, HashMap<String,Float> scores){
        String max2 = "";
        if(scores.keySet().iterator().hasNext())
            max2 = scores.keySet().iterator().next();
        if(!max2.equals(""))
            for(String key: scores.keySet()){
                if(Integer.parseInt(max2) < max1 && scores.get(key) > scores.get(max2)){
                    max2 = key;
                }
            }
        return(Integer.parseInt(max2));
    }
    public HashMap<String, String> invert(HashMap<String, String> mapping){
        HashMap<String, String> invMapping = new HashMap<String, String>();
        for(String key: mapping.keySet()){
            String value = mapping.get(key);
            invMapping.put(value, key);
        }
        return(invMapping);
    }
    public int maxID(ArrayList<String> nodes){
        Integer max = Integer.parseInt(nodes.get(0));
        for(String n: nodes){
            if(Integer.parseInt(n) > max)
                max = Integer.parseInt(n);
        }
       return max; 
    }
    public void propagationStep(Graph<String, Edge> g1, Graph<String, Edge> g2, Double thr){
        ArrayList<String> nodes1 = new ArrayList(g1.getVertices());
        ArrayList<String> nodes2 = new ArrayList(g2.getVertices());

        HashMap<String, HashMap<String, Float>> scores = new HashMap<String, HashMap<String, Float>>();
        for(String u : nodes1){
           // System.out.print("Matching node " + u );
            if(!map.containsKey(u)){
                scores.put(u, matchScores(map, g1, g2,  u));
                float ecc1 = eccentricity(scores.get(u));
                if(ecc1 >= thr){
                    Integer v = pickNode(scores.get(u));
                    if(nodes2.contains(Integer.toString(v))){ ///?
                        scores.put(v.toString() , matchScores(invert(map), g2, g1, Integer.toString(v)));
                        float ecc2 = eccentricity(scores.get(v.toString()));
                        if(ecc2 >= thr){
                            Integer reverse_match = pickNode(scores.get(v.toString()));
                            if(reverse_match == Integer.parseInt(u)){
                                map.put(u, Integer.toString(v));
                                System.out.print("matched "+ u +" with " + v +" ...\n");
                            }//else
                            //    System.out.print(" not found ...\n");                            
                        }//else
                          //   System.out.print(" not found ...\n");
                    }//else
                     //   System.out.print(" not found ...\n");
                }//else
                   //  System.out.print(" not found ...\n");
            }//else
               //  System.out.print(" has already found ...\n");
        }
       System.out.print("one round of propagation step done...\n");
    }
    public void ImprovedPropagationStep(Graph<String, Edge> g1, Graph<String, Edge> g2,
            HashMap<String, HashMap<String, Float>> edgesWeights1,
            HashMap<String, HashMap<String, Float>> edgesWeights2, Double thr, int c)
    {
        ArrayList<String> nodes1 = new ArrayList(g1.getVertices());
        ArrayList<String> nodes2 = new ArrayList(g2.getVertices());
        HashMap<String, HashMap<String, Float>> scores1 = new HashMap<String, HashMap<String, Float>>();
        HashMap<String, HashMap<String, Float>> scores2 = new HashMap<String, HashMap<String, Float>>();
        for(String u : nodes1){
            System.out.print("Matching comm " + u );
            if(!map.containsKey(u)){
                scores1.put(u, calculate_scores_considering_weights(map, g1, g2, edgesWeights1, edgesWeights2, u));
                float ecc1 = eccentricity(scores1.get(u));
                if(ecc1 >= thr){
                    Integer v = pickNode(scores1.get(u));
                    if(nodes2.contains(Integer.toString(v))){
                        scores2.put(v.toString() , calculate_scores_considering_weights(invert(map), g2, g1, edgesWeights2, edgesWeights1, Integer.toString(v)));
                        float ecc2 = eccentricity(scores2.get(v.toString())) ;
                        if(ecc2 >= thr){
                            Integer reverse_match = pickNode(scores2.get(v.toString()));
                            
                          //  if(c<1){
                                if(reverse_match == Integer.parseInt(u)){
                                    map.put(u, Integer.toString(v));
                                    System.out.print(" with comm " + v +" ...\n");
                                }else
                                    System.out.print(" not found ...\n");
                         /*   }else{
                                map.put(u, Integer.toString(v));
                                System.out.print(" with node " + v +" ...\n");
                            }  
                          * 
                          */
                        }else
                             System.out.print(" not found ...\n");
                    }else
                     System.out.print(" not found ...\n");
                }else{
                     System.out.print(" not found ...\n");
                }
            }else
                 System.out.print(" has already found ...\n");
        }
    }

    public float get_crr_mapped_comms(double jaccard_thr){
        HashMap<String, String> matchedComms = Community.init_MatchedComms(map, g1.get_commNodes(), g2.get_commNodes(), jaccard_thr);
        int nodes_counts = 0;
        for(String c1: matchedComms.keySet()){
            String c2 = matchedComms.get(c1);

            int commNodes_cnt1 = 0;
            int commNodes_cnt2 = 0;
            if(g1.get_commNodes().get(c1) != null)
                commNodes_cnt1 = g1.get_commNodes().get(c1).size();
             if(g2.get_commNodes().get(c2) != null)
                commNodes_cnt2 = g2.get_commNodes().get(c2).size();
            nodes_counts = nodes_counts + (commNodes_cnt1+ commNodes_cnt2);
        }
        float matchedGraph = nodes_counts/(float)(g1.getNodeIds().keySet().size() + g2.getNodeIds().keySet().size());
        return(matchedGraph*100);
    }
    public float get_incrr_mapped_comms(){
        HashMap<String, String> crr_matchedComms = Community.get_crr_MatchedComms();       
        int nodes_counts = 0;
        for(String c1:map.keySet()){
           if(!crr_matchedComms.containsKey(c1)){
                String c2 = map.get(c1);
                int commNodes_cnt1 = 0;
                int commNodes_cnt2 = 0;
                if(g1.get_commNodes().get(c1) != null)
                    commNodes_cnt1 = g1.get_commNodes().get(c1).size();
                 if(g2.get_commNodes().get(c2) != null)
                    commNodes_cnt2 = g2.get_commNodes().get(c2).size();
                nodes_counts = nodes_counts + (commNodes_cnt1+ commNodes_cnt2);
            }
        }
        float matchedGraph = nodes_counts/(float)(g1.getNodeIds().keySet().size() + g2.getNodeIds().keySet().size());
        return(matchedGraph*100);
    }
    public float get_mapped_comms(){
       
        int nodes_counts = 0;
        for(String c1: map.keySet()){
            String c2 = map.get(c1);
            
            int commNodes_cnt1 = 0;
            int commNodes_cnt2 = 0;
            if(g1.get_commNodes().get(c1) != null)
                commNodes_cnt1 = g1.get_commNodes().get(c1).size();
             if(g2.get_commNodes().get(c2) != null)
                commNodes_cnt2 = g2.get_commNodes().get(c2).size();
            nodes_counts = nodes_counts + (commNodes_cnt1+ commNodes_cnt2);
        }
        float matchedGraph = nodes_counts/(float)(g1.getNodeIds().keySet().size() + g2.getNodeIds().keySet().size());
        return(matchedGraph*100);
    }
 public HashMap<String, String> NS_comm(MyGraph g1, MyGraph g2, Double thr, String fName) {
        try {

             DecimalFormat myFormat = new DecimalFormat("##.##");
            int counts = 0;
         //   for(float jaccard_thr= (float) 0.1; jaccard_thr <1; jaccard_thr = (float) (jaccard_thr + 0.2)){
            double jaccard_thr = 0.3;
                FileWriter fw = new FileWriter(fName + "-jaccard-" + jaccard_thr, true);               
                fw.write("Round\t\tSuccessRate\tErrRate\t\tNotMapped\tPerCorrectlyMapped\tPerNotCorrectlyMapped\n");
                float numSucc = Community.evaluation_comm(map, g1, g2, jaccard_thr);
                float succRate = (numSucc/map.size() )* 100;
                float errRate = ((map.size()-numSucc)/map.size()) * 100;
                float perNotMapped = ((float)(g1.get_commNodes().size()- map.size())/(float) g1.get_commNodes().size())*100;
                float perCorrMapped = (numSucc / g1.get_commGraph().getVertexCount()) * 100 ;
                float perNotCorrMapped =  ((map.size() - numSucc) / g1.get_commGraph().getVertexCount()) * 100;

                fw.write("0" + "\t\t" + myFormat.format(succRate) + "\t\t" +myFormat.format(errRate) +
                        "\t\t" +myFormat.format(perNotMapped) + "\t\t" + myFormat.format(perCorrMapped) +
                        "\t\t" + myFormat.format(perNotCorrMapped) + "\n");
                fw.close();
         //   }
            int preMapSize = 0;
            int c = 0;
            while (map.keySet().size() != g1.get_graph().getVertexCount()) {
                if (preMapSize != map.size()) {
                    preMapSize = map.size();
                    c = 0;
                } else {
                    c++;
                }
                if (c < 5) {
                    System.out.print("Round " + c + "\n");
                   // propagationStep( g1.get_commGraph(), g2.get_commGraph(), thr);
                    ImprovedPropagationStep( g1.get_commGraph(), g2.get_commGraph(), g1.get_edgesWeights(), g2.get_edgesWeights(),thr, c);
                    counts++;

                 //   for(float jaccard_thr= (float) 0.1; jaccard_thr <1; jaccard_thr = (float)(jaccard_thr + 0.2)){
                        fw = new FileWriter(fName + "-jaccard-" + jaccard_thr, true);
                        numSucc = Community.evaluation_comm(map, g1, g2, jaccard_thr);//Config.thr_jaccard
                        succRate = (numSucc/map.size() )* 100;
                        errRate = ((map.size()-numSucc)/map.size()) * 100;
                        perNotMapped = ((float)(g1.get_commNodes().size()- map.size())/(float)g1.get_commNodes().size())*100;
                        perCorrMapped = (numSucc / g1.get_commGraph().getVertexCount()) * 100 ;
                        perNotCorrMapped =  ((map.size() - numSucc) / g1.get_commGraph().getVertexCount()) * 100;
                        fw.write(counts  + "\t\t" + myFormat.format(succRate) + "\t\t"
                                +myFormat.format(errRate) + "\t\t" +myFormat.format(perNotMapped) +
                                "\t\t" + myFormat.format(perCorrMapped) +
                                "\t\t" + myFormat.format(perNotCorrMapped) + "\n");
                        fw.close();
                //    }
             
                } else {
                    break;
                }
            }
            //matched graph when community mapping is done
            float matchedGraph = get_mapped_comms();
            fw = new FileWriter(Config.dir + "/final-matchedGraph-" + Config.noise + ".txt", true);
            fw.write(myFormat.format(matchedGraph) +"\n");
            fw.close();
            
            //based on crr community mapping
            jaccard_thr = 0.3;
           // for(float jaccard_thr= (float) 0.1; jaccard_thr <1; jaccard_thr = (float)(jaccard_thr + 0.2)){
                fw = new FileWriter(fName + "-matchedGraph-jaccard-" + jaccard_thr + ".txt", true);
                fw.write("per_crr\tper_incrr\n");
                float crr_matchedGraph = get_crr_mapped_comms(jaccard_thr);
                float incrr_matchedGraph = get_incrr_mapped_comms();
                fw.write(myFormat.format(crr_matchedGraph) + "\t" + myFormat.format(incrr_matchedGraph) +"\n");
                fw.close();
           // }
        } catch (IOException ex) {
            Logger.getLogger(Comm_Blind.class.getName()).log(Level.SEVERE, null, ex);
        }
         return map;
    }

 public HashMap<String, String> get_overlappedNodeIDs(){
     HashMap<String, String> overlappedNodes = new HashMap<String, String>();
     for(String u : g1.getNodeIds().keySet()){
           String u_name = g1.getNodeName(u);
           if(g2.getNodeNames().containsKey(u_name))
               overlappedNodes.put(u, g2.getNodeId(u_name));
        }
     return(overlappedNodes);
 }
 public HashMap<String, String> NS_overlappedDataSets(Graph<String, Edge> g1, Graph<String, Edge> g2, Double thr, boolean insideComm, String fName) {
    try {
        DecimalFormat myFormat = new DecimalFormat("##.##");
        HashMap<String, String> overlappedNodes = get_overlappedNodeIDs();
        int num_overlappedNodes = overlappedNodes.size();
        int counts = 0;
        if(!insideComm){

            FileWriter fw = new FileWriter(fName, true);
            fw.write("Round\t\t\tSuccessRate\t\tErrRate\t\tPerCorrectlyMapped\tPerNotCorrMapped\tNotMapped\tPerCorrectlyMapped_overlap\tPerNotCorrMapped_overlap\tPerNotMapped_overlap\n");
            float numSucc = evaluation(map); //number of correctly maps; it is only happens when nodes are in the overlapped sets
            float numMappedNodes_among_overlappedNodes = get_numMappedNodes_among_overlappedNodes(map, overlappedNodes);
            float succRate = (numSucc/map.size())* 100;
            float errRate =((map.size() - numSucc) /map.size())*100;
            float PerNotMapped_in_overlapped = ((float)(num_overlappedNodes - numMappedNodes_among_overlappedNodes) / num_overlappedNodes) * 100; 
            float PerNotMapped = ((float)(g1.getVertexCount() - map.size()) / g1.getVertexCount()) * 100; 
            float perCorrMapped = (numSucc / g1.getVertexCount()) * 100;
            float perCorrMapped_overlapped = (numSucc /  num_overlappedNodes) * 100; 
            float perNotCorrMapped_overlapped =  ((numMappedNodes_among_overlappedNodes - numSucc) /  num_overlappedNodes) * 100; 
            float perNotCorrMapped =  ((map.size() - numSucc) /  g1.getVertexCount()) * 100; 
            fw.write("0" + "\t\t\t" + myFormat.format(succRate) + "\t\t\t" + myFormat.format(errRate) 
                    + "\t\t\t" + myFormat.format(perCorrMapped) + "\t\t\t" + myFormat.format(perNotCorrMapped) + "\t\t\t" + myFormat.format(PerNotMapped) 
                    + "\t\t\t" + myFormat.format(perCorrMapped_overlapped) + "\t\t\t" + myFormat.format(perNotCorrMapped_overlapped)+ "\t\t\t" + myFormat.format(PerNotMapped_in_overlapped)  + "\n");
            fw.close();
        }
        int preMapSize = 0;
        int c = 0;
        while (map.keySet().size() != g1.getVertexCount()) {
            if (preMapSize != map.size()) {
                preMapSize = map.size();
                c = 0;
            } else {
                c++;
            }
            if (c < 5) {
                System.out.print("Round " + c + "\n");
                propagationStep( g1, g2, thr);
                if(!insideComm){
                    FileWriter fw = new FileWriter(fName, true);                       
                    float numSucc = evaluation(map); //number of correctly maps; it is only happens when nodes are in the overlapped sets
                    float numMappedNodes_among_overlappedNodes = get_numMappedNodes_among_overlappedNodes(map, overlappedNodes);
                    float succRate = (numSucc/map.size())* 100;
                    float errRate =((map.size() - numSucc) /map.size())*100;
                    float PerNotMapped_in_overlapped = ((float)(num_overlappedNodes - numMappedNodes_among_overlappedNodes) / num_overlappedNodes) * 100; 
                    float PerNotMapped = ((float)(g1.getVertexCount() - map.size()) / g1.getVertexCount()) * 100; 
                    float perCorrMapped = (numSucc / g1.getVertexCount()) * 100;
                    float perCorrMapped_overlapped = (numSucc /  num_overlappedNodes) * 100; 
                    float perNotCorrMapped_overlapped =  ((numMappedNodes_among_overlappedNodes - numSucc) /  num_overlappedNodes) * 100; 
                    float perNotCorrMapped =  ((map.size() - numSucc) /  g1.getVertexCount()) * 100; 
                    
                    counts++;
                     fw.write(counts + "\t\t\t" + myFormat.format(succRate) + "\t\t\t" + myFormat.format(errRate) 
                    + "\t\t\t" + myFormat.format(perCorrMapped) + "\t\t\t" + myFormat.format(perNotCorrMapped) + "\t\t\t" + myFormat.format(PerNotMapped) 
                    + "\t\t\t" + myFormat.format(perCorrMapped_overlapped) + "\t\t\t" + myFormat.format(perNotCorrMapped_overlapped)+ "\t\t\t" + myFormat.format(PerNotMapped_in_overlapped) + "\n");
                     
                    fw.close();
                }
            } else {
                break;
            }
        }
    } catch (IOException ex) {
        Logger.getLogger(Comm_Blind.class.getName()).log(Level.SEVERE, null, ex);
    }
     return map;
}
    public HashMap<String, String> NS(Graph<String, Edge> g1, Graph<String, Edge> g2, Double thr, boolean insideComm, String fName) {
        try {
            DecimalFormat myFormat = new DecimalFormat("##.##");
            int counts = 0;
            if(!insideComm){
                
                FileWriter fw = new FileWriter(fName, true);
                fw.write("Round\t\tSuccessRate\tErrRate\t\tNotMapped\tPerCorrectlyMapped\tPerNotCorrectlyMapped\n");
                float numSucc = evaluation(map);
                float succRate = (numSucc/map.size())* 100;
                float errRate =((map.size() - numSucc) /map.size())*100;
                float PerNotMapped = ((float)(g1.getVertexCount() - map.size()) / (float) g1.getVertexCount()) * 100;
                float perCorrMapped = (numSucc /  g1.getVertexCount()) * 100;
                float perNotCorrMapped =  ((map.size() - numSucc) /  g1.getVertexCount()) * 100;                
                fw.write("0" + "\t\t" + myFormat.format(succRate) + "\t\t" + myFormat.format(errRate) + "\t\t" + myFormat.format(PerNotMapped)
                        + "\t\t" + myFormat.format(perCorrMapped) + "\t\t" + myFormat.format(perNotCorrMapped) + "\n");
                fw.close();
            }
            int preMapSize = 0;
            int c = 0;
            while (map.keySet().size() != g1.getVertexCount()) {
                if (preMapSize != map.size()) {
                    preMapSize = map.size();
                    c = 0;
                } else {
                    c++;
                }
                if (c < 5) {
                    System.out.print("Round " + c + "\n");
                    propagationStep( g1, g2, thr);
                    if(!insideComm){
                        FileWriter fw = new FileWriter(fName, true);                       
                        float numSucc = evaluation(map);
                        float succRate = (numSucc/map.size())*100;
                        float errRate = ((map.size() - numSucc) /map.size()) * 100;
                        float PerNotMapped = ((float)(g1.getVertexCount() - map.size()) /  (float) g1.getVertexCount()) * 100;
                        float perCorrMapped = (numSucc / g1.getVertexCount()) * 100;
                        float perNotCorrMapped =  ((map.size() - numSucc) /  g1.getVertexCount()) * 100;

                        counts++;
                        fw.write(counts +  "\t\t" + myFormat.format(succRate) + "\t\t" + myFormat.format(errRate) + "\t\t" + myFormat.format(PerNotMapped)
                                + "\t\t" + myFormat.format(perCorrMapped) + "\t\t" + myFormat.format(perNotCorrMapped) + "\n");
                        fw.close();
                    }
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Comm_Blind.class.getName()).log(Level.SEVERE, null, ex);
        }
         return map;
    }

   public void matching_nodes(String n1, Graph<String, Edge> c1, Graph<String, Edge> c2,
         HashMap<String, Double> S, Double thr){

         if(!map.containsKey(n1)){               
               HashMap<String, Double> similarNodes = new HashMap<String, Double>();
               for(String n2 : c2.getVertices()){
                   Double s =  S.get(n2);
                   if(s<=thr){
                        similarNodes.put(n2, s);
                   }
               }
               if(similarNodes.size() == 1){
                   String n2 = similarNodes.keySet().iterator().next();
                   map.put(n1, n2);
               }

               System.out.print("Node " + n1 + " can be mapped to " + similarNodes.size() + " different nodes: " + similarNodes.keySet().toString() + "\n" );
           }
       

       System.out.print("The graph 1 has " + c1.getVertexCount() + " nodes!\n");
       System.out.print("The graph 2 has " + c2.getVertexCount() + " nodes!\n");
       System.out.print("\nThe number of nodes that could be mapped are " + map.size() + "...\n");
 }

 public void matching_nodes(Graph<String, Edge> c1, Graph<String, Edge> c2,
         HashMap<String, HashMap<String, Double>> S, Double thr){

       for(String n1 : c1.getVertices()){
           if(!map.containsKey(n1)){
               HashMap<String, Double> tmp  = S.get(n1);
               HashMap<String, Double> similarNodes = new HashMap<String, Double>();
               for(String n2 : c2.getVertices()){
                   Double s = tmp.get(n2);
                   if(s<=thr){
                        similarNodes.put(n2, s);
                   }
               }
               if(similarNodes.size() == 1){
                   String n2 = similarNodes.keySet().iterator().next();
                   map.put(n1, n2);
               }

               System.out.print("Node " + n1 + " can be mapped to " + similarNodes.size() + " different nodes: " + similarNodes.keySet().toString() + "\n" );
           }
       }

       System.out.print("The graphs has " + c1.getVertexCount() + " nodes!\n");
       System.out.print("\nThe number of nodes that could be mapped are " + map.size() + "...\n");
 }
  public void matching_nodes(Graph<String, Edge> c1, Graph<String, Edge> c2,
         Hashtable<String, Hashtable<String, Double>> S, Double thr){

       for(String n1 : c1.getVertices()){
           if(!map.containsKey(n1)){
               Hashtable<String, Double> tmp  = S.get(n1);
               HashMap<String, Float> similarNodes = new HashMap<String, Float>();
               for(String n2 : c2.getVertices()){
                   if(tmp.containsKey(n2)){
                       Float s = tmp.get(n2).floatValue();
                       if(s<=thr){
                            similarNodes.put(n2, s);
                       }
                   }
               }
               if(eccentricity(similarNodes)>=thr)

               System.out.print("Node " + n1 + " can be mapped to " + similarNodes.size() + " different nodes: " + similarNodes.keySet().toString() + "\n" );
           }
       }

       System.out.print("The graphs has " + c1.getVertexCount() + " nodes!\n");
       System.out.print("\nThe number of nodes that could be mapped are " + map.size() + "...\n");
 }

    public void CommBlind_deAnon_Alg(String[] args){
        try {
            Config config = new Config(args);//, "results-ns-prob-"
            
            System.out.println("Reading cliques...");
            //Cliques for the original network should have be found in advance
            g1.read_cliques(args[6]);
            System.out.print("Read known seeds...\n");
            //Seeds are nodes of some cliques of 4 which are picked randomly
            map.putAll(g1.get_random_cliques_of_size(4, config.num_seeds/4));//numCliques
            String fName2 = config.dir + "ns-" + config.noise + "-" + config.num_seeds/4; //numCliques
            System.out.print("Matching NS Algorithm...\n");
            NS(g1.get_graph(), g2.get_graph(), 0.1, false, fName2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comm_Blind.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void CommBlind_deAnon_Alg_OverlappedNodeSets(String[] args){
        try {
            Config config = new Config(args);//, "results-ns-overlapped-prob-"
            System.out.println("Reading cliques...");
            //Cliques for the original network should have be found in advance
            g1.get_overlapped_cliques(args[6], g2);
            System.out.print("Read known seeds...\n");
            //Seeds are nodes of some cliques of 4 which are picked randomly
            map.putAll(g1.get_random_OverlappedCliques_of_size(4, config.num_seeds/4, g2));//numCliques
            String fName2 = config.dir + "ns-" + config.noise + "-" + config.num_seeds/4; //numCliques
            System.out.print("Matching NS Algorithm...\n");
            NS_overlappedDataSets(g1.get_graph(), g2.get_graph(), config.thr_node_ecc, false, fName2); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comm_Blind.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
    }
    public static void main(String[] args) throws Exception {

       
        Date date= new java.util.Date();
        Timestamp start = new Timestamp(date.getTime());
        DecimalFormat myFormat = new DecimalFormat("##.##");
        if(args.length < 9){
                System.out.println("Usage: de-anonymization ResultsDirectory NumSeeds "
                        + "Noise Exp FileName1.net FileName2.net FileName.clique Prob/DegAnon(False/True) Overlapped(False/True) ");
                System.out.println("Example: comm-blind 16 0.2 1 data/cond-mat-2005-lcc data/noisy-cond-mat-2005-lcc-0.2-1 data/clique.ref  False False");
                System.out.println("\nHint: To get degree of anonymity, you need to run the exps: First with Prob/DegAnon set as False and average the results" 
                        + " then run them with Prob/DegAnon set as True");
                System.exit(1);
        }
        boolean prob_deg = Boolean.parseBoolean(args[7]);
        if (prob_deg && args.length < 10){
                System.out.println("Add the FileName for the average probabilities to the arguments... ");
                System.out.println("Hint: the FileName for the average probabilities should be in the ResultsDirectory");
                System.out.println("\nUsage: de-anonymization ResultsDirectory NumSeeds "
                        + " Noise Exp FileName1.net FileName2.net FileName.clique Prob/DegAnon(False/True) Overlapped(False/True) FileName.avgProb");
                
                System.exit(1);
        }   
        
        MyGraph g1 = new MyGraph(args[4]);
        MyGraph g2 = new MyGraph(args[5]);
        HashMap<String, String> map = new HashMap<String, String>();
        Comm_Blind is = new Comm_Blind(g1, g2, map);
        
        boolean overlapped = Boolean.parseBoolean(args[8]);
        if (overlapped){
            
            is.CommBlind_deAnon_Alg_OverlappedNodeSets(args);
        }else{
            
            is.CommBlind_deAnon_Alg(args);
        }
        
        /* Degree of anonymity: 
             First, to compute and save the probabilities, You should run 
             the algorithm with Prob/Deg argument set as False for several ensambles of noisy networks
            
            Then, calculate the average of probabilities and save them in a file,
            Finally, run all those exps while Prob/Deg argument set as True
            to compute degree of anonymity
          
            There are two different functions for calculating the degree of anonymity:
            The results are similar enough to just use the simpler one
            */
        //Degree_Anonymity_NS degreeAnon = new Degree_Anonymity_NS(map, g1, g2);
        //degreeAnon.compute_and_print_probabilities(Config.dir_seed, "ns-prob-" + Config.noise + ".txt");
        //degreeAnon.print_degree_of_anonymity(Config.dir_seed, "deg-" + Config.noise + ".txt", "ns-prob-" + Config.noise + ".txt");
        Simplified_Degree_Anonymity_CommBlind simpler_degreeAnon = new Simplified_Degree_Anonymity_CommBlind(map, g1, g2);
        if (!prob_deg)
            simpler_degreeAnon.compute_and_print_probabilities(Config.dir, "ns-simpler-prob-" + Config.noise + ".txt");
        else{
            String fileName_avgProb = args[9];
            simpler_degreeAnon.print_degree_of_anonymity(Config.dir +"/" + fileName_avgProb , "simpler-deg-" + Config.noise + ".txt",  "ns-simpler-prob-" + Config.noise + ".txt");
        }
            
         date= new java.util.Date();
         Timestamp stop = new Timestamp(date.getTime());
         FileWriter fw = new FileWriter(Config.dir + "time-" + Config.noise +".txt", true);
         fw.write("Start time: " + start + "\n");
         fw.write("Stop time: " + stop + "\n");
         fw.close();   
         System.out.println("Start time: " + start + "\n");
         System.out.println("Stop time: " + stop + "\n");
    }

 
}
