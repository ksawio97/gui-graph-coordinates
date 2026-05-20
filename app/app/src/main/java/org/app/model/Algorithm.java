package org.app.model;

public enum Algorithm {
    FR("fr"), TUTTE("tutte"); 
    // text is same as c cli parameter string
    private final String text;

    Algorithm(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
