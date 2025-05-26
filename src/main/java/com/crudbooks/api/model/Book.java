package com.crudbooks.api.model;

import lombok.Data;

@Data
public class Book {
    private Integer id;
    private String createdAt;
    private String title;
    private String authors;
    private String cover;
    private String isbn;
    private String synopsis;
    private Status status = Status.ON;

    public enum Status {
        ON, OFF
    }
}
