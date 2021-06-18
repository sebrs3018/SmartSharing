package com.sebrs3018.SmartSharing.TOARRANGE;


public class Book {

    private String name;
    private String category;
    private String author;

    public Book(){
        /** Default constructor required by Firebase */
    }

    public Book( String _name, String _category, String _author ){
        name = _name;
        category = _category;
        author = _author;
    }

    public String getName(){
        return name;
    }
    public String getCategory(){
        return category;
    }
    public String getAuthor(){
        return author;
    }

}
