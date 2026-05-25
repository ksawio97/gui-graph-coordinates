package org.app.model;

public enum FileType {
    BIN("bin"),
    TXT("text");
    // text is same as c cli parameter string
    private final String text;

    FileType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}


