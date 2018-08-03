package com.example.cooked.hnotes2.Database;

public class RecordNoteBook {
    private int id;
    private String name;
    private String shortDescription;
    public int PageCount;
    public String cover;

    public RecordNoteBook()
    {
        id = 0;
        name = "";
        shortDescription = "";
        PageCount = 0;
        cover="";
    }

    public RecordNoteBook(int id, String name, String shortDescription, String cover)
    {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.PageCount = 0;
        this.cover=cover;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String word)
    {
        this.name = word;
    }

    public void setShortDescription(String explanation)
    {
        this.shortDescription = explanation;
    }

    public int getId()
    {
        return (id);
    }

    public String getName()
    {
        return (name);
    }

    public String getShortDescription()
    {
        return (shortDescription);
    }

}
