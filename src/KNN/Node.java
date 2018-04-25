package KNN;

public class Node {

    private int index;
    private double distance;
    private String c;

    public Node(int index, double distance, String c) {
        super();
        this.index = index;
        this.distance = distance;
        this.c = c;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getChar() {
        return c;
    }

    public void setChar(String c) {
        this.c = c;
    }
}
