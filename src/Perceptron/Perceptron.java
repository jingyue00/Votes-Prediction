package Perceptron;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class Perceptron
{
    static int MAX_ITERATIVE = 100;
    static double RATE = 0.1;
    static int theta = 2;

    public List<Double> perceptron(List<List<Double>> trainDatas){

        //Randomly initialize weights
        List<Double> weights = new ArrayList<Double>();
        for (int i = 0; i < trainDatas.get(0).size(); i++) {
            weights.add(randomNumber(-1, 1));
        }

        double globalError = 0.0;
        int count = 0;

        do{
            count++;
            for (int i = 0; i < trainDatas.size(); i++) {
                double output = calOutput(theta, weights, trainDatas.get(i));
                double error = trainDatas.get(i).get(0) - output;
                for (int j = 1; j < trainDatas.get(0).size(); j++) {
                    Double temp = weights.get(j) + RATE * error * trainDatas.get(i).get(j);
                    weights.set(j, temp);
                }
                globalError += (error * error);
            }
        } while (globalError != 0 && count <= MAX_ITERATIVE);
        return weights;
    }

    //Get bias
    public double getBias(List<Double> test, List<Double> standard) {
        return calOutput(theta, standard, test);
    }

    public double randomNumber(int min , int max) {
        DecimalFormat df = new DecimalFormat("#.####");
        double d = min + Math.random() * (max - min);
        String s = df.format(d);
        return Double.parseDouble(s);
    }

    public double calOutput(int theta, List<Double> weights, List<Double> testData) {
        double sum = 0.00;
        for (int i = 1; i < testData.size(); i++) {
            sum += testData.get(i) * weights.get(i);
        }
        return (sum >= theta) ? 1 : 0;
    }
}
