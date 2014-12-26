/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.FactoryUtils;

/**
 *
 * @author shirnili
 */
public class MyGraph {    
    private Graph<String, Edge> graph;
    private HashMap<String, String> nodeComm; //<nodeID, cID>
    private HashMap<String, Vector<String>> commNodes; //<cID, Vector<nodeID>>
    private HashMap<String, HashMap<String, Float>> edgesWeights;
    private HashMap<String, String> NodesNames = new HashMap<String, String> ();//the first String is the name of node and the second string is the id
    private HashMap<String, String> NodesIds = new HashMap<String, String> ();
    private Map<String, Double> ccMap;
    private Graph<String, Edge> commGraph;
    private HashMap<String , Graph<String, Edge>> commSubGraphs;
    private Hashtable<String, List<Clique_Sequence>> commCliques =
            new Hashtable<String, List<Clique_Sequence>> ();
    private ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> seedCliques = new ArrayList<ArrayList<String>>();
    private ArrayList<String> knownSeeds = new ArrayList<String>();
    public HashMap<String, String> seeds = new HashMap<String, String>();
    private Random rnd = new Random(2308952); //12045952
    

    public MyGraph(String filename){
        System.out.print("Graph initialization...\n");
        CreateUndirectedJungGraph(filename + ".net");
        init_clusteringCoefficient();

    }
    public MyGraph(){
    }
    public void initCommunities(String filename){
        System.out.print("Community initialization...\n");
        initNodesAndCommunities_infomap(filename + ".tree");
        System.out.print("Creating graph of communities...\n");
        createGraphOfCommunities();
        initCommSubGraphs();
    }
    
