package com.example.cooked.hnotes2.Database;

public class RecordNoteBook {
    private int id;
    private String name;
    private String shortDescription;
    public int PageCount;

    public RecordNoteBook()
    {
        id = 0;
        name = "";
        shortDescription = "";
        PageCount = 0;
    }

    public RecordNoteBook(int id, String name, String shortDescription)
    {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.PageCount = 0;
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
