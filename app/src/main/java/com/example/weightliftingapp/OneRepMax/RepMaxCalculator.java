package com.example.weightliftingapp.OneRepMax;
import android.util.Log;

import com.example.weightliftingapp.OneRepMax.IOneRepMax;
import com.example.weightliftingapp.OneRepMax.RepMaxAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class RepMaxCalculator implements IOneRepMax {
    private double standardBarWeight;
    private double powerliftingBarWeight;
    // 25 kilograms total, 20 for the bar, and 2.5 for each collar that holds the plates
    private double barbellWeight = 20.0;
    // the collar is what holds the weights on each end of the bar
    // so there is two of them
    private double collarWeight = 2.5;

    private static double[] standardPlates = {
            StandardPlatesValues.fourtyFivePounds,
            StandardPlatesValues.thirtyFivePounds,
            StandardPlatesValues.twentyFivePounds,
            StandardPlatesValues.fifteenPounds,
            StandardPlatesValues.tenPounds,
            StandardPlatesValues.fivePounds,
            StandardPlatesValues.twoPointFivePounds
    };
    private static double[] powerliftingPlates = {
            PowerliftingPlatesValues.twentyFiveKilograms,
            PowerliftingPlatesValues.twentyKilograms,
            PowerliftingPlatesValues.fifteenKilograms,
            PowerliftingPlatesValues.tenKilograms,
            PowerliftingPlatesValues.fiveKilograms,
            PowerliftingPlatesValues.twoPointFiveKilograms,
            PowerliftingPlatesValues.twoKilograms,
            PowerliftingPlatesValues.onePointFiveKilograms,
            PowerliftingPlatesValues.oneKilograms,
            PowerliftingPlatesValues.zeroPointFiveKilograms
    };


    public double[] GetPlatesFromOneRepMax(double oneRepMax, boolean plateFormat) {
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
        double[] format = plateFormat ? standardPlates : powerliftingPlates;

        // subtract the weight of the bar from the totala lift weight
        // to just get the remaining weight of the plates in total
        double tempOneRepMax;
        tempOneRepMax = oneRepMax - barbellWeight - (collarWeight * 2);

        int i;
        i = 0;
        int elementCount = 0;
        // first loop checks how many plates there will be, so we
        // know how many elements to initialize the array with
        while (i < format.length) {
            if (format[i] * 2 <= tempOneRepMax) {
                tempOneRepMax -= format[i] * 2;
                elementCount += 2;
            } else {
                i++;
            }
        }

        i = 0;
        int plateOffset = 0;
        tempOneRepMax = oneRepMax - barbellWeight - (collarWeight * 2);
        double[] plates = new double[elementCount];
        int mid = plates.length / 2;
        // second loop, we fill the array with the plate values
        while (i < format.length) {
            if (format[i] * 2 <= tempOneRepMax) {
                tempOneRepMax -= format[i] * 2;
                plates[mid - 1 - plateOffset] = format[i];
                plates[mid + plateOffset] = format[i];
                // each time we add a plate to increment by 1
                // so we add the biggest plates in the middle of the array
                // and the smaller plates towards the outside
                // then the bar would look like this:  (10|25|45 --- 45|25|10)
                plateOffset += 1;
            } else {
                i++;
            }
        }
        return plates;
    }

    public double Calculate(double lift, int repetition, RepMaxAlgorithms alg) {
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
