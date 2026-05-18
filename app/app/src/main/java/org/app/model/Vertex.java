package org.app.model;

public class Vertex {
    private String name;
    private int ida, idb;
    private double weight;


    public Vertex(String name, int ida, int idb, double weight) {
        this.name = name;
        this.ida = ida;
        this.idb = idb;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getIdA() {
        return ida;
    }

    public int getIdB() {
        return idb;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() { 
        String result = this.name + " " + this.ida + " " + this.idb + " " + this.weight; 
        return result;
    }
}
