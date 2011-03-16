package Clustering;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * User: john
 * Date: 14.03.11
 * Time: 13:11
 */
public class ClusterNode {
    List<Integer> members = new ArrayList<Integer>();
    Hashtable<Integer,Integer> distance = new Hashtable<Integer,Integer>();

    public ClusterNode(int member) {
        this.members.add(member);
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void addMembers(List<Integer> members) {
        this.members.addAll(members);
    }

    public Integer getDistance(int id) {
        return distance.get(id);
    }

    public void setDistance(int id, int distance) {
        this.distance.put(id,distance);
    }

    public int countMembers() {
        return members.size();
    }

}
