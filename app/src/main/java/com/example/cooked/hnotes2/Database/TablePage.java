package com.example.cooked.hnotes2.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cooked on 14/06/2017.
 */

public class TablePage extends TableBase
{
    public TablePage()
    {
    }

    public void dropTableIfExists(SQLiteDatabase db)
    {
        String lSql =
                "DROP TABLE IF EXISTS Page";

        db.execSQL(lSql);
    }

    public void onCreate(SQLiteDatabase db)
    {
        dropTableIfExists(db);

        String lSql =
                "CREATE TABLE Page " +
                        " (" +
                        "   Id INTEGER PRIMARY KEY, " +
                        "   NoteBookId INTEGER, " +
                        "   PageNo INTEGER, " +
                        "   Content TEXT, " +
                        "   PageIndent INTEGER " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        recordPage.setContent(HandleSingleQuotes(recordPage.getContent()));

        String lSql =
                "INSERT INTO Page " +
                        "(Id, NoteBookId, PageNo, Content, PageIndent) " +
                        "VALUES " +
                        "( " + recordPage.getId() + ", " +
                        "  " + recordPage.getNoteBookId() + ", " +
                        "  " + recordPage.getPageNo() + ", " +
                        " '" + recordPage.getContent() + "', " +
                        "  " + recordPage.getPageIndent() + " " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addAfterItem(SQLiteOpenHelper helper, RecordPage currPage, RecordPage newPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "UPDATE Page " +
                        "SET PageNo = PageNo + 1 " +
                        "WHERE NoteBookId = " + currPage.getNoteBookId() + " " +
                        "AND PageNo > " + currPage.getPageNo();

        db.execSQL(lSql);

        newPage.setPageNo(currPage.getPageNo() + 1);

        newPage.setContent(HandleSingleQuotes(newPage.getContent()));

        lSql =
                "INSERT INTO Page " +
                        "(Id, NoteBookId, PageNo, Content, PageIndent) " +
                        "VALUES " +
                        "( " + newPage.getId() + ", " +
                        "  " + newPage.getNoteBookId() + ", " +
                        "  " + newPage.getPageNo() + ", " +
                        " '" + newPage.getContent() + "', " +
                        "  " + newPage.getPageIndent() + " " +
                        ") ";

        db.execSQL(lSql);
    }

    public void addBeforeItem(SQLiteOpenHelper helper, RecordPage currPage, RecordPage newPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String lSql =
                "UPDATE Page " +
                        "SET PageNo = PageNo + 1 " +
                        "WHERE NoteBookId = " + currPage.getNoteBookId() + " " +
                        "AND PageNo >= " + currPage.getPageNo();

        db.execSQL(lSql);

        newPage.setPageNo(currPage.getPageNo());

        newPage.setContent(HandleSingleQuotes(newPage.getContent()));

        lSql =
                "INSERT INTO Page " +
                        "(Id, NoteBookId, PageNo, Content, PageIndent) " +
                        "VALUES " +
                        "( " + newPage.getId() + ", " +
                        "  " + newPage.getNoteBookId() + ", " +
                        "  " + newPage.getPageNo() + ", " +
                        " '" + newPage.getContent() + "', " +
                        "  " + newPage.getPageIndent() + " " +
                        ") ";

        db.execSQL(lSql);
    }

    public RecordPage getItem(SQLiteOpenHelper helper, int id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select Id, NoteBookId, PageNo, Content, PageIndent " +
                        "FROM Page " +
                        "WHERE Id = " + String.valueOf(id);
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            RecordPage page =
                    new RecordPage
                            (
                                    Integer.parseInt(cursor.getString(0)),
                                    Integer.parseInt(cursor.getString(1)),
                                    Integer.parseInt(cursor.getString(2)),
                                    cursor.getString(3),
                                    Integer.parseInt(cursor.getString(4))
                                    );
            return page;
        }
        return null;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 1 && newVersion==2)
        {
            onCreate(db);
        }
        if(oldVersion == 3 && newVersion==4)
        {
            db.execSQL("ALTER TABLE page ADD COLUMN PageType INT(5) DEFAULT 0");
            db.execSQL("UPDATE page SET PageType = 0");
        }
        if(oldVersion == 4 && newVersion==5)
        {
            db.execSQL("ALTER TABLE page ADD COLUMN PageIndent INT(5) DEFAULT 0");
            db.execSQL("UPDATE page SET PageIndent = 0");
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public int getNextId(SQLiteOpenHelper helper)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(Id),0) FROM Page ";
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                return (Integer.parseInt(cursor.getString(0)) + 1);
            }
        }

        return(1);
    }

    public int getNextPageNo(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(MAX(PageNo),0) FROM Page WHERE noteBookId = " + noteBookId;
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                return (Integer.parseInt(cursor.getString(0)) + 1);
            }
        }

        return(1);
    }

    public int getPageCount(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String lSql = "select IFNULL(COUNT(*),0) FROM Page WHERE noteBookId = " + noteBookId;
        Cursor cursor = db.rawQuery(lSql, null);

        if (cursor != null)
        {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                return (Integer.parseInt(cursor.getString(0)));
            }
        }

        return(0);
    }

    public RecordPage[] getList(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String lSql =
                "select NoteBookId, PageNo, Id, Content, PageIndent " +
                        "FROM Page " +
                        "WHERE NoteBookId = " + noteBookId + " " +
                        "ORDER BY PageNo ";
        Cursor cursor = db.rawQuery(lSql, null);

        RecordPage[] list;
        int cnt;
        cnt = cursor.getCount();
        list = new RecordPage[cnt];
        cnt=0;
        if (cursor != null && cursor.getCount() > 0)
        {

            cursor.moveToFirst();
            do
            {
                list[cnt] =
                        new RecordPage
                                (
                                        Integer.parseInt(cursor.getString(0)),
                                        Integer.parseInt(cursor.getString(1)),
                                        Integer.parseInt(cursor.getString(2)),
                                        cursor.getString(3),
                                        Integer.parseInt(cursor.getString(4))
                                );
                cnt++;
            } while(cursor.moveToNext());
        }
        return list;
    }

    public void updateItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        recordPage.setContent(HandleSingleQuotes(recordPage.getContent()));

        String lSql =
                "UPDATE Page " +
                        "SET Content = '" + recordPage.getContent() + "', " +
                        " PageNo = " + recordPage.getPageNo() + ", " +
                        " PageIndent = " + recordPage.getPageIndent() + " " +
                        "WHERE Id = " + recordPage.getId();

        db.execSQL(lSql);
    }

    public void deleteItem(SQLiteOpenHelper helper, RecordPage recordPage)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql =
                "DELETE FROM Page " +
                        "WHERE Id = " + recordPage.getId();

        db.execSQL(lSql);
    }
    public void deleteAll(SQLiteOpenHelper helper, int noteBookId)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String lSql = "DELETE FROM Page WHERE noteBookId = " + noteBookId;

        db.execSQL(lSql);
    }

}
