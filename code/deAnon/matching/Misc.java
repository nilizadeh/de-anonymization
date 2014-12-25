/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import deAnon.matching.Edge;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shirnili
 */
public class Misc {

    Random rnd;

    public Misc(){
        rnd = new Random(120034);
    }

    public ArrayList<Map.Entry<?, Double>> sortValue_Double(Hashtable<?, Double> t){

       //Transfer as List and sort it
       ArrayList<Map.Entry<?, Double>> l = new ArrayList(t.entrySet());
       Collections.sort(l, new Comparator<Map.Entry<?, Double>>(){

         public int compare(Map.Entry<?, Double> o1, Map.Entry<?, Double> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }});

       return(l);
    }
    public static ArrayList<Map.Entry<?, Integer>> sortValue_Integer(Hashtable<?, Integer> t){

       //Transfer as List and sort it
       ArrayList<Map.Entry<?, Integer>> l = new ArrayList(t.entrySet());
       Collections.sort(l, new Comparator<Map.Entry<?, Integer>>(){

         public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }});

       return(l);
    }    
     public double jaccard_index(Vector<String> S1, Vector<String> S2){
         Set<String> intersection = new HashSet<String>();
         intersection.addAll(S1);
         Set<String> union  = new HashSet<String>();
         union.addAll(S1);
         double jaccard = 0;

         intersection.retainAll(S2);
         union.addAll(S2);
         jaccard = intersection.size()/(double) union.size();

         return(jaccard);
     }
     
    public Graph<String, Edge> copyGraph(Graph<String, Edge> in, Graph<String, Edge> out){
      out = new UndirectedSparseGraph<String, Edge>();
      for (String v : in.getVertices())
            out.addVertex(v);

       for (Edge e : in.getEdges())
            out.addEdge(e, in.getIncidentVertices(e));
     return(out);
 }

    public int countLines(String fileName) throws IOException {
        LineNumberReader reader  = new LineNumberReader(new FileReader(fileName));
        int cnt = 0;
        String lineRead = "";
        while ((lineRead = reader.readLine()) != null) {}

        cnt = reader.getLineNumber();
        reader.close();
        return cnt;
    }
    public String getBaseOfFileName (String name){
        int dot = name.lastIndexOf('.');
        String base = (dot == -1) ? name : name.substring(0, dot);
      //  String extension = (dot == -1) ? "" : name.substring(dot+1);
        return base;
    }
    public void readSeeds(String fileName, Integer numSeeds){
         try{                
                String Oneline = null;
                String splitter = ", ";
                int numLines = countLines(fileName);
                Vector<Integer> cliqueNums = new Vector<Integer>();
                Vector<String> seeds = new Vector<String>();
                int count = 0;
                while(count<numSeeds){
                    int num = rnd.nextInt(numLines);
                    if(!cliqueNums.contains(num)){
                        cliqueNums.add(num);

                        FileInputStream fstream1 = new FileInputStream(fileName);
                        DataInputStream in1 = new DataInputStream(fstream1);
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
                        
                        int line = 0;                        
                        while (line < num){
                            Oneline = br1.readLine();
                            line++;
                        }
                        if(Oneline!= null){
                            String[] ids = Oneline.split(splitter);
                            for(int i =0; i<ids.length;i++){
                                if(count <numSeeds){
                                    if(!seeds.contains(ids[i])){
                                        seeds.add(ids[i]);                                        
                                        count++;
                                    }
                                }
                                else break;
                            }
                        }
                        in1.close();
                        br1.close();
                        fstream1.close();
                    }
                 }

                FileWriter fw = new FileWriter(getBaseOfFileName(fileName) + "_" + numSeeds + ".seeds", false);
                fw.write("NumCliques: " + cliqueNums.size() +"\n");
                for(String seed : seeds)
                    fw.write(seed +"\n");
                fw.close();

           }catch (Exception e){//Catch exception if any
                System.err.println("Error in selectSeeds: " + e.getMessage());
           }
    }

 public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
 {
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });
         // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



}
