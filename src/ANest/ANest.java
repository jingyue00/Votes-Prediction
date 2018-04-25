package ANest;

import java.util.ArrayList;
import java.util.List;

public class ANest {

    public List<Cluster> ANest(List<DataNode> dataNodes, int ClusterNum) {
        List<Cluster> clusters;
        //List<Cluster> initClusters = initialCluster(dataNodes);
        clusters = initialCluster(dataNodes);
        while (clusters.size() > ClusterNum) {
            double min = Double.MAX_VALUE;
            int mergeIndexA = 0;
            int mergeIndexB = 0;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = 0; j < clusters.size(); j++) {
                    if (i != j) {
                        Cluster clusterA = clusters.get(i);
                        Cluster clusterB = clusters.get(j);
                        List<DataNode> dataPointsA = clusterA.getDataNodes();
                        List<DataNode> dataPointsB = clusterB.getDataNodes();
                        for (int m = 0; m < dataPointsA.size(); m++) {
                            for (int n = 0; n < dataPointsB.size(); n++) {
                                double tempDis = calDistance(dataPointsA.get(m), dataPointsB.get(n));
                                if (tempDis < min) {
                                    min = tempDis;
                                    mergeIndexA = i;
                                    mergeIndexB = j;
                                }
                            }
                        }
                    }
                }
            }
            //Merge cluster[mergeIndexA] and cluster[mergeIndexB]
            clusters = mergeCluster(clusters, mergeIndexA, mergeIndexB);
        }
        return clusters;
    }

    //Merge cluster
    private List<Cluster> mergeCluster(List<Cluster> clusters, int A, int B) {
        if (A != B) {
            Cluster clusterA = clusters.get(A);
            Cluster clusterB = clusters.get(B);
            List<DataNode> ListA = clusterA.getDataNodes();
            List<DataNode> ListB = clusterB.getDataNodes();
            for (DataNode dp : ListB) {
                DataNode tempDp = new DataNode();
                tempDp.setDataPointName(dp.getDataPointName());
                tempDp.setDimensioin(dp.getDimensioin());
                tempDp.setCluster(clusterA);
                ListA.add(tempDp);
            }
            clusterA.setDataNodes(ListA);
            clusters.remove(B);
        }
        return clusters;
    }

    //initial the cluster
    private List<Cluster> initialCluster(List<DataNode> dataNodes) {
        List<Cluster> originalClusters = new ArrayList<Cluster>();
        for (int i = 0; i < dataNodes.size(); i++) {
            DataNode tempDataNode = dataNodes.get(i);
            List<DataNode> tempDataNodes = new ArrayList<DataNode>();
            tempDataNodes.add(tempDataNode);
            Cluster tempCluster = new Cluster();
            tempCluster.setClusterName("Cluster " + String.valueOf(i));
            tempCluster.setDataNodes(tempDataNodes);
            tempDataNode.setCluster(tempCluster);
            originalClusters.add(tempCluster);
        }
        return originalClusters;
    }

    //Calculate the Euclidean distance
    public double calDistance(DataNode A, DataNode B) {
        double distance = 0;
        List<Double> ListA = A.getDimensioin();
        List<Double> ListB = B.getDimensioin();
        if (ListA.size() == ListB.size()) {
            for (int i = 0; i < ListA.size(); i++) {
                double temp = Math.pow((ListA.get(i) - ListB.get(i)), 2);
                distance = distance + temp;
            }
            distance = Math.sqrt(distance);
        }
        return distance;
    }
}
