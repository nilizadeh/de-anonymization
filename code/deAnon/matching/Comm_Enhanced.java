/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
import deAnon.deg_anon.Simplified_Degree_Anonymity_CommAware;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class Comm_Enhanced {

    MyGraph g1;
    MyGraph g2;
     // mapping nodes to each other
    public HashMap<String, HashMap<String, String>> mapping = new HashMap<String, HashMap<String, String>>();
    public HashMap<String, String> nodeMapping = new HashMap<String, String>();
    public HashMap<String, String> cMapping = new HashMap<String, String>();

     public Comm_Enhanced(MyGraph g1, MyGraph g2){
        this.g1 = g1;
        this.g2 = g2;
     }
     public Comm_Enhanced(){
     }
     public void matching(HashMap<String, HashMap<String, Integer>> S, int thr){
       for(String comm1 : g1.get_commNodes().keySet()){
           if(!cMapping.containsKey(comm1)){
               HashMap<String, Integer> tmp  = S.get(comm1);
               HashMap<String, Integer> similarComms = new HashMap<String, Integer>();
               for(String comm2 : g2.get_commNodes().keySet()){
                   int s = tmp.get(comm2);
                   if(s<=thr){
                        similarComms.put(comm2, s);
                   }
               }
                if(similarComms.size() == 1)
                   for(String comm2 : similarComms.keySet()){
                      if(!cMapping.containsValue(comm2))
                         cMapping.put(comm1, comm2);
                   }

               System.out.print("Community " + comm1 + " can be mapped to " + similarComms.size() + " communities: " + similarComms.keySet().toString() + "\n" );              
           }
       }
       System.out.print("The graphs has " + g1.get_commNodes().size() + " communities!\n");
        System.out.print("\nThe number of communities that could be mapped are " + cMapping.size() + "...\n");
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
    public float eccentricity_distance_metric(HashMap<String,Float> list){
        if(list == null || list.isEmpty())
            return(-1);
        else{
            Float []items = new Float[list.size()];
            list.values().toArray(items);
            if(items.length>1){
                Arrays.sort(items);
                float std = std_dev(items);
                float num = Math.abs(items[0] - items[1]);
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
    }
   public String pickNode(HashMap<String,Float> scores){
        String min = "";
        if(scores.keySet().iterator().hasNext())
            min = scores.keySet().iterator().next();
        if(!min.equals(""))
            for(String key: scores.keySet()){
                if(scores.get(key)<scores.get(min)){
                                min = key;
                }
            }
        return(min);
    }
 public void matching_Float(HashMap<String, HashMap<String, Float>> S, Double thr){
    
     for(String comm1 : g1.get_commNodes().keySet()){
         if(!cMapping.containsKey(comm1)){
             HashMap<String, Float> tmp1  = S.get(comm1);
             if(tmp1 != null)
                 if(eccentricity_distance_metric(tmp1)>=thr){
                     String comm2 = pickNode(tmp1);
                     if(!cMapping.containsValue(comm2)){
                         HashMap<String, Float> tmp2  = new HashMap<String, Float>();
                         tmp2  = S.get(comm2);
                         if(eccentricity_distance_metric(tmp2)>=thr){
                             String comm1prime = pickNode(tmp2);
                             if(comm1prime.equals(comm1))
                                 cMapping.put(comm1, comm2);
                         }
                     } 
             }
         }
     }
 }
 public void matching_nodes_comm(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2,
         HashMap<String, HashMap<String, Float>> S, Double thr){

       HashMap<String, String> tmpMapping = new HashMap<String, String>();
       if(mapping.get(comm1) != null){
           tmpMapping = mapping.get(comm1);
       }
       for(String n1 : c1.getVertices()){
           if(!tmpMapping.containsKey(n1)){
               HashMap<String, Float> tmp  = S.get(n1);
                 if(eccentricity_distance_metric(tmp)>=thr){
                     String n2 = pickNode(tmp);
                     if(!tmpMapping.containsValue(n2)){
                         HashMap<String, Float> tmp2  = new HashMap<String, Float>();
                         tmp2  = S.get(n2);
                         if(eccentricity_distance_metric(tmp2)>=thr){
                             String n1prime = pickNode(tmp2);
                             if(n1prime.equals(n1)){
                                 tmpMapping.put(n1, n2);
                                 nodeMapping.put(n1, n2);
                             }
                         }
                     }
             }
         }
       }
 }
 public void matching_nodes_comm_overlappedSets(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2,
         HashMap<String, HashMap<String, Float>> S, HashMap<String, HashMap<String, Float>> S2, Double thr){

       HashMap<String, String> tmpMapping = new HashMap<String, String>();
       if(mapping.get(comm1) != null){
           tmpMapping = mapping.get(comm1);
       }
       for(String n1 : c1.getVertices()){
           if(!tmpMapping.containsKey(n1)){
               HashMap<String, Float> tmp  = S.get(n1);
                 if(eccentricity_distance_metric(tmp)>=thr){
                     String n2 = pickNode(tmp);
                     if(!tmpMapping.containsValue(n2)){
                         HashMap<String, Float> tmp2  = new HashMap<String, Float>();
                         tmp2  = S.get(n2);
                         if(eccentricity_distance_metric(tmp2)>=thr){
                             String n1prime = pickNode(tmp2);
                             if(n1prime.equals(n1)){
                                 tmpMapping.put(n1, n2);
                                 nodeMapping.put(n1, n2);
                             }
                         }
                     }
             }
         }
       }
       mapping.put(comm1, tmpMapping);
       System.out.print("The graphs has " + c1.getVertexCount() + " nodes!\n");
       System.out.print("\nThe number of nodes that could be mapped are " + mapping.get(comm1).size() + "...\n");
 }
 
 public Map<String, Float>  getDegreeList(Graph<String, Edge> c){
     Map<String, Float> degreeList = new HashMap<String, Float>();
   for(String node:  c.getVertices()){
        degreeList.put(node, (float) c.degree(node));
   }
   return (degreeList);
 }

 public HashMap<String, HashMap<String, Float>> init_S_Degree(Graph<String, Edge> c1, Graph<String, Edge> c2){

      HashMap<String, HashMap<String, Float>> S_degree = new HashMap<String, HashMap<String, Float>>();

      Map<String, Float> degreeList1 = getDegreeList( c1);
      Map<String, Float> degreeList2 = getDegreeList( c2);

      for(String node1 : c1.getVertices()){
        HashMap<String, Float> tmp = new HashMap<String, Float>();
        for(String node2 : c2.getVertices()){
            Float s = Math.abs(degreeList1.get(node1) - degreeList2.get(node2));
            tmp.put(node2, s);
        }
       S_degree.put(node1, tmp);
    }
      return(S_degree);
 }

 public void basedOnDegree(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
       System.out.print("Find seed nodes for community " + comm1 + " using degree distribution...\n");
       HashMap<String, HashMap<String, Float>> S1 = init_S_Degree(c1, c2);
       matching_nodes_comm(comm1, c1, c2, S1, thr);
 } 
public void basedOnDegree_overlappedSets(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
       System.out.print("Find seed nodes for community " + comm1 + " using degree distribution...\n");
       HashMap<String, HashMap<String, Float>> S1 = init_S_Degree(c1, c2);
       HashMap<String, HashMap<String, Float>> S2 = init_S_Degree(c2, c1);
       matching_nodes_comm_overlappedSets(comm1, c1, c2, S1,S2, thr);
 } 
  public HashMap<String, HashMap<String, Float>> init_S_CC(Graph<String, Edge> c1, Graph<String, Edge> c2){

      HashMap<String, HashMap<String, Float>> S_CC = new HashMap<String, HashMap<String, Float>>();

      Map<String, Double> ccList1 =  Metrics.clusteringCoefficients(c1);
      Map<String, Double> ccList2 =  Metrics.clusteringCoefficients(c2);

      for(String node1 : c1.getVertices()){
        HashMap<String, Float> tmp = new HashMap<String, Float>();
        for(String node2 : c2.getVertices()){
            Float s = (float) Math.abs(ccList1.get(node1) - ccList2.get(node2));
            tmp.put(node2, s);
        }
       S_CC.put(node1, tmp);
    }
      return(S_CC);
 }

 public void basedOnCC(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
    System.out.print("Find seed nodes for community " + comm1 + " using clustering coefficient...\n");
    HashMap<String, HashMap<String, Float>> S = init_S_CC(c1, c2);
    matching_nodes_comm(comm1, c1, c2, S, thr);
 }
  public void basedOnCC_overlappedSets(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
    System.out.print("Find seed nodes for community " + comm1 + " using clustering coefficient...\n");
    HashMap<String, HashMap<String, Float>> S = init_S_CC(c1, c2);
    HashMap<String, HashMap<String, Float>> S2 = init_S_CC(c2, c1);
    matching_nodes_comm_overlappedSets(comm1, c1, c2, S, S2, thr);
 }
 public void findSeedNodes(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
     System.out.print("Find seed nodes for community " + comm1 + "...\n");
     basedOnDegree(comm1, c1, c2, thr);
     basedOnCC(comm1, c1, c2, thr);    
 }
 public void findSeedNodes_overlappedSets(String comm1, Graph<String, Edge> c1, Graph<String, Edge> c2, Double thr){
     System.out.print("Find seed nodes for community " + comm1 + "...\n");
     basedOnDegree_overlappedSets(comm1, c1, c2, thr);
     basedOnCC_overlappedSets(comm1, c1, c2, thr);    
 } 
 
 public HashMap<String, HashMap<String, String>> community_based_nodeMatching(MyGraph g1,
         MyGraph g2, Double thr, String dir) throws IOException {

     Graph<String, Edge> tmp1 = g1.get_graph();
     Graph<String, Edge> tmp2 = g2.get_graph();
     HashMap<String, String> crr_mapped_comms = Community.get_crr_MatchedComms();
     System.out.print("Node mathcing after community matching...\n");
     for(String comm1 : cMapping.keySet()){
         String comm2 = cMapping.get(comm1);
         Graph<String, Edge> c1 = g1.get_commSubGraphs().get(comm1);
         Graph<String, Edge> c2 = g2.get_commSubGraphs().get(comm2);


         findSeedNodes(comm1,c1, c2, thr);

         if(mapping.containsKey(comm1)){
             Comm_Blind iso = new Comm_Blind(g1, g2, mapping.get(comm1));
             iso.g1(c1);
             iso.g2(c2);
          
       /*      String fName = "successRate_" + comm1 + "_" + comm2 + "-" + Config.noise + "-" + Config.exp;

             //prob(mapping a node|comm is mapped)
            float numSucc = iso.evaluation(mapping.get(comm1));               
            float perCorrMapped = (numSucc / c1.getVertexCount()) * 100;
            float perNotCorrMapped =  ((nodeMapping.size() - numSucc) / c1.getVertexCount()) * 100;

            int size = (c1.getVertexCount()/10)*10;
            String fileName1 = "crr_matched-nodes-in-comm_size-" + size + "-" +Config.noise + ".txt";
            FileWriter fw1 = new FileWriter( dir +fileName1, true);
            fw1.write(perCorrMapped + "\n");
            fw1.close();
            
            String fileName2 = "incrr_matched-nodes-in-comm_size-" + size + "-" +Config.noise + ".txt";
            FileWriter fw2 = new FileWriter( dir +fileName2, true);
            fw2.write(perNotCorrMapped + "\n");
            fw2.close();
            
              //prob(mapping a node|comm is mapped correctly )
             if(crr_mapped_comms.containsKey(comm1)){
                fileName1 = "crr_matched-nodes-in-crr_comm_size-" + size + "-" +Config.noise + ".txt";
                fw1 = new FileWriter( dir +fileName1, true);
                fw1.write(perCorrMapped + "\n");
                fw1.close();

                fileName2 = "incrr_matched-nodes-in-crr_comm_size-" + size + "-" +Config.noise + ".txt";
                fw2 = new FileWriter( dir +fileName2, true);
                fw2.write(perNotCorrMapped + "\n");
                fw2.close();
            }
*/
         }     
    }
     g1.put_graph(tmp1);
     g2.put_graph(tmp2);
     return(mapping);

 }
 public HashMap<String, String> get_overlappedNodes(MyGraph g1, MyGraph g2){
     HashMap<String, String> overlappedNodes = new HashMap<String, String>();
     for(String nodeName1: g1.get_NodesNames().keySet()){
         String nodeID1 = g1.get_NodesNames().get(nodeName1);
         if(g2.get_NodesNames().containsKey(nodeName1)){
           String nodeID2 = g2.get_NodesNames().get(nodeName1);
           overlappedNodes.put(nodeID1, nodeID2);
         }
     }
       return(overlappedNodes);  
 }
 public HashMap<String, HashMap<String, String>> community_based_nodeMatching_overlappedDataSets(MyGraph g1,
         MyGraph g2, Double thr, String dir) throws IOException {

     Graph<String, Edge> tmp1 = g1.get_graph();
     Graph<String, Edge> tmp2 = g2.get_graph();
     HashMap<String, String> overlappedNodes= get_overlappedNodes(g1, g2);
     
     System.out.print("Node mathcing after community matching...\n");
     int crr_mapping = 0;
     int incrr_mapping = 0;
     for(String comm1 : cMapping.keySet()){
         String comm2 = cMapping.get(comm1);
         Graph<String, Edge> c1 = g1.get_commSubGraphs().get(comm1);
         Graph<String, Edge> c2 = g2.get_commSubGraphs().get(comm2);
         
         findSeedNodes_overlappedSets(comm1,c1, c2, thr);
         
         /***Start: Just for test**/
         if(mapping.containsKey(comm1)){
             for(String nodeID: mapping.get(comm1).keySet()){
                 if(overlappedNodes.containsKey(nodeID)){
                     String nodeID2 = overlappedNodes.get(nodeID);
                     String mappedID = mapping.get(comm1).get(nodeID);
                     if(mappedID.equals(nodeID2)){
                         crr_mapping++;
                         System.out.print("correct mapping...\n");
                     }
                  } else incrr_mapping++;
             }
         }
         /***Stop: Just for test**/
         if(mapping.containsKey(comm1)){
             Comm_Blind iso = new Comm_Blind(g1, g2, mapping.get(comm1));
            // is.map();
             iso.g1(c1);
             iso.g2(c2);
          
             //Local Mapping:
             String fName = "successRate_" + comm1 + "_" + comm2 + "-" + Config.noise + "-" + Config.exp;
             HashMap<String, String> map = iso.NS(c1,c2, thr, true, dir + fName);//false
             nodeMapping.putAll(map);
             if(!map.isEmpty()){
                 mapping.remove(comm1);
                 mapping.put(comm1, map);
             }
             
         }     
    }
     g1.put_graph(tmp1);
     g2.put_graph(tmp2);
     return(mapping);

 }
     public HashMap<String, String> map_comms_basedOn_KnownSeeds_(MyGraph g1, MyGraph g2){
         HashMap<String, String> seedComms1 = new HashMap<String, String>();
       //Find all possible matches based on known seeds
       Hashtable<String, Hashtable<String, Integer>> mappedCommSpace = new Hashtable<String, Hashtable<String, Integer>>();
       for(ArrayList<String> cl : g1.get_seedCliques()){
           for(String seed: cl){
               if(!seedComms1.containsKey(seed)){
                    seedComms1.put(seed, g1.getNodeComm(seed));
               }
               else
                   System.out.print("\nseed " + seed + " exists!!");
               String comm1 = g1.getNodeComm(seed);
               String comm2 = g2.getNodeComm(g1.seeds.get(seed));
               Hashtable<String, Integer> tmp = new Hashtable<String, Integer>();
               if(mappedCommSpace.containsKey(comm1))
                   tmp = mappedCommSpace.get(comm1);
               int count = 0;
               if(tmp.containsKey(comm2))
                   count = tmp.get(comm2);
               tmp.put(comm2, ++count);
               mappedCommSpace.put(comm1, tmp);
           }
       }
       //match communities: 1) order based on scores
       HashMap<String, String> mappedComm = new HashMap<String, String>();
       int count = 0;
       for(String comm1: mappedCommSpace.keySet()){
           ArrayList<Map.Entry<?, Integer>> orderedCommsList = Misc.sortValue_Integer(mappedCommSpace.get(comm1));
           int index = orderedCommsList.size()-1;
           String comm2 = orderedCommsList.get(index).getKey().toString();
           if(orderedCommsList.size()>1){
               int val1 = orderedCommsList.get(index).getValue();
               int val2 = orderedCommsList.get(index-1).getValue();
               if(val1 == val2)
                   count++;
           }
           mappedComm.put(comm1, comm2);
        }

       for(String comm1: mappedComm.keySet()){
          for(String seed: seedComms1.keySet()){
            if(seedComms1.get(seed).equals(comm1)){
               HashMap<String, String> commSeeds = new HashMap<String, String>();
               if(mapping.containsKey(comm1))
                   commSeeds = mapping.get(comm1);
               commSeeds.put(seed, g1.seeds.get(seed));
               nodeMapping.putAll(commSeeds);
               mapping.put(comm1,commSeeds);
              }
           }
         }

       return(mappedComm);
    }


    public void communityMapping_knownSeeds(MyGraph g1, MyGraph g2){

         cMapping = new HashMap<String, String>();

         cMapping.putAll(map_comms_basedOn_KnownSeeds_(g1, g2));
         
         Comm_Blind is = new Comm_Blind(g1, g2, cMapping);
         System.out.print("Community mathcing based on NS alg...\n");
         cMapping = is.NS_comm(g1, g2, Config.thr_comm_ecc, Config.dir
                + "comm_successRate" + Config.noise + "-" + Config.thr_comm_ecc );
    }
    public void findSeedsComms(HashMap<String, String> seeds){
        for(String n1: seeds.keySet()){
                    if(g1.get_nodeComm().containsKey(n1)){
                        String c1 = g1.get_nodeComm().get(n1);
                        HashMap<String, String> tmp = new HashMap<String, String>();
                        if(mapping.containsKey(c1))
                            tmp = mapping.get(c1);
                        tmp.put(n1, seeds.get(n1));
                        nodeMapping.putAll(tmp);
                        mapping.put(c1, tmp);
                    }
                 }     
    }
    public HashMap<String, HashMap<String, String>> reIdentifyRemainingNodes(String dir){
        HashMap<String, String> mapTmp =  new HashMap<String, String>();
        for(String comm:mapping.keySet()){
            mapTmp.putAll(mapping.get(comm));
        }
        Comm_Blind is = new Comm_Blind(g1, g2, mapTmp);
        mapTmp = is.NS(g1.get_graph(), g2.get_graph(), Config.thr_node_ecc, false, dir + "successRate-" + Config.noise + "-" + Config.exp);
        for(String n1: mapTmp.keySet()){
            if(g1.get_nodeComm().containsKey(n1)){
                String c1 = g1.get_nodeComm().get(n1);
                HashMap<String, String> tmp = new HashMap<String, String>();
                if(mapping.containsKey(c1))
                    tmp = mapping.get(c1);
                tmp.put(n1, mapTmp.get(n1));
                nodeMapping.putAll(tmp);
                mapping.put(c1, tmp);
            }
        }
        return(mapping);
    }
   public HashMap<String, HashMap<String, String>> reIdentifyRemainingNodes_overlappedDataSets(String dir, int seeds){
        HashMap<String, String> mapTmp =  new HashMap<String, String>();
        for(String comm:mapping.keySet()){
            mapTmp.putAll(mapping.get(comm));
        }
        Comm_Blind is = new Comm_Blind(g1, g2, mapTmp);
        mapTmp = is.NS_overlappedDataSets(g1.get_graph(), g2.get_graph(), Config.thr_node_ecc, false, dir + "successRate-" + Config.noise + "-" + (int) seeds/4);
        for(String n1: mapTmp.keySet()){
            if(g1.get_nodeComm().containsKey(n1)){
                String c1 = g1.get_nodeComm().get(n1);
                HashMap<String, String> tmp = new HashMap<String, String>();
                if(mapping.containsKey(c1))
                    tmp = mapping.get(c1);
                tmp.put(n1, mapTmp.get(n1));
                nodeMapping.putAll(tmp);
                mapping.put(c1, tmp);
            }
        }
        return(mapping);
    }
    public void communityAware_deAnon_alg(String[] args){
              
         try {
            Config config = new Config(args);//, "results-comm-prob"
            System.out.println("Reading cliques...");
            g1.read_cliques(args[6]);
            
            System.out.print("Read known seeds...\n");
            HashMap<String, String> map = new HashMap<String, String>();
            Comm_Blind is = new Comm_Blind(g1, g2, map);
            is.map.putAll(g1.get_random_cliques_of_size(4, config.num_seeds / 4));
            
            g1.initCommunities(args[4]);
            g2.initCommunities(args[5]);
            
            //Matching Communitites based on comm of known seeds
            communityMapping_knownSeeds(g1, g2);
            
            //feed NS alg with seeds inside each community and map nodes of already mapped communities
            community_based_nodeMatching(g1, g2, config.thr_node_ecc, config.dir);
            
            /*
            * Some communities are not mapped and so the algorithm does not going
            * inside of them for matching. Instead after matching nodes in the
            * already mapped communities, the algorithm uses the kaggle algorithm where
            * all mapped nodes are used as seeds and try to map remaining nodes
            *
            */
            reIdentifyRemainingNodes(config.dir);
            g1.seeds.putAll(nodeMapping);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Comm_Enhanced.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public void communityAware_deAnon_alg_overlappedNodeSets(String[] args){
          try { 
            Config config = new Config(args); //, "results-comm-overlapped-prob-"
            System.out.println("Reading cliques...");
            g1.get_overlapped_cliques(args[6], g2);
           
            System.out.print("Read known seeds...\n");
            HashMap<String, String> map = new HashMap<String, String>();
            Comm_Blind is = new Comm_Blind(g1, g2, map);
            is.map.putAll(g1.get_random_OverlappedCliques_of_size(4, config.num_seeds/4, g2));//numCliques
            
            g1.initCommunities(args[4]);
            g2.initCommunities(args[5]);
            //Matching Communitites based on comm of known seeds
            communityMapping_knownSeeds(g1, g2);
            //feed NS alg with seeds inside each community and map nodes of already mapped communities
            community_based_nodeMatching_overlappedDataSets(g1, g2, config.thr_node_ecc, config.dir);
            /*
            * Some communities are not mapped and so the algorithm does not going
            * inside of them for matching. Instead after matching nodes in the
            * already mapped communities, the algorithm uses the kaggle algorithm where
            * all mapped nodes are used as seeds and try to map remaining nodes
            *
            */
            reIdentifyRemainingNodes_overlappedDataSets(config.dir, config.num_seeds);
           
            g1.seeds.putAll(nodeMapping);
          
          
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comm_Enhanced.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Comm_Enhanced.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   public static void main(String[] args) throws IOException {

        try {  
        
            Date date= new java.util.Date();
            Timestamp start = new Timestamp(date.getTime());
         
            if(args.length < 9){
                System.out.println("Usage: de-anonymization ResultsDirectory NumSeeds "
                        + "Noise Exp FileName1.net FileName2.net FileName.clique Prob/DegAnon(False/True) Overlapped(False/True) ");
                System.out.println("Example: comm-aware 16 0.2 1 data/cond-mat-2005-lcc data/noisy-cond-mat-2005-lcc-0.2-1 data/clique.ref  False False");
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
           
            // Graph Initialization
            MyGraph g1 = new MyGraph(args[4]);
            MyGraph g2 = new MyGraph(args[5]);
            boolean overlapped = Boolean.parseBoolean(args[8]);

            Comm_Enhanced cbm = new Comm_Enhanced(g1, g2);
            if (overlapped){
                cbm.communityAware_deAnon_alg_overlappedNodeSets(args);
                
            }else{
                cbm.communityAware_deAnon_alg(args);
                
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
            Simplified_Degree_Anonymity_CommAware deg = new 
                Simplified_Degree_Anonymity_CommAware(cbm.nodeMapping, cbm.cMapping, cbm.g1, cbm.g2);
            if (!prob_deg)
                deg.compute_and_print_probabilities(Config.dir, "/comm-simple-prob-"+ Config.noise + ".txt");
            else{
                String fileName_avgProb = args[9]; 
                deg.print_degree_of_anonymity(Config.dir + "/" + fileName_avgProb, "comm-simple-deg-" + Config.noise, "comm-simple-prob-"+ Config.noise + ".txt");
                
            }
            
            //Degree_Anonymity_CommAware deg2 = new Degree_Anonymity_CommAware(cbm.nodeMapping, cbm.cMapping, g1, g2);
            //if (!prob_deg)
                //deg2.compute_and_print_probabilities(Config.dir_seed, "comm-prob-"+ Config.noise + ".txt");
            //else
                // deg2.print_degree_of_anonymity(Config.dir_seed, "comm-deg-" + Config.noise, "comm-prob-"+ Config.noise + ".txt");
           
            date= new java.util.Date();
            Timestamp stop = new Timestamp(date.getTime());
            FileWriter fw = new FileWriter(Config.dir + "time-" + Config.noise + ".txt", true);
            fw.write("Start time: " + start + "\n");
            fw.write("Stop time: " + stop + "\n");
            fw.close();
            System.out.println("Start time: " + start + "\n");
            System.out.println("Stop time: " + stop + "\n");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comm_Enhanced.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Comm_Enhanced.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}


