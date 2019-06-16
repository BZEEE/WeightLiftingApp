package com.example.weightliftingapp.OneRepMax;

public interface IOneRepMax {
    double Calculate(double lift, int repetition, RepMaxAlgorithms alg);
    double[] GetPlatesFromOneRepMax(double oneRepMax, boolean plateFormat);
}
