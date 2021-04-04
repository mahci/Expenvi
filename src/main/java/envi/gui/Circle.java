package envi.gui;

import java.awt.*;

import static java.lang.Math.sqrt;

public class Circle {

    public int radius;
    public int tlX, tlY; // Top-left (X,Y)
    public int side;
    public int cx, cy; // Center (X,Y)

    Color color;

    /***
     * Constructor
     * @param cx Center X
     * @param cy Center Y
     * @param r Radius
     */
    public Circle(int cx, int cy, int r) {
        radius = r;
        tlX = cx - r;
        tlY = cy - r;
        this.cx = cx;
        this.cy = cy;
        side = r * 2;
    }

    /**
     * Constructor with center in Point type
     * @param cntPos Center position
     * @param r Radius
     */
    public Circle(Point cntPos, int r) {
        radius = r;
        tlX = cntPos.x - r;
        tlY = cntPos.y - r;
        cx = cntPos.x;
        cy = cntPos.y;
        side = r * 2;
    }

    /***
     * Check if a point is inside the circle or not (border is counted inside)
     * @param x X
     * @param y Y
     * @return True -> inside
     */
    public boolean isInside(int x, int y) {
        double distToCenter = sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
        if (distToCenter <= radius) return true;
        else return false;
    }

    //-------- Setters
    public void setColor(Color clr) {
        color = clr;
    }

    //-------- Getters
    public Color getColor() { return color; }
    public int getRadius() { return radius; }
    public int getSide(){ return side; }

    /**
     * Show parameters in a string
     * @return String of parameters
     */
    public String paramsString() {
        return String.format("Center= %d,%d - Radius= %d", cx, cy, radius);
    }

}
