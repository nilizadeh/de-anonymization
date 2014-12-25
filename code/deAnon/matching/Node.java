/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

/**
 *
 * @author shirnili
 */
public class Node {
    int id;
    String name;

    public Node(int i, String n){
	id = i;
        name = n;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
