package org.eigengo.sogx.core;

public class CoinJ {
    private final double center;
    private final double radius;

    public CoinJ(double center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public double getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
