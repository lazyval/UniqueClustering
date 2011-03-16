package Clustering;

import Harvester.CarEntity;

import java.util.List;

/**
 * User: john
 * Date: 14.03.11
 * Time: 18:23
 */
public interface ClusteringAlgorithm {

//@param cars    list of entities to cluster
//@return        list with the same length with cluster number for every input entity
public List<Integer> process();
}
