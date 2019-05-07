package com.example.weightliftingapp;
import java.util.ArrayList;

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
        // stores 1 rm first, then 95%, then 90%, and so on

    }
}
