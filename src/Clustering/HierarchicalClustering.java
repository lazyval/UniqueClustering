package Clustering;

import Harvester.CarEntity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * User: john
 * Date: 13.03.11
 * Time: 20:26
 */
public class HierarchicalClustering implements ClusteringAlgorithm {
    //Weights for all types of features
    private static final int keyDescWeight = 10;
    private static final int cityWeight = 15;
    private static final int priceWeight = 15;
    private static final int threshold = 50;
    List<SplittedCarEntity> carList = new ArrayList<SplittedCarEntity>();
    Hashtable<Integer,ClusterNode> nodeList = new Hashtable<Integer,ClusterNode>();
    public HierarchicalClustering(List<CarEntity> cars) {
        for(CarEntity c : cars)
            carList.add(new SplittedCarEntity(c));
        //TODO make dictionary: replace all strings with int number (it will speed up comparing while clustering)
    }

    public List<Integer> process() {
        int maxSimilarity[]= new int[] {0,0,0}; // [0] - value of similarity between [1] and [2] clusters;
        List<Integer> result= new ArrayList<Integer>(carList.size());
        for(int i=0;i<carList.size();i++)
            result.add(-1);
        ClusterNode a,b = null;
        int similarity = 0;
        for(int i=0; i<carList.size();i++)
            nodeList.put(i, new ClusterNode(i));
        while (true) {
            maxSimilarity[0] = 0;
            //For each cluster
            for(int i=0; i< nodeList.size()-1; i++ , similarity=0) {

                a = nodeList.get(i);
                if(a!=null) {
                    //For each other cluster in Cluster list
                    for(int j = i+1;j<nodeList.size();j++) {
                        b = nodeList.get(j);
                        if(b!=null) {
                            //Calculate distance between this two clusters
                            similarity= ClustersSimilarity(a, b);
                            nodeList.get(i).setDistance(j,similarity);
                            nodeList.get(j).setDistance(i,similarity);
                            if(similarity>maxSimilarity[0]) {
                                maxSimilarity[0]=similarity;
                                maxSimilarity[1]=i;
                                maxSimilarity[2]=j;
                            }
                        }
                    }
                }
            }
            //After calculating distance between clusters, we need to union closest ones
            if((maxSimilarity[0]>threshold) & (nodeList.size()>1)) {
                ClusterNode newCluster = nodeList.get(maxSimilarity[1]);
                newCluster.addMembers(nodeList.get(maxSimilarity[2]).getMembers());
                nodeList.remove(maxSimilarity[1]);
                nodeList.remove(maxSimilarity[2]);
                nodeList.put(maxSimilarity[1], newCluster);
            }
            else
                break;
        }

        for(int i=0;i< nodeList.values().size();i++)
            if(nodeList.containsKey(i))
                for(Integer n: nodeList.get(i).getMembers())
                    result.set(n, i);
        return result;
    }


    private int ClustersSimilarity(ClusterNode a, ClusterNode b) {
        int distance = 0, i = 0, j = 0;
        SplittedCarEntity firstCar;
        SplittedCarEntity secondCar;
        //Each car from first cluster...
        for(i=0;i<a.countMembers();i++) {
            firstCar = carList.get(a.getMembers().get(i));
            //...we will compare with every car from second Cluster;
            for(j=0; j<b.countMembers();j++) {
                secondCar = carList.get(b.getMembers().get(j));
                distance += distCars(firstCar,secondCar);
            }
        }
            //Average-link clustering
            return distance/(i*j);
    }

    private int distCars(SplittedCarEntity firstCar, SplittedCarEntity secondCar) {
        //Key features: price, type, color,
        int distance = 0;
        int length = Math.min(firstCar.getShortDesc().length,
                              secondCar.getShortDesc().length);
        try
        {
            for(int k=0; k < length;k++)
                if(firstCar.getShortDesc()[k].equals(secondCar.getShortDesc()[k]))
                    distance+= keyDescWeight;
            if(firstCar.getCity().equals(secondCar.getCity()))
                distance+= cityWeight;
            if(firstCar.getPrice()==secondCar.getPrice())
                distance+= priceWeight;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return distance;

    }


}


