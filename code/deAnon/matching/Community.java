/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author shirnili
 */
public class Community {

    static HashMap<String, String> crr_matchedComms;

    public static int evaluation_comm(HashMap<String, String> map,
            MyGraph g1, MyGraph g2 , double thr){

        int commMatch = 0;
        for(String c1 : map.keySet()){
            String c2 = map.get(c1);
            Vector<String> nodes1= g1.get_commNodes().get(c1);
            Vector<String> nodes1_names= new Vector<String>();
            for(String node: nodes1)
                nodes1_names.add(g1.getNodeName(node));
            Vector<String> nodes2= g2.get_commNodes().get(c2);
            Vector<String> nodes2_names= new Vector<String>();
            for(String node: nodes2)
                nodes2_names.add(g2.getNodeName(node));
            Misc misc = new Misc();
            double jaccard = misc.jaccard_index(nodes1_names, nodes2_names);
         //   double jaccard = misc.jaccard_index(nodes1, nodes2);
            if(jaccard>=thr)
                commMatch++;
        }

       // System.out.print("successRate: " + (float) commMatch/commNodes1.keySet().size() * 100+ "%\n");
        return(commMatch);
    }

     public static HashMap<String, String> get_crr_MatchedComms(){
         return(crr_matchedComms);
     }
    public static HashMap<String, String> init_MatchedComms(HashMap<String, String> map,
            HashMap<String, Vector<String>> commNodes1,
            HashMap<String, Vector<String>> commNodes2, double thr){

        HashMap<String, String> matchedComms = new HashMap<String, String>();
        
        crr_matchedComms = new HashMap<String, String>();
        for(String c1 : map.keySet()){
            String c2 = map.get(c1);
            Vector<String> nodes1= commNodes1.get(c1);
            Vector<String> nodes2= commNodes2.get(c2);

            Misc misc = new Misc();
            double jaccard = misc.jaccard_index(nodes1, nodes2);
            if(jaccard>=thr){
                matchedComms.put(c1, c2);
                crr_matchedComms.put(c1, c2);
            }
        }
        return(matchedComms);
    }

   }
