package com.example.weightliftingapp;
import java.util.ArrayList;
import com.example.weightliftingapp.RepMaxAlgorithms.*;

public class RepMaxCalculator {
    private double[] standardPlates = {45, 35, 25, 15, 10, 5, 2.5};
    private double[] PowerliftingPlates = {55.1155, 44.0924, 33.0693, 22.0462, 11.0231, 5.51155, 2.755775, 1.10231};


    public ArrayList PlatesFromWeight(float weight, boolean plateFormat) {
//        Procedure PlatesFromWeight(weight):
//            // returned is a set of the weights on one side of the bar from largest to smallest
//            // Ex. {45, 25, 10} is returned
//            // then the bar would look like this:  (10|25|45 --- 45|25|10)
//            A = {}
//            while a plate can still be added to the bar such that it does not exceed the weight do
//                select the largest weight that can be added to both side of the bar
//                add the plates to the bar (the set A)
//                subtract the added plate weight from the total weight
//            return A

        // use greedy algorithm to determine plates from a list, similar to coin change selection from CMPUT 204
        ArrayList plates = new ArrayList();
        double[] format = plateFormat ? standardPlates : PowerliftingPlates;
        int i = 0;
        while (i < format.length) {
            if (format[i] * 2 < weight) {
                plates.add(format[i]);
                weight -= format[i] * 2;
            } else {
                i++;
            }
        }
        return plates;
    }

    public void one_rm_calc(){
        // returns nothing for now, does the 1 rm calculation.
        // input lift for x reps
        double weight = System.out.printIn("Enter weight: ");
        double reps = System.out.printIn("Enter reps: ");
        double[] rel_perc = new double[10];
        double perc = 1;
        // run a 1 rm formula function
        double max = RepMaxCalculator.EpleyAlgorithm(weight,reps);
        int i = 0;
        for (;i < 10; i++){
            rel_perc[i] = max * perc;
            perc = perc - 0.5;
        }
        // stores 1 rm first into array, then 95%, then 90%, and so on

    }
    public double ipfpointcalc(double total, double bodyweight, char sex) {
        // takes lifter's 3-lift total in (in kilograms) and converts to a score (only for raw/classic).
        // adding in simple lifts just requires more variables
        if (total == 0){
            double IPFpoints = 0;
            return IPFpoints;
        }
        else {
            //initialize male constatns
            if (sex == 'M'){
                double C1 = 3106700;
                double C2 = 8577850;
                double C3 = 532160;
                double C4 = 1470835;

                double IPFpoints = 500 + 100(total - (C1*ln(bodyweight)-C2));
                IPFpoints = IPFpoints / (C3*ln(bodyweight)-C4);
                return IPFpoints;
            }
            else if (sex == 'F'){
                double C1 = 1251435;
                double C2 = 2280300;
                double C3 = 345246;
                double C4 = 868301;

                double IPFpoints = 500 + 100(total - (C1*ln(bodyweight)-C2));
                IPFpoints = IPFpoints / (C3*ln(bodyweight)-C4);
                return IPFpoints;
            }
            else{
                // specify biological sex or nah fam

            }
        }
    }


    public double OneRepMaxCalculation(double lift, int repetition, RepMaxAlgorithms alg) {
        double output;
        switch (alg) {
            case Epley:
                output = EpleyAlgorithm(lift, repetition);
                break;
            case Brzycki:
                output = BrzyckiAlgorithm(lift, repetition);
                break;
            case McGlothin:
                output = McGlothinAlgorithm(lift,repetition);
                break;
            case Lombardi:
                output = LombardiAlgorithm(lift, repetition);
                break;
            case Mayhew:
                output = MayhewAlgorithm(lift, repetition);
                break;
            case OConnor:
                output = OConnorAlgorithm(lift, repetition);
                break;
            case Wathann:
                output = WathannAlgorithm(lift, repetition);
                break;
            default:
                output = EpleyAlgorithm(lift, repetition);
                break;
        }
        return output;
    }

    // various algorithms for choosing 1 rep max
    // https://en.wikipedia.org/wiki/One-repetition_maximum
    // w = weight lifted
    // r = repetitions
    // assuming r > 1, more than one repetition

    private double EpleyAlgorithm(double w, double r) {
        return w * (1 + r / 30);
    }

    private double BrzyckiAlgorithm(double w, double r) {
        return w * 36 / (37 - r);
    }

    private double McGlothinAlgorithm(double w, double r) {
        return 100 * w / (101.3 - 2.67123 * r);
    }

    private double LombardiAlgorithm(double w, double r) {
        return w * Math.pow(r, 0.10);
    }

    private double MayhewAlgorithm(double w, double r) {
        return 100 * w / (52.2 + 41.9 * Math.pow(Math.E ,-0.055 * r));
    }

    private double OConnorAlgorithm(double w, double r) {
        return w * (1 + r / 40);
    }

    private double WathannAlgorithm(double w, double r) {
        return 100 * w / (48.8 + 53.8 * Math.pow(Math.E, -0.075 * r));
    }
}
