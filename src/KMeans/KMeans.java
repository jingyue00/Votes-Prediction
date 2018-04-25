package KMeans;

import java.util.ArrayList;

public class KMeans{

    //Get Euclidean distance of two points
    public Double getDistances(DataPoint p1, DataPoint p2){
        double dist = 0.00;
        for(int i = 1; i < p1.PList.size(); i++){
            dist += Math.pow(p1.PList.get(i) - p2.PList.get(i), 2);
        }
        return Math.sqrt( dist );
    }

    //Add point the nearest cluster
    public void addList(ArrayList<DataPoint> Plist, DataPoint p, ArrayList<ArrayList<DataPoint>> collect){
        ArrayList<Double> distance = new ArrayList<>();
        ArrayList<DataPoint> newl = new ArrayList<>();
        double max = 0.0;
        for(int i = 0; i < Plist.size(); i++ ){
            if(getDistances(Plist.get(i),p) > max){
                max = getDistances(Plist.get(i),p);
            }
            distance.add(getDistances(Plist.get(i),p));
        }
        int index = distance.indexOf(max);
        newl = collect.get(index);
        newl.add(p);
        collect.set(index, newl);
    }

    //Get mean of the cluster
    public DataPoint getMean(ArrayList<DataPoint> list, DataPoint mean){
        if(list == null || list.size() == 0) {
            return mean;
        }
        ArrayList<Double> newList = new ArrayList<>();

        for (int k = 0; k < list.get(0).PList.size(); k++) {
            newList.add(list.get(0).PList.get(k));
        }

        for (int i = 1; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).PList.size(); j++) {
                newList.set(j, newList.get(j) + list.get(i).PList.get(j));
            }
        }

        for (int i = 0; i < newList.size(); i++) {
            newList.set(i, newList.get(i) / list.size());
        }
        DataPoint result = new DataPoint(newList);
        return result;
    }

    public ArrayList<ArrayList<DataPoint>> calculation(ArrayList<DataPoint> list, int clusterNum){
        ArrayList<DataPoint> newCenterPoints = new ArrayList<>();
        ArrayList<DataPoint> oldCenterPoints = new ArrayList<>();
        ArrayList<ArrayList<DataPoint>> collect = new ArrayList<>();
        double newDis = 0.0;
        double oldDis = 0.0;

        for(int i = 0; i < clusterNum; i++){
            oldCenterPoints.add(list.get(i));
        }

        boolean finish = false;
        while(!finish){

            for(int i = 0; i < clusterNum; i++){
                ArrayList<DataPoint> build = new ArrayList<>();
                collect.add(build);
            }

            for(int i = 0; i < list.size(); i++){
                addList(oldCenterPoints,list.get(i),collect);
            }

            for(int i = 0; i < collect.size(); i++){
                newCenterPoints.add(getMean(collect.get(i), oldCenterPoints.get(i)));
            }

            newDis = getDistances(newCenterPoints.get(0),oldCenterPoints.get(0));

            if (oldDis == newDis) {
                finish = true;
            } else {
                oldCenterPoints.clear();
                for(int i = 0; i < newCenterPoints.size(); i++){
                    oldCenterPoints.add(newCenterPoints.get(i));
                }
                collect.clear();
                newCenterPoints.clear();
                oldDis = newDis;
            }

        }
        return collect;
    }
}
