package com.example.weightliftingapp.OneRepMax;

import java.util.ArrayList;

public interface IOneRepMax {
    double Calculate(double lift, int repetition, RepMaxAlgorithms alg);
    ArrayList GetPlatesFromOneRepMax(double oneRepMax, boolean plateFormat);
}
