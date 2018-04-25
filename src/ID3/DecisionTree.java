package ID3;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DecisionTree {
    public static ArrayList<String[]> trainData = new ArrayList<>();
    public static ArrayList<String[]> testData = new ArrayList<>();
    public static Map<Integer, String> thresholdMap; // Features with their threshold values
    public static void main(String[] args) throws IOException {
        ReadFile r = new ReadFile();

        String datafile = new File("").getAbsolutePath() + File.separator + "votes-train.csv";
        String testfile = new File("").getAbsolutePath() + File.separator + "votes-test.csv";

        List<List<Double>> trainDatas = new ArrayList<List<Double>>();
        List<List<Double>> testDatas = new ArrayList<List<Double>>();

        r.read(trainDatas, datafile);
        r.read(testDatas, testfile);

        //Min-Max Normalization
        List<Double> maxl = new ArrayList<Double>(trainDatas.get(0));
        List<Double> minl = new ArrayList<Double>(trainDatas.get(0));
        List<Double> maxt = new ArrayList<Double>(testDatas.get(0));
        List<Double> mint = new ArrayList<Double>(testDatas.get(0));

        for(int i = 0; i < trainDatas.size(); i++ ){
            List<Double> row = trainDatas.get(i);
            for(int j = 0; j < row.size(); j++){
                if( row.get(j) < minl.get(j)){
                    minl.set(j, row.get(j));
                }
                if( row.get(j) > maxl.get(j)){
                    maxl.set(j, row.get(j));
                }
            }
        }

        for(int i = 0; i < testDatas.size(); i++ ){
            List<Double> trow = testDatas.get(i);
            for(int j = 0; j < trow.size(); j++){
                if( trow.get(j) < mint.get(j)){
                    mint.set(j, trow.get(j));
                }
                if( trow.get(j) > maxt.get(j)){
                    maxt.set(j, trow.get(j));
                }
            }
        }

        for(int i = 0; i < trainDatas.size(); i++ ){
            List<Double> row = trainDatas.get(i);
            for(int j = 1; j < row.size(); j++){
                row.set(j,(row.get(j)-minl.get(j))/(maxl.get(j)-minl.get(j)));
            }
        }

        for(int i = 0; i < testDatas.size(); i++ ){
            List<Double> row = testDatas.get(i);
            for(int j = 1; j < row.size(); j++){
                row.set(j,(row.get(j)-mint.get(j))/(maxt.get(j)-mint.get(j)));
            }
        }

        for (int i = 0; i < trainDatas.size(); i++) {
            String[] tempString = new String[trainDatas.get(i).size()];
            for (int j = 0; j < trainDatas.get(i).size(); j++) {
                tempString[j] = Double.toString(trainDatas.get(i).get(j));
            }
            trainData.add(tempString);
        }

        for (int i = 0; i < testDatas.size(); i++) {
            String[] tempString = new String[testDatas.get(i).size()];
            for (int j = 0; j < testDatas.get(i).size(); j++) {
                tempString[j] = Double.toString(testDatas.get(i).get(j));
            }
            testData.add(tempString);
        }

        int depth = 10;
        int numFeature = trainData.get(0).length-1; // Last column is considered as the classifier and all columns before it are features.
        Tree tree = new Tree();
        tree.depth = depth;
        tree.nodeArray = new Node[((int)(Math.pow(2,depth)))-1]; // Maximum Number of nodes is (2^depth) - 1
        Map<Integer, Boolean> featureStatus = new HashMap<Integer, Boolean>();
        Map<Integer, String> tMap = new HashMap<Integer, String>();
        for(int i=0; i<numFeature; i++) {
            featureStatus.put(i, false); // Set feature status as true once it is added to the tree
            tMap.put(i, trainData.get(0)[i]); // The feature values in the first row of data set are considered the threshold of the feature
        }
        thresholdMap = tMap;
        for(int i=0; i<tree.nodeArray.length; i++) {
            if(i > 0) { // if a feature has already been assigned to the root node
                int parentIndex = 0;
                ArrayList<String[]> dataSubset; // Subset after split in the parent
                if(i % 2 == 0) { // If index is even, go right
                    parentIndex = (int)i/2 - 1;
                    if(tree.nodeArray[parentIndex] != null) // If there is no node at parentIndex, move on to the next node
                        if(tree.nodeArray[parentIndex].threshold != null) // If the data can be split at this node
                            dataSubset = getSubset(tree.nodeArray[parentIndex].rightIndices); // Get the data with parent feature value != parent threshold
                        else
                            dataSubset = null;
                    else
                        dataSubset = null;
                } else { // If index is odd, go left
                    parentIndex = (int)(i - 1)/2;
                    if(tree.nodeArray[parentIndex] != null) // If there is no node at parentIndex, move on to the next node
                        if(tree.nodeArray[parentIndex].threshold != null) // If the data can be split at this node
                            dataSubset = getSubset(tree.nodeArray[parentIndex].leftIndices); // Get the data with parent feature value == parent threshold
                        else
                            dataSubset = null;
                    else
                        dataSubset = null;
                }
                if(dataSubset != null && dataSubset.size() > 0) {
                    Map<Integer, Double> gainMap = new HashMap<Integer, Double>();

                    for(int f = 0; f < numFeature; f++) {
                        if(!featureStatus.get(f)) {
                            gainMap.put(f, getInfoGain(f, dataSubset));
                        }
                    }
                    int thisFeature = getBestFeature(gainMap); // get feature with maximum information gain
                    if(gainMap.get(thisFeature) != null && gainMap.get(thisFeature) != -1) {
                        tree.nodeArray[i] = new Node();
                        tree.nodeArray[i].parent = tree.nodeArray[parentIndex];
                        tree.nodeArray[i].feature = thisFeature;
                        tree.nodeArray[i].index = i;
                        featureStatus.put(thisFeature, true);
                        if(i%2 != 0)
                            tree.nodeArray[i].parent.leftChild = tree.nodeArray[i];
                        else
                            tree.nodeArray[i].parent.rightChild = tree.nodeArray[i];

                        String threshold = thresholdMap.get(thisFeature);
                        tree.nodeArray[i].leftIndices = new ArrayList<Integer>();
                        tree.nodeArray[i].rightIndices = new ArrayList<Integer>();
                        tree.nodeArray[i].threshold = threshold;
                        for(int index = 0; index < dataSubset.size(); index++) {
                            if(trainData.get(index)[thisFeature].equals(threshold)) {
                                tree.nodeArray[i].leftIndices.add(index);
                            } else {
                                tree.nodeArray[i].rightIndices.add(index);
                            }
                        }
                    }
                }
            } else {
                Map<Integer, Double> gainMap = new HashMap<Integer, Double>();
                for(int f = 0; f < numFeature; f++) {
                    gainMap.put(f, getInfoGain(f, trainData));
                }
                int thisFeature = getBestFeature(gainMap);
                if(gainMap.get(thisFeature) != null && gainMap.get(thisFeature) != -1) {
                    tree.nodeArray[i] = new Node();
                    tree.nodeArray[i].parent = null;
                    tree.nodeArray[i].feature = thisFeature;
                    tree.nodeArray[i].index = i;
                    featureStatus.put(thisFeature, true);
                    String threshold = thresholdMap.get(thisFeature);
                    tree.nodeArray[i].leftIndices = new ArrayList<Integer>();
                    tree.nodeArray[i].rightIndices = new ArrayList<Integer>();
                    tree.nodeArray[i].threshold = threshold;
                    for(int index = 0; index < trainData.size(); index++) {
                        if(trainData.get(index)[thisFeature].equals(threshold)) {
                            tree.nodeArray[i].leftIndices.add(index);
                        } else {
                            tree.nodeArray[i].rightIndices.add(index);
                        }
                    }
                }
            }
        }
        ArrayList<String> prediction = new ArrayList<String>();
        for(String[] s : testData) {
            int i = 0;
            while (i < tree.nodeArray.length) {
                Node thisNode = tree.nodeArray[i];
                if(thisNode.threshold != null) {
                    if(s[thisNode.feature].equals(thisNode.threshold)) {
                        if(thisNode.leftChild != null) {
                            i = thisNode.leftChild.index;
                        } else {
                            prediction.add(getPrediction(thisNode.leftIndices));
                            break;
                        }
                    } else {
                        if(thisNode.rightChild != null) {
                            i = thisNode.rightChild.index;
                        } else {
                            prediction.add(getPrediction(thisNode.rightIndices));
                            break;
                        }
                    }
                } else {
                    System.out.println("Nothing learnt from training data");
                    break;
                }
            }
        }
        System.out.println("Prediction: ");
        for(String s : prediction) {
            System.out.println(s);
        }
    }

    private static int getBestFeature(Map<Integer, Double> gainMap) {
        int bestFeature = -1;
        double maxValue = -1;
        for(int i : gainMap.keySet()) {
            if(gainMap.get(i) != -1) {
                if(gainMap.get(i) > maxValue) {
                    maxValue = gainMap.get(i);
                    bestFeature = i;
                }
            } else {
                maxValue = -1;
                bestFeature = i;
            }
        }
        return bestFeature;
    }

    public static double getInfoGain(int f, ArrayList<String[]> dataSubset) {
        double entropyBefore = getEntropy(dataSubset);
        if(entropyBefore != 0){
            String threshold = thresholdMap.get(f);
            ArrayList<String[]> leftData = new ArrayList<String[]>();
            ArrayList<String[]> rightData = new ArrayList<String[]>();
            for(String[] d : dataSubset) {
                if(d[f].equals(threshold)) {
                    leftData.add(d);
                } else {
                    rightData.add(d);
                }
            }
            if(leftData.size() > 0 && rightData.size() > 0) {
                double leftProb = (double)leftData.size()/dataSubset.size();
                double rightProb = (double)rightData.size()/dataSubset.size();
                double entropyLeft = getEntropy(leftData);
                double entropyRight = getEntropy(rightData);
                double gain = entropyBefore - (leftProb * entropyLeft) - (rightProb * entropyRight);
                return gain;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static double getEntropy(ArrayList<String[]> dataSubset) {
        double entropy = 0;
        if(dataSubset.size() > 0){
            int y = dataSubset.get(0).length - 1;
            Map<String, Integer> freqMap = new HashMap<String, Integer>();
            for(String[] d : dataSubset) {
                if(freqMap.containsKey(d[y]))
                    freqMap.put(d[y], freqMap.get(d[y]) + 1);
                else
                    freqMap.put(d[y], 1);
            }
            for(String s : freqMap.keySet()) {
                double prob = (double) freqMap.get(s)/dataSubset.size();
                entropy -= prob * Math.log(prob)/Math.log(2);
            }
        }
        return entropy;
    }

    public static ArrayList<String[]> getSubset(ArrayList<Integer> indices) {
        // return data subset, given the indices
        ArrayList<String[]> subSet = new ArrayList<String[]>();
        for(Integer i : indices) {
            subSet.add(trainData.get(i));
        }
        return subSet;
    }

    public static String getPrediction(ArrayList<Integer> indices) {
        int y = 2;
        Map<String, Integer> freqMap = new HashMap<String, Integer>();
        for(Integer i : indices) {
            if(freqMap.containsKey(trainData.get(i)[y]))
                freqMap.put(trainData.get(i)[y], freqMap.get(trainData.get(i)[y]) + 1);
            else
                freqMap.put(trainData.get(i)[y], 1);
        }
        String prediction = null;
        int maxValue = 0;
        for(String i : freqMap.keySet()) {
            if(freqMap.get(i) > maxValue) {
                maxValue = freqMap.get(i);
                prediction = i;
            }
        }
        return prediction;
    }
}
