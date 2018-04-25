package ID3;

import java.util.ArrayList;

public class Node {

    Node parent;
    Node leftChild;
    Node rightChild;

    int index;
    int feature;
    String threshold;
    ArrayList<Integer> leftIndices, rightIndices;
}
