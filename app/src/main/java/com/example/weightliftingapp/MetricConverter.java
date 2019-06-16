package com.example.weightliftingapp;

public class MetricConverter {

    public static double PoundsToKilograms(double pounds) {
        return pounds / 2.2046;
    }

    public static  double KilogramsToPounds(double kg) {
        return kg * 2.2046;
    }
}
