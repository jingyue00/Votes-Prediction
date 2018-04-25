package ANest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class TestANest {

    //Read the file by line
    public void read(List<List<Double>> datas, String path){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String data = br.readLine();
            data = br.readLine();
            List<Double> l = null;

            while (data != null) {
                String t[] = data.split(",");
                l = new ArrayList<Double>();

                for (int i = 0; i < t.length; i++) {
                    l.add(Double.parseDouble(t[i]));
                }
                datas.add(l);
                data = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        TestANest t = new TestANest();
        String testfile = new File("").getAbsolutePath() + File.separator + "votes-test.csv";
        ArrayList<DataNode> dNodes = new ArrayList<DataNode>();

        try {
            List<List<Double>> testDatas = new ArrayList<List<Double>>();
            t.read(testDatas, testfile);

            //Min-Max Normalization
            List<Double> maxl = new ArrayList<Double>(testDatas.get(0));
            List<Double> minl = new ArrayList<Double>(testDatas.get(0));
            for(int i = 0; i < testDatas.size(); i++ ){
                List<Double> trow = testDatas.get(i);
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
                List<Double> row = testDatas.get(i);
                for(int j = 1; j < row.size(); j++){
                    row.set(j,(row.get(j)-minl.get(j))/(maxl.get(j)-minl.get(j)));
                }
            }

            List<Double> data = new ArrayList<Double>();
            if (testDatas != null && !testDatas.isEmpty()) {
                for (int i = 0; i < testDatas.size(); i++) {
                    data = testDatas.get(i);
                    dNodes.add(new DataNode(data, String.valueOf(i)));
                }
            }

            //Agglomerative Nesting algorithm
            int clusterNum = 100;
            ANest Anest = new ANest();
            List<Cluster> clusters = Anest.ANest(dNodes, clusterNum);

            for (Cluster cl : clusters) {
                System.out.println("------" + "Cluster" + "------");
                List<DataNode> tempDps = cl.getDataNodes();
                for (DataNode tempdp : tempDps) {
                    System.out.print(tempdp.getDataPointName());
                    System.out.print(", ");
                }
                System.out.println(clusters.size());
            }

            //Calculate Silhouette coefficient
            double a_i = 0.0;
            for(int i = 0; i < clusters.size(); i++){
                for (int j = 0; j < clusters.get(i).getDataNodes().size(); j++) {
                    for (int k = 0; k < clusters.get(i).getDataNodes().size(); k++) {
                        a_i = a_i + Anest.calDistance(clusters.get(i).getDataNodes().get(j), clusters.get(i).getDataNodes().get(k));
                    }
                }
            }
            int size_a = 0;
            for(int i = 0; i < clusters.size(); i++){
                size_a = size_a + clusters.get(i).getDataNodes().size() * clusters.get(i).getDataNodes().size();
            }
            a_i = a_i/size_a;

            double b_i = 0.0;
            for(int m = 0; m < clusters.size(); m++){
                for(int n = 0; n < clusters.size(); n++) {
                    if(m != n) {
                        for (int j = 0; j < clusters.get(m).getDataNodes().size(); j++) {
                            for (int k = 0; k < clusters.get(n).getDataNodes().size(); k++) {
                                b_i = b_i + Anest.calDistance(clusters.get(m).getDataNodes().get(j), clusters.get(n).getDataNodes().get(k));
                            }
                        }
                    }
                }
            }
            int size_b = 0;
            for(int i = 0; i < clusters.size(); i++){
                for(int j = 0; j < clusters.size(); j++){
                    if(i != j){
                        size_b = size_b + clusters.get(i).getDataNodes().size() * clusters.get(j).getDataNodes().size();
                    }
                }
            }
            b_i = b_i/size_b;

            double s_i = (b_i - a_i)/Math.max(a_i,b_i);
            System.out.println("Cluster Number: " + clusterNum + " Silhouette coefficient: " + s_i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}