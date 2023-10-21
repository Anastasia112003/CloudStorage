package com.example.cloudstorage.exception;

public class FileNotFoundException extends Exception {

    private final long id;

    public FileNotFoundException(String msg, long id) {
        super(msg);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

