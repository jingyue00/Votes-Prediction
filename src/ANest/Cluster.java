package ANest;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private List<DataNode> dataNodes = new ArrayList<DataNode>();
    private String clusterName;

    public List<DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(List<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
