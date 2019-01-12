package com.example.cooked.hnotes2.Database;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RecordPage {

    private int noteBookId;
    private int pageNo;
    private int id;
    private String content;
    private int pageIndent;

    public RecordPage()
    {
        noteBookId = 0;
        pageNo = 0;
        id = 0;
        content = "";
        pageIndent = 0;
    }

    public RecordPage(int noteBookId, int pageNo, int id, String content, int pageIndent)
    {
        this.noteBookId = noteBookId;
        this.pageNo = pageNo;
        this.id = id;
        this.content = content;
        this.pageIndent = pageIndent;
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

    public void setPageIndent(int pageIndent)
    {
        this.pageIndent = pageIndent;
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

    public int getPageIndent()
    {
        return (pageIndent);
    }

    public String getContent()
    {
        return (content);
    }
}
