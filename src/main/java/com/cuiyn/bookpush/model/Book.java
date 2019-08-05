package com.cuiyn.bookpush.model;

public class Book {
    private boolean isDirectory;
    private String name;
    private String URI;

    public Book(boolean isDirectory, String name, String URI) {
        this.isDirectory = isDirectory;
        this.name = name;
        this.URI = URI;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}
