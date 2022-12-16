package ru.vsu.cs.Lukashev;

import flanagan.io.*;
import flanagan.math.*;
import flanagan.plot.*;

import java.util.Arrays;
import java.util.Iterator;

public class Calculation {
    private final int fc = 40;
    private final int SR = 1000;
    private final int fm = 5;
    private final int f = 4;
    private final double[] timeline = new double[SR];


    public Calculation() {
        createTimeline();
    }

    public void createTimeline(){
        double k=0;
        for(int i=0; i< timeline.length; i++, k += (1.0 / SR)){
            timeline[i]= k;
        }
//        System.out.println(Arrays.toString(this.timeline));
//        System.out.println("=============");
    }

    public double[] generateMeanderSignal(int f){
        double[] arr = new double[SR];
        int count = 0, i = 0;

        for(double curTime : timeline){
            if(Math.abs(curTime) > (count + 1.0) / f)
                count++;
            if(Math.abs(curTime) > (count + 0.5) / f){
                arr[i] = 0;
            }
            else
                arr[i] = 1;
            i++;
        }
//        System.out.println(Arrays.toString(arr));
//        System.out.println("===============");
        return arr;
    }

    public double[] generateHarmonicSignal(int A,int f){
        double[] arr = new double[SR];
        int i =0;
        for(double cutTime : timeline){
            arr[i] = (double) A * Math.sin(2 * (Math.PI) * f * cutTime);
            i++;
        }
        return arr;
    }

    public double[] amplModulation(){
        double[] yMeanderSig = generateMeanderSignal(fm);
        double[] yHarmonicSig = generateHarmonicSignal(1, 20 * f);
        double[] arr = new double[SR];
        double curMean, curHar;
        for(int i = 0; i < SR; i++){
            curMean = yMeanderSig[i];
            curHar = yHarmonicSig[i];
            arr[i] = (2 * curHar * curMean);
        }
        return arr;
    }

    public double[] freqModulation(){
        double[] yMeanderSig = generateMeanderSignal(fm);
        double[] arr = new double[SR];
        int integral = 0, i = 0;
        double curMean;
        for(double curTime : timeline){
            arr[i] = Math.cos(2 * Math.PI * fc * curTime + 2 * Math.PI * 0.1 * integral);
            integral += yMeanderSig[i];
            i++;
        }
        return arr;
    }

    public double[] phaseModulation(){
        double[] yMeanderSig = generateMeanderSignal(fm);
        double[] arr = new double[SR];
        int i =0;
        for(double curTime : timeline){
            if(yMeanderSig[i] == 0)
                arr[i] = Math.sin(2 * Math.PI * fc * curTime + 4 * fc);
            else
                arr[i] = Math.sin(2 * Math.PI * fc * curTime);
            i++;
        }
        return arr;
    }

    public double[] getTimeline() {
        return timeline;
    }

    public double[] absolute(double[] arr){
        for(int i = 0; i < arr.length; i++){
            arr[i] = Math.abs(arr[i]);
        }
        return arr;
    }


    public double[][] generateSpectrum(double[] ySignal){
        System.out.println(ySignal.length);
        FourierTransform fourierTransform = new FourierTransform(ySignal);
//        fourierTransform.transform();
//        fourierTransform.inverse();

        fourierTransform.setGaussian((double) 1 / SR);
        fourierTransform.setSegmentLength(SR);
        System.out.println("ourierTransform.getOriginalDataLength(); " + fourierTransform.getOriginalDataLength());

        double[][] arr = fourierTransform.powerSpectrum();
        System.out.println(arr[0].length);
        System.out.println(arr[1].length);
        fourierTransform.setDeltaT(0.001);
//        fourierTransform.plotPowerSpectrum(0.0, 0.2);
        return arr;
    }

    public double[] perform(){
        return amplModulation();
//        System.out.println(Arrays.deepToString());

//        generateSpectrum(amplModulation());
    }























    public double[] generateOnlyOneZero(double[] arr){
        System.out.println(arr.length + " ======");
        double count = 0;
        for (int i = 0; i < arr.length; i++){
            count += arr[i];
            if(i % 10 == 0 && i != 0){
                double number = create_digital(count);
                for(int k = i-10; k < i; k++)
                    arr[k] = number;
            }

        }
        System.out.println(arr.length + " ======");
        return arr;
    }

    public double create_digital(double count){
        return count > 75 ? 1 : 0;
    }

    public double[] concatenateArray(double[] arr1, double[] arr2) {
        double[] out = new double[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, out, 0, arr1.length);
        System.arraycopy(arr2, 0, out, arr1.length, arr2.length);
        return out;
    }
}
