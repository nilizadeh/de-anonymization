/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.matching.Edge;
import graph.matching.MyGraph;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class OverlappedGraphs {
    private Graph<String, Edge> graph;
    private Random rnd = new Random(2308952); 
    
    public OverlappedGraphs(String filename){
        System.out.print("Graph initialization...\n");
        CreateUndirectedJungGraph(filename);
    }
    private void CreateUndirectedJungGraph(String filename) {
        try {
            graph = new UndirectedSparseGraph<String, Edge>();
            
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
    public ArrayList<Set<String>> partition_vertices(ArrayList<String> vertices, double overlap_parameter)
     { 
         /* Partitining is based on the algorithm described in section 6.2.1 of
          paper "De-anonymizing Social Networks"
         */
         
         ArrayList<Set<String>> v1_v2 = new ArrayList<Set<String>> ();
         Set<String> overlappedSet = new HashSet<String>();
         Set<String> v1 = new HashSet<String>();
         Set<String> v2 = new HashSet<String>();
         
         int size_overllappedSet = (int) (vertices.size() * overlap_parameter);
         int size_v1 = (int) (vertices.size() * ((1 - overlap_parameter)/2));
         
         Random rnd = new Random();
         
         while(overlappedSet.size()< size_overllappedSet){
             String node = vertices.get(rnd.nextInt(vertices.size()));
             while(overlappedSet.contains(node))
                 node = vertices.get(rnd.nextInt(vertices.size()));
             overlappedSet.add(node);
         }
         while(v1.size()< size_v1){
             String node = vertices.get(rnd.nextInt(vertices.size()));
             while(overlappedSet.contains(node) || v1.contains(node))
                 node = vertices.get(rnd.nextInt(vertices.size()));
             v1.add(node);
         }
         for(String node: vertices){
             if(!overlappedSet.contains(node) && !v1.contains(node))
                 v2.add(node);
         }
         
         v1.addAll(overlappedSet);
         v2.addAll(overlappedSet);
         v1_v2.add(v1);
         v1_v2.add(v2);
         
         return(v1_v2);
     }
     public ArrayList<Edge> deleteRandomEdges(ArrayList<Edge> E, double edgeOverlap){
         ArrayList<Edge> Eprime = new ArrayList<Edge>();
         double num_deletes = E.size()* ((1- edgeOverlap)/(1 + edgeOverlap));
         ArrayList<Edge> deletedEdges = new ArrayList<Edge>();
         
         while(deletedEdges.size() < num_deletes){
             int index = rnd.nextInt(E.size());
             Edge e = E.get(index);
             if(!deletedEdges.contains(e))
                 deletedEdges.add(e);
         }
         
         for(Edge e: E)
             if(!deletedEdges.contains(e))
                 Eprime.add(e);
         
         return(Eprime);
     }
     public void generate_two_overlapping_subGraphs(double nodeOverlap, double edgeOverlap, String fileName1, String fileName2)
        {
            Graph<String, Edge> g1 = new UndirectedSparseGraph<String, Edge>();
            Graph<String, Edge> g2 = new UndirectedSparseGraph<String, Edge>();
            
            ArrayList<String> vertices = new ArrayList<String>(graph.getVertices());
            ArrayList<Set<String>> v1_v2 = partition_vertices(vertices, nodeOverlap);
            Set<String> V1 = v1_v2.get(0);
            Set<String> V2 = v1_v2.get(1);
            /* Procedure B in section 6.2.1 of de-anonymization of social network
            Edge perturbing algorithm that mimics link prediction algorithm
            */
            //Make two copies of E and independently delete edges at random from each copy.
            ArrayList<Edge> E = new ArrayList<Edge>(graph.getEdges());
            ArrayList<Edge> E1 = deleteRandomEdges(E, edgeOverlap);
            ArrayList<Edge> E2 = deleteRandomEdges(E, edgeOverlap);
            //project E1 on V1
            for(Edge edge: E1){
                String edgeID = edge.getId();
                String[] u1_u2 = edgeID.split("_");
                if(V1.contains(u1_u2[0]) && V1.contains(u1_u2[1]))
                    g1.addEdge(edge, u1_u2[0], u1_u2[1], EdgeType.UNDIRECTED);
            }
            //project E2 on V2
            for(Edge edge: E2){
                String edgeID = edge.getId();
                String[] u1_u2 = edgeID.split("_");
                if(V2.contains(u1_u2[0]) && V2.contains(u1_u2[1]))
                    g2.addEdge(edge, u1_u2[0], u1_u2[1], EdgeType.UNDIRECTED);
            }
            save_Graph_in_Pajek(g1, fileName1 + "-" + nodeOverlap +  "-" + edgeOverlap);
            save_Graph_in_Pajek(g2, fileName2 +  "-" + nodeOverlap +  "-" + edgeOverlap);
     }
  
     public static void main(String[] args) throws Exception {
         if(args.length < 4){
                System.out.println("Usage: generateOverlappedGraphs Dir FileName.net %NodeOverlap(e.g. 0.05) %EdgeOverlap(e.g. 0.05)"); 
                System.exit(1);
         }

          OverlappedGraphs OG = new OverlappedGraphs(args[0] + "/" + args[1]);
          double nodeOverlap = Double.parseDouble(args[2]);
          double edgeOverlap = Double.parseDouble(args[3]);
          OG.generate_two_overlapping_subGraphs(nodeOverlap, edgeOverlap, args[1] + "-1", args[1] + "-2");
     }
}
