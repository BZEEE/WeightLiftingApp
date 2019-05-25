package com.example.weightliftingapp;

public class WilksCalculator implements IWilks {

    public double Calculate(double bodyWeight, String gender) {
        double a;
        double b;
        double c;
        double d;
        double e;
        double f;
        if (gender == "Male"){
            a = -216.0475144;
            b = 16.2606339;
            c = -0.002388645;
            d = -0.00113732;
            e = 7.01863E-06;
            f = -1.291E-08;
        }
        else {
            a = 594.31747775582;
            b = -27.23842536447;
            c = 0.82112226871;
            d = -0.00930733913;
            e = 4.731582E-05;
            f = -9.054E-08;
        }
        return 500 / (a + b*bodyWeight + c*Math.pow(bodyWeight, 2) + d*Math.pow(bodyWeight, 3) + e*Math.pow(bodyWeight, 4) + f*Math.pow(bodyWeight, 5));
    }
}
