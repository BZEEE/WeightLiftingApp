package com.example.weightliftingapp;

public class IPFCalculator implements IIPFPoint {

    public double Calculate(double total, double bodyWeight, String gender) {
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
            if (gender.equals("Male")){
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
            double IPFpoints = 500 + 100 * (total - (C1*Math.log(bodyWeight)-C2));
            IPFpoints = IPFpoints / (C3*Math.log(bodyWeight)-C4);
            return IPFpoints;
        }
    }
}
