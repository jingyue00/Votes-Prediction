package KMeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestKMeans {

    //Read the file by line
    public void read(ArrayList<DataPoint> datas, String path){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String data = br.readLine();
            data = br.readLine();
            ArrayList<Double> l = null;

            while (data != null) {
                String t[] = data.split(",");
                l = new ArrayList<Double>();

                for (int i = 0; i < t.length; i++) {
                    l.add(Double.parseDouble(t[i]));
                }
                datas.add(new DataPoint(l));
                data = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        int clusterNum = 100;
        TestKMeans t = new TestKMeans();
        String testfile = new File("").getAbsolutePath() + File.separator + "votes-test.csv";

        try {
            ArrayList<DataPoint> testDatas = new ArrayList<>();
            t.read(testDatas, testfile);

            //Min-Max Normalization
            List<Double> maxl = new ArrayList<Double>(testDatas.get(0).PList);
            List<Double> minl = new ArrayList<Double>(testDatas.get(0).PList);
            for(int i = 0; i < testDatas.size(); i++ ){
                List<Double> trow = testDatas.get(i).PList;
                for(int j = 0; j < trow.size(); j++){
                    if( trow.get(j) < minl.get(j)){
                        minl.set(j, trow.get(j));
                    }
                    if( trow.get(j) > maxl.get(j)){
                        maxl.set(j, trow.get(j));
                    }
                }
            }
            for(int i = 0; i < testDatas.size(); i++ ){
                List<Double> row = testDatas.get(i).PList;
                for(int j = 1; j < row.size(); j++){
                    row.set(j,(row.get(j)-minl.get(j))/(maxl.get(j)-minl.get(j)));
                }
            }

            KMeans kmean = new KMeans();
            ArrayList<ArrayList<DataPoint>> result = kmean.calculation(testDatas,clusterNum);
//            System.out.println("Cluster1: " + result.get(0).size());
//            for(int i = 0; i < result.get(0).size(); i++){
//                System.out.print(result.get(0).get(i).PList);
//                System.out.print(", ");
//            }
//            System.out.println();
//            System.out.println("Cluster2: " + result.get(1).size());
//            for(int i = 0; i < result.get(1).size(); i++){
//                System.out.print(result.get(1).get(i).PList);
//                System.out.print(", ");
//            }

            //Calculate Silhouette coefficient
            double a_i = 0.0;
            for(int i = 0; i < result.size(); i++){
                for (int j = 0; j < result.get(i).size(); j++) {
                    for (int k = 0; k < result.get(i).size(); k++) {
                        a_i = a_i + kmean.getDistances(result.get(i).get(j), result.get(i).get(k));
                    }
                }
            }
            int size_a = 0;
            for(int i = 0; i < result.size(); i++){
                size_a = size_a + result.get(i).size() * result.get(i).size();
            }
            a_i = a_i/size_a;

            double b_i = 0.0;
            for(int m = 0; m < result.size(); m++){
                for(int n = 0; n < result.size(); n++) {
                    if(m != n) {
                        for (int j = 0; j < result.get(m).size(); j++) {
                            for (int k = 0; k < result.get(n).size(); k++) {
                                b_i = b_i + kmean.getDistances(result.get(m).get(j), result.get(n).get(k));
                            }
                        }
                    }
                }
            }
            int size_b = 0;
            for(int i = 0; i < result.size(); i++){
                for(int j = 0; j < result.size(); j++){
                    if(i != j){
                        size_b = size_b + result.get(i).size() * result.get(j).size();
                    }
                }
            }
            b_i = b_i/size_b;

            double s_i = (b_i - a_i)/Math.max(a_i,b_i);
            System.out.println("Silhouette coefficient: " + s_i);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