     private void CreateUndirectedJungGraph(String filename) {
        try {
            graph = new UndirectedSparseGraph<String, Edge>();
            NodesNames = new HashMap<String, String>();
            NodesIds = new HashMap<String, String>();
            File file1 = new File(filename);
            FileInputStream fstream1 = new FileInputStream(file1);
            DataInputStream in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            String Oneline;
            String splitter = " ";
            //Read File Line By Line
            Oneline = br1.readLine();
            while (Oneline != null) {
                String[] words = Oneline.split(splitter);
                if (words[0].equalsIgnoreCase("*Vertices")) {
                    Oneline = br1.readLine();
                    StringTokenizer st = new StringTokenizer(Oneline);
                    words[0] = st.nextToken();
                    while (!words[0].equalsIgnoreCase("*Edges")) {
                        words[1] = st.nextToken();
                        //Node v = new Node(Integer.parseInt(words[0]), words[1]);
                        if(words[1].startsWith("\"") && words[1].endsWith("\""))
                            words[1] = words[1].substring(1, words[1].length() - 1);
                        NodesNames.put(words[1], words[0]);
                        NodesIds.put(words[0], words[1]);
                        graph.addVertex(words[0]);
                        Oneline = br1.readLine();
                        st = new StringTokenizer(Oneline);
                        words[0] = st.nextToken();
                    }
                    Oneline = br1.readLine();
                    while (Oneline != null) {
                        st = new StringTokenizer(Oneline);
                        words[0] = st.nextToken();
                        words[1] = st.nextToken();
                        String edgeName = words[0] + "_" + words[1];
                        String edgeName2 = words[1] + "_" + words[0];
                        Edge e = new Edge(edgeName, 1);
                        Edge e2 = new Edge(edgeName2, 1);
                        if (!graph.containsEdge(e) && !graph.containsEdge(e2)) {
                            graph.addEdge(e, words[0], words[1], EdgeType.UNDIRECTED);
                        }
                        Oneline = br1.readLine();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
       public static Graph getGraph(String FileName) throws IOException
    {
        PajekNetReader pnr = new PajekNetReader(FactoryUtils.instantiateFactory(Object.class));
        Graph g = new UndirectedSparseGraph();

        pnr.load(FileName, g);
        return g;
    }
     public void initNodesAndCommunities_infomap(String fileName) {
        try {
            commNodes = new HashMap<String, Vector<String>>();
            nodeComm = new HashMap<String, String>();
            File file1 = new File(fileName);
            FileInputStream fstream1 = new FileInputStream(file1);
            DataInputStream in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            //file1: (cid:? ? "nodeID") 1:1 0.000199911 "4283"
            String Oneline = Oneline = br1.readLine();
            while ((Oneline = br1.readLine()) != null) {
                String splitter = ":";
                String[] ids = Oneline.split(splitter);
                String cid = ids[0];
                splitter = " ";
                ids = Oneline.split(splitter);
                String nodeID = ids[2];
                nodeID = nodeID.substring(1, nodeID.length() - 1);
                nodeID = NodesNames.get(nodeID);
                Vector<String> nodes = new Vector<String>();
                if (commNodes.containsKey(cid)) {
                    nodes = commNodes.get(cid);
                }
                nodes.add(nodeID);
                commNodes.put(cid, nodes);
           //     Vector<String> cids = new Vector<String>();
            //    cids.add(cid);
                nodeComm.put(nodeID, cid);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public ArrayList<HashMap<String, Vector<String>>> initNodesAndCommunities_metis(String fileName) {
        ArrayList<HashMap<String, Vector<String>>> output = new ArrayList<HashMap<String, Vector<String>>>();
        try {
            HashMap<String, Vector<String>> commNodes = new HashMap<String, Vector<String>>();
            HashMap<String, Vector<String>> nodeComm = new HashMap<String, Vector<String>>();
            File file1 = new File(fileName);
            FileInputStream fstream1 = new FileInputStream(file1);
            DataInputStream in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            //line i includes neighbors of node i
            String cid = br1.readLine();
            int nodeID = 1;
            while (cid != null) {
                Vector<String> nodes = new Vector<String>();
                if (commNodes.containsKey(cid)) {
                    nodes = commNodes.get(cid);
                }
                nodes.add(nodeID + "");
                commNodes.put(cid, nodes);
                Vector<String> cids = new Vector<String>();
                cids.add(cid);
                nodeComm.put(nodeID + "", cids);
                cid = br1.readLine();
                nodeID++;
            }
            output.add(commNodes);
            output.add(nodeComm);
            
        } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
     private void putWeight(String cid1, String cid2){
        float count = 0;
        HashMap<String, Float> tmp = new HashMap<String, Float>();
        if(edgesWeights.containsKey(cid1)){
            tmp = edgesWeights.get(cid1);
            if(tmp.containsKey(cid2))
                count = tmp.get(cid2);
        }
        tmp.put(cid2, ++count);
        edgesWeights.put(cid1, tmp);
     }
   
     private void updateWeights(){
         for(String c1: edgesWeights.keySet()){
             double sum = 0;
             HashMap<String, Float> tmp = new HashMap<String, Float>();
          
             for(String c2: edgesWeights.get(c1).keySet()){                 
                // System.out.println(c1 + ", " + c2);
                 double count = edgesWeights.get(c1).get(c2);
                 double weight = count;
                 sum = sum + weight;
             }
          }
     }
     private void createGraphOfCommunities(){

         commGraph = new UndirectedSparseGraph<String, Edge>();
         Vector<String> edgesVec = new Vector <String>();
         edgesWeights= new HashMap<String, HashMap<String, Float>>();
          Vector<String> nodes = new Vector<String>();          

         for(String comm: commNodes.keySet()) {
             if(!commGraph.containsVertex(comm))
                    commGraph.addVertex(comm);
         }
     
         for(String node: graph.getVertices()){
             if(nodeComm.get(node) != null){
                 String cid1 = nodeComm.get(node);
                 nodes.add(node);
               //  if(!commGraph.containsVertex(cid1))
                 //   commGraph.addVertex(cid1);
                 ArrayList<String> nghbrs = new ArrayList();
                 if(graph.getNeighbors(node)!= null)
                    nghbrs = new ArrayList(graph.getNeighbors(node));
                 for(String nbr: nghbrs){
                    if(!nodes.contains(nbr)){
                        if(nodeComm.get(nbr) != null){
                            String cid2 = nodeComm.get(nbr);
                           // if(!commGraph.containsVertex(cid2))
                             //   commGraph.addVertex(cid2);
                            if(!cid2.equals(cid1)){                                   
                                String edgeName1 = cid1 + "_" + cid2;
                                String edgeName2 = cid2 + "_" + cid1;
                                Edge e1 = new Edge(edgeName1, 1);
                                if(!edgesVec.contains(edgeName1) && !edgesVec.contains(edgeName2)){
                                    edgesVec.add(edgeName1);
                                    edgesVec.add(edgeName2);
                                    commGraph.addEdge(e1, cid1, cid2, EdgeType.UNDIRECTED);
                                    if(commGraph.getVertexCount()%100 == 0)
                                         System.out.println("communities: "+ commGraph.getVertexCount() + ", Edges: " + commGraph.getEdgeCount());
                                }
                                putWeight(cid1, cid2);
                                putWeight(cid2, cid1);
                             }
                        }
                    }
                 }
                 
             }
         }
         updateWeights();
     }
    
    private void initCommSubGraphs(){

        commSubGraphs = new HashMap<String,Graph<String, Edge>>();
        for(String cid : commNodes.keySet()){
                commSubGraphs.put(cid, createSubGraph_2(cid, commNodes, graph));
        }        
    }

public Graph<String, Edge> createSubGraph_2(String comm,
            HashMap<String, Vector<String>> commNodes, Graph<String, Edge> g){

         Graph<String, Edge> sg = new UndirectedSparseGraph<String, Edge>();

         for(String node : commNodes.get(comm)){
             if( node != null)
                sg.addVertex(node);
        }         
         for (String node1 : sg.getVertices()){
             if(g.containsVertex(node1)){
                 Collection<String> nbrs = g.getNeighbors(node1);
                 for(String node2: nbrs){
                     if(sg.containsVertex(node2)){                         
                        Edge e1 = new Edge(node1 + "_" + node2, 1.0);
                        Edge e2 = new Edge(node2 + "_" + node1, 1);
                        if(!sg.containsEdge(e1))
                             sg.addEdge(e1, node1, node2, EdgeType.UNDIRECTED);
                        else if(!sg.containsEdge(e2))//g.containsEdge(e2) && !sg.containsEdge(e2)){
                             sg.addEdge(e2, node2, node1, EdgeType.UNDIRECTED);
                     }
                 }
             }
        }        
         return(sg);
    }
 public ArrayList<ArrayList<String>> read_cliques(String fileName) throws FileNotFoundException{
       // ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
        try {
            FileInputStream fstream1 = new FileInputStream(fileName);
            DataInputStream in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            String splitter = ", ";
            String Oneline = br1.readLine();
            while (Oneline != null) {
                ArrayList<String> ids = new ArrayList<String>();
                String[] names = Oneline.split(splitter);
                for(String name : names){
                    ids.add(NodesNames.get(name));
                }
                cliques.add(ids);
                Oneline = br1.readLine();
            }
            in1.close();
            br1.close();
            fstream1.close();
        } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(cliques);
    }
 public ArrayList<ArrayList<String>> get_overlapped_cliques(String fileName, MyGraph g2) throws FileNotFoundException{
       
        try {
            FileInputStream fstream1 = new FileInputStream(fileName);
            DataInputStream in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            String splitter = ", ";
            String Oneline = br1.readLine();
            while (Oneline != null) {
                ArrayList<String> ids = new ArrayList<String>();
                String[] names = Oneline.split(splitter);
                for(String name : names){
                    //if this node also appears in g2
                    if(g2.NodesNames.containsKey(name))
                        ids.add(NodesNames.get(name));
                }
                //some cliques overlap but not completely, I decied to also consider them
            //    if(names.length == ids.size()) //if we want to have exact same size for our ovelapped cliques
                if(ids.size()>3)
                    cliques.add(ids);
                Oneline = br1.readLine();
            }
            in1.close();
            br1.close();
            fstream1.close();
        } catch (IOException ex) {
            Logger.getLogger(MyGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(cliques);
    }
 public ArrayList<ArrayList<String>> get_cliques_of_size(int k){
    ArrayList<ArrayList<String>> cliquesSizeK = new ArrayList<ArrayList<String>>();
    for(ArrayList<String> clique : cliques)
        if(clique.size() == k){
            cliquesSizeK.add(clique);
        }
    return(cliquesSizeK);
 }
  public HashMap<String, String> get_random_cliques_of_size(int k, int n){
    //HashMap<String, String> seeds = new HashMap<String, String>();
    ArrayList<ArrayList<String>> cliquesSizeK = get_cliques_of_size(k);
    ArrayList<Integer> visited = new ArrayList<Integer>();
    int index = 0;
    for(int i=0;i<n;i++){
        while(visited.contains(index))
            index = rnd.nextInt(cliquesSizeK.size());
        ArrayList<String> cl = cliquesSizeK.get(index);
        seedCliques.add(cl);
        for(String node: cl){
            knownSeeds.add(node);
            seeds.put(node, node);
        }
        visited.add(index);
    }
        return(seeds);
    }
  public HashMap<String, String> get_random_OverlappedCliques_of_size(int k, int n, MyGraph g2){
    
    ArrayList<ArrayList<String>> cliquesSizeK = get_cliques_of_size(k);
    ArrayList<Integer> visited = new ArrayList<Integer>();
    int index = 0;
    for(int i=0;i<n;i++){
        while(visited.contains(index))
            index = rnd.nextInt(cliquesSizeK.size());
        ArrayList<String> cl = cliquesSizeK.get(index);
        seedCliques.add(cl);
        for(String node: cl){
            knownSeeds.add(node);
            String nodeName = NodesIds.get(node);
            String nodeId_g2 = g2.NodesNames.get(nodeName);
            seeds.put(node, nodeId_g2);
        }
        visited.add(index);
    }

    return(seeds);
    }

    private void init_clusteringCoefficient(){
        ccMap = Metrics.clusteringCoefficients(graph);

    }
    public Graph<String, Edge> getGraph(){
        return graph;
    }

    public String getNodeId(String name){
        return NodesNames.get(name);
    }

     public String getNodeName(String id){
        return NodesIds.get(id);
    }
     public HashMap<String, String> getNodeNames(){
        return NodesNames;
    }
     public HashMap<String, String> getNodeIds(){
        return NodesIds;
    }
    public String getNodeComm(String node){
        return nodeComm.get(node);
    }
    public List<Clique_Sequence> getCommCliques(String comm){
        return commCliques.get(comm);
    }
     public void putCommCliques(String comm, List<Clique_Sequence> seq){
        commCliques.put(comm, seq);
    }
    public String pick_a_node(HashSet<String> tmp_mapped_g, Random rand){
        ArrayList<String> nodeIDs = new ArrayList<String>();
        if(nodeComm.isEmpty() || nodeComm == null) nodeIDs.addAll(NodesIds.keySet());
        else    nodeIDs.addAll(nodeComm.keySet());
        
        int index = rand.nextInt(nodeIDs.size());
        String node = nodeIDs.get(index);
        
        while(tmp_mapped_g.contains(node)){
            index = rand.nextInt(nodeIDs.size());
            node = nodeIDs.get(index);
        }
        return(node);
    }
      public String pick_a_comm(HashSet<String> tmp_cMapped_g, Random rand){
        ArrayList<String> commIDs = new ArrayList<String>();
        commIDs.addAll(commNodes.keySet());
        
        int index = rand.nextInt(commIDs.size());
        String comm = commIDs.get(index);
        
        while(tmp_cMapped_g.contains(comm)){
            index = rand.nextInt(commIDs.size());
            comm = commIDs.get(index);
        }
        return(comm);
    }
    public void save_Graph_in_Pajek(Graph<String, Edge> g, String fileName){
        try{               
              File file = new File(fileName + ".net");
              FileWriter fw = new FileWriter(file, false);
              fw.write("*vertices "+ g.getVertexCount() + "\n");
               List<Integer> list = new ArrayList<Integer>();
              for(String v: g.getVertices())
                list.add(Integer.parseInt(v));
              Collections.sort( list );
              for(Integer v: list)
                 fw.write( list.indexOf(v)+1 + " " +  v +"\n");

              fw.write("*edges "+ g.getEdgeCount()+ "\n");
              Map<Integer, Map<Integer, String>> map = new HashMap<Integer, Map<Integer, String>>();
              for(Edge e: g.getEdges()){
                  String[] vs = e.getId().split("_");
                  Map<Integer, String> tmp = new HashMap<Integer, String>();
                  int v1 = Integer.parseInt(vs[0]);
                  int v2 = Integer.parseInt(vs[1]);
                  if(map.containsKey(list.indexOf(v1)+1))
                      tmp.putAll(map.get(list.indexOf(v1)+1));
                  tmp.put(list.indexOf(v2)+1, null);
                  map.put(list.indexOf(v1)+1, tmp);
              }
              Map<Integer, Map<Integer, String>> sortedMap = new TreeMap<Integer, Map<Integer, String>>(map);
               for(Integer v0: sortedMap.keySet()){
                   Map<Integer,  String> nbrs = sortedMap.get(v0);
                   Map<Integer,  String> sortedNbrs = new TreeMap<Integer, String>(nbrs);
                   for(Integer v1: sortedNbrs.keySet())
                        fw.write( (v0) + " " + (v1) +"\n");
              }
              fw.close();

          }catch (Exception e){//Catch exception if any
               System.err.println("Error in graphToPajek: " + e.getMessage());
          }
   }
     public HashMap<String, Vector<String>> get_commNodes(){
         return(commNodes);
     }
     public Graph<String, Edge> get_graph(){
         return graph;
     }
     public HashMap<String, String> get_nodeComm(){
         return nodeComm;
     }
     public ArrayList<ArrayList<String>> get_seedCliques(){
         return seedCliques;
     }
     public Graph<String, Edge> get_commGraph(){
         return commGraph;
     }
     public HashMap<String , Graph<String, Edge>> get_commSubGraphs(){
         return(commSubGraphs);
     }
     public void put_graph(Graph<String, Edge> graph){
      this.graph =  graph;
     }
     public HashMap<String, HashMap<String, Float>> get_edgesWeights(){
         return edgesWeights;
     }
     public Hashtable<String, List<Clique_Sequence>> get_commCliques(){
         return commCliques;
     }
     public HashMap<String, String> get_NodesNames(){
         return NodesNames;
     }
   public void set_commNodes(HashMap<String, Vector<String>> commNodes){
           this.commNodes = commNodes;
   }
   public void set_nodeComms(HashMap<String, String> nodeComms){
           this.nodeComm = nodeComms;
   }
   
}
