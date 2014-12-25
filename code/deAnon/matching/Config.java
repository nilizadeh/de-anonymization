/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;
import java.io.File;

/**
 *
 * @author shirnili
 */
public final class Config {
    public static String dir;
    public static Integer num_seeds;
    public static Integer exp;
    public static Double noise;
    public static final Double thr_comm_ecc = 0.0; 
    public static final Double thr_comm_dis = 0.1 ;
    public static final Double thr_node_ecc = 0.1;
    public static final Double thr_jaccard = 0.3;
    
    public Config(String args[]){
        String dirName = args[0];
        num_seeds = Integer.parseInt(args[1]);
        noise = Double.parseDouble(args[2]);
        exp = Integer.parseInt(args[3]);
        dir = dirName  + "/" + exp + "/";
        
        boolean success = (new File(dir)).mkdirs();
        if (success) {
            System.out.println("Directory: " + dir+ " created");
         }       
    }
    
}
