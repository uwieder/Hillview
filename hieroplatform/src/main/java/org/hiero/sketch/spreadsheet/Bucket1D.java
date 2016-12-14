package org.hiero.sketch.spreadsheet;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A one dimensional bucket designed for a Histogram1D
 */
public class Bucket1D {
    private Object minObject;
    private Object maxObject;
    private double minValue;
    private double maxValue;
    private long count;

    public Bucket1D() { }

    private Bucket1D(final long count, final double minValue,
                     final double maxValue, final Object minObject, final Object maxObject) {
        this.count = count;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minObject = minObject;
        this.maxObject = maxObject;
    }

    public Object getMinObject() { return this.minObject; }

    public Object getMaxObject() { return this.maxObject; }

    public double getMinValue() { return this.minValue; }

    public double getMaxValue() { return this.maxValue; }

    public long getCount() { return this.count; }

    public void add(final double item, @NonNull final Object currObject) {
        if (this.count == 0) {
            this.minValue = item;
            this.minObject = currObject;
            this.maxValue = item;
            this.maxObject = currObject;
        }
        else if (item < this.minValue )
        {
            this.minValue = item;
            this.minObject = currObject;
        }
        else if (item > this.maxValue )
        {
            this.maxValue = item;
            this.maxObject = currObject;
        }
        this.count++;
    }

    public boolean isEmpty() { return this.count == 0; }

    /**
     * @return A bucket with the union count of the two buckets and the min/max updated accordingly. Procedure allows
     * both buckets to have objects of different types.
     */
    public Bucket1D union(@NonNull final Bucket1D otherBucket) {
        long ucount = this.count + otherBucket.count;
        double uMinValue, uMaxValue;
        Object uMinObject, uMaxObject;
        if (this.minValue < otherBucket.minValue) {
            uMinValue = this.minValue;
            uMinObject = this.minObject;
        }
        else {
                uMinValue = otherBucket.minValue;
                uMinObject = otherBucket.minObject;
        }
        if (this.maxValue > otherBucket.maxValue) {
            uMaxValue = this.maxValue;
            uMaxObject = this.maxObject;
        }
        else {
                uMaxValue = otherBucket.maxValue;
                uMaxObject = otherBucket.maxObject;
        }
        return new Bucket1D(ucount, uMinValue, uMaxValue, uMinObject, uMaxObject);
    }
}