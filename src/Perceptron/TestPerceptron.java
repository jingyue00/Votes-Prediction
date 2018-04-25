package Perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestPerceptron {

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
        TestPerceptron t = new TestPerceptron();

        String datafile = new File("").getAbsolutePath() + File.separator + "votes-train.csv";
        String testfile = new File("").getAbsolutePath() + File.separator + "votes-test.csv";

        try {
            List<List<Double>> trainDatas = new ArrayList<List<Double>>();
            List<List<Double>> testDatas = new ArrayList<List<Double>>();
            t.read(trainDatas, datafile);
            t.read(testDatas, testfile);

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

            Perceptron testP = new Perceptron();
            List<Double> result = testP.perceptron(trainDatas);

            //Calculate the accuracy
            int count = 0;
            for (int i = 0; i < testDatas.size(); i++) {
                if (testDatas.get(i).get(0).equals(testP.getBias(testDatas.get(i), result))) {
                    count++;
                }
            }
            double percent = count * 100 / testDatas.size();
            System.out.print("Accuracy: " + percent + "%");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
