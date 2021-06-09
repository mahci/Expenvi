package envi.gui;

import java.awt.*;

import static java.lang.Math.sqrt;

public class Circle {

    public int radius;
    public int tlX, tlY; // Top-left (X,Y)
    public int side;
    public int cx, cy; // Center (X,Y)

    Color color;

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

    /**
     * Constructor
     * @param cntPos Center position (Point)
     * @param r Radius
     * @param clr Color
     */
    public Circle(Point cntPos, int r, Color clr) {
        radius = r;
        tlX = cntPos.x - r;
        tlY = cntPos.y - r;
        cx = cntPos.x;
        cy = cntPos.y;
        side = r * 2;
        color = clr;
    }

    /**
     * Check if a point is inside the circle or not (border is counted inside)
     * @param x X
     * @param y Y
     * @return True -> inside
     */
    public boolean isInside(int x, int y) {
        double distToCenter = sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
        return distToCenter <= radius;
    }

    /**
     * Check if a Point is inside the circle
     * @param p Point
     * @return Boolean
     */
    public boolean isInside(Point p) {
        return isInside(p.x, p.y);
    }

    //-------- Setters
    public void setColor(Color clr) {
        color = clr;
    }

    //-------- Getters
    public Color getColor() { return color; }
    public int getSide(){ return side; }

    /**
     * Show parameters in a string
     * @return String of parameters
     */
    public String paramsString() {
        return String.format("Center= %d,%d - Radius= %d", cx, cy, radius);
    }

    @Override
    public String toString() {
        return "Circle {" +
                "radius= " + radius +
                ", tlX= " + tlX +
                ", tlY= " + tlY +
                ", side= " + side +
                ", cx= " + cx +
                ", cy= " + cy +
                ", color= " + color +
                '}';
    }
}
