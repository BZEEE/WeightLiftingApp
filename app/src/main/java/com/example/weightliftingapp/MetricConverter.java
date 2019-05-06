package com.example.weightliftingapp;

public class MetricConverter {

    public double PoundsToKilograms(double pounds) {
        return pounds / 2.2046;
    }

    public double KilogramsToPounds(double kg) {
        return kg * 2.2046;
    }
}
