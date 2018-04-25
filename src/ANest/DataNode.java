package ANest;

import java.util.List;

public class DataNode {

    String dataPointName;
    Cluster cluster;
    List<Double> dimension;

    public DataNode(){}

    public DataNode(List<Double> dimensioin, String dataPointName){
        this.dataPointName=dataPointName;
        this.dimension=dimensioin;
    }

    public List<Double> getDimensioin() {
        return dimension;
    }

    public void setDimensioin(List<Double> dimensioin) {
        this.dimension = dimensioin;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getDataPointName() {
        return dataPointName;
    }

    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }
}