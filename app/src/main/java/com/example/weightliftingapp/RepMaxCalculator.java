package com.example.weightliftingapp;
import java.util.ArrayList;

public class RepMaxCalculator {
    private double[] standardPlates = {45, 35, 25, 15, 10};
    private double[] PowerliftingPlates = {45, 35, 25, 15, 10};


    public ArrayList PlatesFromWeight(float num, boolean plateFormat) {
        // use greedy algorithm to determine plates from a list, similar to coin change selection from CMPUT 204
        ArrayList plates = new ArrayList();
        double[] format = plateFormat ? standardPlates : PowerliftingPlates;
        int i = 0;
        return plates;
    }
}
