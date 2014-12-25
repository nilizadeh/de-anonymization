/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deAnon.matching;

/**
 *
 * @author shirnili
 */
public class Edge {

    private String id;
    private double weight;

    public Edge(String id, double weight){
        this.id = id;
        this.weight = weight;
    }

  /*  public Edge(String id){
        this.id = id;
        this.weight = -1;
    }
   * 
   */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}