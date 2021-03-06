package com.example.cooked.hnotes2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cooked.hnotes2.MainActivity;
import com.example.cooked.hnotes2.R;

import java.util.ArrayList;

// derived from SQLiteOpenHelper because it give lots of features like - upgrade/downgrade
// automatically handles the creation and recreation of the database
public class Database extends SQLiteOpenHelper
{
    private static Database database=null;

    //region Member Variables
    // these regions help to compartmentalise the unit
    private TableNoteBook tableNoteBook;
    private TablePage tablePage;
    private TableListItem tableListItem;

    // The version - each change - increment by one
    // if the version increases onUpgrade is called - if decreases - onDowngrade is called
    // if current is 0 (does not exist) onCreate is called
    private static final int DATABASE_VERSION = 8;

    //endregion

    public static Database MyDatabase()
    {
        if(database==null)
            database=new Database(MainActivity.getInstance());

        return(database);
    }

    //region Database functions
    public Database(Context context)
    {
        // super has to be the first command - can't put anything before it
        super(context, context.getResources().getString(R.string.database_filename), null, DATABASE_VERSION);
        tableNoteBook = new TableNoteBook();
        tablePage = new TablePage();
        tableListItem = new TableListItem();
    }

    // called when the current database version is 0
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        tableNoteBook.onCreate(db);
        tablePage.onCreate(db);
        tableListItem.onCreate(db);
    }

    // called when the version number increases
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        tableNoteBook.onUpgrade(db, oldVersion, newVersion);
        tablePage.onUpgrade(db, oldVersion, newVersion);
        tableListItem.onUpgrade(db, oldVersion, newVersion);
    }

    // called when the version number decreases
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        tableNoteBook.onDowngrade(db, oldVersion, newVersion);
        tablePage.onDowngrade(db, oldVersion, newVersion);
        tableListItem.onDowngrade(db, oldVersion, newVersion);
    }
    //endregion


    //region NoteBook
    public void addNoteBook(RecordNoteBook recordNoteBook)
    {
        tableNoteBook.addItem(this, recordNoteBook);
    }

    public RecordNoteBook getNoteBook(int id)
    {
        RecordNoteBook rec=tableNoteBook.getItem(this, id);
        rec.PageCount = tablePage.getPageCount(this, id);
        return(rec);
    }

    public int getNextNoteBookId()
    {
        return tableNoteBook.getNextId(this);
    }

    public RecordNoteBook[] getNoteBookList()
    {
        return tableNoteBook.getList(this);
    }

    public void updateNoteBook(RecordNoteBook recordNoteBook)
    {
        tableNoteBook.updateItem(this, recordNoteBook);
    }

    public void deleteNoteBook(RecordNoteBook recordNoteBook)
    {
        tableListItem.deleteAll(this, recordNoteBook.getId());
        tablePage.deleteAll(this, recordNoteBook.getId());
        tableNoteBook.deleteItem(this, recordNoteBook);
    }

    public void deleteAllNoteBooks()
    {
        tableNoteBook.deleteAll(this);
    }
    //endregion

    //region Page
    public void addPage(RecordPage recordPage)
    {
        tablePage.addItem(this, recordPage);
    }

    public void addAfterPage(RecordPage currPage, RecordPage newPage)
    {
        tablePage.addAfterItem(this, currPage, newPage);
    }

    public void addBeforePage(RecordPage currPage, RecordPage newPage)
    {
        tablePage.addBeforeItem(this, currPage, newPage);
    }

    public RecordPage getPage(int id)
    {
        return tablePage.getItem(this, id);
    }

    public int getNextPageId()
    {
        return tablePage.getNextId(this);
    }

    public int getNextPageNo(int noteBookId)
    {
        return tablePage.getNextPageNo(this, noteBookId);
    }

    public RecordPage[] getPageList(int noteBookId)
    {
        return tablePage.getList(this, noteBookId);
    }

    public void updatePage(RecordPage recordPage)
    {
        tablePage.updateItem(this, recordPage);
    }

    public void deletePage(RecordPage recordPage)
    {
        tablePage.deleteItem(this, recordPage);
    }

    public int getPageCount(int noteBookId)
    {
        return(tablePage.getPageCount(this, noteBookId));
    }
    public void deleteAllPages(int noteBookId)
    {
        tablePage.deleteAll(this, noteBookId);
    }
    //endregion

    //region ListItem
    public void addListItem(RecordListItem item)
    {
        tableListItem.addItem(this, item);
    }

    public RecordListItem getListItem(int id)
    {
        return(tableListItem.getItem(this, id));
    }

    public String[] getParents(int id, boolean includingThis)
    {
        return(tableListItem.getParents(this, id, includingThis));
    }

    public ArrayList<RecordListItem> getListItems(int noteBookId, int parentItemId)
    {
        return(tableListItem.getList(this, noteBookId, parentItemId));
    }

    public void subListResequence(ArrayList<RecordListItem> raa)
    {
        tableListItem.subListResequence(this, raa);
    }

    public void updateListItem(RecordListItem item)
    {
        tableListItem.updateItem(this, item);
    }

    public void deleteListItem(RecordListItem item)
    {
        tableListItem.deleteItem(this, item);
    }

    public void deleteAllForNotebook(int noteBookId)
    {
        tableListItem.deleteAll(this, noteBookId);
    }

    public void deleteAllForSubList(SQLiteOpenHelper helper, int noteBookId, int parentItemId)
    {
        tableListItem.deleteAllForSubList(this, noteBookId, parentItemId);
    }
    //endregion

}

