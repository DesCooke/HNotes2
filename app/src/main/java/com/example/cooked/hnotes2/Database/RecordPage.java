package com.example.cooked.hnotes2.Database;

public class RecordPage {
    private int noteBookId;
    private int pageNo;
    private int id;
    private String content;

    public RecordPage()
    {
        noteBookId = 0;
        pageNo = 0;
        id = 0;
        content = "";
    }

    public RecordPage(int noteBookId, int pageNo, int id, String content)
    {
        this.noteBookId = noteBookId;
        this.pageNo = pageNo;
        this.id = id;
        this.content = content;
    }

    public void setNoteBookId(int noteBookId)
    {
        this.noteBookId = noteBookId;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getNoteBookId()
    {
        return (noteBookId);
    }

    public int getPageNo()
    {
        return (pageNo);
    }

    public int getId()
    {
        return (id);
    }

    public String getContent()
    {
        return (content);
    }
}
