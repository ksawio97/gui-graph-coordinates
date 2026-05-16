
package org.app.model;

public class Point {
    private String id; 
    private double x, y;

    public Point(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() { 
        String result = this.id + ":  " + this.x + ", " + this.y; 
        return result;
    } 
}
