package KNN;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class KNN {

    //Build comparator to compare distance
    private Comparator<Node> comparator = new Comparator<Node>() {
        public int compare(Node o1, Node o2) {
            if (o1.getDistance() >= o2.getDistance()) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    //Calculate the Euclidean distance
    public double Distance(List<Double> d1, List<Double> d2) {
        double distance = 0.00;
        for (int i = 1; i < d1.size(); i++) {
            distance += Math.pow(d1.get(i) - d2.get(i), 2) ;
        }
        return Math.sqrt( distance );
    }

    public String knn(List<List<Double>> trainDatas, List<Double> testData, int k) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(k, comparator);

        for (int i = 0; i < k; i++) {
            List<Double> currData = trainDatas.get(i);
            String c = currData.get(0).toString();
            Node node = new Node(i, Distance(testData, currData), c);
            pq.add(node);
        }

        for (int i = k; i < trainDatas.size(); i++) {
            List<Double> t = trainDatas.get(i);
            double distance = Distance(testData, t);
            Node top = pq.peek();
            if (top.getDistance() > distance) {
                pq.remove();
                pq.add(new Node(i, distance, t.get(0).toString()));
            }
        }
        return getMostClass(pq);
    }

    private String getMostClass(PriorityQueue<Node> pq) {
        Map<String, Integer> classCount = new HashMap<String, Integer>();
        for (int i = 0; i < pq.size(); i++) {
            Node node = pq.remove();
            String c = node.getChar();
            if (classCount.containsKey(c)) {
                classCount.put(c, classCount.get(c) + 1);
            } else {
                classCount.put(c, 1);
            }
        }
        int maxIndex = -1;
        int maxCount = 0;
        Object[] classes = classCount.keySet().toArray();
        for (int i = 0; i < classes.length; i++) {
            if (classCount.get(classes[i]) > maxCount) {
                maxIndex = i;
                maxCount = classCount.get(classes[i]);
            }
        }
        return classes[maxIndex].toString();
    }
}
