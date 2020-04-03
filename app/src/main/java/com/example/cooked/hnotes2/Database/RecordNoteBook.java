package com.example.cooked.hnotes2.Database;

import com.example.cooked.hnotes2.MainActivity;
import com.example.cooked.hnotes2.R;

public class RecordNoteBook {
    private int id;
    private String name;
    private String shortDescription;
    public int PageCount;
    public int BookType;

    public RecordNoteBook()
    {
        id = 0;
        name = "";
        shortDescription = "";
        PageCount = 0;
        BookType=MainActivity.getInstance().getResources().getInteger(R.integer.notebook);
    }

    public RecordNoteBook(int id, String name, String shortDescription, int bookType)
    {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.PageCount = 0;
        this.BookType=bookType;
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
