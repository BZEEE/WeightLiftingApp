package com.example.weightliftingapp;
import java.util.ArrayList;
import com.example.weightliftingapp.RepMaxAlgorithms.*;

public class RepMaxCalculator {
    private static double[] standardPlates = {45, 35, 25, 15, 10, 5, 2.5};
    private static double[] PowerliftingPlates = {55.1155, 44.0924, 33.0693, 22.0462, 11.0231, 5.51155, 2.755775, 1.10231};


    public static ArrayList PlatesFromWeight(float weight, boolean plateFormat) {
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

    public static void one_rm_calc(double weight, int reps){
        // returns nothing for now, does the 1 rm calculation.
        // input lift for x reps
        double[] rel_perc = new double[10];
        double perc = 1;
        // run a 1 rm formula function
        double max = RepMaxCalculator.OneRepMaxCalculation(weight, reps, RepMaxAlgorithms.Epley);
        int i = 0;
        for (;i < 10; i++){
            rel_perc[i] = max * perc;
            perc = perc - 0.05;
        }
        // stores 1 rm first into array, then 95%, then 90%, and so on

    }

    public static double IPFPointCalculation(double total, double bodyweight, char sex) {
        // takes lifter's 3-lift total in (in kilograms) and converts to a score (only for raw/classic).
        // adding in simple lifts just requires more variables
        double C1;
        double C2;
        double C3;
        double C4;
        if (total == 0){
            return 0;
        }
        else {
            // initialize male constants
            if (sex == 'M'){
                C1 = 3106700;
                C2 = 8577850;
                C3 = 532160;
                C4 = 1470835;
            }
            // initialize female constants
            else {
                C1 = 1251435;
                C2 = 2280300;
                C3 = 345246;
                C4 = 868301;
            }
            double IPFpoints = 500 + 100 * (total - (C1*Math.log(bodyweight)-C2));
            IPFpoints = IPFpoints / (C3*Math.log(bodyweight)-C4);
            return IPFpoints;
        }
    }

    public static double WilksCalculator(double total, double bodyweight, char sex){
        double a;
        double b;
        double c;
        double d;
        double e;
        double f;
        if (sex == 'M'){
            a =- 216.0475144;
            b = 16.2606339;
            c =- 0.002388645;
            d =- 0.00113732;
            e = 7.01863E-06;
            f =- 1.291E-08;
        }
        else {
            a = 594.31747775582;
            b =- 27.23842536447;
            c = 0.82112226871;
            d =- 0.00930733913;
            e = 4.731582E-05;
            f =- 9.054E-08;
        }
        double coeff = 500 / (a + b*bodyweight + c*bodyweight*bodyweight + d*bodyweight*bodyweight*bodyweight + e*bodyweight*bodyweight*bodyweight*bodyweight + f*bodyweight*bodyweight*bodyweight*bodyweight*bodyweight);
        return coeff;
    }


    public static double OneRepMaxCalculation(double lift, int repetition, RepMaxAlgorithms alg) {
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

    private static double EpleyAlgorithm(double w, double r) {
        return w * (1 + r / 30);
    }

    private static double BrzyckiAlgorithm(double w, double r) {
        return w * 36 / (37 - r);
    }

    private static double McGlothinAlgorithm(double w, double r) {
        return 100 * w / (101.3 - 2.67123 * r);
    }

    private static double LombardiAlgorithm(double w, double r) {
        return w * Math.pow(r, 0.10);
    }

    private static double MayhewAlgorithm(double w, double r) {
        return 100 * w / (52.2 + 41.9 * Math.pow(Math.E ,-0.055 * r));
    }

    private static double OConnorAlgorithm(double w, double r) {
        return w * (1 + r / 40);
    }

    private static double WathannAlgorithm(double w, double r) {
        return 100 * w / (48.8 + 53.8 * Math.pow(Math.E, -0.075 * r));
    }
}
