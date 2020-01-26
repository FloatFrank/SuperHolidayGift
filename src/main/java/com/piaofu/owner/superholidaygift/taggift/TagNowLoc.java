package com.piaofu.owner.superholidaygift.taggift;

/**
 * 此类为Tag当前的位置信息存储处理对象
 */
public class TagNowLoc {
    private double x;
    private double y;
    private double z;



    public TagNowLoc(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
